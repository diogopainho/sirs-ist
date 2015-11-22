package pt.ulisboa.tecnico.meic.sirs.securesms;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


@Table(name="Messages")
public class Message_Model extends Model {
    @Column(name="Destination") String to;
    @Column(name="Message") String message = new String();
    @Column(name="Timestamp") int timestamp = 0;

    public Message_Model() {

    }

    public Message_Model(String to, String message) {
        this.to = to;
        this.message = message;

    }

    public String getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void incTimestamp() {
        this.timestamp += 1;
    }
}

