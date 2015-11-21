
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Library {
    private AlignMatrix matrix;
    private Sequence[] bank;

    public Library(AlignMatrix matrix, Sequence[] bank) {
        this.matrix = matrix;
        this.bank = bank;
        int i;
    }

    public ArrayList<Sequence> fasta(final Sequence query, int k, int w, int numOfReg) {
        // k - минимально необходимое количество совпадений в диагонали
        // w - ширина полосы, при расширении региона в ширину
        // numOfReg - количество рассматриваемых регионов (регионы сортируются по score, берется numOfReg лучших регионов)
        ArrayList<Sequence> res = new ArrayList<Sequence>();
        for (int i=0; i<bank.length; i++) {
            Sequence s = findAligning(bank[i], query, k, w, numOfReg);
            if (s.score!=0) res.add(s);
        }
        Collections.sort(res, Collections.reverseOrder());
        return res;
    }

    private Sequence findAligning(final Sequence seq, final Sequence query, int k, int w, int numOfReg) {
        SparseArray<DotPlotElem> sa = new SparseArray<DotPlotElem>();
        Sequence seqRes = new Sequence(seq.name, "");
        seqRes.query = "";
        int n = seq.value.length();
        int m = query.value.length();
        int j;


        // строим дот плот
        for (j = 0; j < m; j++) {
            ArrayList<Integer> indexes = seq.findElemsMap.get(query.value.charAt(j));
            if (indexes == null)
                continue;
            for (int i : indexes) {
                char ch1 = seq.value.charAt(i);
                char ch2 = query.value.charAt(j);
                if (ch1 == ch2) sa.set(i, j, new DotPlotElem(i, j));
            }
        }

        Collection<DotPlotElem> values = sa.getNotNullValues();
        DotPlotElem[] arrValues = new DotPlotElem[values.size()];
        arrValues = values.toArray(arrValues);
        Arrays.sort(arrValues);

        ArrayList<Region> regions = new ArrayList<Region>();


        // проверка условия d.length >= k
        for (DotPlotElem d : arrValues) {
            if (d.mark == DotPlotElem.RAW) {
                // спускаемся вниз по диагонали
                int lengthOfDiagonal = 0;
                for (DotPlotElem e = d; e != null; e = sa.get(e.i + 1, e.j + 1))
                    lengthOfDiagonal++;

                if (lengthOfDiagonal >= k) {
                    int beginI = d.i;
                    int beginJ = d.j;
                    int endI = d.i + lengthOfDiagonal - 1;
                    int endJ = d.j + lengthOfDiagonal - 1;

                    //инициализируем регион
                    Region r = new Region(beginI, beginJ, endI, endJ);
                    double regionScore = 0;
                    for (DotPlotElem e = d; e != null; e = sa.get(e.i + 1, e.j + 1)) {
                        char ch1 = seq.value.charAt(e.i);
                        char ch2 = query.value.charAt(e.j);
                        regionScore += matrix.getScore(ch1, ch2);
                        e.mark = DotPlotElem.REGION;
                    }
                    r.score = regionScore;
                    //System.out.println("old:" + r);

                    regionLengthExpand(r, n, m, seq, query);

                    regions.add(r);
                } else {
                    for (DotPlotElem e = d; e != null; e = sa.get(e.i + 1, e.j + 1))
                        e.mark = DotPlotElem.BADREGION;
                }


            }
        }

        Collections.sort(regions, Collections.reverseOrder());
        int i=0;
        //отбираю numOfReg наиболее скорных регионов и выполняю расширение
        for (Region r : regions) {
            if (i<numOfReg) {
                //заполняем расширенный регион в разряженном массиве
                for (j = 0; j < r.length(); j++) {
                    if (sa.get(r.beginI + j, r.beginJ + j) == null) {
                        DotPlotElem elem = new DotPlotElem(r.beginI + j, r.beginJ + j);
                        sa.set(r.beginI + j, r.beginJ + j, elem);
                    }
                    sa.get(r.beginI + j, r.beginJ + j).mark = DotPlotElem.REGION;
                }
                regionWidthExpand(r, n, m, w, sa);

            } else {
                for (j = 0; j < r.length(); j++) {
                    if (sa.get(r.beginI + j, r.beginJ + j) != null) {
                        sa.get(r.beginI + j, r.beginJ + j).mark = DotPlotElem.BADREGION;
                        sa.get(r.beginI + j, r.beginJ + j).score = 0;
                    }
                }
            }
            i++;
        }

        arrValues = values.toArray(arrValues);
        Arrays.sort(arrValues);

        DotPlotElem max = null;

        for (DotPlotElem elem : arrValues) {
            if (elem.mark==DotPlotElem.BADREGION) continue;
            char ch1 = seq.value.charAt(elem.i);
            char ch2 = query.value.charAt(elem.j);
            DotPlotElem leftAndUpElem = sa.get(elem.i - 1, elem.j - 1);
            DotPlotElem leftElem = sa.get(elem.i, elem.j - 1 );
            DotPlotElem upElem = sa.get(elem.i - 1, elem.j );


            double leftAndUpValue = (leftAndUpElem!=null?leftAndUpElem.score:0) + matrix.getScore(ch1,ch2);
            double leftValue = (leftElem!=null?leftElem.score:0) + matrix.getScore('-',ch2);
            double upValue = (upElem!=null?upElem.score:0) + matrix.getScore(ch1,'-');

            elem.score = max(0, leftAndUpValue, leftValue, upValue);

            if (max ==null || elem.score > max.score) {
                max = elem;
            }
        }



        for (DotPlotElem cur = max; cur!=null && cur.score!=0;) {
            char ch1 = seq.value.charAt(cur.i);
            char ch2 = query.value.charAt(cur.j);
            DotPlotElem leftAndUpElem = sa.get(cur.i - 1, cur.j - 1);
            DotPlotElem leftElem = sa.get(cur.i , cur.j - 1 );
            DotPlotElem upElem = sa.get(cur.i - 1 , cur.j );

            if (cur.score == (leftAndUpElem!=null?leftAndUpElem.score:0) + matrix.getScore(ch1,ch2)) {
                seqRes.value = ch1 + seqRes.value;
                seqRes.query = ch2 + seqRes.query;
                cur = sa.get(cur.i - 1, cur.j - 1);
                continue;
            }
            if (cur.score == (leftElem!=null?leftElem.score:0) + matrix.getScore('-',ch2)) {
                seqRes.value = '-' + seqRes.value;
                seqRes.query = ch2 + seqRes.query;
                cur = sa.get(cur.i, cur.j - 1);
                continue;
            }
            if (cur.score == (upElem!=null?upElem.score:0) + matrix.getScore(ch1,'-')) {
                seqRes.value = ch1 + seqRes.value;
                seqRes.query = '-' + seqRes.query;
                cur = sa.get(cur.i - 1, cur.j);
            }
        }





/*        for (Region r: regions)
            System.out.println(r);

        for (i=0; i<n; i++) {
            for (j=0; j<m; j++) {
                DotPlotElem d = sa.get(i,j);
                System.out.print((d==null?0:d.mark)+" ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");

        for (i=0; i<n; i++) {
            for (j=0; j<m; j++) {
                DotPlotElem d = sa.get(i,j);
                System.out.print((d==null?0:d.score)+"      ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");*/

        seqRes.score = (max!=null ? max.score : 0);
        return seqRes;
    }

    private double max(double ... a ) {
        double max = a[0];
        for (double e : a)
            if (e>max) max = e;
        return max;
    }

    private void regionLengthExpand(final Region r, int n, int m, final Sequence seq, final Sequence query) {
        double curRegionScore = r.score;
        int t;

        int maxEndI = r.endI;
        int maxEndJ = r.endJ;

        // ищем максимальный скор при расширении вниз
        for (t=1; curRegionScore>0; t++) {
            if (r.endI+t >= n || r.endJ+t >= m)  break;
            char ch1 = seq.value.charAt(r.endI + t);
            char ch2 = query.value.charAt(r.endJ + t);
            curRegionScore += matrix.getScore(ch1, ch2);
            if (r.score <= curRegionScore) {
                r.score = curRegionScore;
                maxEndI = r.endI + t;
                maxEndJ = r.endJ + t;
            }
        }

        int minBeginI = r.beginI;
        int minBeginJ = r.beginJ;
        curRegionScore = r.score;

        // ищем максимальный скор при расширении вверх
        for (t=1; curRegionScore>0; t++) {
            if (r.beginI-t < 0 || r.beginJ-t < 0)  break;
            char ch1 = seq.value.charAt(r.beginI - t);
            char ch2 = query.value.charAt(r.beginJ - t);
            curRegionScore += matrix.getScore(ch1, ch2);
            if (r.score <= curRegionScore) {
                r.score = curRegionScore;
                minBeginI = r.beginI - t;
                minBeginJ = r.beginJ - t;
            }
        }

        r.beginI = minBeginI;
        r.beginJ = minBeginJ;
        r.endI = maxEndI;
        r.endJ = maxEndJ;
    }

    private void regionWidthExpand(final Region r, int n, int m, int w, SparseArray<DotPlotElem> sa) {
        int i,j,t;
        for (t=0; t<=r.length(); t++) {
            int pI = r.beginI+t-w;
            int pJ = r.beginJ+t-w;
            for (i=pI; i<pI+2*w+1; i++) {
                if (i<0 || i>=n) continue;
                for (j=pJ; j<pJ+2*w+1; j++) {
                    if (j<0 || j>=m) continue;
                    if (sa.get(i,j)==null) {
                        DotPlotElem elem = new DotPlotElem(i,j);
                        elem.mark = DotPlotElem.WIDTH_EXTENSION;
                        sa.set(i,j,elem);
                    }
                }
            }

        }
    }
}
