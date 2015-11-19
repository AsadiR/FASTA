import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Sequence implements Comparable<Sequence> {
    public String name;
    public String value;


    public int gap_counter;

    @Override
    public int compareTo(Sequence o) {
        if (score>o.score)
            return 1;
        else if (score==o.score)
            return  0;
        else
            return -1;
    }

    public HashMap<Character, ArrayList<Integer>> findElemsMap;
    //то с чем происходило выравнивание
    public String query;
    public double score=0;

    @Override
    public String toString() {
        return value;
    }

    public Sequence(Scanner sc) {
        gap_counter = 0;
        name = sc.nextLine();
        name = name.replace(">", "");
        value="";
        while (sc.findInLine(">")==null && sc.hasNext()) {
            value+=sc.nextLine();
        }
        createFindElemsMap();
    }

    public Sequence(String name, String value) {
        gap_counter = 0;
        this.name = name;
        this.value = value;
        createFindElemsMap();
    }

    private void createFindElemsMap() {
        findElemsMap = new HashMap<Character, ArrayList<Integer>>(value.length());
        int i=0;
        for (Character ch : value.toCharArray()) {
            if (findElemsMap.get(ch)==null) findElemsMap.put(ch, new ArrayList<Integer>());
            findElemsMap.get(ch).add(i);
            i++;
        }
    }

    public void restore() {
        value = value.replaceAll("-", "");
        gap_counter = 0;
    }

}
