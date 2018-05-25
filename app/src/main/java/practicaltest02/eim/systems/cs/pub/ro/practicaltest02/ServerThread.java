package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by alex on 25.05.2018.
 */

public class ServerThread extends Thread{
    public int port;
    public ServerSocket serverSocket = null;

    public ServerThread(int port){
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(port);
        }
        catch (IOException ioException){
            Log.e(Constants.TAG, ioException.getMessage());
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Log.e("ServerERR", "[SERVER THREAD] Waiting for a client invocation...");
            try {
                Socket socket = serverSocket.accept();
                Log.e("ServerERR", "[SERVER THREAD] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());
                CommunicationThread communicationThread = new CommunicationThread(this, socket);
                communicationThread.start();
            } catch (IOException e) {
                Log.e("ServerERR", "[SERVER THREAD] An exception has occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
