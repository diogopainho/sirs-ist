package pt.ulisboa.tecnico.meic.sirs.securesms;

import android.util.Log;

import com.activeandroid.query.Select;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javax.crypto.SecretKey;

public class SecureSmsProtocol {
    private static final String TAG = SecureSmsProtocol.class.getSimpleName();
    private static final int INTEGRITY_LENGTH = 128;
    private static final int CIPHERED_KEY_LENGTH = 128;

    public static void send(String plainBody, Contact_Model destination) throws Exception {
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
            Log.d(TAG, "cipheredKey: " + SmsEncoding.encode(cipheredKey));
            messageComposition.write(cipheredKey);
        }
        else {
            messageComposition.write('0');
        }
        byte[] cipheredBody = SymCrypto.encrypt(plainBodyBytes, sessionKey);
        Log.d(TAG, "cipheredBody: " + SmsEncoding.encode(cipheredBody));
        messageComposition.write(cipheredBody);

        byte[] intermediateMessage = messageComposition.toByteArray();
        byte[] integrityPart = AsymCrypto.sign(intermediateMessage, user.getPrivateKey());

        Log.d(TAG, "intermediateMessage: " + SmsEncoding.encode(intermediateMessage));
        Log.d(TAG, "integrityPart: " + SmsEncoding.encode(integrityPart));

        ByteArrayOutputStream finalMessageComposition = new ByteArrayOutputStream();
        finalMessageComposition.write(integrityPart);
        finalMessageComposition.write(intermediateMessage);

        String finalMessage = SmsEncoding.encode(finalMessageComposition.toByteArray());
        Log.d(TAG, "send - finalMessage: " + finalMessage);

        SmsSender.send(destination.getPhoneNumber(), finalMessage);
    }

    public static String receive(String finalMessageText, Contact_Model sender)
            throws Exception {
        UserModel user = new Select().from(UserModel.class).executeSingle();

        Log.d(TAG, "receive - finalMessageText: " + finalMessageText);
        byte[] finalMessageBytes = SmsEncoding.decode(finalMessageText);
        byte[] integrityPart = Arrays.copyOfRange(finalMessageBytes, 0, INTEGRITY_LENGTH);
        byte[] intermediateMessage = Arrays.copyOfRange(finalMessageBytes, INTEGRITY_LENGTH, finalMessageBytes.length);

        Log.d(TAG, "integrityPart: " + SmsEncoding.encode(integrityPart));
        Log.d(TAG, "intermediateMessage: " + SmsEncoding.encode(intermediateMessage));

        if (!AsymCrypto.verify(intermediateMessage, integrityPart, sender.getPublicKey()))
            throw new Exception(); // TODO

        byte[] cipheredBody;
        if (intermediateMessage[0] == 'k') {
            byte[] cipheredKeyBytes = Arrays.copyOfRange(intermediateMessage, 1, CIPHERED_KEY_LENGTH + 1);
            cipheredBody = Arrays.copyOfRange(intermediateMessage, CIPHERED_KEY_LENGTH + 1, intermediateMessage.length);

            Log.d(TAG, "cipheredKeyBytes: " + SmsEncoding.encode(cipheredKeyBytes));
            byte[] sessionKeyBytes = AsymCrypto.decrypt(cipheredKeyBytes, user.getPrivateKey());

            sender.setSessionKey(KeyHelper.bytesToSecretKey(sessionKeyBytes));
            sender.save();
        }
        else {
            cipheredBody = Arrays.copyOfRange(intermediateMessage, 1, intermediateMessage.length);
        }
        Log.d(TAG, "cipheredBody: " + SmsEncoding.encode(cipheredBody));

        if (!sender.hasValidSessionKey()) {
            // request key
        }

        SecretKey sessionKey = sender.getSessionKey();

        byte[] plainBodyBytes = SymCrypto.decrypt(cipheredBody, sessionKey);
        return new String(plainBodyBytes);
    }
}
