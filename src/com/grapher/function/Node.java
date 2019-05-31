package com.grapher.function;

public abstract class Node
{	
	public abstract double get(double x, double y);
	protected Node[] childs;
	
	public Node getOnlyChild()
	{
		if(childs != null && childs.length == 1) return childs[0];
		return null;
	}
	
	public boolean isConstant()
	{
		for(int i = 0; i < childs.length; i++)
			if(childs[i].isConstant() == false)
				return false;
		return true;
	}
	
	public void simplify()
	{
		//simplifies the blank nodes
		for(int i = 0; i < childs.length; i++)
			if(childs[i].getOnlyChild() != null)
				childs[i] = childs[i].getOnlyChild();
		
		//simplifies the constant nodes
		for(int i = 0; i < childs.length; i++)
			if(childs[i].isConstant())
				childs[i] = new ValueNode(childs[i].get(0, 0));
	}
	
}
