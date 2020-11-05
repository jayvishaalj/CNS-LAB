import java.util.*;

// 65 - 90
public class PlayfairCipherMap {
    public static void main(String[] args) {
        System.out.println("----Playfair Cipher----");
        Scanner scan = new Scanner(System.in);
        Set<Character> set = new HashSet<>();
        System.out.println("Enter the Key in CAPS : ");
        String keyString = scan.next();
        HashMap<Character, int[]> map = new HashMap<>();
        char[][] matrix = new char[5][5];
        int j = 0;
        int k = 0;
        for (int i = 0; i < keyString.length(); i++) {
            set.add(keyString.charAt(i));
            map.put(keyString.charAt(i), new int[] { k, j });
            matrix[k][j] = keyString.charAt(i);
            j++;
            if (j >= 5) {
                j = 0;
                k++;
            }
        }
        System.out.println(set);
        for (int i = 65; i < 91; i++) {
            char temp = (char) i;
            System.out.println(temp + " : " + set.contains(temp) + " j : " + j + " k : " + k);
            if (!set.contains(temp)) {
                if (temp == 'J') {
                    System.out.println("Omited  : " + temp);
                    continue;
                }
                matrix[k][j] = temp;
                map.put(temp, new int[] { k, j });
                j++;
                if (j >= 5) {
                    j = 0;
                    k++;
                }
            }
        }

        System.out.println("-------MATRIX--------");
        map.entrySet().forEach(entry -> {
            System.out.println( entry.getKey() + " => " + entry.getValue()[0] + entry.getValue()[1] );
        });
        for (int i = 0; i < matrix.length; i++) {
            for (int l = 0; l < matrix[i].length; l++) {
                System.out.print(matrix[i][l] + " ");
            }
            System.out.println();
        }


        System.out.println("Enter the Plain Text : ");
        String plainText = scan.next();
        if (plainText.length() % 2 != 0) {
            plainText += "z";
        }
        plainText = plainText.toUpperCase();
        System.out.println("Final PlainText is : " + plainText);
        String encryptedString = "";

        for (int i = 0; i < plainText.length() - 1; i += 2) {
            System.out.println(plainText.charAt(i) + " -> " + map.get(plainText.charAt(i))[0]
                    + map.get(plainText.charAt(i))[1] + " , " + plainText.charAt(i + 1) + " -> "
                    + map.get(plainText.charAt(i + 1))[0] + map.get(plainText.charAt(i + 1))[1]);

            int[] arr1 = map.get(plainText.charAt(i));
            int[] arr2 = map.get(plainText.charAt(i + 1));
            if (arr1[1] == arr2[1]) {
                encryptedString += matrix[(arr1[0] + 1)%5][arr1[1]];
                encryptedString += matrix[(arr2[0] + 1)%5][arr2[1]];
            }
            else if (arr1[0] == arr2[0]) {
                encryptedString += matrix[arr1[0]][(arr1[1] + 1) % 5];
                encryptedString += matrix[arr2[0]][(arr2[1] + 1) % 5];
            }
            else {
                encryptedString += matrix[arr1[0]][arr2[1]];
                encryptedString += matrix[arr2[0]][arr1[1]];
            }
        }
        System.out.println("Encrypted String: "+encryptedString);


        String decryptedString = "";

        for (int i = 0; i < encryptedString.length() - 1; i += 2) {
            System.out.println(encryptedString.charAt(i) + " -> " + map.get(encryptedString.charAt(i))[0]
                    + map.get(encryptedString.charAt(i))[1] + " , " + encryptedString.charAt(i + 1) + " -> "
                    + map.get(encryptedString.charAt(i + 1))[0] + map.get(encryptedString.charAt(i + 1))[1]);

            int[] arr1 = map.get(encryptedString.charAt(i));
            int[] arr2 = map.get(encryptedString.charAt(i + 1));
            if (arr1[1] == arr2[1]) {
                decryptedString += matrix[Math.floorMod((arr1[0] - 1), 5)][arr1[1]];
                decryptedString += matrix[Math.floorMod((arr2[0] - 1), 5)][arr2[1]];
            } else if (arr1[0] == arr2[0]) {
                decryptedString += matrix[arr1[0]][Math.floorMod((arr1[1] - 1), 5)];
                decryptedString += matrix[arr2[0]][Math.floorMod((arr2[1] - 1), 5)];
            } else {
                decryptedString += matrix[arr1[0]][arr2[1]];
                decryptedString += matrix[arr2[0]][arr1[1]];
            }
        }
        System.out.println("Decrypted String: "+decryptedString);

        scan.close();
    }
}
