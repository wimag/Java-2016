package torrentGUI.scnene.trackers;

/**
 * Created by Mark on 14.12.2016.
 *
 * Entity tracking list entries
 */
public interface ListTracker {

    /**
     * Get refresh interval
     * @return float representing number of seconds between refreshes
     */
    float getTimeout();

    /**
     * List of entries to be shown
     * @return array of displayable options
     */
    String[] getOptions();

    /**
     * Callback when item is picked
     * @param id
     */
    void onPick(int id);

    FileMetaInf getMetaFor(int id);
}
