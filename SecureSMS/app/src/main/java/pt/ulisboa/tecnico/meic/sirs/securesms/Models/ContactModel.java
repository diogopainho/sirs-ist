package pt.ulisboa.tecnico.meic.sirs.securesms.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.security.PublicKey;

import javax.crypto.SecretKey;

import pt.ulisboa.tecnico.meic.sirs.securesms.Crypto.KeyHelper;
import pt.ulisboa.tecnico.meic.sirs.securesms.MessageExchange.MessageExchangeProtocol;


@Table(name="Contacts")
public class ContactModel extends Model {
    @Column(name="Name") String name;
    @Column(name="Phone_Number") String phonenumber;
    @Column(name="Public_Key") byte[] publickey;
    @Column(name="Session_Key") byte[] sessionKey;
    @Column(name="Session_Key_Expiration") long sessionKeyExpiration;

    public ContactModel() {
    }

    public ContactModel(String name, String phoneNumber, byte[] publicKeyBytes) {
        this.name = name;
        this.phonenumber = phoneNumber;
        this.publickey = publicKeyBytes;
        this.sessionKey = null;
        this.sessionKeyExpiration = 0;
    }

    public ContactModel(String name, String phoneNumber, PublicKey publicKey) {
        this(name, phoneNumber, publicKey.getEncoded());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phonenumber;
    }

    public byte[] getPublicKeyBytes() {
        return publickey;
    }

    public PublicKey getPublicKey() {
        return KeyHelper.bytesToPublicKey(this.publickey);
    }

    public SecretKey getSessionKey() {
        return KeyHelper.bytesToSecretKey(this.sessionKey);
    }

    public void setSessionKey(SecretKey newSessionKey) {
        this.sessionKey = newSessionKey.getEncoded();
        this.sessionKeyExpiration = System.currentTimeMillis() + MessageExchangeProtocol.SESSION_KEY_DURATION;
    }

    public boolean hasValidSessionKey() {
        return sessionKey != null && sessionKeyExpiration >= System.currentTimeMillis();
    }
}
