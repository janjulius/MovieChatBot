package chart;

public class LineDataType {
    private double v;
    private Comparable r, c;
    public LineDataType(
            double value,
            Comparable rowKey,
            Comparable columnKey
    ){
        v = value;
        r = rowKey;
        c = columnKey;
    }
    public double getValue(){ return v;}

    public Comparable getRowKey(){return r;}
    public Comparable getColumnKey(){return c;}

}
