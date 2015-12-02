package pt.ulisboa.tecnico.meic.sirs.securesms;


public class BusMessage {
    private Message_Model message_model;

    public BusMessage(Message_Model message_model){
        this.message_model = message_model;
    }

    public Message_Model getMessage_model() {
        return message_model;
    }
}
