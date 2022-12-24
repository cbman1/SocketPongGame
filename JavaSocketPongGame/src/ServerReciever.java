import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerReciever implements Runnable {
    Socket connectionSock = null;
    Scene game = null;
    boolean noInput = false;

    public ServerReciever(Scene game, Socket connectionSock) {
        this.game = game;
        this.connectionSock = connectionSock;
    }

    public void run() {
        try {
            BufferedReader clientInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
            int ballX = 450, ballY = 250;
            int lRect = 0, rRect = 0;
            String score = "0 - 0";
            while (true) {
                String clientText = clientInput.readLine();
                if (checkInput(clientText)) {
                    ballX = Integer.parseInt(clientText);
                }

                clientText = clientInput.readLine();
                if (checkInput(clientText)) {
                    ballY = Integer.parseInt(clientText);
                }

                clientText = clientInput.readLine();
                if (checkInput(clientText)) {
                    lRect = Integer.parseInt(clientText);
                }

                clientText = clientInput.readLine();
                if (checkInput(clientText)) {
                    rRect = Integer.parseInt(clientText);
                }

                clientText = clientInput.readLine();
                if (checkInput(clientText)) {
                    score = clientText;
                }

                if (noInput) {
                    System.out.println("Closing connection for socket " + connectionSock);
                    connectionSock.close();
                    break;
                }
                game.changeBallPosition(ballX, ballY);
                game.moveLeftPaddle(lRect);
                game.moveRightPaddle(rRect);
                game.modifyScore(score);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean checkInput(String clientText) {
        if (clientText != null) {
            return true;
        } else {
            noInput = true;
            return false;
        }
    }
}	
