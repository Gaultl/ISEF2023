import java.util.Scanner;
import java.util.Arrays;

public class KYBER_8Tests {
    public static void main(String[] args) {
        System.out.println();
        System.out.println();
        System.out.println();

        KYBEROperations ops = new KYBEROperations();
        Scanner input = new Scanner(System.in);
        int q = 17;
        int p = (q+1)/2;
        int q1 = 5;
        int q2 = 13;
        int[] f = {1,0,0,0,1};
        int[][] s = {{0,1,-1,-1},{0,-1,0,-1}};
        int[][][] A = {{{11,16,16,6},{3,6,4,9}},{{1,10,3,5},{15,9,1,6}}};
        int[][] t = {{7,0,15,16},{6,11,12,10}};
        int blocSize = 4;

        double[] encryptTime = new double[30];
        double[] decryptTime = new double[30];
        String inputMessage = " The quick brown fox jumps over the lazy dog.";

        String response = "";

        int totalCount = 0;
        int errorCount = 0;

        for(int z = 0; z < 30; z++) {
            int[][] message = ops.toScaledBinary(inputMessage, blocSize, p);
            int[][] r = {ops.polyRandom(3, -1, 1), ops.polyRandom(3, -1, 1)};
            int[][] e1 = {ops.polyRandom(3, -1, 1), ops.polyRandom(3, -1, 1)};
            int[] e2 = ops.polyRandom(3, -1, 1);
            int[][] u1 = new int[2][blocSize];
            int[][] v1 = new int[message.length][blocSize];

            long enStart = System.nanoTime();

            //encrypt here

            for(int i = 0; i < A[0].length; i++){
                int[] term;
                int[] forMod1 = new int[A[0][0].length + r[0].length - 1];
                for(int j = 0; j < A.length; j++){
                    int[] temp = ops.polyDist(A[j][i], r[j]);

                    for(int k = 0; k < temp.length; k++){
                        forMod1[k] += temp[k];
                    }
                }
                term = ops.fullMod(forMod1, f, q);
                for(int l = 0; l < term.length; l++){
                    term[l] += e1[i][l];
                }
                u1[i] = term;
            }

            int[] term;
            int[] forMod2 = new int[t[0].length + r[0].length - 1];
            for(int j = 0; j < t.length; j++){
                int[] temp = ops.polyDist(t[j], r[j]);

                for(int k = 0; k < temp.length; k++){
                    forMod2[k] += temp[k];
                }
            }
            term = ops.fullMod(forMod2, f, q);
            for(int l = 0; l < term.length; l++){
                term[l] += e2[l];
            }
            for(int i = 0; i < message.length; i++){
                v1[i] = term.clone();
                for(int j = 0; j < message[i].length; j++){
                    v1[i][j] += message[i][j];
                }
            }


            String enU = Arrays.toString(u1[0]).replaceAll("\\p{P}", "") + " , " + Arrays.toString(u1[1]).replaceAll("\\p{P}", "");
            String enV = "";
            for (int[] i: v1) {
                enV += Arrays.toString(i).replaceAll("\\p{P}", "") + "  ";
            }

            long enElapsedTime = System.nanoTime() - enStart; //does it need to move below print statement? it's below in RSA

            encryptTime[z] = enElapsedTime/1000000.0;



            //accepts input of encrypted message from user

            int[][] u = ops.cleanUp(enU, blocSize);
            int[][] v = ops.cleanVp(blocSize, enV);
            int[][] MESSage = new int[v.length][blocSize];

            long start = System.nanoTime();

            //decrypt here

            int[] su;
            int[] forMod = new int[s[0].length + u[0].length - 1];
            for(int j = 0; j < t.length; j++){
                int[] temp = ops.polyDist(s[j], u[j]);

                for(int k = 0; k < temp.length; k++){
                    forMod[k] += temp[k];
                }
            }
            su = ops.fullMod(forMod, f, q);
            for(int i = 0; i < MESSage.length; i++){
                for(int j = 0; j < MESSage[i].length; j++){
                    MESSage[i][j] = v[i][j] - su[j];
                }
                MESSage[i] = ops.coefMod(MESSage[i], q);
            }

            int[] finalMessage = new int[MESSage.length*MESSage[0].length];
            int count = 0;
            for (int[] ints : MESSage) {
                for (int anInt : ints) {
                    if (anInt > q1 && anInt < q2) {
                        finalMessage[count] = 1;
                    } else {
                        finalMessage[count] = 0;
                    }
                    count++;
                }
            }

            String CLEANage = ops.fromBinary(finalMessage);
            if(CLEANage.equals("The quick brown fox jumps over the lazy dog.")){
                long elapsedTime = System.nanoTime() - start;
                decryptTime[z] = elapsedTime/1000000.0;
                totalCount++;
            } else {
                z--;
                totalCount++;
                errorCount++;
            }
        }
        System.out.println("encrypt times:");
        System.out.println(Arrays.toString(encryptTime));
        System.out.println("\ndecrypt times");
        System.out.println(Arrays.toString(decryptTime));
        System.out.println(errorCount);
        System.out.println(totalCount);
    }
}
