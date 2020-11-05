import java.util.Scanner;

public class ModInverse {
    public static void main(String[] args) {
        System.out.println("----Mod Inverse----");
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the value : ");
        int a = scan.nextInt();
        System.out.println("Enter the mod  : ");
        int m = scan.nextInt();
        a = a % m;
        for (int i = 1; i < m; i++) {
            if ((a * i) % m == 1) {
                System.out.println("FOUND : " + i);
            }
        }
    }
    
    public static int modInverseExtendedEuclid(int a, int m) {
        int m0 = m;
        int y = 0;
        int x = 1;
        if (m == 1)
            return 0;
        while (a > 1) {
            int q = a / m;
            int t = m;
            m = a % m;
            a = t;
            t = y;
            y = x - q * y;
            x = t;
        }
        if (x < 0)
            x += m0;

        return x;
    }
}
