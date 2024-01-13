import java.util.Scanner;
import java.util.Arrays;

public class babyKYBER {
    public static void main(String[] args) {
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

        String response = "";

        /*BigInteger messArray = new BigInteger(message.getBytes());

        System.out.println(messArray.toString(2));
        System.out.println(Arrays.toString(ops.toBinary(message)));*/

        //System.out.println(Arrays.toString(ops.polyDist(test,A[0][0])));

        /* rounded array -> binary -> ascii -> message

        int[] H = {0,17,0,0,17,0,0,0};

        H = Arrays.stream(H).map(c -> c/q).toArray();

        String binary = Arrays.toString(H).replaceAll("\\p{P}", "").replaceAll(" ", "");

        int ascii = Integer.parseInt(binary, 2);

        System.out.println(Arrays.toString(H));*/

        while(true) {
            System.out.println("Do you want to encrypt or decrypt? enter done to finish");
            response = input.nextLine().toLowerCase();
            while (!response.equals("encrypt") && !response.equals("decrypt") && !response.equals("done")) {
                System.out.println("Please type 'encrypt', 'decrypt', or 'done'");
                response = input.nextLine().toLowerCase();
            }



            if (response.equals("encrypt")) {
                //accepts an input of characters from the user
                System.out.println("Enter the text you wish to encrypt");
                String plainText = " " + input.nextLine();

                int[][] message = ops.toScaledBinary(plainText, blocSize, p);
                int[][] r = {ops.polyRandom(3, -1, 1), ops.polyRandom(3, -1, 1)};
                int[][] e1 = {ops.polyRandom(3, -1, 1), ops.polyRandom(3, -1, 1)};
                int[] e2 = ops.polyRandom(3, -1, 1);
                int[][] u = new int[2][blocSize];
                int[][] v = new int[message.length][blocSize];

                long start = System.currentTimeMillis();

                //encrypt here

                for(int i = 0; i < A[0].length; i++){
                    int[] term;
                    int[] forMod = new int[A[0][0].length + r[0].length - 1];
                    for(int j = 0; j < A.length; j++){
                        int[] temp = ops.polyDist(A[j][i], r[j]);

                        for(int k = 0; k < temp.length; k++){
                            forMod[k] += temp[k];
                        }
                    }
                    term = ops.fullMod(forMod, f, q);
                    for(int l = 0; l < term.length; l++){
                        term[l] += e1[i][l];
                    }
                    u[i] = term;
                }

                int[] term;
                int[] forMod = new int[t[0].length + r[0].length - 1];
                for(int j = 0; j < t.length; j++){
                    int[] temp = ops.polyDist(t[j], r[j]);

                    for(int k = 0; k < temp.length; k++){
                        forMod[k] += temp[k];
                    }
                }
                term = ops.fullMod(forMod, f, q);
                for(int l = 0; l < term.length; l++){
                    term[l] += e2[l];
                }
                for(int i = 0; i < message.length; i++){
                    v[i] = term.clone();
                    for(int j = 0; j < message[i].length; j++){
                        v[i][j] += message[i][j];
                    }
                }

                long elapsedTime = System.currentTimeMillis() - start; //does it need to move below print statement? it's below in RSA
                System.out.println("Encrypted message displayed as:\nu\nv");
                System.out.println(Arrays.toString(u[0]).replaceAll("\\p{P}", "") + " , " + Arrays.toString(u[1]).replaceAll("\\p{P}", ""));
                for (int[] i: v) {
                    System.out.print(Arrays.toString(i).replaceAll("\\p{P}", "") + "  ");
                }

                System.out.println("\n\nElapsed Time: " + elapsedTime + " milliseconds");

                System.out.println();

            } else if (response.equals("decrypt")) {
                //accepts input of encrypted message from user
                System.out.println("Enter the ciphertext in the form: \nu\nv");
                String messyU = input.nextLine();
                String messyV = input.nextLine();

                int[][] u = ops.cleanUp(messyU, blocSize);
                int[][] v = ops.cleanVp(blocSize, messyV);
                int[][] MESSage = new int[v.length][blocSize];

                long start = System.currentTimeMillis();

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

                int[] message = new int[MESSage.length*MESSage[0].length];
                int count = 0;
                for (int[] ints : MESSage) {
                    for (int anInt : ints) {
                        if (anInt > q1 && anInt < q2) {
                            message[count] = 1;
                        } else {
                            message[count] = 0;
                        }
                        count++;
                    }
                }

                String CLEANage = ops.fromBinary(message);
                System.out.println("\nDecrypted Message: ");
                System.out.println(CLEANage);

                long elapsedTime = System.currentTimeMillis() - start;
                System.out.println("\nElapsed Time: " + elapsedTime + " milliseconds\n");

            } else {
                break;
            }
        }
    }
}