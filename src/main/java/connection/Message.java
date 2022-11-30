package connection;

import javax.swing.*;
import java.io.File;
import java.io.Serializable;

public class Message implements Serializable {
    public MessageType messageType;
    public String message;
    public ImageIcon image;


    public Message(MessageType messageType, String message) {
        this.messageType = messageType;
        this.message = message;
        this.image = null;
    }

    public Message(MessageType messageType) {
        this.messageType = messageType;
        this.message = null;
        this.image = null;
    }

    public Message(MessageType messageType, ImageIcon image) {
        this.messageType = messageType;
        this.message = null;
        this.image = image;
    }


    @Override
    public boolean equals(Object o) {
        return this.messageType == o;
    }
}
