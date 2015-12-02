package pt.ulisboa.tecnico.meic.sirs.securesms.Crypto;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class KeyHelper {
    public static KeyPair generateKeyPair() {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }


    public static PublicKey bytesToPublicKey(byte[] pubkeybytes){

        PublicKey pub = null;
        X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubkeybytes);
        KeyFactory keyFacPub = null;

        try {
            keyFacPub = KeyFactory.getInstance("RSA");
            pub = keyFacPub.generatePublic(pubSpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return pub;
    }

    public static PrivateKey bytesToPrivateKey(byte[] privkeybytes) {
        KeyFactory keyFacPriv = null;
        PrivateKey priv = null;
        PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privkeybytes);

        try {
            keyFacPriv = KeyFactory.getInstance("RSA");
            priv = keyFacPriv.generatePrivate(privSpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return priv;
    }


    public static SecretKey generateSymmetricKey() throws NoSuchAlgorithmException {
        KeyGenerator KeyGen = KeyGenerator.getInstance("AES");
        KeyGen.init(128);

        return KeyGen.generateKey();
    }


    public static SecretKey bytesToSecretKey(byte[] secretKeyBytes) {
        return new SecretKeySpec(secretKeyBytes, "AES");
    }
}
