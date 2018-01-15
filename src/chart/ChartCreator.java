package chart;

import Util.Settings;
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

    int width = 800, height = 800;

    String HorizontalLabel = "HorizontalLabel", VerticalLabel = "VerticleLable";

    public File getChartImage(String chartName){
        try {
            if(chartName == null)
                chartName = "out\\";

            //dataset = createDataset(typeList);

            OutputStream out = new FileOutputStream(chartName);

            chart = ChartFactory.createBarChart(
                    chartName,
                    HorizontalLabel,
                    VerticalLabel,
                    dataset);

            if(Settings.DEBUG_MODE){
                System.out.println("Attempting to create line diagram");
            }

            ChartUtilities.writeChartAsPNG(out,
                    chart,
                    width,
                    height);

            return new File("Results");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void SetVerticleLabel(String v){
        VerticalLabel = v;
    }

    public void SetHorizontalLabel(String v){
        HorizontalLabel = v;
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

    public void AddToDataSet(LineDataType dataObject, DefaultCategoryDataset dSet){
        dSet.addValue(dataObject.getValue(), dataObject.getRowKey(), dataObject.getColumnKey());
    }

    public DefaultCategoryDataset getDataset() {
        return dataset;
    }
}
