package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import models.Attachment;
import models.Bug;
import models.BugActivity;
import models.Change;
import models.Commit;


public class FixInducingDB extends DbConnection
{
	public FixInducingDB()
	{
		super();
	}
	
	/**
	 * This functions returns all commits that are in between two commit IDs 
	 * inclusively.
	 * @param commitID_start
	 * @param commitID_end
	 * @return
	 */
	public List<Commit> getAllCommits(String commitID_start, String commitID_end) {
		try {
			List<Commit> commits = new ArrayList<Commit>();
			String sql = "SELECT id, commit_id, author, author_email, comments, commit_date FROM commits WHERE " +
					"commit_date <= (select commit_date from commits where commit_id=?) AND " +
					"commit_date >= (select commit_date from commits where commit_id=?) AND " +
					"(branch_id is NULL or branch_id=?) " +
					"ORDER BY commit_date, commit_id";
			String[] params = {commitID_end, commitID_start, branchID};
			ResultSet rs = execPreparedQuery(sql, params);
			while(rs.next())
				commits.add(new Commit(rs.getInt("id"), rs.getString("commit_id"), rs.getString("author"),
						rs.getString("author_email"), rs.getString("comments"), null, branchID));
			
			return commits;
		}
		catch(SQLException e) {
			return null;
		}
	}
	
	public Bug getBug(int bug_id) {
		try {
			Bug bug = null;
			String sql = "SELECT bug_id, assigned_to, assigned_to_name, bug_status, short_desc " +
					"FROM bugzilla_bugs WHERE bug_id=?";
			String[] params = {((Integer)bug_id).toString()};
			ResultSet rs = execPreparedQuery(sql, params);
			if(rs.next())
				bug = new Bug(rs.getInt("bug_id"), rs.getString("assigned_to"), rs.getString("assigned_to_name"),
						rs.getString("bug_status"), rs.getString("short_desc"));
			
			return bug;
		}
		catch(SQLException e) {
			return null;
		}
	}
	
	public List<Attachment> getBugAttachments(int bug_id) {
		try {
			List<Attachment> attachments = new ArrayList<Attachment>();
			String sql = "SELECT bug_id, description, filename, submitter_id FROM bugzilla_attachments WHERE " +
					"bug_id=?";
			String[] params = {((Integer)bug_id).toString()};
			ResultSet rs = execPreparedQuery(sql, params);
			while(rs.next())
				attachments.add(new Attachment(rs.getInt("bug_id"), rs.getString("description"), 
						rs.getString("filename"), rs.getInt("submitter_id")));
			
			return attachments;
		}
		catch(SQLException e) {
			return null;
		}
	}
	
	public List<BugActivity> getBugActivity(int bug_id) {
		try {
			List<BugActivity> activity = new ArrayList<BugActivity>();
			String sql = "SELECT bug_id, field, fieldid, added FROM bugzilla_bugs_activity WHERE " +
					"bug_id=?";
			String[] params = {((Integer)bug_id).toString()};
			ResultSet rs = execPreparedQuery(sql, params);
			while(rs.next())
				activity.add(new BugActivity(rs.getInt("bug_id"), rs.getString("field"), 
						rs.getInt("fieldid"), rs.getString("added")));
			
			return activity;
		}
		catch(SQLException e) {
			return null;
		}
	}
}