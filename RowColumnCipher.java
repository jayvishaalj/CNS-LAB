import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RowColumnCipher {
    public static void main(String[] args) {
        System.out.println("----Row Column Cipher----");
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the number of Keys : ");
        int len = scan.nextInt();
        System.out.println("Enter the Keys one by one : ");
        HashMap<Integer, Integer> keys = new HashMap<>();
        for (int i = 0; i < len; i++) {
            keys.put(scan.nextInt(), i);
        }
        System.out.println(keys);
        System.out.println("Enter the Input Text : ");
        String inputString = scan.next();
        System.out.println(inputString);
        int r= 0;
        if(inputString.length()%len == 0){
            r = inputString.length()/len;
        }
        else{
            r = (inputString.length()/len) + (inputString.length()%len);
        }
        int k = 0;
        char[][] matrix = new char[r][len];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < len; j++) {
                if(k != inputString.length()){
                    matrix[i][j] = inputString.charAt(k++);
                }
                else
                {
                    matrix[i][j] = 'X';
                }
            }
        }
        System.out.println("----Input Matrix----");
        for (int i = 0; i < matrix.length; i++) {
            for (int l = 0; l < matrix[i].length; l++) {
                System.out.print(matrix[i][l] + " ");
            }
            System.out.println();
        }

        String encryptedString = "";
        for(Map.Entry<Integer, Integer> entry : keys.entrySet()){
            System.out.println( entry.getKey() + " => " + entry.getValue());
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[i][entry.getValue()] != '\0') {
                    encryptedString +=matrix[i][entry.getValue()];
                }
            }   
        }
        System.out.println("Encrypted String : "+ encryptedString);
        k=0;
        for(Map.Entry<Integer, Integer> entry : keys.entrySet()){
            System.out.println( entry.getKey() + " => " + entry.getValue());
            for (int i = 0; i < matrix.length; i++) {
                if(k != encryptedString.length()){
                    matrix[i][entry.getValue()] = encryptedString.charAt(k++);
                }
                else{
                    break;
                }
            }   
        }
        String decryptedString = "";
        for (int i = 0; i < matrix.length; i++) {
            for (int l = 0; l < matrix[i].length; l++) {
                if (matrix[i][l] != 'X') {
                    decryptedString+=matrix[i][l];
                }
            }
        }
        System.out.println("Decrypted String : "+ decryptedString);
        scan.close();
    }
}
