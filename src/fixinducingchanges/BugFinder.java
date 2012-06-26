package fixinducingchanges;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.Change;
import models.Commit;
import models.CommitFamily;
import models.Diff;
import models.Issue;
import models.Link;
import models.Pair;
import db.SocialDB;
import db.TechnicalDB;

public class BugFinder
{
	private SocialDB sDB;
	private TechnicalDB tDB;
	
	public BugFinder(SocialDB sDB, TechnicalDB tDB)
	{
		super();
		
		this.sDB = sDB;
		this.tDB = tDB;
		
		createTable();
	}
	
	public void createTable() {
		tDB.createTable();
	}
	
	public void findBugs() {
		int offset = 0;
		
		List<Issue> issues = sDB.getIssues(FixResources.DB_LIMIT, offset);
		
		for(;;) {
			for(Issue issue: issues) {
				List<Link> links = sDB.getLinksFromIssue(issue);
				for(Link link: links) {
					Set<String> files = getFilesFromCommit(link.getCommit_id());
					
					Set<String> fixInducing = new HashSet<String>();

					for(String file: files) {
						List<Diff> diffs = tDB.getDiffsFromCommitAndFile(link.getCommit_id(), file);

						if(!diffs.isEmpty()) {
							List<CommitFamily> oldCommitPath = tDB.getCommitPathToRoot(diffs.get(0).getOld_commit_id());

							List<Change> oldOwners = tDB.getAllOwnersForFileAtCommit(file, diffs.get(0).getOld_commit_id(), 
									oldCommitPath);
							
							if(!oldOwners.isEmpty()) {
								for(Change change: oldOwners) {
									for(Diff diff: diffs) {
										if(rangesIntersect(diff.getChar_start(), diff.getChar_end(), 
												change.getCharStart(), change.getCharEnd())) {
											updateCandidates(fixInducing, change.getCommitId(), issue.getCreation_ts());
										}
									}
								}
							}
						}
					}
					
					if(!fixInducing.isEmpty()) {
						tDB.exportBugs(fixInducing, link.getCommit_id());
					}
				}
			}
			
			if(issues.size() < FixResources.DB_LIMIT)
				break;
			else {
				offset += FixResources.DB_LIMIT;
				issues = sDB.getIssues(FixResources.DB_LIMIT, offset);
			}
		}
		
	}
	
	private boolean rangesIntersect(int start1, int end1, int start2, int end2) {
		return !(start1 >= end2 || start2 >= end1);
	}
	
	private Set<String> getFilesFromCommit(String commit) {
		return tDB.getChangedFilesForCommit(commit);
	}
	
	private void updateCandidates(Set<String> candidates, String candidate, Timestamp limit) {
		if(candidates.contains(candidate))
			return;
		
		Commit commit = tDB.getCommit(candidate);
		if(!commit.getCommit_date().after(limit))
			candidates.add(candidate);
	}
}
