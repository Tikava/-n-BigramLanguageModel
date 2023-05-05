import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class TableVisualizer {

    public static void main(String[] args) {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};

        CategoryDataset dataset = createDataset(data);
        JFreeChart chart = createChart(dataset);
        BufferedImage image = chart.createBufferedImage(800, 600);
        saveImage(image, "table.png");
    }

    private static CategoryDataset createDataset(double[][] data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                dataset.addValue(data[i][j], "Row " + i, "Column " + j);
            }
        }
        return dataset;
    }

    private static JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
                "Table Visualization",
                null,
                null,
                dataset,
                PlotOrientation.HORIZONTAL,
                true,
                true,
                false);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelFont(domainAxis.getTickLabelFont().deriveFont(14.0f));

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickLabelFont(rangeAxis.getTickLabelFont().deriveFont(14.0f));

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        return chart;
    }

    private static void saveImage(BufferedImage image, String fileName) {
        try {
            ImageIO.write(image, "PNG", new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
