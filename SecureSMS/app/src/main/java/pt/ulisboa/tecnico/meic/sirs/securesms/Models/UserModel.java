package pt.ulisboa.tecnico.meic.sirs.securesms.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import pt.ulisboa.tecnico.meic.sirs.securesms.Crypto.KeyHelper;


@Table(name="Users")
public class UserModel extends Model {

    @Column(name="Name") String name;
    @Column(name="PhoneNumber") String phonenumber;
    @Column(name="Password") String password;
    @Column(name="PrivateKey") byte[] privatekey;
    @Column(name="PublicKey") byte[] publickey;

    public UserModel() {
    }

    public UserModel(String name, String phonenumber, String password, KeyPair keyPair) {
        this.name = name;
        this.phonenumber = phonenumber;
        this.password = password;
        this.privatekey = keyPair.getPrivate().getEncoded();
        this.publickey = keyPair.getPublic().getEncoded();
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phonenumber;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getPrivateKeyBytes() { return privatekey; }

    public byte[] getPublicKeyBytes() { return publickey; }

    public PublicKey getPublicKey() { return KeyHelper.bytesToPublicKey(this.publickey); }

    public PrivateKey getPrivateKey() { return KeyHelper.bytesToPrivateKey(this.privatekey); }


}
