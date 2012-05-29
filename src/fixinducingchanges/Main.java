package fixinducingchanges;

import java.util.List;

import db.FixInducingDB;

import analyzers.SyntacticAnalyzer;
import models.Commit;
import models.SyntacticConfidence;

public class Main
{
	/**
	 * This is the entry point on the fix inducing changes project.
	 * Need to pass in two parameters [START] [END] which are
	 * two commit IDs for which to find all fix inducing changes
	 * in their span.
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("FixInducingChanges tool developed by eggnet.");
		FixInducingDB db = new FixInducingDB();
		try {
			if (args.length < 2 )
			{
				System.out.println("Retry: callGraphAnalyzer [starting_commit] [ending_commit]");
				throw new ArrayIndexOutOfBoundsException();
			}
			else
			{
				try 
				{
					// Get DB connection
					db.connect(Resources.repoDBName);
					db.setBranchName(Resources.repoBranch);
					
					// Generate links
					LinkGenerator generator = new LinkGenerator(db, args[0], args[1]);
					generator.generateLinks();
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				} 
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			System.out.println("Please consult the usage above.");
		}	
		
		// Test stuff
		Commit commit = new Commit();
		commit.setComment("Updated copyrights to 2004");
		List<SyntacticConfidence> result = SyntacticAnalyzer.getSyntacticConfidence(commit);
		for(SyntacticConfidence syn: result) {
			System.out.println("Number: " + syn.getBugNumber() + " Confidence: " + syn.getConfidence());
		}
	}
}
