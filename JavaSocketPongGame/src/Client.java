import javax.swing.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        JFrame window = new JFrame("Pong");
        window.setSize(900, 500);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

		Scene game = new Scene();
        window.add(game);
        try {
            String hostname = "localhost";
            Socket connectionSock = new Socket(hostname, 5555);
			DataOutputStream out = new DataOutputStream(connectionSock.getOutputStream());
			BufferedReader clientInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));

            clientInput.readLine();

            ServerReciever sceneUpdater = new ServerReciever(game, connectionSock);
            Thread recieving = new Thread(sceneUpdater);
            recieving.start();

            out.writeBytes("start\n");
			float mouseY = 0;

            while (true) {
                Thread.sleep(5);
                mouseY += game.getYOffset();
                mouseY = Math.max(0, Math.min(500, mouseY));
                out.writeBytes((int)mouseY + "\n");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
