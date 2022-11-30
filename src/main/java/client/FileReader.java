package client;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileReader extends JFileChooser {

    ImageIcon image;
    File file;
    JFrame frame;

    public FileReader(JFrame frame) {
        this.frame = frame;
        this.setCurrentDirectory(new File(System.getProperty("user.home")));
        if(this.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            this.file = this.getSelectedFile();
        }
    }

    public ImageIcon getImage() {
        this.image = new ImageIcon(this.file.getAbsolutePath());
        return this.image;
    }

}
