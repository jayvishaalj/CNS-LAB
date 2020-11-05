import java.util.Scanner;

class CeaserCipher {
    public static void main(String args[]){
        System.out.println("---- Ceaser Cipher-----");
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the Message");
        String inputString = scan.next();
        String outputString = "";
        System.out.println("Enter the Key");
        int key = scan.nextInt();
        for (int i = 0; i < inputString.length(); i++) {
            char temp = inputString.charAt(i);
            if (Character.isUpperCase(temp)) {
                outputString += (char)(Math.floorMod(temp + key - 65, 26) + 65);
            }
            else {
                outputString += (char)(Math.floorMod(temp + key - 97, 26)+ 97);
            }
        }
        System.out.println("Encrypted : " + outputString);
        String outputDecrypted = "";
        for (int i = 0; i < outputString.length(); i++) {
            char temp = outputString.charAt(i);
            if (Character.isUpperCase(temp)) {
                outputDecrypted +=  (char)( Math.floorMod(temp - key - 65, 26)  + 65);
            }
            else {
                outputDecrypted += (char)(Math.floorMod(temp - key - 97, 26) + 97);
            }
        }
        System.out.println("Decrypted : "+ outputDecrypted);
        scan.close();
    }
}