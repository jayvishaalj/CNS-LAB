import java.math.BigInteger;
import java.util.*;

class RSA {
    private BigInteger p;
    private BigInteger q;
    private BigInteger N;
    private BigInteger phi;
    private BigInteger e;
    private BigInteger d;
    private int bitlength = 1024;
    private Random r;

    public RSA() {
        r = new Random();
        p = BigInteger.probablePrime(bitlength, r);
        q = BigInteger.probablePrime(bitlength, r);
        N = p.multiply(q);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = BigInteger.probablePrime(bitlength / 2, r);
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0) {
            e.add(BigInteger.ONE);
        }
        d = e.modInverse(phi);
    }

    public RSA(BigInteger e, BigInteger d, BigInteger N) {
        this.e = e;
        this.d = d;
        this.N = N;
    }

    private static String bytesToString(byte[] encrypted) {
        String test = "";
        for (byte b : encrypted) {
            test += Byte.toString(b);
        }
        return test;
    }

    // Encrypt message
    public byte[] encrypt(byte[] message) {
        return (new BigInteger(message)).modPow(e, N).toByteArray();
    }

    // Decrypt message
    public byte[] decrypt(byte[] message) {
        return (new BigInteger(message)).modPow(d, N).toByteArray();
    }

    public static void main(String[] args) {
        RSA rsa = new RSA();
        String teststring = "";
        byte[] encrypted = {};
        Scanner in = new Scanner(System.in);
        int cont = 1;
        while (cont != 0) {
            System.out.println(
                    "Enter your choice (Please select 1 before encrypt or decrypt)\n1.Text \n2.Encrypt\n3.Decrypt");
            int choice = in.nextInt();
            in.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("Enter the plain text:");
                    teststring = in.nextLine();
                    break;
                case 2:
                    System.out.println("Encrypting String: " + teststring);
                    System.out.println("String in Bytes: " + bytesToString(teststring.getBytes()));
                    encrypted = rsa.encrypt(teststring.getBytes());
                    System.out.println(encrypted);
                    break;
                case 3:
                    System.out.println("Decrypting : " + encrypted);
                    byte[] decrypted = rsa.decrypt(encrypted);
                    System.out.println("Decrypting Bytes: " + bytesToString(decrypted));
                    System.out.println("Decrypted String: " + new String(decrypted));
                    break;
                default:
                    System.out.println("Wrong choice");
            }
            // sc.nextLine();
            System.out.println("Do you want to continue?\nYes(1)\nNo(0)");
            cont = in.nextInt();
            in.nextLine();
        }
    }
}