public class server {

    private static final int portNumber = 2222;

    public static void main(String [] args)
    {
        serverCommunicationProtocol myServer = new serverCommunicationProtocol(portNumber);
        myServer.start();
    }
}
