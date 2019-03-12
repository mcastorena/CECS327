package server.core;

import server.model.Collection;
import server.model.User;
import server.util.Deserializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {

    private static final int PORT_NUMBER = 2223;

    // TODO: move most of this static info to a new user session class?
    public static byte[] byteSearchResult;
    public static byte[] bytePlaylists;

    public static Deserializer d;

    public static List<User> currentSessions = new ArrayList<>();
    public static List<User> userList;
    public static HashMap<String,User> usersInfo = new HashMap<>();

    public static List<Collection> songList;

    public static Map<String, String> requestCache;
    
        // Used to update userList and usersInfo after a new user registers
    public static void update(){songList = d.getMusicDatabase();
        userList = d.deserializeUsers();
        for (User u : userList) {
            if (usersInfo.containsValue(u)) {
                throw new IllegalStateException("Duplicate user found in usersInfo");
            }
            usersInfo.put(u.getUsername()+u.getPassword(), u);
        }
    }

    public static void main(String[] args) {
        requestCache = new HashMap<>();
        d = new Deserializer();
        songList = d.getMusicDatabase();
        userList = d.deserializeUsers();
        for (User u : userList) {
            if (usersInfo.containsValue(u)) {
                throw new IllegalStateException("Duplicate user found in usersInfo");
            }
            usersInfo.put(u.getUsername()+u.getPassword(), u);
        }

        ServerCommunicationProtocol scp = new ServerCommunicationProtocol(PORT_NUMBER);
        scp.start();
    }
}
