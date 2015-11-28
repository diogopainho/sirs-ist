package pt.ulisboa.tecnico.meic.sirs.securesms;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;


@Table(name="MyContact")
public class MyContact extends Model {

    @Column(name="Name") String name;
    @Column(name="PhoneNumber") String phonenumber;
    @Column(name="Password") String password;
    @Column(name="PrivateKey") byte[] privatekey;
    @Column(name="PublicKey") byte[] publickey;

    public MyContact() {
    }

    public MyContact(String name, String phonenumber, String password, KeyPair keyPair) {
        this.name = name;
        this.phonenumber = phonenumber;
        this.password = password;
        this.privatekey = keyPair.getPrivate().getEncoded();
        this.publickey = keyPair.getPublic().getEncoded();
    }

    public String getPhoneNumber() {
        return phonenumber;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getBytesPrivatekey() {
        return privatekey;
    }

    public byte[] getBytesPublickey() { return publickey; }

    public String getName() {
        return name;
    }
}
