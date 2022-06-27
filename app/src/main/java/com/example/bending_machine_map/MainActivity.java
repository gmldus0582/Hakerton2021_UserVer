package com.example.bending_machine_map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bending_machine_map.Map.map;
import com.example.bending_machine_map.Ocr.OcrActivity;
import com.example.bending_machine_map.blue_ex.blueActivity;
import com.example.bending_machine_map.menu.menuActivity;


public class MainActivity extends OcrActivity {

    private View view;
    public SharedPreferences preferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = super.preferences;

        Intent intent = new Intent(MainActivity.this, OcrActivity.class);
        Intent intentMenu = new Intent(MainActivity.this, menuActivity.class);


        Button button2 = (Button) findViewById(R.id.firstLog) ;

        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(preferences.getString("userId","").length()>3){
                    startActivity(intentMenu);
                }else{startActivity(intent);}
            }
        });



    }
}
