
import java.util.ArrayList;
import java.util.Arrays;

public class Library {
    private AlignMatrix matrix;
    private int k;
    private int w;
    private Sequence[] bank;

    public Library(AlignMatrix matrix, int k, int w, Sequence[] bank) {
        this.matrix = matrix;
        this. k = k;
        this.w = w;
        this.bank = bank;
        int i;
    }

    public Sequence[] fasta(final Sequence query) {
        Sequence[] res = new Sequence[bank.length];
        for (int i=0; i<bank.length; i++) {
            res[i] = findAligning(bank[i],query);
        }
        Arrays.sort(res);
        return res;
    }

    private Sequence findAligning(final Sequence seq, final Sequence query) {
        SparseArray<Double> sa = new SparseArray<Double>();
        Sequence seqRes = new Sequence(seq.name, "");
        String queryStr ="";
        double score = 0;
        int n = seq.value.length();
        int m = query.value.length();
        int j;


        for (j=0; j<m; j++) {
            ArrayList<Integer> indexes = seq.findElemsMap.get(query.value.charAt(j));
            if (indexes==null)
                continue;
            for (int i : indexes) {
                char ch1 = seq.value.charAt(i);
                char ch2 = query.value.charAt(j);
                //System.out.println(ch1+" "+ch2+" "+matrix.getScore(ch1,ch2));
                if (matrix.getScore(ch1,ch2)>=0) sa.set(i+1,j+1,1d);
            }
        }

        for (j=0; j<m; j++) {
            ArrayList<Integer> indexes = seq.findElemsMap.get(query.value.charAt(j));
            if (indexes==null)
                continue;
            for (int i : indexes) {
                char ch1 = seq.value.charAt(i);
                char ch2 = query.value.charAt(j);
                //System.out.println(ch1+" "+ch2+" "+matrix.getScore(ch1,ch2));
                if (matrix.getScore(ch1,ch2)>=0) sa.set(i+1,j+1,1d);
            }
        }




        for (int i=0; i<n+1; i++) {
            for (j=0; j<m+1; j++) {
                Double d = sa.get(i,j);
                System.out.print((d==null?0:d)+" ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");


        seqRes.query = queryStr;
        seqRes.score = score;
        return seqRes;
    }
}
