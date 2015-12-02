package pt.ulisboa.tecnico.meic.sirs.securesms.MessageExchange;

import android.util.Log;

import com.activeandroid.query.Select;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import pt.ulisboa.tecnico.meic.sirs.securesms.Crypto.AsymCrypto;
import pt.ulisboa.tecnico.meic.sirs.securesms.Crypto.KeyHelper;
import pt.ulisboa.tecnico.meic.sirs.securesms.Crypto.SymCrypto;
import pt.ulisboa.tecnico.meic.sirs.securesms.Models.ContactModel;
import pt.ulisboa.tecnico.meic.sirs.securesms.Models.UserModel;
import pt.ulisboa.tecnico.meic.sirs.securesms.Sms.SmsEncoding;

public class MessageExchangeProtocol {
    private static final String TAG = MessageExchangeProtocol.class.getSimpleName();

    public static final int SESSION_KEY_DURATION = 5 * 24 * 60 * 60 * 1000; // 5 days
    private static final int INTEGRITY_LENGTH = 128;
    private static final int CIPHERED_KEY_LENGTH = 128;

    public static String send(String plainBody, ContactModel destination, BinaryTextEncoding encoding)
            throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, IOException,
            InvalidAlgorithmParameterException, InvalidParameterSpecException {

        UserModel user = new Select().from(UserModel.class).executeSingle();
        boolean includeSymmetricKey = false;

        byte[] plainBodyBytes = plainBody.getBytes();

        if (!destination.hasValidSessionKey()) {
            destination.setSessionKey(KeyHelper.generateSymmetricKey());
            destination.save();
            includeSymmetricKey = true;
        }

        SecretKey sessionKey = destination.getSessionKey();

        ByteArrayOutputStream messageComposition = new ByteArrayOutputStream();
        if (includeSymmetricKey) {
            messageComposition.write('k');
            byte[] cipheredKey = AsymCrypto.encrypt(sessionKey.getEncoded(), destination.getPublicKey());
            Log.d(TAG, "cipheredKey: " + encoding.encode(cipheredKey));
            messageComposition.write(cipheredKey);
        }
        else {
            messageComposition.write('0');
        }
        byte[] cipheredBody = SymCrypto.encrypt(plainBodyBytes, sessionKey);
        Log.d(TAG, "cipheredBody: " + encoding.encode(cipheredBody));
        messageComposition.write(cipheredBody);

        byte[] intermediateMessage = messageComposition.toByteArray();
        byte[] integrityPart = AsymCrypto.sign(intermediateMessage, user.getPrivateKey());

        Log.d(TAG, "intermediateMessage: " + encoding.encode(intermediateMessage));
        Log.d(TAG, "integrityPart: " + encoding.encode(integrityPart));

        ByteArrayOutputStream finalMessageComposition = new ByteArrayOutputStream();
        finalMessageComposition.write(integrityPart);
        finalMessageComposition.write(intermediateMessage);

        String finalMessage = encoding.encode(finalMessageComposition.toByteArray());
        Log.d(TAG, "send - finalMessage: " + finalMessage);

        return finalMessage;
    }

    public static String receive(String finalMessageText, ContactModel sender, BinaryTextEncoding encoding)
            throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, IOException,
            InvalidAlgorithmParameterException, InvalidParameterSpecException,
            FailedVerificationException, InvalidSessionKeyException {

        UserModel user = new Select().from(UserModel.class).executeSingle();

        Log.d(TAG, "receive - finalMessageText: " + finalMessageText);
        byte[] finalMessageBytes = encoding.decode(finalMessageText);
        byte[] integrityPart = Arrays.copyOfRange(finalMessageBytes, 0, INTEGRITY_LENGTH);
        byte[] intermediateMessage = Arrays.copyOfRange(finalMessageBytes, INTEGRITY_LENGTH, finalMessageBytes.length);

        Log.d(TAG, "integrityPart: " + encoding.encode(integrityPart));
        Log.d(TAG, "intermediateMessage: " + encoding.encode(intermediateMessage));

        if (!AsymCrypto.verify(intermediateMessage, integrityPart, sender.getPublicKey()))
            throw new FailedVerificationException();

        byte[] cipheredBody;
        if (intermediateMessage[0] == 'k') {
            byte[] cipheredKeyBytes = Arrays.copyOfRange(intermediateMessage, 1, CIPHERED_KEY_LENGTH + 1);
            cipheredBody = Arrays.copyOfRange(intermediateMessage, CIPHERED_KEY_LENGTH + 1, intermediateMessage.length);

            Log.d(TAG, "cipheredKeyBytes: " + encoding.encode(cipheredKeyBytes));
            byte[] sessionKeyBytes = AsymCrypto.decrypt(cipheredKeyBytes, user.getPrivateKey());

            sender.setSessionKey(KeyHelper.bytesToSecretKey(sessionKeyBytes));
            sender.save();
        }
        else {
            cipheredBody = Arrays.copyOfRange(intermediateMessage, 1, intermediateMessage.length);
        }
        Log.d(TAG, "cipheredBody: " + encoding.encode(cipheredBody));

        if (!sender.hasValidSessionKey()) {
            throw new InvalidSessionKeyException();
        }

        SecretKey sessionKey = sender.getSessionKey();

        byte[] plainBodyBytes = SymCrypto.decrypt(cipheredBody, sessionKey);
        return new String(plainBodyBytes);
    }
}
