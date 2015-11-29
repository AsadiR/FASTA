import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class StartClass {
    public static void main(String[] argv) throws Exception {
        Scanner inputSc = new Scanner(System.in);

        System.out.println("Blossum matrix считана из файла matrix.txt");
        Scanner matrixSc = new Scanner(new File("matrix.txt"));
        AlignMatrix matrix = new AlignMatrix(matrixSc);


        System.out.println("Введите имя файла содержащего базу данных:");
        String nameOfLibFile = inputSc.nextLine();
        if (nameOfLibFile.equals("")) nameOfLibFile = "lib2.txt";
        Scanner libSc = new Scanner(new File(nameOfLibFile));


        int i;
        int n = Integer.parseInt(libSc.nextLine());
        Sequence[] bank = new Sequence[n];
        for (i=0; i<n; i++) {
            bank[i] = new Sequence(libSc);
        }
        Library lib = new Library(matrix, bank);


        System.out.println("Введите минимально количество совпадений в диагонали:");
        int k = Integer.parseInt(inputSc.nextLine());
        System.out.println("Введите ширину полосы:");
        int w = Integer.parseInt(inputSc.nextLine());
        System.out.println("Введите максимальное количество рассматриваемых регионов:");
        int numOfRegions = Integer.parseInt(inputSc.nextLine());
        System.out.println("Введите имя файла содержащего последовательность-запрос в формате FASTA:");
        String nameOfQueryFile = inputSc.nextLine();
        if (nameOfQueryFile.equals("")) nameOfQueryFile = "Query.txt";

        Scanner querySc = new Scanner(new File(nameOfQueryFile));
        Sequence q = new Sequence(querySc);



        ArrayList<Sequence> res = lib.fasta(q,k,w,numOfRegions);

        FileWriter writer = new FileWriter("output.txt");

        for (Sequence p : res) {
            System.out.println("name:" + p.name);
            writer.write("name:"+p.name+'\n');
            System.out.println("score:" + p.score);
            writer.write("score:" + p.score +'\n');
            System.out.println("query/libSeq");
            writer.write("query/libSeq\n");
            System.out.println(p.query);
            writer.write(p.query+'\n');
            System.out.println(p.value);
            writer.write(p.value+'\n');
            System.out.println("----------");
            writer.write("----------\n");
            writer.flush();
        }
        System.out.println("Вывод программы сохранен в файл output.txt");

        writer.close();
        matrixSc.close();
        libSc.close();
        querySc.close();

    }
}
