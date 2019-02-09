import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class TestDriverA {
    public static void main(String[] args) {
        try {
            // 1. Deserialize Music and User JSONs into POJOs
            Deserializer d = new Deserializer();
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
            ////////////////////////////////////////////////////////

            /** Makeshift login system **/
            String username = "mark",
                    password = "manson";

            User session = null;
            if ((session = usersInfo.get(username+password)) == null) {
                System.out.println("Login failed.");
                return;
            }
            System.out.println("Login succeeded.");
            /****************************/

            Release r = new Release();
            r.setId(555);
            r.setName("A new song");

            Artist a = new Artist();
            a.setName("John");

            Song s = new Song();
            s.setTitle("A new songs");

            Collection c = new Collection(r, a, s);
            session.getUserProfile().addSongToPlaylist(c, "hits");

            new Serializer().updateUsersJson(usersInfo);

            System.out.println();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
