package pt.ulisboa.tecnico.meic.sirs.securesms.MessageExchange;

public interface BinaryTextEncoding {
    String encode(byte[] data);

    byte[] decode(String encodedText);
}
