package pt.ulisboa.tecnico.meic.sirs.securesms;

public class PlainMessage {
    private byte[] content;

    public PlainMessage(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {
        return this.content;
    }
}
