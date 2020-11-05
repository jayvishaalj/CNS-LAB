import java.util.Scanner;

public class RailFence {
    public static void main(String[] args) {
        System.out.println("----Rail Fence----");
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the Input String : ");
        String inputText = scan.next();
        System.out.println("Enter the Key Value: ");
        int key = scan.nextInt();    
        int r = key, len = inputText.length();
        int c = len / key;
        char mat[][] = new char[r][c];
        int k = 0;

        String cipherText = "";

        for (int i = 0; i < c; i++) {
            for (int j = 0; j < r; j++) {
                if (k != len)
                    mat[j][i] = inputText.charAt(k++);
                else
                    mat[j][i] = 'X';
            }
        }
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                cipherText += mat[i][j];
            }
        }
        
        System.out.println("Encrypted String : " + cipherText);
        k=0;
        String plainText = "";
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                mat[i][j] = cipherText.charAt(k++);
            }
        }
        for (int i = 0; i < c; i++) {
            for (int j = 0; j < r; j++) {
                plainText += mat[j][i];
            }
        }

        System.out.println("Decrypted String : " + plainText);
    }
}
