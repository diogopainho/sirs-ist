package pt.ulisboa.tecnico.meic.sirs.securesms;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;


@Table(name="Contacts")
public class Contact_Model extends Model {
    @Column(name="Name") String name;
    @Column(name="Phone_Number") String phonenumber;
    @Column(name="Public_Key") byte[] publickey;
    @Column(name="Session_Key") byte[] sessionKey;

    public Contact_Model() {
    }

    public Contact_Model(String name, String phone_number, byte[] publicKeyBytes) {
        this.name = name;
        this.phonenumber = phone_number;
        this.publickey = publicKeyBytes;
    }

    public Contact_Model(String name, String phone_number, PublicKey publicKey) {
        this.name = name;
        this.phonenumber = phone_number;
        this.publickey = publicKey.getEncoded();
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
    }

    public boolean hasValidSessionKey() {
        return sessionKey != null; // TODO: implement expiration
    }
}
