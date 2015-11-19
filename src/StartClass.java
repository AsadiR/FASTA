import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class StartClass {
    public static void main(String[] argv) throws FileNotFoundException {
        Scanner matrixSc = new Scanner(new File("matrix.txt"));
        AlignMatrix matrix = new AlignMatrix(matrixSc);
        //System.out.println(matrix.getScore('A', '-'));
        Scanner libSc = new Scanner(new File("lib.txt"));
        int i;
        int n = Integer.parseInt(libSc.nextLine());
        Sequence[] bank = new Sequence[n];
        for (i=0; i<n; i++) {
            bank[i] = new Sequence(libSc);
        }
        Library lib = new Library(matrix, 2, 1, bank);
        Scanner querySc = new Scanner(new File("Query.txt"));
        Sequence q = new Sequence(querySc);

        Sequence[] res = lib.fasta(q);
        for (Sequence p : res) {
            System.out.println("name:" + p.name);
            System.out.println("score:" + p.score);
            System.out.println("query/libSeq");
            System.out.println(p.query);
            System.out.println(p.value);
            System.out.println("----------");
        }

/*
        for (int j : q.findElemsMap.get('A'))
            System.out.println(j);

        Pair<Integer, Integer> p1 = new Pair<Integer, Integer>(1,2);
        Pair<Integer, Double> p2 = new Pair<Integer, Double>(1,2d);
        Pair<Integer, Integer> p3 = new Pair<Integer, Integer>(1,2);
        SparseArray<Integer>  array = new SparseArray<Integer>();
        array.set(1, 1, 2);
        array.set(1, 3, 2);
        //System.out.println(array.get(1,1));

        for (int k : array) {
            System.out.println(k);
        }
*/

    }
}
