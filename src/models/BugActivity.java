package models;

public class BugActivity
{
	private int 	bug_id;
	private String	field;
	private int		fieldid;
	private String	added;
	
	public BugActivity()
	{
		super();
	}

	public BugActivity(int bug_id, String field, int fieldid, String added)
	{
		super();
		this.bug_id = bug_id;
		this.field = field;
		this.fieldid = fieldid;
		this.added = added;
	}

	public int getBug_id()
	{
		return bug_id;
	}

	public void setBug_id(int bug_id)
	{
		this.bug_id = bug_id;
	}

	public String getField()
	{
		return field;
	}

	public void setField(String field)
	{
		this.field = field;
	}

	public int getFieldid()
	{
		return fieldid;
	}

	public void setFieldid(int fieldid)
	{
		this.fieldid = fieldid;
	}

	public String getAdded()
	{
		return added;
	}

	public void setAdded(String added)
	{
		this.added = added;
	}
}
