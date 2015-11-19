import java.util.HashMap;
import java.util.Scanner;

public class AlignMatrix {
    private HashMap<Character, Integer> charToInt;
    private double[][] matrix;
    public AlignMatrix(Scanner sc) {
        int i=0,j=0;
        int n = Integer.parseInt(sc.nextLine());
        charToInt = new HashMap<Character, Integer>(n);
        matrix = new double[n][];
        for (i=0; i<n; i++)
            matrix[i] = new double[n];
        String alphabet = sc.nextLine();
        alphabet = alphabet.replace(" ","");
        alphabet = alphabet.replace("\n","");
        alphabet = alphabet.replace("\t","");
        String[] symArray = alphabet.split(",");
        if (symArray.length!=n) throw new RuntimeException("Wrong size of alphabet!\n");
        for (i=0; i<n; i++) {
            if (symArray[i].length()>1) throw new RuntimeException("Sym length should not be greater than 1!\n");
            charToInt.put(symArray[i].charAt(0), i);
        }
        String str;
        String[] strArray;
        for (i=0; i<n; i++) {
            str = sc.nextLine();
            strArray = str.split(" ");
            if (strArray.length!=n) throw new RuntimeException("Wrong size of matrix!\n");
            for (j=0; j<n; j++) {
                matrix[i][j] = Double.parseDouble(strArray[j]);
            }
        }
    }
    public double getScore(char sym1, char sym2) {
        return matrix[charToInt.get(sym1)][charToInt.get(sym2)];
    }
}
