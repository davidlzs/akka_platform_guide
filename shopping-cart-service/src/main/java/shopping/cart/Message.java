package shopping.cart;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Message implements CborSerializable{
    public final String text;

    @JsonCreator
    public Message(String text) {
        this.text = text;
    }
}
