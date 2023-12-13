package client;

import java.io.*;
import java.net.Socket;

public class GameClient {
    private Socket socket;
    private BufferedWriter writer;

    public GameClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void startClient() {
        // Handle incoming messages from the server in a separate thread.
    }

    public void sendMessage(String message) throws IOException {
        writer.write(message);
        writer.newLine();
        writer.flush();
    }

    public void disconnect() throws IOException {
        socket.close();
    }
}