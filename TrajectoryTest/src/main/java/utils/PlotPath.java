package utils;


import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import javax.swing.JFrame;
import javax.swing.JPanel;


//import org.usfirst.frc.team159.robot.TestPlot.DrawSine;

public class PlotPath extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ArrayList<PathData> list = new ArrayList<>();
	private static final int PREF_W = 800;
	private static final int PREF_H = 650;
	private static final int MIN_H = 400;
	private static final int MAX_H = 800;
	protected double tmax=0;
	protected double ymin=1000;
	protected double ymax=-1000;
	protected double xmin=1000;
	protected double xmax=-1000;
	protected double aspect=1;
	protected int plot_width=PREF_W;
	protected int plot_height=PREF_H;
	protected int traces;
	public static final int TIME_MODE=0;
	public static final int XY_MODE=1;

	private int plotMode=TIME_MODE;
	private String labels[]=null;

	static public Color colors[]= {
			Color.BLUE,Color.RED,Color.GREEN,Color.ORANGE,Color.DARK_GRAY,Color.GRAY};
	
	public PlotPath(ArrayList<PathData> d, int n) {
		this(d,n,TIME_MODE);
	}
	public PlotPath(ArrayList<PathData> d, int n, int m) {
		this(d,n,m,null);
	}
	public PlotPath(ArrayList<PathData> d, int n, int m, String[] l) {
		labels=l;
		plotMode=m;
		list.addAll(d);
		traces=n;
		getLimits();

		setSize(plot_width, plot_height);
		setTitle("Path Plot");
		setLocationRelativeTo(null);
		add(new DrawPlot(), BorderLayout.CENTER);
		pack();
		setVisible(true);
	}
	void getLimits() {
		for(PathData  data : list) {
			tmax=data.tm>tmax?data.tm:tmax;	
			for (int i=0;i< PathData.DATA_SIZE;i++) {		
				if(plotMode==XY_MODE){
					xmax=data.d[i]>xmax?data.d[i]:xmax;
					xmin=data.d[i]<xmin?data.d[i]:xmin;
					i++;
				}
				ymax=data.d[i]>ymax?data.d[i]:ymax;
				ymin=data.d[i]<ymin?data.d[i]:ymin;			
			}
		}
		if(plotMode==TIME_MODE){
			xmax=tmax;
			xmin=0;
		}
		else{
			double dx=xmax-xmin;
			double dy=ymax-ymin;
			aspect=dx/dy;
			plot_width=(int)(PREF_W);
			plot_height=(int)(PREF_H/aspect);
			if(aspect>1 && plot_height<MIN_H){
				plot_height=MIN_H;
				plot_width=(int)(plot_height*aspect);
			}
			else if (aspect<1 && plot_height>MAX_H){
				plot_height=MAX_H;
				plot_width=(int)(plot_height*aspect);
			}
		}	
		System.out.println("PlotPath: xmax="+xmax+" xmin="+xmin+" ymax="+ymax+" ymin="+ymin+" aspect:"+aspect);
	}
	class DrawPlot extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int padding = 25;
		private int labelPadding = 35;
		private Color gridColor = new Color(200, 200, 200, 200);
		private int pointWidth = 2;
		private int numberYDivisions = 10;
		private int numberXDivisions = 10;

		private Stroke LINE_STROKE = new BasicStroke(1f);
		private Stroke GRAPH_STROKE = new BasicStroke(2f);
		private Stroke DASHED = new BasicStroke(3f, BasicStroke.CAP_ROUND,
		        BasicStroke.JOIN_ROUND, 3.0f, new float[]{5,5}, 0.0f);

		
		public DrawPlot() {
			setPreferredSize(new Dimension(plot_width, plot_height));
		}

	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        Graphics2D g2 = (Graphics2D) g.create();
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (xmax-xmin);
			double yScale = ((double) getHeight() - (2 * padding) - labelPadding) /  (ymax-ymin);

			//System.out.println("w="+ getWidth()+" h="+getHeight()+" xScale="+xScale+" yscale="+yScale);

			// draw white background
			g2.setColor(Color.WHITE);
			g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
			g2.setColor(Color.BLACK);

			//g2.setStroke(GRAPH_STROKE);

			// create hatch marks and grid lines for y axis.
			for (int i = 0; i < numberYDivisions + 1; i++) {
				int x0 = padding + labelPadding;
				int x1 = pointWidth + padding + labelPadding;
				int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
				int y1 = y0;
				if (list.size() > 0) {
					g2.setColor(gridColor);
					g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
					g2.setColor(Color.BLACK);
					String yLabel = ((int) ((ymin + (ymax - ymin) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
					FontMetrics metrics = g2.getFontMetrics();
					int labelWidth = metrics.stringWidth(yLabel);
					g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
				}
				g2.drawLine(x0, y0, x1, y1);
			}

			// and for x axis
			for (int i = 0; i < numberXDivisions + 1; i++) {
				int x0 = i * (getWidth() - padding * 2 - labelPadding) / numberXDivisions + padding + labelPadding;
				int x1 = x0;
			    int y0 = getHeight() - padding - labelPadding;
			    int y1 = y0 - pointWidth;
				if (list.size() > 0) {
					g2.setColor(gridColor);
					g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
					g2.setColor(Color.BLACK);
					String xLabel = ((int) ((xmin + (xmax - xmin) * ((i * 1.0) / numberXDivisions)) * 100)) / 100.0 + "";
					FontMetrics metrics = g2.getFontMetrics();
					int labelWidth = metrics.stringWidth(xLabel);
					g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
				}
				g2.drawLine(x0, y0, x1, y1);
			}
			// double last_tick=0;
			// for (int i = 0; i < list.size(); i++) {
			// 	double new_tick=Math.round(list.get(i).tm);
			// 	if (new_tick>last_tick) {
			// 		int x0 = i * (getWidth() - padding * 2 - labelPadding) / (list.size() - 1) + padding + labelPadding;
			// 		int x1 = x0;
			// 		int y0 = getHeight() - padding - labelPadding;
			// 		int y1 = y0 - pointWidth;
			// 		g2.setColor(gridColor);
			// 		g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
			// 		g2.setColor(Color.BLACK);
			// 		String xLabel = Math.round(list.get(i).tm) + "";
			// 		FontMetrics metrics = g2.getFontMetrics();
			// 		int labelWidth = metrics.stringWidth(xLabel);
			// 		g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
			// 		last_tick=new_tick;
			// 		g2.drawLine(x0, y0, x1, y1);
			// 	}				
			// }

			// create x and y axes 
			g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
			g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);
			
			String xAxisLabel="Time";
			if(plotMode==XY_MODE)
				 xAxisLabel="X";
			int x0 =  (getWidth() - padding * 2 - labelPadding) / 2 + padding + labelPadding;
			int y0 = getHeight() - labelPadding;
			FontMetrics metrics = g2.getFontMetrics();
			int labelWidth = metrics.stringWidth(xAxisLabel);

		    g2.drawString(xAxisLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
		    int[] xs = new int[list.size()];
			int[] ys = new int[list.size()];
			if (plotMode == TIME_MODE) {
				for (int i = 0; i < list.size(); i++) {
					xs[i] = (int) (list.get(i).tm * xScale + padding + labelPadding);
				}
				for (int j = 0; j < traces; j++) {
					g2.setColor(colors[j]);
					for (int i = 0; i < list.size(); i++) {
						ys[i] = (int) ((ymax - list.get(i).d[j]) * yScale + padding);
					}
					if ((j % 2) == 0)
						g2.setStroke(GRAPH_STROKE);
					else
						g2.setStroke(DASHED);
					g2.drawPolyline(xs, ys, list.size());
				}
			}
			else{
				for (int j = 0; j < traces; j++) {
					g2.setColor(colors[j]);
					for (int i = 0; i < list.size(); i++) {
						xs[i] = (int) ((list.get(i).d[2*j]) * xScale + padding+labelPadding);
						ys[i] = (int) ((ymax - list.get(i).d[2*j+1]) * yScale + padding);
					}
					if ((j % 2) == 0)
						g2.setStroke(GRAPH_STROKE);
					else
						g2.setStroke(DASHED);
					g2.drawPolyline(xs, ys, list.size());
				}
			}
			// draw optional legend
			if(labels != null){
				int legend_line_length = 50;
				int legend_width =0;
				int spacing=metrics.getHeight()+5;
				int legend_height=labels.length*(spacing);
				for (int i=0;i<labels.length;i++){
					labelWidth = metrics.stringWidth(labels[i]);
					legend_width=labelWidth>legend_width?labelWidth:legend_width;
				}
				legend_width+=legend_line_length+30;
				int top = padding+20;
				int left = padding + labelPadding+10;
				g2.setStroke(LINE_STROKE);
				g2.setColor(Color.WHITE);
				g2.fillRect(left, top, legend_width, legend_height);
				g2.setColor(Color.BLACK);

				g2.drawRect(left, top, legend_width, legend_height);
				left+=10;
				top+=10;
				for (int j=0;j<labels.length;j++){
					g2.setColor(colors[j]);
					if ((j % 2) == 0)
						g2.setStroke(GRAPH_STROKE);
					else
						g2.setStroke(DASHED);
					g2.drawLine(left,top,left+legend_line_length,top);
					g2.setColor(Color.BLACK);
					g2.setStroke(LINE_STROKE);
					g2.drawString(labels[j], left+legend_line_length+10, top+5);
					top+=spacing;
				}
			}
		    g2.dispose();
	    }    
	 }
}
