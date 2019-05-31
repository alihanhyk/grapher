package com.grapher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import com.grapher.function.Function;

public class GraphFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private GraphCanvas canvas;
	
	private JTextArea inputField;
	private JComboBox<String> colorBox;
	private JButton clearButton;
	private JButton graphButton;
	
	public GraphFrame()
	{
		setLayout(new BorderLayout());
		setSize(new Dimension(800, 800));
		setMinimumSize(new Dimension(500, 500));
		setTitle("Grapher");
		
		canvas = new GraphCanvas();
		canvas.setBackground(Color.WHITE);
		add(canvas, BorderLayout.CENTER);

		JPanel topPanel = new JPanel(new BorderLayout());
		
		inputField = new JTextArea();
		Border borderLine = BorderFactory.createLineBorder(new Color(122, 138, 153), 1);
		Border borderInside = BorderFactory.createEmptyBorder(3, 5, 3, 5);
		Border borderOutside = BorderFactory.createEmptyBorder(5, 5, 6, 0);
		inputField.setBorder(BorderFactory.createCompoundBorder(borderOutside, BorderFactory.createCompoundBorder(borderLine, borderInside)));
		topPanel.add(inputField, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 5));
		buttonPanel.setBackground(Color.WHITE);
		
		String[] colors = {"Black", "Blue", "Red", "Green", "Purple", "Orange", "Yellow"};
		colorBox = new JComboBox<String>(colors);	
		colorBox.setSelectedIndex(1);
		buttonPanel.add(colorBox);
		
		graphButton = new JButton("GRAPH");
		graphButton.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e)
			{
				Color color = null;
				int colorIndex = colorBox.getSelectedIndex();
				
				if(colorIndex == 0) color = Color.BLACK;
				else if(colorIndex == 1) color = Color.BLUE;
				else if(colorIndex == 2) color = Color.RED;
				else if(colorIndex == 3) color = new Color(34, 177, 76);
				else if(colorIndex == 4) color = new Color(163, 73, 164);
				else if(colorIndex == 5) color = new Color(255, 127, 39);
				else if(colorIndex == 6) color = Color.YELLOW;
				
				try
				{
					String input = inputField.getText();
					
					input = input.toUpperCase(Locale.ENGLISH);
					input = input.replaceAll("\\s","");
					
					int equalSign = input.indexOf("=");
					if(equalSign != -1)
					{
						String input0 = input.substring(0, equalSign);
						String input1 = input.substring(equalSign + 1);
						
						if(input0.equals("Y") && input1.indexOf("Y") == -1) canvas.graphFunction(new Function(input1), color);
						else if(input1.equals("Y") && input0.indexOf("Y") == -1) canvas.graphFunction(new Function(input0), color);
						else canvas.graph(new Function(input0 + "-(" + input1 + ")"), color);					
					}
					else
					{
						int inequalitySign = input.indexOf(">");
						if(inequalitySign != -1)
						{
							input = input.substring(0, inequalitySign) + "-(" + input.substring(inequalitySign + 1) + ")";
						}
						else
						{
							inequalitySign = input.indexOf("<");
							input = input.substring(inequalitySign + 1) + "-(" + input.substring(0, inequalitySign) + ")";
						}
						canvas.graphInequality(new Function(input), color);
					}
				}
				catch(Exception exception)
				{
					 JOptionPane.showMessageDialog(null, "The input could not be resolved to any graph.", "Syntax Error", JOptionPane.ERROR_MESSAGE);
					 return;
				}
			}
	    });
		buttonPanel.add(graphButton);
		
		clearButton = new JButton("Clear Canvas");
		clearButton.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e)
			{
				canvas.clear();
			}
	    });
		buttonPanel.add(clearButton);
		
		topPanel.add(buttonPanel, BorderLayout.LINE_END);
		add(topPanel, BorderLayout.PAGE_START);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static void main( String[] args)
	{
		new GraphFrame();
	}
}
