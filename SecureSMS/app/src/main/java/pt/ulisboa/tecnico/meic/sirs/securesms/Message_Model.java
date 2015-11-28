package pt.ulisboa.tecnico.meic.sirs.securesms;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.security.Timestamp;
import java.util.Calendar;
import java.util.Date;


@Table(name="Messages")
public class Message_Model extends Model {
    @Column(name="PhoneNumber") String phonenumber;
    @Column(name="Type") Boolean type;
    @Column(name="Message") String message = new String();

    public Message_Model() {

    }

    public Message_Model(String fromto, String message, Boolean type) {
        this.phonenumber = fromto;
        this.message = message;
        this.type = type;

    }


    public String getMessage() {
        return message;
    }

    public String getPhoneNumber() {
        return phonenumber;
    }

    public Boolean getType() {
        return type;
    }
}

