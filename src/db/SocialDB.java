package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import models.Issue;
import models.Link;

public class SocialDB extends DbConnection
{
	public SocialDB()
	{
		super();
	}
	
	public List<Issue> getIssues(int iLIMIT, int iOFFSET) {
		try 
		{
			LinkedList<Issue> bugs = new LinkedList<Issue>();
			String sql = "SELECT * FROM issues ORDER BY item_id " +
					"LIMIT " + iLIMIT + " OFFSET " + iOFFSET; 
			String[] parms = {};
			ResultSet rs = execPreparedQuery(sql, parms);
			while(rs.next())
			{
				bugs.add(new Issue(rs.getInt("item_id"), rs.getString("issue_num"), rs.getString("title"), 
						rs.getString("description"), rs.getString("status"), rs.getTimestamp("creation_ts"), 
						rs.getTimestamp("last_modified_ts"), rs.getString("keywords")));
			}
			return bugs;
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Link> getLinksFromIssue(Issue issue) {
		try {
			List<Link> links = new ArrayList<Link>();
			String sql = "SELECT * FROM links WHERE item_id = " + issue.getItem_id();
			String[] parms = {};
			ResultSet rs = execPreparedQuery(sql, parms);
			while(rs.next())
			{
				links.add(new Link(rs.getString("commit_id"), rs.getInt("item_id"),
						rs.getFloat("confidence")));
			}
			
			return links;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
