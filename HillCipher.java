import java.util.*;

public class HillCipher {
    public static void main(String[] args) {
        System.out.println("----Hill Cipher----");
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the Plain Text : ");
        String plainText = scan.next();
        System.out.println("Enter the Key Text : ");
        String keyText = scan.next();
        plainText = plainText.toUpperCase();
        keyText = keyText.toUpperCase();
        int[][] matrix = new int[plainText.length()][plainText.length()];
        int j = 0;
        int k = 0;
        for (int i = 0; i < keyText.length(); i++) {
            if (k == plainText.length()) {
                System.out.println("String out of N ");
                break;
            }
            matrix[k][j] = (int) keyText.charAt(i) % 65;
            j++;
            if (j >= plainText.length()) {
                j = 0;
                k++;
            }
        }
        int[][] textMatrix = new int[plainText.length()][1];
        for (int i = 0; i < plainText.length(); i++) {
            textMatrix[i][0] = plainText.charAt(i) % 65;
        }
        System.out.println("-------KEY MATRIX--------");
        for (int i = 0; i < matrix.length; i++) {
            for (int l = 0; l < matrix[i].length; l++) {
                System.out.print(matrix[i][l] + " ");
            }
            System.out.println();
        }
        System.out.println("-------TEXT MATRIX--------");
        for (int i = 0; i < textMatrix.length; i++) {
            System.out.println(textMatrix[i][0]);
        }

        int[][] result = new int[plainText.length()][1];
        for (int i = 0; i < matrix.length; i++) {
            for (int l = 0; l < matrix[i].length; l++) {
                result[i][0] += matrix[i][l] * textMatrix[l][0];
            }
        }
        String encryptString = "";
        System.out.println("-------RESULT MATRIX--------");
        for (int i = 0; i < result.length; i++) {

            result[i][0] = (result[i][0] % 26);
            System.out.println(result[i][0]);
            encryptString += (char) (result[i][0] + 65);
        }
        System.out.println("ECRYPTED STRING : " + encryptString);
        float[][] inv = new float[plainText.length()][plainText.length()];
        inverse(matrix, inv, plainText.length());
        System.out.println("-------INVERSE MATRIX--------");
        for (int i = 0; i < inv.length; i++) {
            for (int l = 0; l < inv[i].length; l++) {
                System.out.print(inv[i][l] + " ");
            }
            System.out.println();
        }
        scan.close();
    }
    
    static void getCofactor(int A[][], int temp[][], int p, int q, int n) {
        int i = 0, j = 0;

        // Looping for each element of the matrix
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                // Copying into temporary matrix only those element
                // which are not in given row and column
                if (row != p && col != q) {
                    temp[i][j++] = A[row][col];

                    // Row is filled, so increase row index and
                    // reset col index
                    if (j == n - 1) {
                        j = 0;
                        i++;
                    }
                }
            }
        }
    }

    /*
     * Recursive function for finding determinant of matrix. n is current dimension
     * of A[][].
     */
    static int determinant(int A[][], int n, int N) {
        int D = 0; // Initialize result

        // Base case : if matrix contains single element
        if (n == 1)
            return A[0][0];

        int[][] temp = new int[N][N]; // To store cofactors

        int sign = 1; // To store sign multiplier

        // Iterate for each element of first row
        for (int f = 0; f < n; f++) {
            // Getting Cofactor of A[0][f]
            getCofactor(A, temp, 0, f, n);
            D += sign * A[0][f] * determinant(temp, n - 1, N);

            // terms are to be added with alternate sign
            sign = -sign;
        }

        return D;
    }

    // Function to get adjoint of A[N][N] in adj[N][N].
    static void adjoint(int A[][], int[][] adj, int N) {
        if (N == 1) {
            adj[0][0] = 1;
            return;
        }

        // temp is used to store cofactors of A[][]
        int sign = 1;
        int[][] temp = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                // Get cofactor of A[i][j]
                getCofactor(A, temp, i, j, N);

                // sign of adj[j][i] positive if sum of row
                // and column indexes is even.
                sign = ((i + j) % 2 == 0) ? 1 : -1;

                // Interchanging rows and columns to get the
                // transpose of the cofactor matrix
                adj[j][i] = (sign) * (determinant(temp, N - 1, N));
            }
        }
    }

    // Function to calculate and store inverse, returns false if
    // matrix is singular
    static boolean inverse(int A[][], float[][] inverse, int N) {
        // Find determinant of A[][]
        int det = determinant(A, N, N);
        if (det == 0) {
            System.out.print("Singular matrix, can't find its inverse");
            return false;
        }

        // Find adjoint
        int[][] adj = new int[N][N];
        adjoint(A, adj, N);

        // Find Inverse using formula "inverse(A) = adj(A)/det(A)"
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                inverse[i][j] = adj[i][j] / (float) det;

        return true;
    }
}
