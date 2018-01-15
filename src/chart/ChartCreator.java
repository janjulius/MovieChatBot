package chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.omg.CORBA.portable.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

public class ChartCreator {

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    JFreeChart chart;
    ChartPanel panel;

    public void getChartImage(String chartName, List<LineDataType> typeList){
        try {

            if(chartName == null)
                chartName = "a chart";

            dataset = createDataset(typeList);

            OutputStream out = new FileOutputStream(chartName);

            ChartUtilities.writeChartAsPNG(out,
                    chart,
                    panel.getWidth(),
                    panel.getHeight());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private DefaultCategoryDataset createDataset(List<LineDataType> l) {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        for(int i = 0; i < l.size(); i++){
            d.addValue(l.get(i).getValue(),
                    l.get(i).getRowKey(),
                    l.get(i).getColumnKey());
        }
        return d;
    }
}
