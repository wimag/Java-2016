package torrentGUI.scnene.files;

import torrentGUI.GuiClient;
import torrentGUI.scnene.trackers.FileMetaInf;

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
public class FileDownloader implements Runnable {
    private final GuiClient client;
    private final FileMetaInf meta;

    public FileDownloader(GuiClient client, FileMetaInf meta) {
        this.client = client;
        this.meta = meta;
    }

    @Override
    public void run() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        while(true){
            int result = chooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION)
            {
                File file = chooser.getSelectedFile();
                if(file != null){
                    try {
                        client.client.downloadFile(meta.id, file.getAbsolutePath(), meta.size);
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
