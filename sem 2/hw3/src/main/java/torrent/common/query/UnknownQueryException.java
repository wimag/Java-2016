package torrent.common.query;

/**
 * Created by Mark on 04.11.2016.
 */
public class UnknownQueryException extends IllegalArgumentException {
    public UnknownQueryException(){
        super("Encountered unknown query, abotring");
    }
}
