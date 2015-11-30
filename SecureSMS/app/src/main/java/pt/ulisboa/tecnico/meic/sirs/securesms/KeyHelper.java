package pt.ulisboa.tecnico.meic.sirs.securesms;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by ruimams on 30/11/2015.
 */
public class KeyHelper {
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
}