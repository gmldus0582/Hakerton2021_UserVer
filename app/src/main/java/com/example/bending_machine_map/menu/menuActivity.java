package com.example.bending_machine_map.menu;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bending_machine_map.Map.map;
import com.example.bending_machine_map.R;
import com.example.bending_machine_map.blue_ex.blueActivity;
import com.example.bending_machine_map.complete.completeActivity;

public class menuActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = new Intent(menuActivity.this, blueActivity.class);
        Intent intent2 = new Intent(menuActivity.this, map.class);
        Button buttonPad = (Button) findViewById(R.id.btn_get_pad);
        Button buttonMap = (Button) findViewById(R.id.btn_map);


        buttonPad.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }

        });

        buttonMap.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent2);
            }
        });
    }
}
