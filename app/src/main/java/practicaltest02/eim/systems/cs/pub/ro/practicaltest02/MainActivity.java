package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText portServer, nrLit = null, cuvant = null;
    TextView showAnagram;
    Button goBtn, connectBtn;
    ServerThread serverThread;
    ClientThread clientThread;

    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = portServer.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            Log.e(Constants.TAG, serverPort);
            if (serverThread.serverSocket == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }
    }

    private class GetAnagram implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String portS = portServer.getText().toString();
            if (portS == null || portS.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String nrL = nrLit.getText().toString();
            String cuv = cuvant.getText().toString();
            if (nrL == null || nrL.isEmpty() || cuv == null || cuv.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            showAnagram.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(
                    Integer.parseInt(portS), Integer.parseInt(nrL), cuv,  showAnagram
            );
            clientThread.start();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        portServer = findViewById(R.id.portClientId);
        nrLit = findViewById(R.id.nrLitId);
        cuvant = findViewById(R.id.cuvantId);
        showAnagram = findViewById(R.id.showAnagramId);
        goBtn = findViewById(R.id.goId);
        connectBtn = findViewById(R.id.connectId);

        connectBtn.setOnClickListener(new ConnectButtonClickListener());
        goBtn.setOnClickListener(new GetAnagram());
    }
}
