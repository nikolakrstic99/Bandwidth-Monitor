import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.xy.*;

public class GraphPanel extends JPanel {
	private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
	private ChartPanel chartPanel1 = new ChartPanel(null), chartPanel2 = new ChartPanel(null);
	private List<Long> times;
	private List<Long> inPackets;
	private List<Long> outPackets;
	private List<Double> inBandwidth;
	private List<Double> outBandwidth;
	private boolean hasData = false;
	private Data data;

	public GraphPanel() {
		setLayout(new GridLayout(2,1));
		add(chartPanel1);
		add(chartPanel2);
	}

	private JFreeChart createPacketsChart() {
		XYSeries series1 = new XYSeries("In");
		XYSeries series2 = new XYSeries("Out");

		long minX = times.get(0), maxX = times.get(times.size() - 1);
		if (minX == maxX)
			maxX++;

		long minY = inPackets.get(0), maxY = inPackets.get(0);
		for (int i = 0; i < times.size(); i++) {
			series1.add(times.get(i), inPackets.get(i));
			series2.add(times.get(i), outPackets.get(i));
			minY = Math.min(minY, Math.min(inPackets.get(i), outPackets.get(i)));
			maxY = Math.max(maxY, Math.max(inPackets.get(i), outPackets.get(i)));
		}
		if (minY == maxY)
			maxY++;

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);

		JFreeChart chart = ChartFactory.createXYLineChart("", "Time", "Bytes", dataset);

		XYPlot plot = chart.getXYPlot();
		NumberAxis domain = (NumberAxis) plot.getDomainAxis();
		NumberAxis range = (NumberAxis) plot.getRangeAxis();
		domain.setRange(minX, maxX);
		range.setRange(0.95 * minY, 1.05 * maxY);

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		renderer.setSeriesPaint(0, Color.RED);
		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		renderer.setSeriesPaint(1, Color.BLUE);
		renderer.setSeriesStroke(1, new BasicStroke(2.0f));

		plot.setRenderer(renderer);
		plot.setBackgroundPaint(TRANSPARENT);
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.LIGHT_GRAY);

		chart.setBackgroundPaint(TRANSPARENT);
		chart.getLegend().setFrame(BlockBorder.NONE);
		chart.getLegend().setBackgroundPaint(TRANSPARENT);

		return chart;
	}

	private JFreeChart createBandwidthChart() {
		XYSeries series1 = new XYSeries("In");
		XYSeries series2 = new XYSeries("Out");

		long minX = times.get(0), maxX = times.get(times.size() - 1);
		if (minX == maxX)
			maxX++;

		double minY = inBandwidth.get(0), maxY = inBandwidth.get(0);
		for (int i = 0; i < times.size(); i++) {
			series1.add(times.get(i), inBandwidth.get(i));
			series2.add(times.get(i), outBandwidth.get(i));
			minY = Math.min(minY, Math.min(inBandwidth.get(i), outBandwidth.get(i)));
			maxY = Math.max(maxY, Math.max(inBandwidth.get(i), outBandwidth.get(i)));
		}
		if (minY == maxY)
			maxY++;

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);

		JFreeChart chart = ChartFactory.createXYLineChart("", "Time", "Bandwidth [bps]", dataset);

		XYPlot plot = chart.getXYPlot();
		NumberAxis domain = (NumberAxis) plot.getDomainAxis();
		NumberAxis range = (NumberAxis) plot.getRangeAxis();
		domain.setRange(minX, maxX);
		range.setRange(0.95 * minY, 1.05 * maxY);

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		renderer.setSeriesPaint(0, Color.RED);
		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		renderer.setSeriesPaint(1, Color.BLUE);
		renderer.setSeriesStroke(1, new BasicStroke(2.0f));

		plot.setRenderer(renderer);
		plot.setBackgroundPaint(TRANSPARENT);
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.LIGHT_GRAY);

		chart.setBackgroundPaint(TRANSPARENT);
		chart.getLegend().setFrame(BlockBorder.NONE);
		chart.getLegend().setBackgroundPaint(TRANSPARENT);

		return chart;
	}

	public void change(String router, String port) {
		if(data!=null)
			data.stop();
		data=new Data(router,port,this);
	}

	public void update() {
		updateData();
		draw();
	}

	public void updateData() {		
		inPackets=data.getInPackets();
		outPackets=data.getOutPackets();
		inBandwidth=data.getInBandwidth();
		outBandwidth=data.getOutBandwidth();
		times=data.getTimes();
		hasData = true;
	}

	public void draw() {
		JFreeChart chart1 = null, chart2 = null;
		//if (hasData) { // ???
			chart1 = createPacketsChart();
			chart2 = createBandwidthChart();
		//}
		this.chartPanel1.setChart(chart1);
		this.chartPanel2.setChart(chart2);
		repaint();
	}

}
