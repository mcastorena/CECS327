package server.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *
 */
public class ServerCommunicationProtocol extends Thread {

    /**
     * Size of the fragment
     */
    private static final int FRAGMENT_SIZE = 44100;

    /**
     * Size of the packet as a byte array
     */
    private byte[] packetSize = new byte[FRAGMENT_SIZE];

    /**
     * Socket to connect to the port
     */
    private DatagramSocket mySocket;

//    DataInputStream inputStream;
//    DataOutputStream mp3Out;
//    Socket clientSocket;
//    String input;
//    Dispatcher myDispatcher;

    /**
     * Port number for the client to connect to
     */
    private int portNumber;

    /**
     * Constructor
     *
     * @param num Port to communicate with
     */
    ServerCommunicationProtocol(int num) {
        this.portNumber = num;
    }

    /**
     * Connects the server with the client. Port number must be greater than 1023.
     */
    private void connect() {
        try {
            mySocket = new DatagramSocket(this.portNumber); // Initialize socket
            System.out.println("ServerSocket opened on port: " + this.portNumber);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Opens a client socket and listens for requests
     */
    private void listen() {
        System.out.println("Server listening.");

        try {
            while (true) {
                DatagramPacket requestPacket;
                Thread thread;

                requestPacket = new DatagramPacket(packetSize, packetSize.length);  // Initialize request packet
                mySocket.receive(requestPacket);    // Receive request packet
                System.out.println("Client packet received: " + requestPacket);
                System.out.println("Creating new thread for handling this client packet.");

                // Create new thread to handle this request packet and return a response packet
                thread = new ClientRequestPacketHandler(mySocket, requestPacket);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Used to run Server and Clients concurrently
     */
    @Override
    public void run() {
        connect();
        listen();
    }
}