package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by parsahejabi on 6/21/17.
 */
public class Client {
    static Socket clientSocket;
    static Profile profileOwner;

    static ObjectInputStream clientInputStream;
    static ObjectOutputStream clientOutputStream;

    public Client() throws IOException, ClassNotFoundException {
        connect(Server.SERVER_PORT);
    }

    static void connect(int port) {
        try {
            clientSocket = new Socket("localhost", port);

            System.out.println("connected to the " + clientSocket.getInetAddress().getHostAddress());
            System.out.println("setting io streams...");
            clientInputStream = new ObjectInputStream(clientSocket.getInputStream());
            clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            System.out.println("has been set.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void sendMessage(String message) {
        try {
            clientOutputStream.writeUTF(message);
            clientOutputStream.flush();
        } catch (Exception e) {
            System.out.println("FUCKED");
        }
    }

    static void disconnect() throws IOException {
        clientSocket.close();
        clientInputStream.close();
        clientOutputStream.close();
    }

    static void refreshOwner() throws IOException, ClassNotFoundException
    {
        clientOutputStream.reset();
        Client.profileOwner = (Profile) clientInputStream.readObject();
    }
}