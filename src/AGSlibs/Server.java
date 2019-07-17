package AGSlibs;

import java.io.*;
import java.net.*;

public class Server implements AutoCloseable{
    //initialize socket and input stream
    private Socket socket   = null;
    private ServerSocket server   = null;
    public DataInputStream in       =  null;

    // constructor with port
    public Server(int port) throws IOException{
        // starts server and waits for a connection
        server = new ServerSocket(port);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");

            socket = server.accept();
            System.out.println("Client accepted");

            // takes input from the client socket
            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));
    }

    @Override
    public void close() throws Exception {
        // close connection
        socket.close();
        in.close();
    }
}
