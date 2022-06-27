package com.example.bending_machine_map.complete;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bending_machine_map.MainActivity;
import com.example.bending_machine_map.Ocr.OcrActivity;
import com.example.bending_machine_map.R;
import com.example.bending_machine_map.blue_ex.blueActivity;
import com.example.bending_machine_map.menu.menuActivity;

import org.w3c.dom.Text;

public class completeActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        Intent intent = new Intent(completeActivity.this, menuActivity.class);
        Button button = (Button) findViewById(R.id.btnCheck);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }
}
