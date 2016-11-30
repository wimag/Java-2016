package torrent.server.storage;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Mark on 08.11.2016.
 */
public class ConnectionManager {
    private final Map<InetSocketAddress, Date> liveUsers = new HashMap<>();
    private static final long TIMEOUT = 1 * 60 * 1000; //5 minutes timeout TODO

    /**
     * Given a collection of addresses filter those, who updated
     * their status recently
     * @param peers - collection of peer addresses
     * @return collection of peers that are alive
     */
    public synchronized List<InetSocketAddress> filterDead(Collection<InetSocketAddress> peers){
        update();
        return peers.stream().filter(liveUsers::containsKey).collect(Collectors.toList());
    }

    /**
     * register a new peer or update info about existing one
     * @param peer - who pinged
     * @return operation status (almost always true)
     */
    public synchronized boolean registerPeer(InetSocketAddress peer){
        System.out.println("Heartbeat from " + peer + " received");
        liveUsers.put(peer, new Date());
        return true;
    }

    /**
     * Filter alive peers and remove dead users
     */
    private synchronized void update(){
        Date now = new Date();
        for(Iterator<Map.Entry<InetSocketAddress, Date>> it = liveUsers.entrySet().iterator(); it.hasNext();){
            Map.Entry<InetSocketAddress, Date> entry = it.next();
            if(now.getTime() - entry.getValue().getTime() > TIMEOUT){
                it.remove();
            }
        }
    }
}
