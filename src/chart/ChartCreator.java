package chart;

import org.jfree.chart.ChartUtilities;
import org.omg.CORBA.portable.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ChartCreator {

    private ImageTypeSpecifier objJFreechart;
    BufferedImage objBufferedImage = objJFreechart.createBufferedImage(600, 800);
    ByteArrayOutputStream bas = new ByteArrayOutputStream();

    public File getChartImage() {
        //ChartUtilities.writeChartAsPNG(ouputstream, jfreechart, x, y)
        return null;
    }
}
