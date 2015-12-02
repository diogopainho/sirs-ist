package pt.ulisboa.tecnico.meic.sirs.securesms.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


@Table(name="Messages")
public class MessageModel extends Model {
    @Column(name="PhoneNumber") String phoneNumber;
    @Column(name="Type") Boolean type;
    @Column(name="Message") String message = new String();

    public MessageModel() {

    }

    public MessageModel(String fromto, String message, Boolean type) {
        this.phoneNumber = fromto;
        this.message = message;
        this.type = type;
    }


    public String getMessage() {
        return message;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Boolean getType() {
        return type;
    }
}

