import java.util.Arrays;
public class createKeys {
    public static void main(String[] args){
        int q = 3329;
        int[] f = {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1};
        int[][] s = new int[2][256];
        int[][][] A = new int[2][2][256];
        int[][] t = new int[2][256];
        int[][] e = new int[2][256];
        KYBEROperations ops = new KYBEROperations();

        for (int i = 0; i < s.length; i++) {
            s[i] = ops.polyRandom(256, -2, 2);
        }

        for (int i = 0; i < A.length; i++) {
            for(int j = 0; j < A[i].length; j++){
                A[i][j] = ops.polyRandom(256, 0, 3329);
            }
        }

        for (int i = 0; i < e.length; i++) {
            e[i] = ops.polyRandom(256, -2, 2);
        }

        for(int i = 0; i < A[0].length; i++){
            int[] term;
            int[] forMod = new int[A[0][0].length + s[0].length - 1];
            for(int j = 0; j < A.length; j++){
                int[] temp = ops.polyDist(A[i][j], s[j]);

                for(int k = 0; k < temp.length; k++){
                    forMod[k] += temp[k];
                }
            }
            term = ops.fullMod(forMod, f, q);
            for(int l = 0; l < term.length; l++){
                term[l] += e[i][l];
            }
            t[i] = term;
        }

        System.out.println(Arrays.toString(s[0]));
        System.out.println(Arrays.toString(s[1]));
        System.out.println(Arrays.toString(A[0][0]));
        System.out.println(Arrays.toString(A[0][1]));
        System.out.println(Arrays.toString(A[1][0]));
        System.out.println(Arrays.toString(A[1][1]));
        System.out.println(Arrays.toString(t[0]));
        System.out.println(Arrays.toString(t[1]));
    }
}
