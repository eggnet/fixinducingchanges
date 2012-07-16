package db;

import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import db.util.ISetter;
import db.util.PreparedStatementExecutionItem;
import db.util.ISetter.StringSetter;
import fixinducingchanges.FixResources;

import models.Change;
import models.Commit;
import models.CommitFamily;


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
	
	public List<Change> getSourceCommitsForFileAtCommit(String file, List<CommitFamily> commitPath) {
		try {
			String commit = getCommitInPathWithOwnershipOfFile(file, commitPath);
			
			if(commit == null)
				return null;
			
			String sql = "SELECT source_commit_id, owner_id, file_id, char_start, char_end, change_type FROM " +
						"owners WHERE file_id=? AND commit_id=?";
			
			ISetter[] params = {new StringSetter(1,file), new StringSetter(2,commit)};
			PreparedStatementExecutionItem ei = new PreparedStatementExecutionItem(sql, params);
			addExecutionItem(ei);
			ei.waitUntilExecuted();
			ResultSet rs = ei.getResult();

			List<Change> sourceChanges = new ArrayList<Change>();

			while(rs.next())
			{
				sourceChanges.add(new Change(rs.getString("owner_id"),
						  rs.getString("source_commit_id"), 
						  Resources.ChangeType.valueOf(rs.getString("change_type")),
						  rs.getString("file_id"),
						  rs.getInt("char_start"),
						  rs.getInt("char_end")));
			}

			return sourceChanges;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String getCommitInPathWithOwnershipOfFile(String file, List<CommitFamily> commitPath) {
		try {
			String commitIN = "(";
			for(CommitFamily commitF: commitPath) {
				commitIN += "\'" + commitF.getChildId() + "\', ";
			}
			commitIN = commitIN.substring(0, commitIN.length()-2) + ")";
			
			String sql = "SELECT * FROM (" +
						"SELECT owners.commit_id, source_commit_id FROM owners JOIN commits ON (owners.commit_id=commits.commit_id) WHERE " +
						"file_id=? AND owners.commit_id IN " + commitIN + 
						") AS temp JOIN commits ON (temp.commit_id=commits.commit_id) WHERE " +
						"commits.commit_date = (SELECT max(commits.commit_date) FROM (" +
						"SELECT owners.commit_id, source_commit_id FROM owners JOIN commits ON (owners.commit_id=commits.commit_id) WHERE " +
						"file_id=? AND owners.commit_id IN " + commitIN +
						") AS tempB JOIN commits ON (tempB.commit_id=commits.commit_id))";
			
			ISetter[] params = {new StringSetter(1,file), new StringSetter(2,file)};
			PreparedStatementExecutionItem ei = new PreparedStatementExecutionItem(sql, params);
			addExecutionItem(ei);
			ei.waitUntilExecuted();
			ResultSet rs = ei.getResult();
			
			if(rs.next()) {
				return rs.getString("commit_id");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}