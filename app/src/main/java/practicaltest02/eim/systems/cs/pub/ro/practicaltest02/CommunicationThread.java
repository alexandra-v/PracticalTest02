package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.content.Context;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by alex on 25.05.2018.
 */

public class CommunicationThread extends Thread{
    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.socket = socket;
        this.serverThread = serverThread;

    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client url");
            String nrLit = bufferedReader.readLine();
            String cuvant = bufferedReader.readLine();
            if (nrLit == null || nrLit.isEmpty() || cuvant == null || cuvant.isEmpty() ) {
                Log.e(Constants.TAG, nrLit + " "+ cuvant);
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type!");
                return;
            }

            Log.e(Constants.TAG, nrLit + " " + cuvant);
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(Constants.URL
                    + "?" + Constants.PARAM1 + "=" + cuvant
                    + "&" + Constants.PARAM2+ "=" + nrLit);
            Log.e(Constants.TAG, Constants.URL
                    + "?" + Constants.PARAM1 + "=" + cuvant
                    + "&" + Constants.PARAM2+ "=" + nrLit);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String pageSourceCode = httpClient.execute(httpGet, responseHandler);
            if (pageSourceCode == null) {

                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error getting the information from the webservice!");
                return;
            }
            Log.e(Constants.TAG, pageSourceCode);
            Document document = Jsoup.parse(pageSourceCode);
            Element element = document.child(0);
            Elements elements = element.getElementsByTag("string");
            String result = "";
            for (Element str : elements) {
                result =result + " " + str.text();
            }
            printWriter.println(result);
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(Constants.TAG, e.getMessage());
        }finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    ioException.printStackTrace();
                }
            }
        }
    }
}
