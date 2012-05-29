package analyzers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Commit;
import models.SyntacticConfidence;

public class SyntacticAnalyzer
{
	public static List<SyntacticConfidence> getSyntacticConfidence(Commit commit) {		
		List<SyntacticConfidence> results = new ArrayList<SyntacticConfidence>();
		
		// Find numbers
		Pattern number = Pattern.compile("([0-9]+)");
		Matcher numberMatcher = number.matcher(commit.getComment());
		while(numberMatcher.find()) {
			updateConfidenceList(results, commit, Integer.parseInt(numberMatcher.group(1)), 0);
		}
		
		
		// Find bug numbers
		Pattern bugNumber = Pattern.compile("bug[#\\s]*([0-9]+{1})");
		Matcher bugNumberMatcher = bugNumber.matcher(commit.getComment());
		while(bugNumberMatcher.find()) {
			updateConfidenceList(results, commit, Integer.parseInt(bugNumberMatcher.group(1)), 1);
		}
		
		// Find keyword or only numbers
		if(containsKeyword(commit.getComment()) || containsOnlyNumbers(commit.getComment())) {
			for(SyntacticConfidence syn: results) {
				syn.setConfidence(syn.getConfidence()+1);
			}
		}
		
		return results;
	}
	
	private static boolean containsKeyword(String comment) {
		String[] tokens = comment.split(" ");
		
		for(String token: tokens) {
			if(token.matches("fix(e[ds])?|bugs?|defects|patch"))
					return true;
		}
		
		return false;
	}
	
	private static boolean containsOnlyNumbers(String comment) {
		String[] tokens = comment.split(" ");
		
		for(String token: tokens) {
			if(!token.matches("[0-9]+.*"))
					return false;
		}
		
		return true;
	}
	
	private static void updateConfidenceList(List<SyntacticConfidence> list, Commit commit, int number, int addition) {
		for(SyntacticConfidence syn: list) {
			if(syn.getBugNumber() == number) {
				syn.setConfidence(syn.getConfidence()+addition);
				return;
			}
		}
		
		list.add(new SyntacticConfidence(commit, number, 0));
	}
}
