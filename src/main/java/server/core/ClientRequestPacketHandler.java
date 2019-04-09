package server.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientRequestPacketHandler extends Thread {
    private DatagramSocket socket = null;
    private DatagramPacket packet = null;
    public Dispatcher dispatcher = null;

    ClientRequestPacketHandler(DatagramSocket s, DatagramPacket d) {
        this.socket = s;                                            // Set socket
        this.packet = d;                                            // Set packet as received request packet

        this.dispatcher = new Dispatcher();                       // Create a dispatcher object to process request
        dispatcher.registerDispatcher(SongDispatcher.class.getSimpleName(), new SongDispatcher());  // Add dispatcher modules
        dispatcher.registerDispatcher(SearchResultDispatcher.class.getSimpleName(), new SearchResultDispatcher());
        dispatcher.registerDispatcher(LoginDispatcher.class.getSimpleName(), new LoginDispatcher());
        dispatcher.registerDispatcher(PlaylistDispatcher.class.getSimpleName(), new PlaylistDispatcher());
        dispatcher.registerDispatcher(RegistrationDispatcher.class.getSimpleName(), new RegistrationDispatcher());

        System.out.println("New client packet handler created");
    }

    @Override
    public void run() {
        String request = new String(packet.getData(), 0, packet.getLength());               // Get packet's payload
        System.out.println("Server request string: " + request);
        String response = dispatcher.dispatch(request.trim());           // Send request to dispatcher
        System.out.println("Server preparing response packet");
        byte[] payload = response.getBytes();                       // Initialize payload with response bytes
        DatagramPacket responsePacket = new DatagramPacket(payload, payload.length, packet.getAddress(), packet.getPort());     // Prepare response packet
        try {
            socket.send(responsePacket);                            // Send response packet
            System.out.println("Server has sent response packet, thread terminating");
            this.interrupt();                                       // Kill thread after execution
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
