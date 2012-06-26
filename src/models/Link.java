package models;

public class Link
{
	private String 	commit_id;
	private int 	item_id;
	private float 	confidence;
	
	public Link()
	{
		super();
	}

	public Link(String commit_id, int item_id, float confidence)
	{
		super();
		this.commit_id = commit_id;
		this.item_id = item_id;
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

	public int getIssue_num()
	{
		return item_id;
	}

	public void setIssue_num(int item_id)
	{
		this.item_id = item_id;
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
