package analyzers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Commit;
import models.SemanticConfidence;
import models.SyntacticConfidence;

public class SemanticAnalyzer
{
	public static List<SemanticConfidence> getSyntacticConfidence(List<SyntacticConfidence> syns) {
		
		List<SemanticConfidence> results = new ArrayList<SemanticConfidence>();
		
		for(SyntacticConfidence syn: syns) {
			SemanticConfidence sem = new SemanticConfidence();
			
			// Do are the confidence building here
			
			results.add(sem);
		}
		
		return results;
	}
}
