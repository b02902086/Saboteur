import java.util.*;
import java.lang.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] arg) throws Exception{
        Socket socket = new Socket("127.0.0.1", 4000);
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();

        try {
            Data message = (Data)in.readObject();
            System.out.println(message.id);
            message.id *= 10;
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
