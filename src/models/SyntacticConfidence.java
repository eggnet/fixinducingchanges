package models;

public class SyntacticConfidence
{
	private Commit 	commit;
	private int		bugNumber;
	private int		confidence;
	
	public SyntacticConfidence(Commit commit, int bugNumber, int confidence)
	{
		super();
		this.commit = commit;
		this.bugNumber = bugNumber;
		this.confidence = confidence;
	}

	public SyntacticConfidence()
	{
		super();
	}

	public Commit getCommit()
	{
		return commit;
	}

	public void setCommit(Commit commit)
	{
		this.commit = commit;
	}

	public int getBugNumber()
	{
		return bugNumber;
	}

	public void setBugNumber(int bugNumber)
	{
		this.bugNumber = bugNumber;
	}

	public int getConfidence()
	{
		return confidence;
	}

	public void setConfidence(int confidence)
	{
		this.confidence = confidence;
	}
}
