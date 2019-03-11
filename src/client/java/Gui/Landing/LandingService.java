package Gui.Landing;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import app.Main;
import com.google.gson.stream.JsonReader;
import data.UserSession;
import model.User;
import rpc.CECS327InputStream;
import rpc.ProxyInterface;

public class LandingService {
    private static LandingService instance;

    //private Deserializer deserializer;

    protected User currentSession;
    //public List<User> userList;
    //public HashMap<String,User> usersInfo = new HashMap<>();
    ProxyInterface proxy;

    private LandingService () {
//       try {
           //deserializer = new Deserializer();
           //userList = deserializer.deserializeUsers();

//           for (User u : userList) {
//               if (usersInfo.containsValue(u)) {
//                   throw new IllegalStateException("Duplicate user found in usersInfo");
//               }
//               usersInfo.put(u.getUsername()+u.getPassword(), u);
//           }
//
//       } catch (IOException e) {
//           e.printStackTrace();
//       }
    }

    public static LandingService getInstance(ProxyInterface proxy) {
        if (instance == null) {
                instance = new LandingService();
                instance.setProxy(proxy);
        }
        return instance;
    }

    public boolean authorizeUser(String username, String password) throws IOException {
        //currentSession = usersInfo.get(username+password);
        JsonReader reader = new JsonReader(new InputStreamReader(new CECS327InputStream(username, password, proxy), "UTF-8"));

        reader.beginObject();
        reader.nextName();

        Main.userToken = reader.nextInt();
        reader.endObject();
        System.out.println("In authorize user" + Main.userToken + reader);

        // TODO: Add this info to loginDispatcher
        currentSession = new User("default", "default","default");
        UserSession.setCurrentSession(currentSession);
        //return currentSession != null;
        return Main.userToken != 0;
    }

    protected User getCurrentSession() {
        return this.currentSession;
    }

//    public List<User> userList() {
//        return userList;
//    }

    public void setProxy(ProxyInterface proxy)
    {
        this.proxy = proxy;
    }
}
