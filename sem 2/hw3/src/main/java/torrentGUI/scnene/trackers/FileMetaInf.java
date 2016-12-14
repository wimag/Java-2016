package torrentGUI.scnene.trackers;

/**
 * Created by Mark on 14.12.2016.
 *
 * helper class
 */
public class FileMetaInf {
    public final long size;
    public final String name;
    public final boolean downloaded;
    public final int id;
    private final String bases[] = {"B", "KB", "MB", "GB", "TB"};

    public FileMetaInf(long size, String name, boolean downloaded, int id) {
        this.size = size;
        this.name = name;
        this.downloaded = downloaded;
        this.id = id;
    }

    public String formatName() {
        return name + (downloaded ? "\n(exists)" : "");
    }

    public String formatSize(){
        int base = 0;
        long tmp = size;
        while(tmp >= 1024){
            tmp /= 1024;
            base ++;
        }
        return Long.toString(tmp) + " " + bases[base];
    }
}
