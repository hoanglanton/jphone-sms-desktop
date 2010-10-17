package it.flaminiandrea.jphonesms.export.html;

import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.CategoryLabelWidthType;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.text.TextBlockAnchor;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

public class BarChart3D {
	private DefaultCategoryDataset dataset;
	private JFreeChart chart;

	public BarChart3D(DefaultCategoryDataset dataset) {
		this.dataset = dataset;
		this.chart = createChart(this.dataset);
	}

	public void saveChartAsPNG(String pathToChartImage) throws IOException {
		File file = new File(pathToChartImage);
		ChartUtilities.saveChartAsPNG(file , this.chart, 550, this.dataset.getColumnCount()*20 + 140);
	}

	private JFreeChart createChart(final CategoryDataset dataset) {

		final JFreeChart chart = ChartFactory.createBarChart3D(
				"jPhone SMS Desktop CHART",  // chart title
				"Contacts",                  // domain axis label
				"Number of messages",        // range axis label
				dataset,                     // data
				PlotOrientation.HORIZONTAL,  // orientation
				false,                       // include legend
				true,                        // tooltips
				false                        // urls
		);

		final CategoryPlot plot = chart.getCategoryPlot();
		plot.setForegroundAlpha(1.0f);

		final CategoryAxis axis = plot.getDomainAxis();
		final CategoryLabelPositions p = axis.getCategoryLabelPositions();

		final CategoryLabelPosition left = new CategoryLabelPosition(
				RectangleAnchor.LEFT, TextBlockAnchor.CENTER_LEFT, 
				TextAnchor.CENTER_LEFT, 0.0,
				CategoryLabelWidthType.RANGE, 0.30f
		);
		axis.setCategoryLabelPositions(CategoryLabelPositions.replaceLeftPosition(p, left));

		return chart;        

	}

}

