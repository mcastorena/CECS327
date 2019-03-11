package ServerMain;

import model.Collection;
import model.User;
import utility.Deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class server {

    private static final int portNumber = 2223;

    // TODO: move most of this static info to a new user session class?
    public static byte[] byteSearchResult;
    public static byte[] bytePlaylists;

    public static Deserializer d;

    public static List<User> currentSessions = new ArrayList<>();
    public static List<User> userList;
    public static HashMap<String,User> usersInfo = new HashMap<>();

    static {
        try {
            d = new Deserializer();
            userList = d.deserializeUsers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Collection> songList = d.getMusicDatabase();

    public static void main(String [] args)
    {
        currentSessions.add(new User("dummy", "dummy", "dummy"));
        System.out.println("User Added");
        for (User u : userList) {
            if (usersInfo.containsValue(u)) {
                throw new IllegalStateException("Duplicate user found in usersInfo");
            }
            usersInfo.put(u.getUsername()+u.getPassword(), u);
        }


        serverCommunicationProtocol myServer = new serverCommunicationProtocol(portNumber);
        myServer.start();
    }
}
