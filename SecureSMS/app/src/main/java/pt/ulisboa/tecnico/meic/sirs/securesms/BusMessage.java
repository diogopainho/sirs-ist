package pt.ulisboa.tecnico.meic.sirs.securesms;


import pt.ulisboa.tecnico.meic.sirs.securesms.Models.MessageModel;

public class BusMessage {
    private MessageModel message_model;

    public BusMessage(MessageModel message_model){
        this.message_model = message_model;
    }

    public MessageModel getMessage_model() {
        return message_model;
    }
}
