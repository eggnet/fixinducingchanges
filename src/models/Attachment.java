package models;

public class Attachment
{
	private int 	bug_id;
	private String	description;
	private String	filename;
	private int		submitter_id;
	
	public Attachment()
	{
		super();
	}

	public Attachment(int bug_id, String description, String filename,
			int submitter_id)
	{
		super();
		this.bug_id = bug_id;
		this.description = description;
		this.filename = filename;
		this.submitter_id = submitter_id;
	}

	public int getBug_id()
	{
		return bug_id;
	}

	public void setBug_id(int bug_id)
	{
		this.bug_id = bug_id;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public int getSubmitter_id()
	{
		return submitter_id;
	}

	public void setSubmitter_id(int submitter_id)
	{
		this.submitter_id = submitter_id;
	}
}
