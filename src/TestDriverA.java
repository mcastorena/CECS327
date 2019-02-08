import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class TestDriverA {
    public static void main(String[] args) {
        try {
            Deserializer d = new Deserializer();

            /** Deserialize User JSON into a list of Users **/
            List<User> users = d.deserializeUsers();

            /**
             *  Map used for login.
             *  Key: username+password, no space
             *  Value: the User object
             */
            HashMap<String,User> usersInfo = new HashMap<>();

            for (User u : users) {
                if (usersInfo.containsValue(u)) {
                    throw new IllegalStateException("Duplicate user found in usersInfo");
                }
                usersInfo.put(u.getUsername()+u.getPassword(), u);
            }

            /** Makeshift login system **/
            String username = "mark",
                    password = "manson";

            User session = null;
            if ((session = usersInfo.get(username+password)) == null) {
                System.out.println("Login failed.");
                return;
            }
            System.out.println("Login succeeded.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
