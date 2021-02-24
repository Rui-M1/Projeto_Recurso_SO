public class Main3 {


    public static int factorial(int n)
    {
        int fact = 1;

        for (int i = 1; i <= n; i++)
            fact = fact * i;

        return fact;
    }

    public static int [][] permutations(int l[],int nPermu,int n) {

        int k=0;

        int results[][] = new int[nPermu][n];

        int[] indexes = new int[n];
        for (int i = 0; i < n; i++) {
            indexes[i] = 0;
        }

        for (int j=0; j < n; j++)
            results[k][j] = l[j];
        k++;

        int i = 0;
        while (i < n) {
            if (indexes[i] < i) {
                swap(l, i % 2 == 0 ?  0: indexes[i], i);
                for (int j=0; j < n; j++)
                   results[k][j] = l[j];
                indexes[i]++;
                i = 0;
                k++;
            }
            else {
                indexes[i] = 0;
                i++;
            }
        }
        return results;
    }

    private static void swap(int [] input, int a, int b) {
        int tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }


    public static void showPermutations(int [][] list,int nPermutations,int t) {
        for (int i=0; i < nPermutations; i++) {
            for (int j = 0; j < t; j++)
                System.out.print(list[i][j]+" ");
            System.out.println();
        }
    }


    public static void main(String[] s) {
        int list [] = {3,4,5};
        int nPermu = factorial(list.length);

        int [][] perm = permutations(list,nPermu, list.length);
        showPermutations(perm,nPermu, list.length);

    }

}
