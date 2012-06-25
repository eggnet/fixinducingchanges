package fixinducingchanges;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import models.Change;
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
	}
	
	public void findBugs() {
		int offset = 0;
		
		List<Issue> issues = sDB.getIssues(Resources.DB_LIMIT, offset);
		
		for(Issue issue: issues) {
			// Handle each issue
			List<Link> links = sDB.getLinksFromIssue(issue);
			for(Link link: links) {
				Set<String> files = getFilesFromCommit(link.getCommit_id());
				
				for(String file: files) {
					List<Diff> diffs = tDB.getDiffsFromCommitAndFile(link.getCommit_id(), file);
					
					if(!diffs.isEmpty()) {
						List<CommitFamily> oldCommitPath = tDB.getCommitPathToRoot(diffs.get(0).getOld_commit_id());
						
						List<Change> oldOwners = tDB.getAllOwnersForFileAtCommit(file, diffs.get(0).getOld_commit_id(), 
								oldCommitPath);
						
						for(Change change: oldOwners) {
							for(Diff diff: diffs) {
								if(rangesIntersect(diff.getChar_start(), diff.getChar_end(), 
										change.getCharStart(), change.getCharEnd())) {
									// Do something with the found old change
								}
							}
						}
						
						
					}
					
				}
			}
			
			
			if(issues.size() < Resources.DB_LIMIT)
				break;
			else {
				offset += Resources.DB_LIMIT;
				issues = sDB.getIssues(Resources.DB_LIMIT, offset);
			}
		}
		
	}
	
	private boolean rangesIntersect(int start1, int end1, int start2, int end2) {
		return !(start1 >= end2 || end2 >= start1);
	}
	
	private Set<String> getFilesFromCommit(String commit) {
		return tDB.getChangesetForCommit(commit);
	}
}
