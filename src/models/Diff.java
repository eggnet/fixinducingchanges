package models;

import db.Resources;
import db.TechnicalDB;

public class Diff
{
	private String 	file_id;
	private String 	new_commit_id;
	private String 	old_commit_id;
	private String 	diff_text;
	private int		char_start;
	private int		char_end;
	private String	diff_type;
	
	private int		line_start;
	private int		line_end;
	
	public Diff()
	{
		super();
		
		line_start = -1;
		line_end = -1;
	}
	
	public Diff(String file_id, String new_commit_id, String old_commit_id,
			String diff_text, int char_start, int char_end, String diff_type)
	{
		super();
		this.file_id = file_id;
		this.new_commit_id = new_commit_id;
		this.old_commit_id = old_commit_id;
		this.diff_text = diff_text;
		this.char_start = char_start;
		this.char_end = char_end;
		this.diff_type = diff_type;
		
		line_start = -1;
		line_end = -1;
	}
	
	public int getLine_start(TechnicalDB tDB) {
		if(line_start != -1)
			return line_start;
		else 
			setLines(tDB);
		return line_start;
	}
	
	public int getLine_end(TechnicalDB tDB) {
		if(line_end != -1)
			return line_end;
		else
			setLines(tDB);
		return line_end;
	}
	
	private void setLines(TechnicalDB tDB) {
		String rawFile = tDB.getRawFileFromDiffTree(file_id, new_commit_id, 
				tDB.getCommitPathToRoot(new_commit_id));
		
		this.line_start = Resources.convertCharToLine(char_start, rawFile);
		this.line_end	= Resources.convertCharToLine(char_end, rawFile);
	}

	public String getFile_id()
	{
		return file_id;
	}

	public void setFile_id(String file_id)
	{
		this.file_id = file_id;
	}

	public String getNew_commit_id()
	{
		return new_commit_id;
	}

	public void setNew_commit_id(String new_commit_id)
	{
		this.new_commit_id = new_commit_id;
	}

	public String getOld_commit_id()
	{
		return old_commit_id;
	}

	public void setOld_commit_id(String old_commit_id)
	{
		this.old_commit_id = old_commit_id;
	}

	public String getDiff_text()
	{
		return diff_text;
	}

	public void setDiff_text(String diff_text)
	{
		this.diff_text = diff_text;
	}

	public int getChar_start()
	{
		return char_start;
	}

	public void setChar_start(int char_start)
	{
		this.char_start = char_start;
	}

	public int getChar_end()
	{
		return char_end;
	}

	public void setChar_end(int char_end)
	{
		this.char_end = char_end;
	}

	public String getDiff_type()
	{
		return diff_type;
	}

	public void setDiff_type(String diff_type)
	{
		this.diff_type = diff_type;
	}
}
