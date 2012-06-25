package models;

public class Link
{
	private String 	commit_id;
	private String 	issue_num;
	private float 	confidence;
	
	public Link()
	{
		super();
	}

	public Link(String commit_id, String issue_num, float confidence)
	{
		super();
		this.commit_id = commit_id;
		this.issue_num = issue_num;
		this.confidence = confidence;
	}

	public String getCommit_id()
	{
		return commit_id;
	}

	public void setCommit_id(String commit_id)
	{
		this.commit_id = commit_id;
	}

	public String getIssue_num()
	{
		return issue_num;
	}

	public void setIssue_num(String issue_num)
	{
		this.issue_num = issue_num;
	}

	public float getConfidence()
	{
		return confidence;
	}

	public void setConfidence(float confidence)
	{
		this.confidence = confidence;
	}
}
