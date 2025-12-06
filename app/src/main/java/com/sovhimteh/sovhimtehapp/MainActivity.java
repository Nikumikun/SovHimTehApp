package com.sovhimteh.sovhimtehapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;



public class MainActivity extends AppCompatActivity {

    private EditText plotnost, temp;
    private TextView resultTextView;
    InputStream inputStream;
    String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        plotnost = findViewById(R.id.plotnost);
        temp = findViewById(R.id.temperature);
        resultTextView = findViewById(R.id.textView2);
        Button button = findViewById(R.id.button);
        inputStream = getResources().openRawResource(R.raw.data);

        int[] pl_array = new int[50];
        for (int i = 0; i < pl_array.length; i++) {
            pl_array[i] = 500 + i * 10;
        }

        double[] tmp_array = new double[301];
        for (int i = 0; i < tmp_array.length; i++) {
            tmp_array[i] = -25 + i * 0.5;
        }
        String[][] pl_result = new String[tmp_array.length][pl_array.length];

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            for (int i = 0; ((csvLine = reader.readLine()) != null); i++) {
                data = csvLine.split(",");
                System.arraycopy(data, 0, pl_result[i], 0, pl_array.length);
            }
            for (int i = 0; i < tmp_array.length; i++) {
                StringBuilder text = new StringBuilder(" ");
                for (int j = 0; j < pl_array.length; j++) {
                    text.append(pl_result[i][j]).append(" ");
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file:"+ex);
        }

        button.setOnClickListener(view -> {
            int num1 = Integer.parseInt(plotnost.getText().toString());
            double num2 = Double.parseDouble(temp.getText().toString());

            int indexplotnosti;
            int indextemp;
            int roundNum1;
            double roundNum2;

            if (num1 % 10 == 0) {
                indexplotnosti = Arrays.binarySearch(pl_array,num1);
            }else {
                roundNum1 = (num1 / 10) * 10;
                indexplotnosti = Arrays.binarySearch(pl_array,roundNum1);
            }

            if (num2 % 10 == 5) {
                indextemp = Arrays.binarySearch(tmp_array,num2);
            }else {
                roundNum2 = Math.round(num2);
                indextemp = Arrays.binarySearch(tmp_array,roundNum2);
            }

            try {
                double pl_table = Double.parseDouble(pl_result[indextemp][indexplotnosti]);
                double round_pl_table = (Math.floor(pl_table*1e3) / 1e3);
                double plotnost = round_pl_table + ((num1 % 10) * 0.001);
                double result = Math.floor(plotnost*1e3) / 1e3;
                resultTextView.setText(String.valueOf(result));
            } catch (RuntimeException e) {
                resultTextView.setText("-");
            }



        });
    }
}