package client.gui.Landing;

import client.app.App;
import com.google.gson.stream.JsonReader;
import client.data.UserSession;
import client.model.User;
import client.rpc.CECS327InputStream;
import client.rpc.ProxyInterface;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 */
public class LandingService {

    /**
     * The single instance that verifies user credentials
     */
    private static LandingService instance;

    /**
     * The session associated with an authenticated user
     */
    protected User currentSession;

    /**
     * Proxy that the client is connected through
     */
    ProxyInterface proxy;

    private LandingService() {
    }

    /**
     * Singleton instance of LandingService
     *
     * @param proxy - Proxy that the client is connected through
     * @return - Singleton instance of LandingService
     */
    public static LandingService getInstance(ProxyInterface proxy) {
        if (instance == null) {
            instance = new LandingService();
            instance.setProxy(proxy);
        }
        return instance;
    }

    /**
     * Attempts to authorize the user with the provided credentials
     *
     * @param username - Username that the user entered
     * @param password - Password that the user entered
     * @return - True if the User is authorized, false otherwise
     * @throws IOException - Required for InputStream
     */
    public boolean authorizeUser(String username, String password) throws IOException {
        // Grab the list of authorized Users
        JsonReader reader = new JsonReader(new InputStreamReader(new CECS327InputStream(username, password, proxy), "UTF-8"));

        reader.beginObject();
        reader.nextName();
        App.userToken = reader.nextInt();
        reader.endObject();

        // TODO: Add this info to loginDispatcher
        currentSession = new User(username, "default", password);
        UserSession.setCurrentSession(currentSession);
        return App.userToken != -1;
    }

    //region Getters and Setters
    protected User getCurrentSession() {
        return this.currentSession;
    }

    public void setProxy(ProxyInterface proxy) {
        this.proxy = proxy;
    }
    //endregion
}
