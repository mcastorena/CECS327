import model.Collection;
import utility.Deserializer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
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

    public static List<Collection> songList;

    static {
        try {
            songList = d.deserializeMusicLibrary();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String [] args)
    {
        int a = new SearchResultDispatcher().getSize("friend");
        System.out.println(a);
        serverCommunicationProtocol myServer = new serverCommunicationProtocol(portNumber);
        myServer.start();
    }
}
