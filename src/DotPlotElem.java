
public class DotPlotElem implements Comparable<DotPlotElem> {
    public static final int RAW = 1;
    public static final int BADREGION = 2;
    public static final int REGION = 3;
    public static final int WIDTH_EXTENSION = 4;
    public int mark = RAW;
    public double score = 0d;
    public int i;
    public int j;
    public DotPlotElem(int i, int j) {
        this.i = i;
        this.j = j;
    }


    @Override
    public int compareTo(DotPlotElem o) {
        if (i>o.i || i==o.i && j>o.j) return 1;
        if (i==o.i && j==o.j) return 0;
        return -1;
    }

    @Override
    public String toString() {
        return "("+i+","+j+")";
    }
}
