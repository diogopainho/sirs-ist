package pt.ulisboa.tecnico.meic.sirs.securesms;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by diogopainho on 22/11/15.
 */

@Table(name="Contacts")
public class Contact_Model extends Model {
    @Column(name="Name") String name;
    @Column(name="Phone_Number") int phonenumber;
    @Column(name="Public_Key") int publickey;

    public Contact_Model() {
    }

    public Contact_Model(String name, int phone_number, int public_key) {
        this.name = name;
        this.phonenumber = phone_number;
        this.publickey = public_key;
    }

    public String getName() {
        return name;
    }

    public int getPhoneNumber() {
        return phonenumber;
    }

    public int getPublicKey() {
        return publickey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(int phone_number) {
        this.phonenumber = phone_number;
    }

    public void setPublicKey(int public_key) {
        this.publickey = public_key;
    }
}
