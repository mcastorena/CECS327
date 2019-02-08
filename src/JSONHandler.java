import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;

public class JSONHandler {

    private String UserTextBoxEntry;
    private String PasswordTextBoxEntry;

    public JSONHandler(String username, String password) {
        UserTextBoxEntry = username;
        PasswordTextBoxEntry = password;
    }

    public boolean JSONValidation() {
        boolean isUser = false;

        try (FileReader fr = new FileReader("user.json");
             JsonReader jr = new JsonReader(fr)) {
            System.out.println(UserTextBoxEntry + " " + PasswordTextBoxEntry);
            String infoType = "";
            String potentialUser = "";
            String potentialPassword = "";
            boolean foundUser;
            int i = 1;
            while (jr.hasNext()) {
                System.out.println("On Iteration" + i);
                JsonToken token = jr.peek();
                switch (token) {
                    case END_OBJECT: jr.endObject();
                        System.out.println("Found }");
                        break;
                    case NAME: infoType = jr.nextName();
                        System.out.println("item type " + infoType);
                        if(infoType.equals("username")) {
                            System.out.println("We have found a username");
                            potentialUser = jr.nextString();
                            System.out.println("Potential user is"+ potentialUser);
                            if (potentialUser.equals(UserTextBoxEntry)) {
                                jr.nextName();
                                potentialPassword = jr.nextString();
                                System.out.println("Potential password is" + potentialPassword);
                                if (potentialPassword.equals(PasswordTextBoxEntry) ) {
                                    isUser = true;
                                }
                            }
                        }
                        break;
                    case BEGIN_ARRAY: jr.beginArray();
                        System.out.println("Found [");
                        break;
                    case BEGIN_OBJECT: jr.beginObject();
                        System.out.println("Found {");
                        break;
                    case BOOLEAN: boolean bool = jr.nextBoolean();
                        System.out.println("Found bool is " + String.valueOf(bool));
                        break;
                    case END_ARRAY: jr.endArray();
                        System.out.println("Found ]");
                        break;
                    case END_DOCUMENT: jr.close();
                        System.out.println("Closing");
                        break;
                    case NULL: jr.nextNull();
                        System.out.println("Found null");
                        break;
                    case NUMBER: int number = jr.nextInt();
                        System.out.println("found " + number);
                        if(jr.hasNext() == false)
                            jr.skipValue();
                        break;
                    case STRING: String value = jr.nextString();
                        System.out.println("Found string " + value);
                        break;
                    default: System.out.println("Unknown Character found");
                        break;
                }


                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isUser;
    }
}