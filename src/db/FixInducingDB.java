package db;

import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import db.util.ISetter;
import db.util.PreparedStatementExecutionItem;
import db.util.ISetter.StringSetter;
import fixinducingchanges.FixResources;

import models.Change;
import models.Commit;


public class FixInducingDB extends TechnicalDb
{
	public FixInducingDB()
	{
		super();
	}
	
	public void exportBugs(Set<String> bugs, String fix) {
		for(String bug: bugs) {
			String query = "INSERT INTO fix_inducing (bug, fix) VALUES " +
					"(?, ?)";
			ISetter[] params = {
					new StringSetter(1,bug),
					new StringSetter(2,fix)
			};
			PreparedStatementExecutionItem ei = new PreparedStatementExecutionItem(query, params);
			addExecutionItem(ei);
			ei.waitUntilExecuted();
		}
	}
	
	public void createTable() {
		try {
			// Drop the table if it already exists
			String query = "DROP TABLE IF EXISTS fix_inducing";
			PreparedStatementExecutionItem ei = new PreparedStatementExecutionItem(query, null);
			addExecutionItem(ei);
			ei.waitUntilExecuted();

			runScript(new InputStreamReader(FixResources.class.getResourceAsStream("createTable.sql")));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
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
			
			ISetter[] parms = {new StringSetter(1, commitID_end), new StringSetter(2, commitID_start), new StringSetter(3, branchID)};
			PreparedStatementExecutionItem ei = new PreparedStatementExecutionItem(sql, parms);
			addExecutionItem(ei);
			ei.waitUntilExecuted();
			ResultSet rs = ei.getResult();

			while(rs.next())
				commits.add(new Commit(rs.getInt("id"), rs.getString("commit_id"), rs.getString("author"),
						rs.getString("author_email"), rs.getString("comments"), null, branchID));
			
			return commits;
		}
		catch(SQLException e) {
			return null;
		}
	}
	
	public List<Change> getAllOwnersForFileAtCommit(String FileId, String CommitId)
	{
		try 
		{
			LinkedList<Change> changes = new LinkedList<Change>();
			String sql = "SELECT source_commit_id, file_id, owner_id, char_start, char_end, change_type FROM owners natural join commits where commit_id=?" +
					"and (branch_id is NULL OR branch_id=?) and file_id=? order by commit_date, commit_id, char_start;"; 
		
			ISetter[] parms = {new StringSetter(1, CommitId), new StringSetter(2, branchID), new StringSetter(3, FileId)};
			PreparedStatementExecutionItem ei = new PreparedStatementExecutionItem(sql, parms);
			addExecutionItem(ei);
			ei.waitUntilExecuted();
			ResultSet rs = ei.getResult();

			while(rs.next())
			{
				changes.add(new Change(rs.getString("owner_id"), rs.getString("source_commit_id"), 
						Resources.ChangeType.valueOf(rs.getString("change_type")), rs.getString("file_id"),
						rs.getInt("char_start"), rs.getInt("char_end")));
			}
			return changes;
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
}