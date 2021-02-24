public class Main1 {


    public static void showSubsets(int [][] list,int nSubsets,int t) {
        for (int i=0; i < nSubsets; i++) {
            for (int j = 0; j < t; j++)
                System.out.print(list[i][j]+" ");
            System.out.println();
        }
    }

    public static int [][] subsets(int [] list,int t) {
        int [][] sub = new int[list.length-t+1][t];

        for (int i=0; i < list.length-t+1; i++) {
            for (int j = 0; j < t; j++)
                sub[i][j] = list[i + j];
        }
        return sub;
    }


    public static void main(String[] s) {
        int l [] = {3,4,5,1,2};
        int t = 3;

        int sub [][] = subsets(l,t);
        showSubsets(sub,l.length-t+1,t);

    }
}
