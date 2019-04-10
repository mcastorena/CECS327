package server.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * This class handles all of the packets requested by the Client
 */
public class ClientRequestPacketHandler extends Thread {

    /**
     * Socket for client connection
     */
    private DatagramSocket socket = null;

    /**
     * Datagram packet for requests
     */
    private DatagramPacket packet = null;

    /**
     * Dispatcher for handling the client request
     */
    public Dispatcher dispatcher = null;

    /**
     * Default constructor
     *
     * @param dgramSocket
     * @param dgramPacket
     */
    ClientRequestPacketHandler(DatagramSocket dgramSocket, DatagramPacket dgramPacket) {
        this.socket = dgramSocket;  // Set socket
        this.packet = dgramPacket;  // Set packet as received request packet

        this.dispatcher = new Dispatcher(); // Create a dispatcher object to process request
        dispatcher.registerDispatcher(SongDispatcher.class.getSimpleName(), new SongDispatcher());  // Add dispatcher modules
        dispatcher.registerDispatcher(SearchResultDispatcher.class.getSimpleName(), new SearchResultDispatcher());
        dispatcher.registerDispatcher(LoginDispatcher.class.getSimpleName(), new LoginDispatcher());
        dispatcher.registerDispatcher(PlaylistDispatcher.class.getSimpleName(), new PlaylistDispatcher());
        dispatcher.registerDispatcher(RegistrationDispatcher.class.getSimpleName(), new RegistrationDispatcher());

        System.out.println("New client packet handler created");
    }

    /**
     * Override of the Thread's run() method
     */
    @Override
    public void run() {
        String request;
        String response;
        byte[] payload;
        DatagramPacket responsePacket;

        // Get packet's payload
        request = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Server request string: " + request);

        // Send request to dispatcher
        response = dispatcher.dispatch(request.trim());
        System.out.println("Server preparing response packet");

        // Initialize payload with response bytes
        payload = response.getBytes();

        // Prepare the response packet
        responsePacket = new DatagramPacket(payload, payload.length, packet.getAddress(), packet.getPort());
        try {
            socket.send(responsePacket);                            // Send response packet
            System.out.println("Server has sent response packet, thread terminating");
            this.interrupt();                                       // Kill thread after execution
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
