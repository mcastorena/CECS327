package server.chord;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public interface MapReduceInterface {
    public void map(String key, JsonObject value, IDFSInterface context, ChordMessageInterface chordContext, String file) throws Exception;

    public void reduce(String key, ArrayList values, IDFSInterface context, ChordMessageInterface chordContext, String file) throws Exception;
}