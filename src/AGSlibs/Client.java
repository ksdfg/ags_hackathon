package AGSlibs;

import java.io.*;
import java.net.*;

public class Client implements AutoCloseable{

    // initialize socket and input output streams
    private Socket socket;
    public DataOutputStream out;

    // constructor to put ip address and port
    public Client(String address, int port) throws IOException {
        // establish a connection
        socket = new Socket(address, port);
        System.out.println("Connected");

        // sends output to the socket
        out = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void close() throws Exception {
        // close the connection
        out.close();
        socket.close();
    }
}
