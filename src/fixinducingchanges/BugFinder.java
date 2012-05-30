package fixinducingchanges;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import analyzers.SyntacticAnalyzer;

import models.Bug;
import models.Change;
import models.Commit;
import models.Link;
import models.Pair;
import models.SyntacticConfidence;

import db.FixInducingDB;
import differ.filediffer;
import differ.filediffer.diffObjectResult;

public class BugFinder
{
	private FixInducingDB db;
	
	public BugFinder()
	{
		super();
	}

	public BugFinder(FixInducingDB db)
	{
		super();
		this.db = db;
	}
	
	public List<String> findBugs(List<Link> links) {
		List<String> bugChanges = new ArrayList<String>();
		
		for(Link link: links) {
			if(link.getSem().getConfidence() > 0 || 
					(link.getSem().getConfidence() == 1 && link.getSyn().getConfidence() > 0)) {
				// Get all files from commit
				Set<String> files = getFilesFromCommit(link.getSem().getCommit().getCommit_id());
				for(String file: files) {
					// Build files
					String fileNew = db.getRawFile(file, link.getSem().getCommit().getCommit_id());
					// TODO Need one older commit ID here
					String fileOld = db.getRawFile(file, link.getSem().getCommit().getCommit_id());
					
					// Diff files
					filediffer differ = new filediffer(fileOld, fileNew);
					differ.diffFilesLineMode();
					
					// Get old commit ID of changes
					List<String> blameCommits = blameAtOldCommit(differ, file, "oldCommit");
					
					// Build candidates
					List<Pair<String, String>> candidates = buildCandidates(blameCommits, 
							link.getSem().getCommit().getCommit_id());
					
					// Get rid of suspects appropriately
					List<Pair<String, String>> results = removeSuspects(links, candidates);
					
					// Add results to final list
					for(int i = 0; i < results.size(); i++) {
						String result = results.get(i).getFirst();
						if(!bugChanges.contains(result))
							bugChanges.add(result);
					}
				}
			}
		}
		
		return bugChanges;
	}
	
	private Set<String> getFilesFromCommit(String commit) {
		return db.getChangedFilesFromCommit(commit);
	}
	
	private List<String> blameAtOldCommit(filediffer differ, String file, String commitID) {
		List<Change> changes = db.getAllOwnersForFileAtCommit(file, commitID);
		List<String> results = new ArrayList<String>();
		
		for(Change change: changes) {
			// Do the deletes
			for(diffObjectResult DOR: differ.getDeleteObjects()) {
				if(isIntersecting(change, DOR))
					if(!results.contains(change.getCommitId()))
						results.add(change.getCommitId());
			}
			// Do the inserts
			for(diffObjectResult DOR: differ.getInsertObjects()) {
				if(isIntersecting(change, DOR))
					if(!results.contains(change.getCommitId()))
						results.add(change.getCommitId());
			}
		}
		
		return results;
	}
	
	private boolean isIntersecting(Change change, diffObjectResult diffObject) {
		return ((diffObject.start < change.getCharStart() && diffObject.end > change.getCharStart()) ||
			   (diffObject.start >= change.getCharStart() && diffObject.end <= change.getCharEnd()) ||
			   (diffObject.start > change.getCharStart() && diffObject.start <= change.getCharEnd()));
	}
	
	private List<Pair<String, String>> buildCandidates(List<String> oldCommits, String newCommit) {
		List<Pair<String, String>> candidates = new ArrayList<Pair<String, String>>();
		
		for(String oldCommit: oldCommits) {
			Pair<String, String> candidate = new Pair<String, String>(oldCommit, newCommit);
			if(!candidatesContains(candidates, candidate))
				candidates.add(candidate);
		}
		
		return candidates;
	}
	
	private boolean candidatesContains(List<Pair<String, String>> candidates, Pair<String, String> candidate) {
		for(Pair<String, String> existing: candidates) {
			if(existing.getFirst().equals(candidate.getFirst()) &&
					existing.getSecond().equals(candidate.getSecond())) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isSuspect(String commitID, int bug_id) {
		Commit oldCommit = db.getCommit(commitID);
		Bug bug = getBugDB().getBug(bug_id);
		
		return oldCommit.getCommit_date().after(bug.getCreation_ts());
	}
	
	private FixInducingDB getBugDB() {
		// Get DB connection
		FixInducingDB db = new FixInducingDB();
		db.connect(Resources.bugDBName);
		return db;
	}
	
	private List<Link> getLinksAtCommit(List<Link> total, String commitID) {
		List<Link> results = new ArrayList<Link>();
		for(Link link: total) {
			if(link.getSyn().getCommit().getCommit_id().equals(commitID))
				results.add(link);
		}
		return results;
	}
	
	private int getLatestReportedBug(List<Link> total, String commitID) {
		Bug result = null;
		for(Link link: getLinksAtCommit(total, commitID)) {
			if(link.getSem().getConfidence() > 0 || 
					(link.getSem().getConfidence() == 1 && link.getSyn().getConfidence() > 0)) {
				Bug bug = getBugDB().getBug(link.getSem().getBugNumber());
				if(result == null)
					result = bug;
				else if(result.getCreation_ts().after(bug.getCreation_ts()))
					result = bug;
			}
		}
		return result.getBug_id();
	}
	
	private List<Pair<String, String>> removeSuspects(List<Link> total, List<Pair<String, String>> candidates) {
		List<Pair<String, String>> results = new ArrayList<Pair<String, String>>();
		
		for(Pair<String, String> candidate: candidates) {
			if(isSuspect(candidate.getFirst(), getLatestReportedBug(total, candidate.getSecond()))) {
				// Handle suspects here
			}
		}
		
		return results;
	}
}
