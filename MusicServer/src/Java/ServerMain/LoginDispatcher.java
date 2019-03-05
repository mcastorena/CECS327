package ServerMain;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.User;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

public class LoginDispatcher {
    static final int FRAGMENT_SIZE = 8192;

    public LoginDispatcher()
    {

    }


    /*
     * login: return login token if authorized
     * @param username: the username from client
     * @param password: the password from client
     */
    public String login(String username, String password) throws IOException
    {
        User currentSession = server.usersInfo.get(username+password);

        JsonObject loginToken = new JsonObject();
        if(currentSession != null)
        {
            server.currentSessions.add(currentSession);

            loginToken.addProperty("loginToken", Integer.toString(server.currentSessions.indexOf(currentSession)));
            System.out.println(loginToken);
        }

        byte[] loginBytes = loginToken.toString().getBytes();

        byte buf[] = new byte[FRAGMENT_SIZE];

        System.out.println("LoginDispatcher is getting chunk");
        InputStream inputStream = new ByteArrayInputStream(loginBytes);
        inputStream.read(buf);
        inputStream.close();

        return Base64.getEncoder().encodeToString(buf);
    }
}
