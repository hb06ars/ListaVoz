package com.example.listvoz;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    public static ArrayList<String> ITEMS =new ArrayList<String>();
    ListView listaItens;
    ArrayAdapter<String> adapter;
    public static final String FILE_NAME = "listaVoz.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSpeak = findViewById(R.id.btnSpeak);

        ArrayList<String> listaCarregada = FileHelper.readDataAsArrayList(this, FILE_NAME);
        if(!listaCarregada.isEmpty())
            ITEMS = listaCarregada;


        listaItens = findViewById(R.id.listView);
        adapter = new CustomAdapter(this, ITEMS);
        listaItens.setAdapter(adapter);

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();
            }
        });

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                atualizar();
            }
        });

    }

    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale algo...");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ITEMS.add(result.get(0));
            FileHelper.saveData(this, FILE_NAME, ITEMS);
            listaItens.setAdapter(adapter);

        }
    }

    public void atualizar() {
        FileHelper.saveData(this, FILE_NAME, ITEMS);
    }

}
