package torrent.common.storage;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class represents server information about File
 */
public final class ServerFile implements Serializable {
    public final int id;
    public final String name;
    public final long size;
    public final Set<InetSocketAddress> peers = new HashSet<>();

    public ServerFile(int id, String name, long size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }

}
