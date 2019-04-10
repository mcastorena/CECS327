/**
 * RemoteInputFileStream Implements an Input Stream for big
 * files. It creates a server and return the address
 * The client must call connect() before reading
 *
 * @author Oscar Morales-Ponce
 * @version 0.15
 * @since 03-3-2019
 */
package server.chord;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;


/**
 * This class represents a remote stream of input data from a File
 */
public class RemoteInputFileStream extends InputStream implements Serializable {

    /**
     * IP address
     */
    public InetAddress IP;

    /**
     * Port for socket connection
     */
    public int port;

    /**
     * Length of the file being streamed
     */
    public int total;

    /**
     * Position of data being streamed
     */
    public int pos;

    /**
     * References numbytes summed with the previous value of lastRead
     */
    public int lastRead = 0;

    /**
     * Saves the previous numbytes read
     */
    private int lastRead2;

    /**
     * TODO
     */
    private int index = 0;

    /**
     * Number of bytes being read
     */
    public int numbytes;

    /**
     * Input of data being streamed
     */
    public InputStream input;

    /**
     *  Semaphore required for multithreading
     */
    public Semaphore sem;

    /**
     * Buffer length
     */
    private static int BUFFER_LENGTH = 2 << 15;

    /**
     * It stores a buffer with FRAGMENT_SIZE bytes for the current reading.
     * This variable is useful for UDP sockets. Thus buf is the datagram
     */
    protected byte buf[];

    /**
     * It prepares for the next buffer. In UDP sockets you can read nextbufer
     * while buf is in use
     */
    protected byte nextBuf[];

    /**
     * It is used to read the buffer
     */
    protected int fragment = 0;

    /**
     * Connects to the server to provide the file
     */
    public void connect() {
        this.buf = new byte[BUFFER_LENGTH];
        this.nextBuf = new byte[BUFFER_LENGTH];
        pos = 0;
        try {
            Socket socket = new Socket(IP.getLoopbackAddress(), port);
            input = socket.getInputStream();
            sem = new Semaphore(1);
            sem.acquire();
            getBuff(fragment);
            fragment++;
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
    }

    /**
     * Empty constructor
     * @throws FileNotFoundException
     */
    public RemoteInputFileStream() throws FileNotFoundException {

    }

    /**
     * Constructor.
     *
     * Starts a Server to provide the file.
     *
     * @param pathName Path of the file to be provided.
     * @param deleteAfter Boolean to determine whether to delete the file after the InputStream is closed.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public RemoteInputFileStream(String pathName, boolean deleteAfter) throws FileNotFoundException, IOException {
        File file = new File(pathName);
        total = (int) file.length();
        pos = 0;

        try {
            ServerSocket serverSocket = new ServerSocket(0);
            port = serverSocket.getLocalPort();
            IP = serverSocket.getInetAddress();

            new Thread() {
                public void run() {
                    try {
                        Socket socket = serverSocket.accept();
                        OutputStream socketOutputStream = socket.getOutputStream();
                        FileInputStream is = new FileInputStream(pathName);
                        while (is.available() > 0)
                            socketOutputStream.write(is.read());
                        is.close();
                        if (deleteAfter) {
                            file.delete();
                        }
                    } catch (Exception v) {
                        System.out.println(v);
                    }
                }
            }.start();
        } catch (Exception exc) {
            System.out.println(exc);
        }
    }

    /**
     * Constructor #2
     *
     * @param pathName
     * @throws FileNotFoundException
     * @throws IOException
     */
    public RemoteInputFileStream(String pathName) throws FileNotFoundException, IOException {
        this(pathName, false);
    }

    /**
     * getNextBuff reads the buffer. It gets the data using
     * the remote method getSongChunk
     */
    protected void getBuff(int fragment) throws IOException {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(total<10000?50:150); // Fill the buffer more
                    numbytes = input.read(nextBuf);
                    lastRead2 = lastRead; // Save the previous numbytes read so we know when to
                                          // transfer nextbuf into buf.
                    lastRead = numbytes + lastRead;
                    //System.out.println("Read buffer " + numbytes);

                    sem.release();
                    //             System.out.println("Read buffer");
                } catch (Exception e) {

                }
            }
        }.start();
    }


    /**
     * Reads the next byte of data from the input stream.
     */
    @Override
    public synchronized int read() throws IOException {

        if (pos >= total) {
            pos = 0;
            return -1;
        }
        //int posmod = pos % BUFFER_LENGTH;
        if (pos == 0 || pos == lastRead || pos == lastRead2)
        {
            try {
                sem.acquire();
            } catch (InterruptedException exc) {
                System.out.println(exc);
            }

            for (int i=0; i< numbytes; i++)
                buf[i] = nextBuf[i];

            getBuff(fragment);
            fragment++;
            index = 0;
        }
        int p = index;
        index++;
        pos++;

        return buf[p] & 0xff;
    }

    /**
     * Reads some number of bytes from the input stream and stores them
     * into the buffer array b.
     */
    @Override
    public synchronized int read(byte b[], int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        }

        if (pos >= total) {
            return -1;
        }
        int avail = total - pos;
        if (len > avail) {
            len = avail;
        }
        if (len <= 0) {
            return 0;
        }
        for (int i = off; i < off + len; i++)
            b[i] = (byte) read();

        return len;
    }

    /**
     *
     *
     * @return
     * @throws IOException
     */
    public int available() throws IOException {
        return total - pos;
    }

    /**
     * Generates threads to allow caller to block
     *
     * @param task
     * @return
     */
    public Thread getThread(Runnable task) {
        return new Thread(task);
    }
}
