package com.grapher;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.grapher.function.Function;

public class GraphCanvas extends JPanel
{
	private static final long serialVersionUID = 1L;	
		
	private double range;
	private double xOffset;
	private double yOffset;
	
	private int toCanvasX(double x) { return (int) ((x - xOffset) * getWidth() / range) + getWidth() / 2; }
	private int toCanvasY(double y) { return -(int) ((y - yOffset) * getWidth() / range) + getHeight() / 2; }
	
	private List<Function> graphs;
	private List<Color> colors;
	
	private List<Function> inequalities;
	private List<Color> inequalityColors;
	
	private List<Function> functions;
	private List<Color> functionColors;
		
	public void graph(Function graph, Color color)
	{
		graphs.add(graph);
		colors.add(color);
		repaint();
	}
	
	public void graphInequality(Function inequality, Color color)
	{
		inequalities.add(inequality);
		inequalityColors.add(color);
		repaint();
	}
	
	public void graphFunction(Function function, Color color)
	{
		functions.add(function);
		functionColors.add(color);
		repaint();
	}
	
	public void clear()
	{
		graphs.clear();
		colors.clear();
		
		inequalities.clear();
		inequalityColors.clear();
		
		functions.clear();
		functionColors.clear();
		
		repaint();
	}
		
	private MouseEvent oldMouse;
	
	public GraphCanvas()
	{
		range = 10;
		xOffset = 0;
		yOffset = 0;
				
		graphs = new LinkedList<Function>();
		colors = new LinkedList<Color>();
		
		inequalities = new LinkedList<Function>();
		inequalityColors = new LinkedList<Color>();
		
		functions = new LinkedList<Function>();
		functionColors = new LinkedList<Color>();
		
		addMouseWheelListener(new MouseWheelListener()
		{
			@Override
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				if(e.getWheelRotation() == 1) range *= 2;
				else range *= 0.5;
				
				repaint();
			}
		});
		
		addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseDragged(MouseEvent e)
			{
				if(SwingUtilities.isLeftMouseButton(e) == false) return;
				
				int deltaX = e.getX() - oldMouse.getX();
				int deltaY = e.getY() - oldMouse.getY();
				
				xOffset += (double) -deltaX * range / getWidth();
				yOffset += (double) deltaY * range / getWidth();
				
				oldMouse = e;
				repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e)
			{
				oldMouse = e;
			}
		});
				
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem item;
		
		item = new JMenuItem("Save As An Image");
		item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter("PNG File", ".png"));
				fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
				fileChooser.showSaveDialog(null);
				
				BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics g = image.getGraphics();
				paint(g);
				
				try
				{
					String fileName = fileChooser.getSelectedFile().getAbsolutePath();
					if(fileName.endsWith(".png") == false) fileName += ".png";
					
					ImageIO.write(image, "PNG", new File(fileName));
				} 
				catch (IOException exception)
				{
					JOptionPane.showMessageDialog(null, "The image could not be saved.", "IO Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(item);
		
		popupMenu.addSeparator();
		
		item = new JMenuItem("Reset Back To Origin");
		item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				xOffset = 0;
				yOffset = 0;
				repaint();
			}
		});
		popupMenu.add(item);
		
		item = new JMenuItem("Reset Zoom Level");
		item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				range = 10;
				repaint();
			}
		});
		popupMenu.add(item);
		
		popupMenu.addSeparator();
				
		item = new JMenuItem("Zoom In");
		item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				range *= 0.5;
				repaint();
			}
		});
		popupMenu.add(item);
		
		item = new JMenuItem("Zoom Out");
		item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				range *= 2;
				repaint();
			}
		});
		popupMenu.add(item);
		
		this.setComponentPopupMenu(popupMenu);
	}
	
	@Override
	public void paint(Graphics g)
	{	
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		drawAxes(g);
		
		for(int i = 0; i < inequalities.size(); i++)
			drawInequality(g, inequalities.get(i), inequalityColors.get(i));
		
		for(int i = 0; i < graphs.size(); i++)
			drawGraph(g, graphs.get(i), colors.get(i));
		
		for(int i = 0; i < functions.size(); i++)
			drawFunction(g, functions.get(i), functionColors.get(i));
	}
		
	private void drawGraph(Graphics g, Function graph, Color color)
	{
		g.setColor(color);
		double step = (double) range / getWidth();
		double yRange = range * getHeight() / getWidth();
		
		double[] oldValues = new double[getWidth() + 1];
		for(int i = 0; i < oldValues.length; i++) oldValues[i] = Double.NaN;
		
		for(double y = - yRange / 2 + yOffset; y <= yRange / 2 + yOffset; y += step)
		{
			double oldValue = Double.NaN;
			int i = 0;
			
			for(double x = - range / 2 + xOffset; x <= range / 2 + xOffset; x += step)
			{
				double value = graph.get(x, y);
				if(Double.isFinite(value))
				{
					if((Double.isFinite(oldValue) && Math.signum(value) != Math.signum(oldValue)) || 
							Double.isFinite(oldValues[i]) && Math.signum(value) != Math.signum(oldValues[i]))
					{
						int pointX = toCanvasX(x);
						int pointY = toCanvasY(y);
						g.drawLine(pointX, pointY, pointX, pointY);
					}
				}
				
				oldValue = value;
				oldValues[i] = value;
				i++;
			}
		}
	}
	
	private void drawInequality(Graphics g, Function inequality, Color color)
	{
		g.setColor(color);
		double step = (double) range / getWidth();
		double yRange = range * getHeight() / getWidth();
		
		for(double y = - yRange / 2 + yOffset; y <= yRange / 2 + yOffset; y += step)
		{			
			for(double x = - range / 2 + xOffset; x <= range / 2 + xOffset; x += step)
			{
				double value = inequality.get(x, y);
				if(value > 0)
				{
					int pointX = toCanvasX(x);
					int pointY = toCanvasY(y);
					g.drawLine(pointX, pointY, pointX, pointY);
				}
			}
		}
	}
	
	private void drawFunction(Graphics g, Function inequality, Color color)
	{
		g.setColor(color);
		double step = (double) range / getWidth();
		
		double oldValue = Double.NaN;
		for(double x = - range / 2 + xOffset; x <= range / 2 + xOffset; x += step)
		{
			double value = inequality.get(x, 0);
			if(Double.isFinite(value) && Double.isFinite(oldValue))
				g.drawLine(toCanvasX(x), toCanvasY(value), toCanvasX(x - step), toCanvasY(oldValue));
			oldValue = value;
		}
	}
	
	private void drawAxes(Graphics g)
	{
		int originX = toCanvasX(0);
		int originY = toCanvasY(0);
		
		g.setColor(Color.BLACK);
		g.drawLine(0, originY, getWidth(), originY);
		g.drawLine(originX, 0, originX, getHeight());
				
		FontMetrics fm = ((Graphics2D) g).getFontMetrics();
		
		int xStart = (int) Math.round((-range / 2 + xOffset) / (range / 10));
		for(int i = xStart; i <= xStart + 10; i++)
		{
			double num = i * range / 10;
			String label = Double.toString(num);
			int positionX = toCanvasX(num);
			
			Rectangle2D labelBounds = fm.getStringBounds(label, (Graphics2D) g);
			
			int positionY = originY;
			if(positionY < 0) positionY = (int) labelBounds.getHeight() / 2 + 5;
			else if(positionY > getHeight()) positionY = getHeight() - (int) labelBounds.getHeight() / 2 - 5;
			
			g.setColor(Color.WHITE);
			g.fillRect(positionX  - (int) labelBounds.getWidth() / 2, positionY - (int) labelBounds.getHeight() / 2, (int) labelBounds.getWidth(), (int) labelBounds.getHeight());
			g.setColor(Color.BLACK);
			g.drawString(label, positionX - (int) labelBounds.getWidth() / 2, positionY - (int) labelBounds.getHeight() / 2 + fm.getAscent());
		}
		
		double yRange = range * getHeight() / getWidth();
		int yStart = (int) Math.round((-yRange / 2 + yOffset) / (range / 10));
		int yCount = (int) Math.round(10 * yRange / range);
		
		for(int i = yStart; i <= yStart + yCount; i++)
		{
			double num = i * range / 10;
			String label = Double.toString(num);
			int positionY = toCanvasY(num);
			
			Rectangle2D labelBounds = fm.getStringBounds(label, (Graphics2D) g);
			
			int positionX = originX;
			if(positionX < 0) positionX = (int) labelBounds.getWidth() / 2 + 5;
			else if(positionX > getWidth()) positionX = getWidth() - (int) labelBounds.getWidth() / 2 - 5;
			
			g.setColor(Color.WHITE);
			g.fillRect(positionX  - (int) labelBounds.getWidth() / 2, positionY - (int) labelBounds.getHeight() / 2, (int) labelBounds.getWidth(), (int) labelBounds.getHeight());
			g.setColor(Color.BLACK);
			g.drawString(label, positionX - (int) labelBounds.getWidth() / 2, positionY - (int) labelBounds.getHeight() / 2 + fm.getAscent());
		}
	}
}
