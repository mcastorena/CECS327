package rpc; /**
 * The Proxy implements ProxyInterface class. The class is incomplete
 *
 * @author Oscar Morales-Ponce
 * @version 0.15
 * @since 2019-01-24
 */

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;


public class Proxy implements ProxyInterface {
    clientCommunicationProtocol communicate = null;

    // Constructor
    public Proxy(int portNumber) {
        this.communicate = new clientCommunicationProtocol();
        communicate.connect(portNumber);
    }

    /*
     * Executes the  remote method "remoteMethod". The method blocks until
     * it receives the reply of the message.
     */
    public synchronized JsonObject synchExecution(String remoteMethod, String[] param) throws IOException {
        JsonObject nextObject = null;
        JsonParser parser = new JsonParser();

        // Read json from methods.json
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream("/appdata/methods.json")));
             JsonReader jsonReader = new JsonReader(br)){

                 jsonReader.beginArray();

                 boolean methodNotFound = true;
                 while(jsonReader.hasNext() && methodNotFound)
                 {
                     // Get each remote method
                     nextObject = parser.parse(jsonReader).getAsJsonObject();
                     JsonPrimitive method = nextObject.get("remoteMethod").getAsJsonPrimitive();

                     // If the remote method is the one we are trying to execute
                     // extract call semantics from json and add the params.
                    if(method.getAsString().equals(remoteMethod))
                    {
                         methodNotFound = false;

                         // TODO: implement call semantics
                         JsonPrimitive semantics = nextObject.get("callSemantic").getAsJsonPrimitive();

                         nextObject.remove("callSemantic");

                         JsonObject jsonParams = nextObject.get("params").getAsJsonObject();
                         JsonObject jsonParamsToWrite = new JsonObject();

                         // Get each param key, so we can add the value
                         int i = 0;
                         for (Map.Entry<String, JsonElement> entry : jsonParams.entrySet()) {
                             String key = entry.getKey();
                             jsonParamsToWrite.addProperty(key, param[i]);
                             i++;
                         }

                         // Remove empty params
                         nextObject.remove("params");

                         // Add new params
                         nextObject.add("param", jsonParamsToWrite);
                     }


                 }
             }

                     System.out.println("Sending request: " + nextObject.toString());
                     String strRet = communicate.sendRequest(nextObject.toString().trim());
                     System.out.println("Returning response from server to inputstream: " + strRet);
                     String myReturn = strRet.trim();
        return parser.parse(myReturn).getAsJsonObject();
    }

    /*
     * Executes the  remote method remoteMethod and returns without waiting
     * for the reply. It does similar to synchExecution but does not
     * return any value
     *
     */
    public void asynchExecution(String remoteMethod, String[] param) {
        return;
    }
}


