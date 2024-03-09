package it.project.server.model;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class Server {
    static final int port = 3737;
    private ServerSocket serverSocket = null;
    ObjectInputStream inStream = null;
    ObjectOutputStream outStream = null;

    synchronized public void startServerThread() throws IOException{
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                while(true){
                    Socket socket = serverSocket.accept();
                    // ** costruttore: Server svr1 = new Server(socket,)

                    inStream = new ObjectInputStream(socket.getInputStream());

                    outStream = new ObjectOutputStream(socket.getOutputStream());

                    System.out.println("qui ci sono");

                    try {
                        // Receive the request from the client
                        Object request = inStream.readObject();

                    } catch (IOException | ClassNotFoundException e) {
                        break;
                    }

                    try {
                        inStream.close();
                        outStream.close();
                        socket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally{
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }









}
