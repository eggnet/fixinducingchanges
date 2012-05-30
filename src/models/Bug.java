package models;

import java.sql.Timestamp;

public class Bug
{
	private int		bug_id;
	private String	assigned_to;
	private String	bug_status;
	private String	short_desc;
	private Timestamp 	creation_ts;
	
	public Bug()
	{
		super();
	}

	public Bug(int bug_id, String assigned_to, String bug_status, String short_desc, Timestamp creation_ts)
	{
		super();
		this.bug_id = bug_id;
		this.assigned_to = assigned_to;
		this.bug_status = bug_status;
		this.short_desc = short_desc;
		this.creation_ts = creation_ts;
	}

	public int getBug_id()
	{
		return bug_id;
	}

	public void setBug_id(int bug_id)
	{
		this.bug_id = bug_id;
	}

	public String getAssigned_to()
	{
		return assigned_to;
	}

	public void setAssigned_to(String assigned_to)
	{
		this.assigned_to = assigned_to;
	}

	public String getBug_status()
	{
		return bug_status;
	}

	public void setBug_status(String bug_status)
	{
		this.bug_status = bug_status;
	}

	public String getShort_desc()
	{
		return short_desc;
	}

	public void setShort_desc(String short_desc)
	{
		this.short_desc = short_desc;
	}

	public Timestamp getCreation_ts()
	{
		return creation_ts;
	}

	public void setCreation_ts(Timestamp creation_ts)
	{
		this.creation_ts = creation_ts;
	}
}
