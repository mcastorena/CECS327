package server.chord;

import com.google.gson.JsonElement;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * Chord Interface
 */
public interface ChordMessageInterface extends Remote {

    public ChordMessageInterface getPredecessor() throws RemoteException;

    public ChordMessageInterface getSuccessor() throws RemoteException;

    ChordMessageInterface locateSuccessor(long key) throws RemoteException;

    ChordMessageInterface closestPrecedingNode(long key) throws RemoteException;

    public void joinRing(String Ip, int port) throws RemoteException;

    public void joinRing(ChordMessageInterface successor) throws RemoteException;

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

    public void bulk(DFS.PagesJson page, String file) throws Exception;

    public void onChordSize(Long source, int n) throws RemoteException;

    int getChordSize() throws RemoteException;

    public void mapContext(long pageGUID, MapReduceInterface mapper, IDFSInterface coordinator, String file) throws Exception;

    public void reduceContext(long pageGuid, MapReduceInterface reducer, DFS coordinator, String file) throws Exception;

    void emit(String key, JsonElement value, IDFSInterface context, String file) throws Exception;

    void addKeyValue(String key, String value, String filename, long guid) throws Exception;
}
