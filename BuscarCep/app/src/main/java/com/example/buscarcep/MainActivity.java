package com.example.buscarcep;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText cepInserido;
    private Button buscarCep;
    private TextView resultado;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cepInserido = findViewById(R.id.etMain_cep);
        buscarCep = findViewById(R.id.btnMain_buscarCep);
        resultado = findViewById(R.id.etMain_resposta);

        buscarCep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyRunnable runable = new MyRunnable();
                new Thread(runable).start();
            }
        });

    }


    public class MyRunnable implements Runnable{

        @Override
        public void run() {
            StringBuilder resposta = new StringBuilder(cepInserido.getText().toString());
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = null;


           if (resposta != null && resposta.length() == 8) {
                try {
                    URL url = new URL("https://viacep.com.br/ws/" + resposta + "/json/" );

                    //HttpConnection abre uma conexao com o servidor
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //InputStream recupera os dados em formato de Bytes
                    inputStream = connection.getInputStream();
                    //InputStreamReader lê os dados em Bytes e decodifica para caracteres
                    inputStreamReader = new InputStreamReader(inputStream);
                    //BufferedReader lê os dados do InputStreamReader e visualiza
                    BufferedReader reader = new BufferedReader(inputStreamReader);

                    //Inicia linha vazia e enquanto for encontrando bufferedReader,
                    // vai adicionando num conjunto de strings chamado StringBuffer
                    buffer = new StringBuffer();
                    String line = "";
                    while ((line = reader.readLine())!= null){
                        buffer.append(line);
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


           }
            String value = buffer.toString();

            handler.post(new Runnable() {
                @Override
                public void run() {

                        resultado.setText(value);
                    cepInserido.setText("");
                }
            });


        }


        }
    }
