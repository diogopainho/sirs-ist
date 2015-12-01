package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.util.Base64;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

/**
 * Created by ruimams on 01/12/2015.
 */
public class KeyHelperTest extends TestCase {
    KeyPair keyPair;

    protected void setUp() {
        keyPair = KeyHelper.generateKeyPair();
    }

    public void testExportImportPrivateKey() {
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();

        PrivateKey importedPrivateKey = KeyHelper.bytesToPrivateKey(privateKeyBytes);
        byte[] importedPrivateKeyBytes = importedPrivateKey.getEncoded();

        Assert.assertTrue(Arrays.equals(privateKeyBytes, importedPrivateKeyBytes));
    }

    public void testExportImportPublicKey() {
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();

        PublicKey importedPublicKey = KeyHelper.bytesToPublicKey(publicKeyBytes);
        byte[] importedPublicKeyBytes = importedPublicKey.getEncoded();

        Assert.assertTrue(Arrays.equals(publicKeyBytes, importedPublicKeyBytes));
    }

    public void testImportBase64PublicKey() {
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();

        String publicKeyEncoded = Base64.encodeToString(publicKeyBytes, Base64.DEFAULT);
        byte[] publicKeyDecoded = Base64.decode(publicKeyEncoded, Base64.DEFAULT);
        PublicKey importedPublicKey = KeyHelper.bytesToPublicKey(publicKeyDecoded);
        byte[] importedPublicKeyBytes = importedPublicKey.getEncoded();

        Assert.assertTrue(Arrays.equals(publicKeyBytes, importedPublicKeyBytes));
    }
}
