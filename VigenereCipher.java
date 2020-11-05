import java.util.*;

public class VigenereCipher {
    public static void main(String[] args) {
        System.out.println("----Vigenere Cipher----");
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the Plain Text: ");
        String text = scan.next();
        System.out.println("Enter the Key Text: ");
        String key = scan.next();
        key = key.repeat(text.length() / key.length()) + key.substring(0, (text.length() % key.length()));
        System.out.println("KEY : "+key);
        String encrypted = "";
        for (int i = 0; i < text.length(); i++) {
            encrypted += (char) ((key.charAt(i) + text.charAt(i)) % 26 + 65);
        }
        System.out.println("Encrypted Text : " + encrypted);
        String Decrypted = "";
        for (int i = 0; i < encrypted.length(); i++) {
            Decrypted += (char) ((encrypted.charAt(i) - key.charAt(i) + 26) % 26 + 65);
        }
        System.out.println("Decrypted Text : " + Decrypted);
        scan.close();
    }
}
