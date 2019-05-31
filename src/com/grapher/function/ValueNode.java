package com.grapher.function;

import java.util.List;

public class ValueNode extends Node
{
	private enum Type {NODE, VALUE, X, Y}
	
	private Type type;
	private boolean negative;
	private double value;
	
	public ValueNode(String input, List<Node> nodes)
	{
		negative = false;
		if(input.charAt(0) == '#')
		{
			negative = true;
			input = input.substring(1);
		}
		
		if(input.charAt(0) == '$')
		{
			int index = Integer.parseInt(input.substring(1, input.length() -1));
			
			childs = new Node[1];
			childs[0] = nodes.get(index);
			simplify();
			
			type = Type.NODE;
		}
		else if(input.charAt(0) == 'X') type = Type.X;
		else if(input.charAt(0) == 'Y') type = Type.Y;
		else
		{
			if(input.charAt(0) == 'E') value = Math.E;
			else if(input.charAt(0) == 'P') value = Math.PI;
			else value = Double.parseDouble(input);
			
			type = Type.VALUE;
		}
	}
	
	//needed for the simplify method
	public ValueNode(double value)
	{
		this.value = value;
		type = Type.VALUE;
	}
	
	@Override
	public double get(double x, double y)
	{
		if(type == Type.VALUE) return (negative ? -1 : 1) * value;
		else if(type == Type.X) return (negative ? -1 : 1) * x;
		else if(type == Type.Y) return (negative ? -1 : 1) * y;
		return (negative ? -1 : 1) * childs[0].get(x, y);
	}
	
	@Override
	public boolean isConstant()
	{
		if(type == Type.VALUE) return true;
		else if(type == Type.X || type == Type.Y) return false;
		
		//this is actually inside of a parentheses
		return super.isConstant();
	}

}
