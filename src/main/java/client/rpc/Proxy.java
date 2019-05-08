package client.rpc;

/**
 * The Proxy implements ProxyInterface class. The class is incomplete
 *
 * @author Oscar Morales-Ponce
 * @version 0.15
 * @since 2019-01-24
 */

import com.fasterxml.uuid.Generators;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


/**
 * Proxy for communication between a client and servers
 */
public class Proxy implements ProxyInterface {

    /**
     * Logger for the Proxy
     */
    private static final Logger LOGGER = Logger.getLogger(Proxy.class);

    /**
     * Connection to Client
     */
    private ClientCommunicationProtocol communicate;

    /**
     * Location of Music
     */
    private static final URL MUSIC_URL = Proxy.class.getResource("/client/appdata/methods.json");

    /**
     * Constructor
     *
     * @param portNumber - Port to open the Proxy on
     */
    public Proxy(int portNumber) {
        this.communicate = new ClientCommunicationProtocol();
        communicate.connect(portNumber);
    }

    /**
     * Synchronous execution of a remote method. The method blocks until it
     * receives the reply of the message.
     *
     * @param remoteMethod - Remote method being executed
     * @param params       - Parameters being sent to the remote method
     * @return Server response as a JSON object
     */
    public synchronized JsonObject syncExecution(String remoteMethod, Map<String, String> params) {
        JsonObject remoteMethodJO = getRemoteMethodFromJson(remoteMethod);

        attachParams(remoteMethodJO, params);

        attachUUID(remoteMethodJO);
        String callSemantic = getCallSemanticFromJson(remoteMethodJO);

        System.out.println("Sending request: " + remoteMethodJO.toString());
        String strRet = communicate.sendRequest(remoteMethodJO.toString().trim(), callSemantic);
        if (strRet == null) return null;
        System.out.println("Returning response from server to inputstream: " + strRet);
        String myReturn = strRet.trim();

        JsonParser parser = new JsonParser();

        return parser.parse(myReturn).getAsJsonObject();
    }

    /**
     * Asynchronous execution of a remote method.
     *
     * @param remoteMethod - Remote method being executed
     * @param params       - Parameters being sent to the remote method
     */
    public synchronized void asyncExecution(String remoteMethod, Map<String, String> params) {
        JsonObject remoteMethodJO = getRemoteMethodFromJson(remoteMethod);

        attachParams(remoteMethodJO, params);
        attachUUID(remoteMethodJO);

        communicate.sendAsyncRequest(remoteMethodJO.toString().trim());
    }

    /**
     * Retrieves a remote method given it's name.
     *
     * @param remoteMethod - Name of the remote method being searched for
     * @return The remote method if found, otherwise null
     */
    public JsonObject getRemoteMethodFromJson(String remoteMethod) {
        Gson gson = new Gson();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(MUSIC_URL.openStream()));
            JsonObject jsonObject = gson.fromJson(br, JsonObject.class).getAsJsonObject();

            return jsonObject.get(remoteMethod).getAsJsonObject();
        } catch (IOException e) {
            LOGGER.error("ERROR: Proxy.getRemoteMethod: " + e);
            return null;
        }
    }

    /**
     * Attaches parameters to the remote method object.
     *
     * @param methodObject - Remote method
     * @param params       - Parameters to be attached
     */
    private void attachParams(JsonObject methodObject, Map<String, String> params) {
        JsonObject paramsObject = methodObject.get("params").getAsJsonObject();

        Set<Map.Entry<String, String>> entrySet = params.entrySet();

        for (Map.Entry<String, String> entry : entrySet) {
            paramsObject.addProperty(entry.getKey(), entry.getValue());
        }

        methodObject.add("params", paramsObject);
    }

    /**
     * Attaches UUID to the remote method.
     *
     * @param methodObject - Remote method to have UUID attached
     */
    private void attachUUID(JsonObject methodObject) {
        UUID uuid = Generators.timeBasedGenerator().generate();
        methodObject.addProperty("requestId", uuid.toString());
    }

    /**
     * Retrieves the call semantic from the remote method.
     *
     * @param methodObject - Remote method being accessed
     * @return Call semantic associated with the remote method
     */
    private String getCallSemanticFromJson(JsonObject methodObject) {
        return methodObject.get("callSemantic").getAsString();
    }
}


