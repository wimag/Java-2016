package torrentGUI.scnene.trackers;

import torrent.common.storage.ServerFile;
import torrentGUI.GuiClient;
import torrentGUI.scnene.MainScreen;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Mark on 14.12.2016.
 */
public class RemoteFilesTracker implements ListTracker{
    private final GuiClient client;
    private final MainScreen parent;
    private List<ServerFile> files;
    public RemoteFilesTracker(MainScreen parent, GuiClient client) {
        this.client = client;
        this.parent = parent;
    }


    @Override
    public String[] getOptions() {
        try {
            this.files = client.client.listFiles();
        } catch (IOException e) {
            return new String[0];
        }
        files.sort((ServerFile a, ServerFile b) -> Integer.compare(a.id, b.id));
        return files.stream().map((ServerFile a) -> (new File(a.name)).getName() + " (" + a.id + ")").toArray(String[]::new);
    }

    @Override
    public void onPick(int id) {
        FileMetaInf meta = getMetaFor(id);
        parent.showMetaInf(meta);

    }

    @Override
    public float getTimeout() {
        return 3f;
    }

    /**
     * Get meta infomation from selected number
     * @return
     */
    @Override
    public FileMetaInf getMetaFor(int id){
        if(files.size() < id){
            return null;
        }
        ServerFile file = files.get(id);
        return new FileMetaInf(file.size, new File(file.name).getName(), client.storage.hasFile(file.id), file.id);
    }
}
