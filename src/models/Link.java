package models;

public class Link
{
	private SyntacticConfidence syn;
	private SemanticConfidence sem;
	
	public Link(SyntacticConfidence syn, SemanticConfidence sem)
	{
		super();
		this.syn = syn;
		this.sem = sem;
	}

	public Link()
	{
		super();
	}

	public SyntacticConfidence getSyn()
	{
		return syn;
	}

	public void setSyn(SyntacticConfidence syn)
	{
		this.syn = syn;
	}

	public SemanticConfidence getSem()
	{
		return sem;
	}

	public void setSem(SemanticConfidence sem)
	{
		this.sem = sem;
	}
}
