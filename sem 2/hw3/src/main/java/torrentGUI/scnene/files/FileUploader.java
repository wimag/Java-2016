package torrentGUI.scnene.files;

import torrentGUI.GuiClient;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Mark on 14.12.2016.
 *
 * Class for picking and uploading files.
 * Picking ifles with libgdx is hard due to
 * crossplatform issues. We can ignore it
 */
public class FileUploader implements Runnable {
    private final GuiClient client;

    public FileUploader(GuiClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        while(true){
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION)
            {
                File file = chooser.getSelectedFile();
                if(file != null && file.exists()){
                    try {
                        client.client.upload(file.getAbsolutePath());
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
