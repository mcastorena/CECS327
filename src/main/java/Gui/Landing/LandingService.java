package Gui.Landing;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import model.User;
import utility.Deserializer;

public class LandingService {
    private static LandingService instance;

    private Deserializer deserializer;

    protected User currentSession;
    public List<User> userList;
    public HashMap<String,User> usersInfo = new HashMap<>();

    private LandingService () {
       try {
           deserializer = new Deserializer();
           userList = deserializer.deserializeUsers();

           for (User u : userList) {
               if (usersInfo.containsValue(u)) {
                   throw new IllegalStateException("Duplicate user found in usersInfo");
               }
               usersInfo.put(u.getUsername()+u.getPassword(), u);
           }

       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    public static LandingService getInstance() {
        if (instance == null) {
                instance = new LandingService();
        }
        return instance;
    }

    public boolean authorizeUser(String username, String password) {
        currentSession = usersInfo.get(username+password);
        return currentSession != null;
    }

    protected User getCurrentSession() {
        return this.currentSession;
    }

    public List<User> userList() {
        return userList;
    }
}
