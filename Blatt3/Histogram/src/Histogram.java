import java.awt.Dimension;
import java.util.Random;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;


class Histogram extends ApplicationFrame
{
	static int instance_counter = 0;
	
	public Histogram(int[] hist,String title)
	{
		super(title);
		instance_counter++;
			
		final IntervalXYDataset dataset = createDataset(hist);
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartPanel);
        
        this.pack();
        
        
        RefineryUtilities.centerFrameOnScreen(this);
        Random rn = new Random();
        int offset = (rn.nextInt() % 10) + instance_counter*25;
        this.setLocation(this.getLocation().x+offset, this.getLocation().y+offset);
        this.setVisible(true);
	}

	private JFreeChart createChart(IntervalXYDataset dataset)
	{
		return ChartFactory.createXYBarChart("Histogram", "Greyvalue", false,
				"Occurrence", dataset, PlotOrientation.VERTICAL, true, true,
				false);
	}

	private IntervalXYDataset createDataset(int[] histogramm)
	{
		XYSeries dataset = new XYSeries("Histogram");
		
		for(int i = 0; i < histogramm.length; i++ )
		{
			dataset.add(i,histogramm[i]);
		}
		
		return new XYSeriesCollection(dataset);
	}
	
	private static final long serialVersionUID = -94821631433170012L;	
}