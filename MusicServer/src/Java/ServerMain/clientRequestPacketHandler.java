package ServerMain;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class clientRequestPacketHandler extends Thread {
    private DatagramSocket socket = null;
    private DatagramPacket packet = null;
    public Dispatcher myDispatcher = null;

    public clientRequestPacketHandler(DatagramSocket s, DatagramPacket d){
        this.socket = s;                                            // Set socket
        this.packet = d;                                            // Set packet as received request packet

        this.myDispatcher = new Dispatcher();                       // Create a dispatcher object to process request
        myDispatcher.registerObject(new SongDispatcher(), "SongServices");  // Add dispatcher modules
        myDispatcher.registerObject(new SearchResultDispatcher(), "SearchResultServices");
        System.out.println("New client packet handler created");
    }

    @Override
    public void run(){
        String request = new String(packet.getData(), 0, packet.getLength());               // Get packet's payload
        System.out.println("Server request string: "+ request);
        String response = myDispatcher.dispatch(request.trim());           // Send request to dispatcher
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
