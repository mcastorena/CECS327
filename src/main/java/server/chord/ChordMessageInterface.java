package server.chord;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import server.chord.RemoteInputFileStream;

import java.rmi.*;
import java.io.*;



public interface ChordMessageInterface extends Remote
{

    public ChordMessageInterface getPredecessor()  throws RemoteException;
    public ChordMessageInterface getSuccessor() throws RemoteException;

    // Pass a GUID and returns the node contianing the key
    ChordMessageInterface locateSuccessor(long key) throws RemoteException;


    ChordMessageInterface closestPrecedingNode(long key) throws RemoteException;
    public void joinRing(String Ip, int port)  throws RemoteException;
    public void joinRing(ChordMessageInterface successor)  throws RemoteException;
    public void notify(ChordMessageInterface j) throws RemoteException;
    public boolean isAlive() throws RemoteException;
    public long getId() throws RemoteException;
    
    public void leave() throws Exception;
    public String print() throws Exception;
    public String getPrefix() throws Exception;
    public void put(long guidObject, RemoteInputFileStream inputStream) throws IOException, RemoteException;
    public void put(long guidObject, String text) throws IOException, RemoteException;
    public RemoteInputFileStream get(long guidObject) throws IOException, RemoteException;   
    public byte[] get(long guidObject, long offset, int len) throws IOException, RemoteException;  
    public void delete(long guidObject) throws IOException, RemoteException;

    public int onChordSize(Long source, int n) throws RemoteException;
    public void bulk(DFS.PagesJson page) throws IOException;
    public void mapContext(long pageGUID, MapReduceInterface mapper, IDFSInterface coordinator, String file) throws Exception;
    public void reduceContext(JsonObject page, MapReduceInterface reducer, DFS coordinator, String file) throws Exception;
    void emit(String key, JsonElement value, IDFSInterface context, String file) throws Exception;
    void addKeyValue(String key, JsonElement value, IDFSInterface context, String filename, int pageNumber, long guid) throws Exception;

//    public void onPageComplete(String file) throws Exception;
}
