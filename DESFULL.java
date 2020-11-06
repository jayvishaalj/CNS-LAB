import java.util.*;
import java.io.*; 

class IncorrectInputFormatException extends Exception { 
    
    private static final long serialVersionUID = 1L;
    private final String message; 

    IncorrectInputFormatException(String message) {
        super(); 
        this.message = message; 
    }

    @Override
    public String toString() { 
        return this.message;  
    }
}

class EncryptionException extends Exception { 
    
    private static final long serialVersionUID = 1L;
    private int issueCode;  

    EncryptionException(int code) {
        super(); 
        this.issueCode = code; 
    }

    @Override
    public String toString() { 
        
        if (this.issueCode == 0){
            return "Number of allowed rounds exceeded!";
        }
        else{ 
            return "unknown";
        }
    }
}

class StringParser { 

    private String text;
    private String keyString; 

    StringParser(String text, String keyString) { 
        this.text = text; 
        this.keyString = keyString;
    }

    private void convertHex (int x, ArrayList<Integer> seq) {   

        int[] binary = new int[4]; 
        int k = 0; 

        while (x!=0) { 
            binary[k++] = x%2; 
            x = x/2; 
        }

        for (int i=3; i>=0; i--) {
            seq.add(binary[i]);
        }
    }

    ArrayList<ArrayList<Integer>> evaluate () throws IncorrectInputFormatException { //returns list of blocks
        
        if (this.keyString.length() != 16) { 
            throw new IncorrectInputFormatException("invalid key format."); 
        }

        if (this.text.length() == 0) { 
            throw new IncorrectInputFormatException("empty input."); 
        }
    
        for (int i=0; i<this.keyString.length(); i++) {
            
            int encoding = (int) this.keyString.charAt(i);
            if ((encoding>=48) && (encoding<=57)) { 
                continue;
            }
            else if ((encoding>=65) && (encoding<=70)) { 
                continue;
            }
            else if ((encoding>=97) && (encoding<=102)) { 
                continue; 
            }
            else {  
                    throw new IncorrectInputFormatException("invalid key format.");
            }
            
        }

        ArrayList<ArrayList<Integer>> blocks = new ArrayList<>();
        ArrayList<Integer> binaryInput = new ArrayList<>(); 

        for (int i=0; i<this.text.length(); i++) {
            
            int encoding = (int) this.text.charAt(i); 
            if ((encoding>=48) && (encoding<=57)) { 
                this.convertHex(encoding-48, binaryInput); 
            }
            else if ((encoding>=65) && (encoding<=70)) { 
                this.convertHex(encoding-55, binaryInput); 
            }
            else if ((encoding>=97) && (encoding<=102)) { 
                this.convertHex(encoding-87, binaryInput); 
            }
            else {
                if (encoding != 32) {
                    throw new IncorrectInputFormatException("invalid input format.");
                }
            }
        }

        for (int i=0; i<binaryInput.size(); i+=64) { 
            
            ArrayList<Integer> block = new ArrayList<>();
            for (int j=0; j<64; j++) {
                if (i+j < binaryInput.size()) { 
                    block.add (binaryInput.get(i+j)); 
                }
            }

            int suffix = 64 - block.size();
            for (int j=0; j<suffix; j++) {
                block.add(0); 
            }

            blocks.add(block); 
        }

        return blocks;
    }

    ArrayList<ArrayList<Integer>> evaluateDecryption () throws IncorrectInputFormatException { //returns list of blocks
        
        if (this.keyString.length() != 16) { 
            throw new IncorrectInputFormatException("invalid key format."); 
        }

        if (this.text.length() == 0) { 
            throw new IncorrectInputFormatException("empty input."); 
        }

        for (int i=0; i<this.keyString.length(); i++) {
            
            int encoding = (int) this.keyString.charAt(i); 
            if ((encoding>=48) && (encoding<=57)) { 
                continue;
            }
            else if ((encoding>=65) && (encoding<=70)) { 
                continue;
            }
            else if ((encoding>=97) && (encoding<=102)) { 
                continue; 
            }
            else {  
                    throw new IncorrectInputFormatException("invalid key format.");
            }
            
        }

        ArrayList<ArrayList<Integer>> blocks = new ArrayList<>();
        ArrayList<Integer> binaryInput = new ArrayList<>(); 

        for (int i=0; i<this.text.length(); i++) {
            
            int encoding = (int) this.text.charAt(i); 
            if ((encoding>=48) && (encoding<=57)) { 
                this.convertHex(encoding-48, binaryInput); 
            }
            else if ((encoding>=65) && (encoding<=70)) { 
                this.convertHex(encoding-55, binaryInput); 
            }
            else if ((encoding>=97) && (encoding<=102)) { 
                this.convertHex(encoding-87, binaryInput); 
            }
            else {
                if (encoding != 32) {
                    throw new IncorrectInputFormatException("invalid input format.");
                }
            }
        }

        for (int i=0; i<binaryInput.size(); i+=64) { 
            
            ArrayList<Integer> block = new ArrayList<>();
            for (int j=0; j<64; j++) {
                if (i+j < binaryInput.size()) { 
                    block.add (binaryInput.get(i+j)); 
                }
            }
            
            if (block.size() < 64) { 
                throw new IncorrectInputFormatException("missing cipher text.");
            }

            blocks.add(block); 
        }

        return blocks;
    }
}

class DESKeyOperations {   

    // PERMUTATION PC1
    private ArrayList<Integer> leftKey;
    private ArrayList<Integer> rightKey;    
    private int roundsCompleted = 0; 

    private static int[][] pc1 = {   
        {57,49,41,33,25,17,9},
        {1,58,50,42,34,26,18},
        {10,2,59,51,43,35,27},
        {19,11,3,60,52,44,36},
        {63,55,47,39,31,23,15},
        {7,62,54,46,38,30,22},
        {14,6,61,53,45,37,29},
        {21,13,5,28,20,12,4}
    };

    // PERMUTATION PC2
    private static int[][] pc2  = {   
        {14,17,11,24,1,5},
        {3,28,15,6,21,10},
        {23,19,12,4,26,8},
        {16,7,27,20,13,2},
        {41,52,31,37,47,55},
        {30,40,51,45,33,48},
        {44,49,39,56,34,53},
        {46,42,50,36,29,32}
    };

    // LEFT SHIFTS						
    private int[] shifts = {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};

    DESKeyOperations (String keyString) {

        ArrayList<Integer> initialKey = new ArrayList<>();
        this.leftKey = new ArrayList<>(); 
        this.rightKey = new ArrayList<>();

        for (int i=0; i<keyString.length(); i++) { 
            initialKey.add (Character.getNumericValue(keyString.charAt(i)));  
        }

        int count = 0; 
        int half = (pc1.length*pc1[0].length)/2;

        for (int i=0; i<pc1.length; i++) { 
            for (int j=0; j<pc1[i].length; j++) {  
                
                if (count < half) { 
                    this.leftKey.add (initialKey.get(pc1[i][j]-1)); 
                }
                else {
                    this.rightKey.add (initialKey.get(pc1[i][j]-1)); 
                }
                count++; 
            }
        }
    }

    private void leftShift (ArrayList<Integer> sequence) {
        
        int first = sequence.get(0);  
        for (int i=1; i<sequence.size(); i++) { 
            sequence.set(i-1, sequence.get(i)); 
        }
        sequence.set(sequence.size()-1, first); 
    }

    public ArrayList<Integer> getKey() throws EncryptionException {

        if (this.roundsCompleted > this.shifts.length) { 
            throw new EncryptionException(0); 
        }

        int keyShifts = this.shifts[this.roundsCompleted]; 
        for (int i=0; i<keyShifts; i++) { 
            this.leftShift(this.leftKey);
            this.leftShift(this.rightKey); 
        }

        ArrayList<Integer> combined = new ArrayList<>(); 
        ArrayList<Integer> permuted = new ArrayList<>(); 
        
        for (Integer value: this.leftKey) { 
            combined.add (value); 
        }
        for (Integer value: this.rightKey) { 
            combined.add (value); 
        }

        for (int i=0; i<pc2.length; i++) { 
            for (int j=0; j<pc2[i].length; j++) { 
                permuted.add(combined.get(pc2[i][j]-1)); 
            }
        }

        this.roundsCompleted++;  
        return permuted; 
    }
}

class Cipher {  

    // INITIAL PERMUTATION 
    private static int[][] ip = {   
        {58,50,42,34,26,18,10,2},
        {60,52,44,36,28,20,12,4},
        {62,54,46,38,30,22,14,6},
        {64,56,48,40,32,24,16,8},
        {57,49,41,33,25,17,9,1},
        {59,51,43,35,27,19,11,3},
        {61,53,45,37,29,21,13,5},
        {63,55,47,39,31,23,15,7}
    }; 

    // FINAL INVERSE PERMUTATION
    private static int[][] ipinverse = {   
        {40,8,48,16,56,24,64,32},
        {39,7,47,15,55,23,63,31},
        {38,6,46,14,54,22,62,30},
        {37,5,45,13,53,21,61,29},
        {36,4,44,12,52,20,60,28},
        {35,3,43,11,51,19,59,27},
        {34,2,42,10,50,18,58,26},
        {33,1,41,9,49,17,57,25}
    };

    // E BIT SELECTION TABLE
    private static int[][] e = {  
        {32,1,2,3,4,5},
        {4,5,6,7,8,9},
        {8,9,10,11,12,13},
        {12,13,14,15,16,17},
        {16,17,18,19,20,21},
        {20,21,22,23,24,25},
        {24,25,26,27,28,29},
        {28,29,30,31,32,1}
    };

    // PERMUTATION P
    private static int[][] p = {   
        {16,7,20,21},
        {29,12,28,17},
        {1,15,23,26},
        {5,18,31,10},
        {2,8,24,14},
        {32,27,3,9},
        {19,13,30,6},
        {22,11,4,25}
    };

    // S BOXES
    private static int[][][] S = {
        {   // S1
            {14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7},
            {0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8},
            {4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0},
            {15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13}
        },
        {   // S2
            {15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10},
            {3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5},
            {0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15},
            {13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9}
        },
        {   // S3
            {10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8},
            {13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1},
            {13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7},
            {1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12}
        },
        {   // S4
            {7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15},
            {13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9},
            {10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4},
            {3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14}
        },
        {   // S5
            {2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9},
            {14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6},
            {4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14},
            {11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3}
        },
        {   // S6
            {12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11},
            {10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8},
            {9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6},
            {4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13}
        },
        {   // S7
            {4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1},
            {13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6},
            {1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2},
            {6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12}
        },
        {   // S8
            {13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7},
            {1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2},
            {7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8},
            {2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11}
        }
    };

    private ArrayList<ArrayList<Integer>> keys; 

    Cipher (String keyString) throws EncryptionException {
        
        this.keys = new ArrayList<>();  
        DESKeyOperations generator = new DESKeyOperations(keyString); 
        for (int i=0; i<16; i++) { 
            this.keys.add (generator.getKey()); 
        }

        System.out.println ("Round keys: "); 
        for (int i=0; i<this.keys.size(); i++) {
            System.out.print ("Round " + i + ": ");  
            for (int j=0; j<this.keys.get(i).size(); j++) {
                System.out.print(this.keys.get(i).get(j)); 
            }
            System.out.println(); 
        }
    }

    private int[] sBox (int[] expanded) {
        
        int[] contracted = new int[32]; 
        int filled = 0;  
        int count = 0; 

        for (int i=0; i<48; i+=6) { 
            int row = expanded[i]*2 + expanded[i+5];
            int col = expanded[i+1]*8 + expanded[i+2]*4 + expanded[i+3]*2 + expanded[i+4]; 
            int val = S[count][row][col];
            
            int[] fourBits = new int[4];
            int k = 0;
            while (val != 0) { 
                fourBits[k++] = val%2;   
                val = val/2; 
            }

            for (int j=3; j>=0; j--) {
                contracted[filled] = fourBits[j]; 
                filled++;
            }
            count++; 
        }
        return contracted;
    }

    private ArrayList<Integer> round (ArrayList<Integer> input, ArrayList<Integer> roundKey) { 

        ArrayList<Integer> output = new ArrayList<>();
        int[] left = new int[32];
        int[] right = new int[32]; 

        for (int i=0; i<32; i++) { 
            left[i] = input.get(i);
            right[i] = input.get(i+32); 
            output.add (right[i]);
        }

        int[] expansionPermutation = new int[48];
        int k = 0; 

        for (int i=0; i<e.length; i++) { 
            for (int j=0; j<e[i].length; j++) { 
                expansionPermutation[k] = right[e[i][j]-1]; 
                k++; 
            }
        }
        for (int i=0; i<48; i++) { 
            expansionPermutation[i] = expansionPermutation[i]^roundKey.get(i);  
        }

        int[] postS = this.sBox(expansionPermutation); 
        int[] postSPermutation = new int[32]; 

        k=0; 
        for (int i=0; i<p.length; i++) { 
            for (int j=0; j<p[i].length; j++) {
                postSPermutation[k] = postS[p[i][j]-1];
                k++;
            }
        }

        for (int i=0; i<32; i++) {
            output.add (left[i]^postSPermutation[i]);
        }

        return output;
    }

    private ArrayList<Integer> encryptBlock (ArrayList<Integer> block) {

        //Input permutation 
        ArrayList<Integer> input = new ArrayList<>(); 
        for (int i=0; i<ip.length; i++){
            for (int j=0; j<ip[i].length; j++) { 
                input.add (block.get(ip[i][j]-1)); 
            }
        }
        
        //round operations 
        for (int i=0; i<16; i++) { 
            ArrayList<Integer> nextInput = this.round(input, this.keys.get(i)); 
            input = nextInput; 
        }

        //32 bit swap  
        int half = input.size()/2;  
        for (int i=0; i<half; i++) { 
            int temp = input.get (i); 
            input.set (i, input.get(i+half)); 
            input.set (i+half, temp);
        }

        //ipinverse 
        ArrayList<Integer> cipher = new ArrayList<>(); 
        for (int i=0; i<ipinverse.length; i++) { 
            for (int j=0; j<ipinverse[i].length; j++) { 
                cipher.add (input.get(ipinverse[i][j]-1)); 
            }
        }
        return cipher; 
    }

    private ArrayList<Integer> decryptBlock (ArrayList<Integer> block) {

        //Input permutation 
        ArrayList<Integer> input = new ArrayList<>(); 
        for (int i=0; i<ip.length; i++){
            for (int j=0; j<ip[i].length; j++) { 
                input.add (block.get(ip[i][j]-1)); 
            }
        }
        
        //round operations 
        for (int i=15; i>=0; i--) { 
            ArrayList<Integer> nextInput = this.round(input, this.keys.get(i)); 
            input = nextInput; 
        }

        //32 bit swap
        int half = input.size()/2;  
        for (int i=0; i<half; i++) { 
            int temp = input.get (i); 
            input.set (i, input.get(i+half)); 
            input.set (i+half, temp);
        }

        //ipinverse 
        ArrayList<Integer> plain = new ArrayList<>(); 
        for (int i=0; i<ipinverse.length; i++) { 
            for (int j=0; j<ipinverse[i].length; j++) { 
                plain.add (input.get(ipinverse[i][j]-1)); 
            }
        }

        return plain; 
    }

    private String convertToHex (ArrayList<ArrayList<Integer>> ans) {

        StringBuilder hexCipher = new StringBuilder(); 

        for (ArrayList<Integer> blockCipher : ans){    
            for (int i=0; i<blockCipher.size(); i+=4){ 

                int val = blockCipher.get(i)*8 + blockCipher.get(i+1)*4 + blockCipher.get(i+2)*2 + blockCipher.get(i+3); 
                //conversion to Hex 
                if (val < 10) { 
                    hexCipher.append(val);
                }
                else{
                    int ten = (int) 'A';
                    int newVal = ten + (val-10);
                    hexCipher.append((char) newVal); 
                }
            }
            
            // hexCipher.append(' '); 
        }
        return hexCipher.toString(); 
    }

    String encrypt (ArrayList<ArrayList<Integer>> input) { 
    
        ArrayList<ArrayList<Integer>> ciphers = new ArrayList<>(); 
        for (ArrayList<Integer> block: input) { 
            ciphers.add(this.encryptBlock(block));
        }
        return this.convertToHex(ciphers);  
    }

    String decrypt (ArrayList<ArrayList<Integer>> input) { 
    
        ArrayList<ArrayList<Integer>> plainTexts = new ArrayList<>(); 
        for (ArrayList<Integer> block: input) { 
            plainTexts.add(this.decryptBlock(block));
        }
        return this.convertToHex(plainTexts);   
    }
}

class DESFULL { 

    private static String ASCIItoHEX(String ascii) 
	{  
		StringBuilder hex = new StringBuilder();  
 
		for (int i = 0; i < ascii.length(); i++) { 

			char ch = ascii.charAt(i); 
			int in = (int)ch; 
			String part = Integer.toHexString(in);  
			hex.append (part);   
		}  
		return hex.toString();  
    }
    
    private static String hexToASCII(String hex) 
	{  
		StringBuilder ascii = new StringBuilder(); 

		for (int i = 0; i < hex.length(); i += 2) { 

			String part = hex.substring(i, i + 2);  
			char ch = (char)Integer.parseInt(part, 16);  
			ascii.append(ch); 
		} 
		return ascii.toString();  
    } 
    
    private static String hextoBin(String input) {

			int n = input.length() * 4; 
			input = Long.toBinaryString( 
				Long.parseUnsignedLong(input, 16)); 
			while (input.length() < n) 
				input = "0" + input; 
			return input; 
	}

    public static void main (String[] args) throws IOException{ 
        
        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in)); 
        String c="";
        String keyString=""	;
        
        while (true) { 

            try {

                System.out.println ("\n" + "1.Encrypt\n2.Decrypt\n3.Exit\nEnter your choice : ");  
                int option = Integer.parseInt(scanner.readLine());  
                

                if ((option>3) || (option<=0)) { 
                    System.out.println("Enter valid input!"); 
                    continue;
                }

                if (option == 3) { 
                    break; 
                } 

                if (option == 1) { 

                    String temp; 
                    System.out.println ("Input your plaintext : "); 
                    temp = scanner.readLine(); 

                    String inputString;
                    inputString = ASCIItoHEX(temp); 

                       
                    System.out.println ("Input your key : "); 
                    keyString = scanner.readLine();
                     
                    if (keyString.length() == 0) { 
                        keyString = "133457799BBCDFF1"; 
                    } 
                    System.out.println ("Key used: " + keyString);
                    
                    StringParser checker = new StringParser(inputString, keyString);  
                    ArrayList<ArrayList<Integer>> inputBlocks = checker.evaluate(); //Returns list of 64 bit blocks

                    String keyStringBinary = hextoBin(keyString); 
                    Cipher cipher = new Cipher(keyStringBinary);    
                    System.out.println ("Cipher: " + cipher.encrypt(inputBlocks));   
                    c= cipher.encrypt(inputBlocks);
                
                }
                else{

                    String inputString;  
                    inputString = c;

                 
               
                     
                    if (keyString.length() == 0) { 
                        keyString = "133457799BBCDFF1"; 
                    } 
                    System.out.println ("Key used: " + keyString);

                    StringParser checker = new StringParser(inputString, keyString);  
                    ArrayList<ArrayList<Integer>> inputBlocks = checker.evaluateDecryption(); //Returns list of 64 bit blocks
                    
                    String keyStringBinary; 
                    keyStringBinary = hextoBin(keyString);
                    Cipher cipher = new Cipher(keyStringBinary);    
                    System.out.println ("Plain text: " + hexToASCII(cipher.decrypt(inputBlocks))); 
                } 

            } catch (IncorrectInputFormatException|NumberFormatException|EncryptionException e) { 
                System.out.println("Error " + e); 
            } 

        }
        
    }
}