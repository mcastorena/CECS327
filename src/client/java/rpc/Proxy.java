/**
* The Proxy implements ProxyInterface class. The class is incomplete 
* 
* @author  Oscar Morales-Ponce
* @version 0.15
* @since   2019-01-24 
*/

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Proxy implements ProxyInterface {
    clientCommunicationProtocol communicate = null;

    // Constructor
    public Proxy(int portNumber)
    {
        this.communicate = new clientCommunicationProtocol();
        communicate.connect(portNumber);
    }
    
    /*
    * Executes the  remote method "remoteMethod". The method blocks until
    * it receives the reply of the message. 
    */
    public JsonObject synchExecution(String remoteMethod, String[] param)
    {
        JsonObject jsonRequest = new JsonObject();
        JsonObject jsonParam = new JsonObject();
        
        jsonRequest.addProperty("remoteMethod", remoteMethod);
        jsonRequest.addProperty("objectName", "SongServices");
        // It is hardcoded. Instead it should be dynamic using  RemoteRef
        if (remoteMethod.equals("getSongChunk"))
        {
            
            jsonParam.addProperty("song", param[0]);
            jsonParam.addProperty("fragment", param[1]);       
        
        }
        if (remoteMethod.equals("getFileSize"))
        {
            jsonParam.addProperty("song", param[0]);        
        }
        jsonRequest.add("param", jsonParam);
        
        JsonParser parser = new JsonParser();
        System.out.println("Sending request: "+ jsonRequest.toString());
        String strRet =  communicate.sendRequest(jsonRequest.toString());
        System.out.println("Returning response from server to inputstream: "+strRet);
        String myReturn = strRet.trim();
        return parser.parse(myReturn).getAsJsonObject();
    }

    /*
    * Executes the  remote method remoteMethod and returns without waiting
    * for the reply. It does similar to synchExecution but does not 
    * return any value
    * 
    */
    public void asynchExecution(String remoteMethod, String[] param)
    {
        return;
    }
}


