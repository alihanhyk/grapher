package com.grapher.function;

import java.util.LinkedList;
import java.util.List;

public class ParenthesesNode extends Node
{
	private enum Type {SIN, COS, TAN, ASN, ACS, ATN, LOG, LGE, MAX, MIN, ABS}
	private Type type;
		
	public ParenthesesNode(String input, List<Node> nodes, Type type)
	{
		this.type = type;
		
		int start = input.indexOf("(");
		if(start != -1)
		{
			Type nextType = null;	//null means plain parentheses without any function property
			if(start >= 3)
			{
				if(input.substring(start - 3, start).equals("SIN")) nextType = Type.SIN;
				else if(input.substring(start - 3, start).equals("COS")) nextType = Type.COS;
				else if(input.substring(start - 3, start).equals("TAN")) nextType = Type.TAN;
				else if(input.substring(start - 3, start).equals("ASN")) nextType = Type.ASN;
				else if(input.substring(start - 3, start).equals("ACS")) nextType = Type.ACS;
				else if(input.substring(start - 3, start).equals("ATN")) nextType = Type.ATN;
				else if(input.substring(start - 3, start).equals("LOG")) nextType = Type.LOG;
				else if(input.substring(start - 3, start).equals("LGE")) nextType = Type.LGE;
				else if(input.substring(start - 3, start).equals("MAX")) nextType = Type.MAX;
				else if(input.substring(start - 3, start).equals("MIN")) nextType = Type.MIN;
				else if(input.substring(start - 3, start).equals("ABS")) nextType = Type.ABS;
			}
			
			int paranDepth = 0;
			int semiColon = -1;
			
			for(int i = start; i < input.length(); i++)
			{
				//saving the index of semicolon with correct depth to use later
				if(input.charAt(i) == ';' && paranDepth == 1) semiColon = i;
				
				if(input.charAt(i) == '(') paranDepth++;
				else if(input.charAt(i) == ')') paranDepth--;
				
				if(paranDepth == 0)
				{
					String subInput = "";
					if(nextType == null) subInput = input.substring(0, start);
					else subInput = input.substring(0, start - 3);
					subInput += "$" + nodes.size() + "$" + input.substring(i + 1);
					
					if(semiColon == -1)
					{
						//function with single parameter
						String paranInput = input.substring(start + 1, i);						
						nodes.add(new ParenthesesNode(paranInput, new LinkedList<Node>(), nextType));
					}
					else
					{
						//function with double parameters
						
						String paranInput0 = input.substring(start + 1, semiColon);
						String paranInput1 = input.substring(semiColon + 1, i);
						
						nodes.add(new ParenthesesNode(paranInput0, paranInput1, new LinkedList<Node>(), nextType));
					}
					
					childs = new Node[1];
					childs[0] = new ParenthesesNode(subInput, nodes, null);
					simplify();

					break;
				}	
			}
		}
		else
		{
			childs = new Node[1];
			childs[0] = new OperatorNode(input, nodes);
			simplify();
		}
	}
	
	public ParenthesesNode(String input0, String input1, List<Node> nodes, Type type)
	{
		this.type = type;
		
		childs = new Node[2];
		childs[0] = new ParenthesesNode(input0, nodes, null);		
		childs[1] = new ParenthesesNode(input1, nodes, null);
		
		simplify();
	}
	
	public ParenthesesNode(String input, List<Node> nodes)
	{
		this(input, nodes, null);
	}

	@Override
	public double get(double x, double y)
	{
		if(type == Type.SIN) return Math.sin(childs[0].get(x, y));
		else if(type == Type.COS) return Math.cos(childs[0].get(x, y));
		else if(type == Type.TAN) return Math.tan(childs[0].get(x, y));
		else if(type == Type.ASN) return Math.asin(childs[0].get(x, y));
		else if(type == Type.ACS) return Math.acos(childs[0].get(x, y));
		else if(type == Type.ATN) return Math.atan(childs[0].get(x, y));
		else if(type == Type.LOG) return Math.log(childs[1].get(x, y)) / Math.log(childs[0].get(x, y));
		else if(type == Type.LGE) return Math.log(childs[0].get(x, y));
		else if(type == Type.MAX) return Math.max(childs[0].get(x, y), childs[1].get(x, y));
		else if(type == Type.MIN) return Math.min(childs[0].get(x, y), childs[1].get(x, y));
		else if(type == Type.ABS) return Math.abs(childs[0].get(x, y));
		return childs[0].get(x, y);
	}
	
	@Override
	public Node getOnlyChild()
	{
		//If this is a function node; although it has only one child, it is not a blank node.
		if(type != null) return null;
		
		if(childs != null && childs.length == 1) return childs[0];
		return null;
	}
}
