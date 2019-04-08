package client.rpc;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

class ClientCommunicationProtocol {

    private static final Logger LOGGER = Logger.getLogger(ClientCommunicationProtocol.class);
    private static final int FRAGMENT_SIZE = 62000;                      // Packet size
    private static final int TIMEOUT_DURATION = 20000;
    private static final int RETRY_ATTEMPTS = 15;
    private byte[] packetSize = new byte[FRAGMENT_SIZE];

    private DatagramSocket socket;
    private InetAddress ip;
    private int portNumber;

    void connect(int portNum) {
        try {
            this.ip = InetAddress.getByName("localhost");   // Get localhost IP address
            this.socket = new DatagramSocket();             // Initialize Socket
            socket.setSoTimeout(TIMEOUT_DURATION);
            this.portNumber = portNum;                      // Initialize port number

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String sendRequest(String request, String callSemantic) {
        byte[] requestPayload = new byte[FRAGMENT_SIZE];    // Initialize payload
        requestPayload = request.getBytes();                // Fill payload
        DatagramPacket requestPacket = new DatagramPacket(requestPayload, requestPayload.length, this.ip, this.portNumber);    // Initialize request packet
        System.out.println("Client sending request packet.");

        String response = "";
        switch (callSemantic) {
            case "MAYBE":
                response = sendAndReceiveRequest(requestPacket, 0, 0);
                break;
            case "AT_LEAST_ONCE":
            case "AT_MOST_ONCE":
                response = sendAndReceiveRequest(requestPacket, TIMEOUT_DURATION, RETRY_ATTEMPTS);
                break;
        }

        return response;    // Return request response
    }

    public void sendAsyncRequest(String request) {
        String response = "";
        try {

            byte[] requestPayload = new byte[FRAGMENT_SIZE];    // Initialize payload
            requestPayload = request.getBytes();                // Fill payload
            DatagramPacket requestPacket = new DatagramPacket(requestPayload, requestPayload.length, this.ip, this.portNumber); // Initialize request packet
            System.out.println("Client sending request packet.");
            socket.send(requestPacket);                         // Send request packet
            System.out.print("Client request packet sent.");

            new Thread() {
                public void run() {
                    byte[] responseData = new byte[FRAGMENT_SIZE];      // Prepare response packet
                    DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length);  // Initialize reponse packet
                    System.out.println("Client attempting to receive response packet.");
                    try {
                        socket.receive(responsePacket);                                                     // Retrieve reponse packet
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Client has received response packet from server.");
                    String response = new String(responsePacket.getData());                                 // Get reponse packet's payload
                    System.out.println(response);
                }
            }.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String sendAndReceiveRequest(DatagramPacket requestPacket, int timeout, int numRetries) {
        for (; numRetries >= 0; numRetries--) {
            try {
                if (timeout > 0) {
                    socket.setSoTimeout(timeout);
                }
                socket.send(requestPacket);

                DatagramPacket responsePacket = new DatagramPacket(new byte[FRAGMENT_SIZE], FRAGMENT_SIZE);
                System.out.println("Client attempting to receive response packet.");
                socket.receive(responsePacket);                                             // Retrieve reponse packet
                System.out.println("Client has received response packet from server.");
                return new String(responsePacket.getData());                                // Get response packet's payload

            } catch (SocketTimeoutException e) {
                LOGGER.debug("SocketTimeoutException: ClientCommunicationProtocol.sendAndReceiveRequest: " + e);
            } catch (IOException e) {
                LOGGER.error("IOException: ClientCommunicationProtocol.sendAndReceiveRequest: " + e);
            }
        }

        return null;
    }

}

