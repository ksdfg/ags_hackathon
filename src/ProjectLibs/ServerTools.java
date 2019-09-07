package ProjectLibs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTools implements AutoCloseable{
    //initialize socket and input stream
    private Socket socket;
    private ServerSocket server;
    public DataInputStream in;
    public DataOutputStream out;

    // constructor with port
    public ServerTools(int port) throws IOException {
        // starts server and waits for a connection
        server = new ServerSocket(port);
        System.out.println("Server started");
    }

    public void accept() throws IOException{
        System.out.println("Waiting for a client ...");

        socket = server.accept();
        System.out.println("Client accepted");

        // takes input from the client socket
        in = new DataInputStream(socket.getInputStream());

        // sends output to the socket
        out = new DataOutputStream(socket.getOutputStream());
    }

    public void closeSocket() throws Exception {
        System.out.println("Closing Socket");
        // close connection
        out.close();
        in.close();
        socket.close();
    }

    @Override
    public void close() throws Exception {
        server.close();
    }
}
