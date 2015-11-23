package pt.ulisboa.tecnico.meic.sirs.securesms;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


@Table(name="Messages")
public class Message_Model extends Model {
    @Column(name="ReceivedFrom") String from;
    @Column(name="SentTo") String to;
    @Column(name="Message") String message = new String();
    @Column(name="Timestamp") int timestamp = 0;

    public Message_Model() {

    }

    public Message_Model(String fromto, String message, Boolean sent) {

        if(sent){
            this.to = fromto;
        } else {
            this.from = fromto;
        }

        this.message = message;
        this.timestamp += 1;

    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public void setFrom(String from) { this.from = from; }

    public void setTo(String to) {
        this.to = to;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

