//to display the chart
package com.example.detectionsystem;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.plot.PlotOrientation;
import org.afree.data.xy.XYDataset;
import org.afree.data.xy.XYSeries;
import org.afree.data.xy.XYSeriesCollection;
import org.afree.graphics.SolidColor;

import android.content.Context;
import android.graphics.Color;

public class ChartView extends DemoView
{
	private static XYSeries series=new XYSeries("");
	
	public ChartView(Context c)
	{
		super(c);
		final AFreeChart chart=createChart(createDataset());
		this.setChart(chart);
	}
	
	public static AFreeChart createChart(XYDataset dataset)
	{
		
		AFreeChart chart=ChartFactory.createXYLineChart("Vehicle's Gas Detection System","","ppm of CO", 
				dataset,PlotOrientation.VERTICAL,false,false,false);
        chart.getXYPlot().getDomainAxis().setVisible(false);
        chart.getXYPlot().getRenderer().setSeriesPaintType(0, new SolidColor(Color.BLUE));
        
        return chart;
	}
	
	public static XYDataset createDataset()
	{		
		for(int a=0;a<=60;a++)
		{
			series.add(a,0);
		}
		
		XYSeriesCollection dataset=new XYSeriesCollection();
		dataset.addSeries(series);
		
		return dataset;
	}
	
	public XYSeries getSeries()
	{
		return series;
	}
	
	public void SetSeries(XYSeries s)
	{
		series=s;
	}
}