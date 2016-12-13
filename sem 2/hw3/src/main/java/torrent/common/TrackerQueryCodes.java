package torrent.common;

/**
 * Created by Mark on 15.11.2016.
 */
public class TrackerQueryCodes {
    private TrackerQueryCodes(){}

    public static final byte EXIT_QUERY = -1;
    public static final byte LIST_QUERY = 1;
    public static final byte UPLOAD_QUERY = 2;
    public static final byte SOURCES_QUERY = 3;
    public static final byte UPDATE_QUERY = 4;
}
