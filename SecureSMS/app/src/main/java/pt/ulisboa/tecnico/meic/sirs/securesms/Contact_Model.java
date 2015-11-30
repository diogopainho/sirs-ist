package pt.ulisboa.tecnico.meic.sirs.securesms;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by diogopainho on 22/11/15.
 */

@Table(name="Contacts")
public class Contact_Model extends Model {
    @Column(name="Name") String name;
    @Column(name="Phone_Number") String phonenumber;
    @Column(name="Public_Key") byte[] publickey;

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

    public String getPhoneNumber() {
        return phonenumber;
    }

    public byte[] getPublicKey() {
        return publickey;
    }

    public void setName(String name) {
        this.name = name;
    }

}
