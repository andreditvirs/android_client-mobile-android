package com.uwika.androidclient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText txtInput;
    EditText txtHasil;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtInput = findViewById(R.id.txtCode);
        txtHasil = findViewById(R.id.txtHasil);
    }

    public void getData(View v)
    {
        new ambilData(this).execute();
        Toast.makeText(this, "Data diload!", Toast.LENGTH_SHORT).show();
    }

    private class ambilData extends AsyncTask<String, Void, Void>
    {
        private final Context context;

        private ambilData(Context context){
            this.context = context;
        }

        protected void onPreExecute()
        {
            progressDialog = new ProgressDialog(this.context);
            progressDialog.setMessage("Loading Data!");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                String code = txtInput.getText().toString();
                URL url = new URL("https://yuliushari.com/mobile2022/index.php?id="+code);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setRequestProperty("USER-AGENT", "Mozila/6.0");

                StringBuilder data = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder response = new StringBuilder();

                while((line = br.readLine()) != null){
                    response.append(line);
                }

                br.close();
                data.append(response.toString());

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtHasil.setText(data.toString());
                        progressDialog.dismiss();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}