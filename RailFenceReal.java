import java.util.*;

public class RailFenceReal {
    public static void main(String[] args) {
        System.out.println("----Rail Fence----");
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the Input String : ");
        String inputText = scan.next();
        System.out.println("Enter the Key Value: ");
        int key = scan.nextInt();
        int r = key;
        int len = inputText.length();
        int c = len / (key-1);
        char[][] mat = new char[r][c];
        int k = 1;
        mat[0][0] = inputText.charAt(0);
        boolean reverse = false;
        System.out.println("C : "+c);
        for (int i = 0; i < c; i++) {
            if (!reverse) {
                for (int j = 1; j < r; j++) {
                    if (k != len) {
                        mat[j][i] = inputText.charAt(k);
                        k++;
                    } else {
                        mat[j][i] = 'X';
                    }
                }
            } else {
                for (int j = key - 2; j >= 0; j--) {
                    if (k != len) {
                        mat[j][i] = inputText.charAt(k);
                        k++;
                    } else {
                        mat[j][i] = 'X';
                    }
                }
            }
            reverse = !reverse;
        }
        String encryptedString =  "";
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                if ((int) mat[i][j] != 0) {
                    encryptedString+=mat[i][j];
                }
            }
        }
        System.out.println(encryptedString);


        char[] decrypted = new char[encryptedString.length()];
        int n = 0;
        for(k = 0 ; k < key; k ++) {
            int index = k;
            boolean down = true;
            while(index < encryptedString.length() ) {
                decrypted[index] = encryptedString.charAt(n++);
                if(k == 0 || k == key - 1) {
                    index = index + 2 * (key - 1);
                }
                else if(down) {
                    index = index + 2 * (key - k - 1);
                    down = !down;
                }
                else {
                    index = index + 2 * k;
                    down = !down;
                }
            }
        }
        System.out.println( new String(decrypted));
        scan.close();
    }
}
