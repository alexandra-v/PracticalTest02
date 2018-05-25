package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by alex on 25.05.2018.
 */

public class ClientThread extends Thread {
    String address;
    int port;
    int nrLit;
    String cuvant;
    TextView showAnagram;
    Socket socket;

    public ClientThread(int port, int nrLit, String cuvant, TextView showAnagram) {
        this.address = "127.0.0.1";
        this.port = port;
        this.nrLit = nrLit;
        this.cuvant = cuvant;
        this.showAnagram = showAnagram;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.i("ServerERR", "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            if (bufferedReader == null || printWriter == null) {
                Log.i("ServerERR", "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            // SCRIU INFO IN BUFFER
            printWriter.println(nrLit);
            printWriter.flush();
            printWriter.println(cuvant);
            printWriter.flush();


            String result;
            while ((result = bufferedReader.readLine()) != null) {
                final String finalizedWeateherInformation = result;
                showAnagram.post(new Runnable() {
                    @Override
                    public void run() {
                        showAnagram.setText(finalizedWeateherInformation);
                    }
                });
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
