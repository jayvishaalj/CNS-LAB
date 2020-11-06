import java.io.UnsupportedEncodingException;
import java.util.*;
import java.security.*;

public class DSS {
    public static void main(String[] args)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Text : ");
        String msg = sc.nextLine();
        KeyPairGenerator keys = KeyPairGenerator.getInstance("DSA");
        keys.initialize(2048);
        KeyPair key = keys.generateKeyPair();
        PrivateKey pk = key.getPrivate();
        PublicKey puk = key.getPublic();
        Signature sign = Signature.getInstance("SHA256withDSA");
        sign.initSign(pk);
        byte[] text = msg.getBytes();
        sign.update(text);
        byte[] signature = sign.sign();
        sign.initVerify(puk);
        System.out.print("Data : ");
        String data = sc.nextLine();
        sign.update(data.getBytes());
        if (sign.verify(signature))
            System.out.println("Signature Verified !");
        else
            System.out.println("Signature Invalid !");
    }
}
