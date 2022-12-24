import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static int leftWins = 0, rightWins = 0;
    private static int ballX = 450, ballY = 250;
    private static double ballVelX = (Math.random() * 2) - 1;
    private static double ballVelY = (Math.random() * 2) - 1;
    private static IntHolder lRectY = new IntHolder(0);
    private static IntHolder rRectY = new IntHolder(0);
    private static String score = leftWins + " - " + rightWins;


    public static void main(String[] args) {
        if (ballVelX == 0) {
            ballVelX = 0.1;
        }
        if (ballVelY == 0) {
            ballVelY = 0.1;
        }
        try {
            System.out.println("Waiting for client connections");
            ServerSocket serverSock = new ServerSocket(5555);
            Socket connectionOne = serverSock.accept();
            Socket connectionTwo = serverSock.accept();
            DataOutputStream clientOutput1 = new DataOutputStream(connectionOne.getOutputStream());
            DataOutputStream clientOutput2 = new DataOutputStream(connectionTwo.getOutputStream());

            ClientReciever left = new ClientReciever(connectionOne, lRectY);
            ClientReciever right = new ClientReciever(connectionTwo, rRectY);
            Thread lRecieve = new Thread(left);
            Thread rRecieve = new Thread(right);

            clientOutput1.writeBytes("start\n");
            clientOutput2.writeBytes("start\n");

            BufferedReader clientInput1 = new BufferedReader(new InputStreamReader(connectionOne.getInputStream()));
            BufferedReader clientInput2 = new BufferedReader(new InputStreamReader(connectionTwo.getInputStream()));

            clientInput1.readLine();
            lRecieve.start();
            clientInput2.readLine();
            rRecieve.start();

            boolean newGame = false;
            while (true) {
                if (ballX == 0) {
                    if (ballY < lRectY.getValue() || ballY > lRectY.getValue() + 150) {
                        rightWins++;
                        resetValues();
                        newGame = true;
                    } else {
                        ballVelX = -ballVelX;
                    }
                } else if (ballX == 876) {
                    if (ballY < rRectY.getValue() || ballY > rRectY.getValue() + 150) {
                        leftWins++;
                        resetValues();
                        newGame = true;
                    } else {
                        ballVelX = -ballVelX;
                    }
                }

                if (ballY == 0 || ballY == 500) {
                    ballVelY = -ballVelY;
                }

                if (newGame) {
                    newGame = false;
                } else {
                    if (ballVelX >= 1) {
                        ballX++;
                        ballVelX = 0.1;
                    } else if (ballVelX <= -1) {
                        ballX--;
                        ballVelX = -0.1;
                    } else if (ballVelX > 0) {
                        ballVelX += 0.1;
                    } else if (ballVelX < 0) {
                        ballVelX -= 0.1;
                    }

                    if (ballVelY >= 1) {
                        ballY++;
                        ballVelY = 0.1;
                    } else if (ballVelY <= -1) {
                        ballY--;
                        ballVelY = -0.1;
                    } else if (ballVelY > 0) {
                        ballVelY += 0.1;
                    } else if (ballVelY < 0) {
                        ballVelY -= 0.1;
                    }
                }
                if (connectionOne == null || connectionTwo == null) {
                    break;
                }

                if (lRectY.getValue() < 0) {
                    lRectY.setValue(0);
                } else if (lRectY.getValue() > 350) {
                    lRectY.setValue(350);
                }

                if (rRectY.getValue() < 0) {
                    rRectY.setValue(0);
                } else if (rRectY.getValue() > 350) {
                    rRectY.setValue(350);
                }

                clientOutput1.writeBytes(ballX + "\n");
                clientOutput2.writeBytes(ballX + "\n");
                clientOutput1.writeBytes(ballY + "\n");
                clientOutput2.writeBytes(ballY + "\n");
                clientOutput1.writeBytes(lRectY.getValue() + "\n");
                clientOutput2.writeBytes(lRectY.getValue() + "\n");
                clientOutput1.writeBytes(rRectY.getValue() + "\n");
                clientOutput2.writeBytes(rRectY.getValue() + "\n");
                clientOutput1.writeBytes(score + "\n");
                clientOutput2.writeBytes(score + "\n");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void resetValues() {
        ballX = 450;
        ballY = 250;
        ballVelX = (Math.random() * 2) - 1;
        ballVelY = (Math.random() * 2) - 1;
        IntHolder lRectY = new IntHolder(0);
        IntHolder rRectY = new IntHolder(0);
        score = leftWins + " - " + rightWins;
        if (ballVelX == 0) {
            ballVelX = 0.1;
        }
        if (ballVelY == 0) {
            ballVelY = 0.1;
        }
    }
}