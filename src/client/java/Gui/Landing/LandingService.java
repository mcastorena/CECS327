package Gui.Landing;

import app.Main;
import com.google.gson.stream.JsonReader;
import data.UserSession;
import model.User;
import rpc.CECS327InputStream;
import rpc.ProxyInterface;

import java.io.IOException;
import java.io.InputStreamReader;

public class LandingService {

    private static LandingService instance;
    protected User currentSession;
    ProxyInterface proxy;

    private LandingService() {
    }

    public static LandingService getInstance(ProxyInterface proxy) {
        if (instance == null) {
            instance = new LandingService();
            instance.setProxy(proxy);
        }
        return instance;
    }

    public boolean authorizeUser(String username, String password) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(new CECS327InputStream(username, password, proxy), "UTF-8"));

        reader.beginObject();
        reader.nextName();

        Main.userToken = reader.nextInt();
        reader.endObject();
        System.out.println("In authorize user" + Main.userToken + reader);

        // Can get email from loginDispatcher, if needed in the future.
        currentSession = new User(username, "default", password);
        UserSession.setCurrentSession(currentSession);
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
        if(this.proxy == null)this.proxy = proxy;

    }
}
