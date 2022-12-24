import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReciever implements Runnable {
    private Socket connectionSock = null;
    private IntHolder paddleY;

    public ClientReciever(Socket connectionSock, IntHolder paddleY) {
        this.connectionSock = connectionSock;
        this.paddleY = paddleY;
    }

    public void run() {
        try {
            BufferedReader clientInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));

            while (true) {
                String clientText = clientInput.readLine();
                if (clientText != null) {
                    paddleY.setValue(Integer.parseInt(clientText) - 75);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}