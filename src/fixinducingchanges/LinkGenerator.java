package fixinducingchanges;

import java.util.ArrayList;
import java.util.List;

import analyzers.SemanticAnalyzer;
import analyzers.SyntacticAnalyzer;

import db.FixInducingDB;

import models.Commit;
import models.Link;
import models.SemanticConfidence;
import models.SyntacticConfidence;

public class LinkGenerator
{
	FixInducingDB db;
	
	String commitID_start;
	String commitID_end;

	public LinkGenerator(FixInducingDB db, String commitID_start, String commitID_end)
	{
		super();
		this.db = db;
		this.commitID_start = commitID_start;
		this.commitID_end = commitID_end;
	}
	
	public List<Link> generateLinks() {
		List<Link> links = new ArrayList<Link>();
		
		List<Commit> commits = db.getAllCommits(commitID_start, commitID_end);
		
		if(commits == null) {
			System.out.println("Unable to get commits for syntactis analysis.");
			return null;
		}
		
		List<SyntacticConfidence> t = new ArrayList<SyntacticConfidence>();
		for(Commit commit: commits) {
			t.addAll(SyntacticAnalyzer.getSyntacticConfidence(commit));
		}
		
		List<SemanticConfidence> b = SemanticAnalyzer.getSyntacticConfidence(t);

		if(t.size() != b.size()) {
			System.out.println("Unable to build links due to uneven size.");
			return null;
		}
		else {
			for(int i = 0; i < t.size(); i++) {
				links.add(new Link(t.get(i), b.get(i)));
			}
			return links;
		}
	}
}
