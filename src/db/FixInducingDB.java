package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
}