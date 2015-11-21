import com.sun.istack.internal.NotNull;

public class Region implements Comparable<Region> {
    public int beginI;
    public int beginJ;
    public int endI;
    public int endJ;
    public double score;

    @Override
    public String toString() {
        return "region[" + score + "]:"
                + "(" + beginI + "," + beginJ + ") - (" + endI + "," + endJ +")";
    }

    public Region(int beginI, int beginJ, int endI, int endJ) {
        this.beginI = beginI;
        this.beginJ = beginJ;
        this.endI = endI;
        this.endJ = endJ;
    }
    public int length() {
        return endI - beginI + 1;
    }

    @Override
    public int compareTo(@NotNull Region  o) {
        if (score>o.score)
            return 1;
        else if (score==o.score)
            return  0;
        else
            return -1;
    }
}
