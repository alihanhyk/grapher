package com.grapher.function;

import java.util.List;

public class OperatorNode extends Node
{
	private enum Type {ADDITION, SUBSTRACTION, MULTIPLICATION, DIVISION, EXPONENT}
	private Type type;
	
	public OperatorNode(String input, List<Node> nodes, Type type)
	{
		this.type = type;
		
		String[] subInputs = null;
		if(type == Type.ADDITION) subInputs = input.split("\\+");
		else if(type == Type.SUBSTRACTION) subInputs = input.split("\\-");
		else if(type == Type.MULTIPLICATION) subInputs = input.split("\\*");
		else if(type == Type.DIVISION) subInputs = input.split("\\/");
		else if(type == Type.EXPONENT) subInputs = input.split("\\^");
		childs = new Node[subInputs.length];
		
		//defines the order of operations 
		Type nextType = null;
		if(type == Type.ADDITION) nextType = Type.SUBSTRACTION;
		else if(type == Type.SUBSTRACTION) nextType = Type.MULTIPLICATION;
		else if(type == Type.MULTIPLICATION) nextType = Type.DIVISION;
		else if(type == Type.DIVISION) nextType = Type.EXPONENT;
		else if(type == Type.EXPONENT) nextType = null;
		
		for(int i = 0; i < childs.length; i++)
		{
			if(nextType != null) childs[i] = new OperatorNode(subInputs[i], nodes, nextType);
			else childs[i] = new ValueNode(subInputs[i], nodes);
		}
		simplify();
	}
	
	public OperatorNode(String input, List<Node> nodes)
	{
		this(input, nodes, Type.ADDITION);
	}

	@Override
	public double get(double x, double y)
	{
		double value = childs[0].get(x, y);
		for(int i = 1; i < childs.length; i++)
		{
			if(type == Type.ADDITION) value += childs[i].get(x, y);
			else if(type == Type.SUBSTRACTION) value -= childs[i].get(x, y);
			else if(type == Type.MULTIPLICATION) value *= childs[i].get(x, y);
			else if(type == Type.DIVISION) value /= childs[i].get(x, y);
			else if(type == Type.EXPONENT) value = Math.pow(value, childs[i].get(x, y));
		}
		
		return value;
	}
}
