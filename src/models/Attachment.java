package models;

public class Attachment
{
	private String 	issue_num;
	private String	title;
	private String	description;
	
	public Attachment()
	{
		super();
	}

	public Attachment(String issue_num, String title, String description)
	{
		super();
		this.issue_num = issue_num;
		this.title = title;
		this.description = description;
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
}
