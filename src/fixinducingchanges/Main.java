package fixinducingchanges;

import java.util.List;

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
		Commit commit = new Commit();
		commit.setComment("Updated copyrights to 2004");
		List<SyntacticConfidence> result = SyntacticAnalyzer.getSyntacticConfidence(commit);
		for(SyntacticConfidence syn: result) {
			System.out.println("Number: " + syn.getBugNumber() + " Confidence: " + syn.getConfidence());
		}
	}
}
