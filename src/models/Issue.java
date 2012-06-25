package models;

import java.sql.Timestamp;

public class Issue
{
	private int			item_id;
	private String		issue_num;
	private String		title;
	private String		description;
	private String		status;
	private Timestamp 	creation_ts;
	private Timestamp	last_modified_ts;
	private String		keywords;
	
	public Issue()
	{
		super();
	}

	public Issue(int item_id, String issue_num, String title,
			String description, String status, Timestamp creation_ts,
			Timestamp last_modified_ts, String keywords)
	{
		super();
		this.item_id = item_id;
		this.issue_num = issue_num;
		this.title = title;
		this.description = description;
		this.status = status;
		this.creation_ts = creation_ts;
		this.last_modified_ts = last_modified_ts;
		this.keywords = keywords;
	}

	public int getItem_id()
	{
		return item_id;
	}

	public void setItem_id(int item_id)
	{
		this.item_id = item_id;
	}

	public String getIssue_num()
	{
		return issue_num;
	}

	public void setIssue_num(String issue_num)
	{
		this.issue_num = issue_num;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public Timestamp getCreation_ts()
	{
		return creation_ts;
	}

	public void setCreation_ts(Timestamp creation_ts)
	{
		this.creation_ts = creation_ts;
	}

	public Timestamp getLast_modified_ts()
	{
		return last_modified_ts;
	}

	public void setLast_modified_ts(Timestamp last_modified_ts)
	{
		this.last_modified_ts = last_modified_ts;
	}

	public String getKeywords()
	{
		return keywords;
	}

	public void setKeywords(String keywords)
	{
		this.keywords = keywords;
	}
}
