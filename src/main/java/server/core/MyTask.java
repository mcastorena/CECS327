package server.core;

import server.chord.RemoteInputFileStream;

/**
 * TODO
 */
public class MyTask implements Runnable {

    private volatile boolean done = false;

    private volatile RemoteInputFileStream datastream = null;

    private volatile String filename = null;

    /**
     * Constructor
     *
     * @param filename ??
     */
    public MyTask(String filename) {
        this.filename = filename;
    }

    @Override
    public void run() {
        try {
            while (!done) {
                datastream = new RemoteInputFileStream(filename);
                done = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            done = true;
        }
    }

    public RemoteInputFileStream getDatastream() {
        return datastream;
    }
}
