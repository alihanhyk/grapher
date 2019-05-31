package com.grapher.function;

import java.util.LinkedList;
import java.util.Locale;

public class Function extends Node
{
	public Function(String input)
	{
		input = input.toUpperCase(Locale.ENGLISH);
		
		input = input.replaceAll("\\s","");
		input = input.replaceAll("PI","P");
		input = input.replaceAll("ASIN","ASN");
		input = input.replaceAll("ACOS","ACS");
		input = input.replaceAll("ATAN","ATN");
		input = input.replaceAll("LN","LGE");
		
		//Negative number operator is replaced with '#' to differentiate from subtraction operator.
		if(input.charAt(0) == '-') input = "#" + input.substring(1);
		for(int i = 1; i < input.length(); i++)
		{
			if(input.charAt(i) == '-')
			{
				char before = input.charAt(i - 1);
				if(before == '*' || before == '/' || before == '^' || before == '(')
					input = input.substring(0, i) + "#" + input.substring(i + 1);
			}
		}
		
		childs = new Node[1];
		childs[0] = new ParenthesesNode(input, new LinkedList<Node>());
		simplify();
	}
	
	@Override
	public double get(double x, double y)
	{
		return childs[0].get(x, y);
	}
	
	public double get(double x)
	{
		return get(x, 0);
	}
	
	public double get()
	{
		return get(0, 0);
	}
}
