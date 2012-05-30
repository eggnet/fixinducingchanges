package analyzers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import models.Attachment;
import models.Bug;
import models.BugActivity;
import models.SemanticConfidence;
import models.SyntacticConfidence;
import db.FixInducingDB;
import fixinducingchanges.Resources;

public class SemanticAnalyzer
{
	public static List<SemanticConfidence> getSemanticConfidence(List<SyntacticConfidence> syns) {
		
		List<SemanticConfidence> results = new ArrayList<SemanticConfidence>();
		
		// Get DB connection
		FixInducingDB db = new FixInducingDB();
		db.connect(Resources.bugDBName);
		
		for(SyntacticConfidence syn: syns) {
			SemanticConfidence sem = new SemanticConfidence(syn.getCommit(), syn.getBugNumber(), 0);
			
			// Get the bug
			Bug bug = db.getBug(syn.getBugNumber());
			if(bug != null) {
				// Check for description
				if(syn.getCommit().getComment().contains(bug.getShort_desc()))
						sem.setConfidence(sem.getConfidence()+1);
				
				// Check the assignee of the bug
				if(syn.getCommit().getAuthor().equals(bug.getAssigned_to()))
					sem.setConfidence(sem.getConfidence()+1);
				
				// Check the bug activity for fixes
				List<BugActivity> bugActivity = db.getBugActivity(syn.getBugNumber());
				for(BugActivity ba: bugActivity) {
					if(ba.getField().equals("bug_status") && ba.getAdded().contains("FIXED")) {
						sem.setConfidence(sem.getConfidence()+1);
						break;
					}
				}
				
				// Check attachments for file
				FixInducingDB dbRepo = new FixInducingDB();
				dbRepo.connect(Resources.repoDBName);
				dbRepo.setBranchName(Resources.repoBranch);
				Set<String> files = dbRepo.getChangedFilesFromCommit(syn.getCommit().getCommit_id());
				List<Attachment> attachments = db.getBugAttachments(syn.getBugNumber());
				for(Attachment attach: attachments) {
					if(files.contains(attach.getFilename())) {
						sem.setConfidence(sem.getConfidence()+1);
						break;
					}
				}
			}
			else {
				sem.setConfidence(-1);
			}
			
			results.add(sem);
		}
		
		return results;
	}
}
