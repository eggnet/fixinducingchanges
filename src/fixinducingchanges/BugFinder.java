package fixinducingchanges;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import models.Change;
import models.Link;

import db.FixInducingDB;

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
	
	public List<Change> findBugs(List<Link> links) {
		List<Change> bugChanges = new ArrayList<Change>();
		
		for(Link link: links) {
			if(link.getSem().getConfidence() > 0 || 
					(link.getSem().getConfidence() == 1 && link.getSyn().getConfidence() > 0)) {
				// Get all files from commit
				Set<String> files = getFilesFromCommit(link.getSem().getCommit().getCommit_id());
				for(String file: files) {
					/*
					 * Build file
					 * Build one commit older file
					 * Diff them
					 * Convert to line number
					 * Get commit ID of those lines
					 * Build set of possibles
					 * Find suspects and investigate
					 */
				}
			}
		}
		
		return bugChanges;
	}
	
	private Set<String> getFilesFromCommit(String commit) {
		return db.getChangedFilesFromCommit(commit);
	}
}
