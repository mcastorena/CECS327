package ServerMain;

import model.Collection;
import utility.Deserializer;

import java.io.IOException;
import java.util.List;

public class server {

    private static final int portNumber = 2223;
    public static byte[] byteSearchResult;
    public static Deserializer d;

    static {
        try {
            d = new Deserializer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Collection> songList = d.getMusicDatabase();;

    public static void main(String [] args)
    {
        serverCommunicationProtocol myServer = new serverCommunicationProtocol(portNumber);
        myServer.start();
    }
}
