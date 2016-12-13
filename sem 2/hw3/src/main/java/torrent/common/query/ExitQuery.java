package torrent.common.query;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Mark on 09.11.2016.
 */
public class ExitQuery extends Query{
    public ExitQuery(DataInputStream is) {
        super(is);
    }

    @Override
    public void execute(DataOutputStream os) throws IOException {

    }
}
