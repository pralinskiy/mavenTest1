package connection;

import java.io.Serializable;

public class Message implements Serializable {
    public MessageType messageType;
    public String message;

    public Message(MessageType messageType, String message) {
        this.messageType = messageType;
        this.message = message;
    }

    public Message(MessageType messageType) {
        this.messageType = messageType;
        this.message = null;
    }



    @Override
    public boolean equals(Object o) {
        return this.messageType == o;
    }
}
