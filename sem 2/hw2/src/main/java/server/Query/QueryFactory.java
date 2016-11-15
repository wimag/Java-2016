package server.Query;

import commons.Commands;
import exceptions.UnknownQueryException;

/**
 * Created by Mark on 11.10.2016.
 */
public class QueryFactory {

    /**
     * Given command and argument create a correct ServerQuery object
     *
     * @param type - type of query. Should be 1 or 2
     * @param path - path do desired file/directory
     * @return ServerQuery that represent requested type
     */
    public static ServerQuery create(Integer type, String path) {
        switch (type) {
            case Commands.LIST:
                return new ListServerQuery(path);
            case Commands.GET:
                return new GetServerQuery(path);
            case Commands.EXIT:
                return new ExitServerQuery();
            default:
                throw new UnknownQueryException("Invalid command type" + type.toString());
        }
    }
}
