import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
public class KYBEROperations {
    public int posMod(int toMod, int modBy){
        int ans = toMod % modBy;

        while(ans < 0){
            ans += modBy;
        }
        return ans;
    }

    public int[] coefMod(int[] toMod, int modBy){
        int[] ans = Arrays.stream(toMod).map(c -> posMod(c, modBy)).toArray();

        return ans;
    }

    public int[] polyMod(int[] toMod, int[] f){
        int[] ans = new int[f.length - 1];
        int modExpo = f.length - 1;

        for(int i = 0; i < toMod.length; i++){
            int coefficient = toMod[i];
            int expo = posMod(i,modExpo);
            if((i / modExpo) % 2 == 0) {
                ans[expo] = ans[expo] + coefficient;
            } else {
                ans[expo] = ans[expo] - coefficient;
            }
        }

        return ans;
    }

    public int[] fullMod(int[] toMod, int[] f, int q){
        int[] ans = polyMod(toMod, f);
        ans = coefMod(ans, q);

        return ans;
    }

    public int[] polyDist(int[] first, int[] second){
        int[] ans = new int[first.length + second.length - 1];

        for(int i = 0; i < first.length; i++){
            for(int j = 0; j < second.length; j++){
                ans[i+j] += first[i] * second[j];
            }
        }

        return ans;
    }

    public int[][] toScaledBinary(String plainText, int blocSize, int scaleFactor){
        BigInteger ascii = new BigInteger(plainText.getBytes());
        String binary = ascii.toString(2);
        binary = binary.substring(6);

        int blocs = (int)Math.ceil(binary.length()/(double)blocSize);

        int[][] message = new int[blocs][blocSize];

        for(int i = 0; i < blocs; i++){
            if(i == blocs - 1){
                message[i] = binary.substring(i * blocSize).chars().map(c -> (c - 48) * scaleFactor).toArray();
            } else {
                message[i] = binary.substring(i * blocSize, (i + 1) * blocSize).chars().map(c -> (c - 48) * scaleFactor).toArray();
            }
        }

        return message;
    }

    public String fromBinary(int[] message){
        String binary = Arrays.toString(message).replaceAll("\\p{P}", "").replaceAll(" ", "");

        byte[] bytenary = new BigInteger(binary, 2).toByteArray();

        return new String(bytenary, StandardCharsets.US_ASCII);
    }

    public int[] polyRandom(int degree, int coefMin, int coefMax){
        int[] poly = new int[degree + 1];
        int range = coefMax-coefMin;

        for(int i = 0; i <= degree; i++){
            poly[i] = (int)(Math.random() * range) + coefMin;
        }
        return poly;
    }

    // for turning printed encrypted message back to array, .split(" "); for v and .split(","); then .split(" "); for u
    public int[][] cleanUp(String messyU, int blocSize){
        int[][] cleanedUp = new int[2][blocSize];
        String[] split1 = messyU.split(" , ");
        for(int i = 0; i < 2; i++) {
            cleanedUp[i] = Arrays.stream(split1[i].split(" ")).mapToInt(c -> Integer.parseInt(c)).toArray();
        }
        return cleanedUp;
    }

    public int[][] cleanVp(int blocSize, String messyV){
        String[] blocOut = messyV.split("  ");
        int[][] cleanedVp = new int[blocOut.length][blocSize];
        for(int i = 0; i < blocOut.length; i++) {
            cleanedVp[i] = Arrays.stream(blocOut[i].split(" ")).mapToInt(c -> Integer.parseInt(c)).toArray();
        }
        return cleanedVp;
    }
}
