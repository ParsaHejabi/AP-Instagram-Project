package Client;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by parsahejabi on 6/21/17.
 */
public class Server {

    static final int SERVER_PORT = 12400;
    static ServerSocket serverSocket;
    static Socket clientSocket;
    static ArrayList<Thread> clients;
    static ArrayList<Profile> profiles;
    static File profilesDir = new File("Profiles/");

    public static void main(String[] args) throws IOException {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            profiles = new ArrayList<>();
            clients = new ArrayList<>();
            initializeProfiles();
            while (true){
                clientSocket = serverSocket.accept();
                Thread clientsThread = new Thread(new ClientHandler(clientSocket));
                clients.add(clientsThread);
                clientsThread.start();
                System.out.println("someone connected!");
                removeClientThread();
                System.out.println(clients.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            serverSocket.close();
        }
    }

    public static void removeClientThread(){
        ArrayList<Thread> removable = new ArrayList<>();
        for (Thread th:clients){
            if (th.getThreadGroup() == null){
                removable.add(th);
            }
        }
        System.out.println("this threads are null: " + removable);
        clients.removeAll(removable);
    }

    public static void initializeProfiles() throws IOException, ClassNotFoundException {
        File[] serializedProfiles = profilesDir.listFiles();
        if (serializedProfiles!=null){
            for (File f:serializedProfiles){

                File ff = new File(f, f.getName());
                profiles.add(deserialize(ff));

            }
        }
    }
    public static Profile deserialize(File profile) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(profile);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Profile p = (Profile) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        return p;
    }
    public static void serialize(Profile profile) throws IOException {
        File f = new File(profilesDir,profile.username+"/" + profile.username);
        if(!f.createNewFile())
        {
            f.delete();
            f.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(f);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(profile);
        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.close();
    }

    static ArrayList<Profile> search(String token, Profile searchingClient)
    {
        ArrayList<Profile> ps = new ArrayList<>(5);
        Iterator<Profile> it = profiles.iterator();
        while (ps.size()<5 && it.hasNext())
        {
            Profile p = it.next();
            if (p.equals(searchingClient))
                continue;
            if (p.username.contains(token)|| p.fullName.contains(token))

                ps.add(p);
        }
        return ps;
    }

    static void createPost(Profile p, File f, boolean canComment, String caption){
        File ax = new File(profilesDir, p.username+"/"+Post.POST_NAME+Integer.toString(p.posts.size()));
        try {
            Files.write(ax.toPath(), Files.readAllBytes(f.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Post post = new Post(p, ax, caption, canComment);
        p.posts.add(post);
        try {
            serialize(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}