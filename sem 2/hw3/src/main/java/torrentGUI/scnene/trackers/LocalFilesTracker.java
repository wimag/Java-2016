package torrentGUI.scnene.trackers;

import torrent.client.storage.ClientFile;
import torrent.client.storage.ClientStorage;
import torrentGUI.scnene.MainScreen;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Mark on 14.12.2016.
 */
public class LocalFilesTracker implements ListTracker {
    private final ClientStorage storage;
    private final MainScreen parent;

    public LocalFilesTracker(MainScreen parent, ClientStorage storage) {
        this.parent = parent;
        this.storage = storage;
    }

    @Override
    public String[] getOptions() {
        return storage.getAllIds().stream().sorted().map(x -> new File(storage.getFile(x).path).getName()).toArray(String[]::new);
    }

    @Override
    public void onPick(int id) {
        parent.showMetaInf(getMetaFor(id));
        //TODO - show progress
    }

    @Override
    public float getTimeout() {
        return 0.25f;
    }

    @Override
    public FileMetaInf getMetaFor(int id) {
        ArrayList<Integer> ids = storage.getAllIds();
        Collections.sort(ids);
        if(!ids.contains(id)){
            return null;
        }
        int readID = ids.get(id);
        ClientFile file = storage.getFile(readID);
        String name = (new File(file.path)).getName();
        return new FileMetaInf(file.size, name, true, readID);

    }
}
