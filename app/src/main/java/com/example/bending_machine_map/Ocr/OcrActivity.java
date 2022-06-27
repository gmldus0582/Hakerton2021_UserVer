package com.example.bending_machine_map.Ocr;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bending_machine_map.MainActivity;
import com.example.bending_machine_map.Map.map;
import com.example.bending_machine_map.R;
import com.example.bending_machine_map.blue_ex.blueActivity;
import com.example.bending_machine_map.complete.completeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class OcrActivity extends AppCompatActivity {

    final private static String TAG = "태그명";
    final static int TAKE_PICTURE = 1;
    String mCurrentPhotoPath;
    final static int REQUEST_TAKE_PHOTO = 1;

    static final int REQUEST_CODE = 2;
    File file;
    ImageView imageView;    // 갤러리에서 가져온 이미지를 보여줄 뷰
    Uri uri;                // 갤러리에서 가져온 이미지에 대한 Uri
    static Bitmap bitmap;          // 갤러리에서 가져온 이미지를 담을 비트맵
    InputImage image;       // ML 모델이 인식할 인풋 이미지
    TextView text_info;     // ML 모델이 인식한 텍스트를 보여줄 뷰
    Button btn_get_image, btn_detection_image;  // 이미지 가져오기 버튼, 이미지 인식 버튼
    TextRecognizer recognizer;    //텍스트 인식에 사용될 모델
    JSONArray Array;
    static ArrayList<String> list = new ArrayList<String>();
    JSONObject jsonObject;
    private View view;
    public SharedPreferences preferences;
    String resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        imageView = findViewById(R.id.imageView);
        text_info = findViewById(R.id.text_info);
        recognizer = TextRecognition.getClient();    //텍스트 인식에 사용될 모델

        btn_get_image = findViewById(R.id.btn_get_image);
        preferences = getSharedPreferences("userId", MODE_PRIVATE);

        try {
            String json = "";

            InputStream is = getAssets().open("Information.json"); // json파일 이름
            int fileSize = is.available();
            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();
            //json파일명을 가져와서 String 변수에 담음
            json = new String(buffer, "UTF-8");
            jsonObject = new JSONObject(json);
            //배열로된 자료를 가져올때
            Array = jsonObject.getJSONArray("Information");//배열의 이름
            for (int i = 0; i < Array.length(); i++) {
                JSONObject Object = Array.getJSONObject(i);
                list.add(Object.getString("cardNum"));
                Log.d("--  # ", Object.getString("#"));
                Log.d("--  Card Number is", Object.getString("cardNum"));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            } else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(OcrActivity.this, new String[]{
                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        btn_get_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_get_image:
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });

        // IMAGE DETECTION 버튼
        btn_detection_image = findViewById(R.id.btn_detection_image);
        Intent intent2 = new Intent(OcrActivity.this, map.class);

        btn_detection_image.setOnClickListener(new Button.OnClickListener() {
            int begin;
            int end;
            @Override
            public void onClick(View v) {
                if(bitmap==null){
                    Toast toast=Toast.makeText(OcrActivity.this,"인증이 필요합니다.",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    TextRecognition(recognizer);
                }
            }
        });

    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    { super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]); } } // 카메라로 촬영한 사진의 썸네일을 가져와 이미지뷰에 띄워줌
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case TAKE_PICTURE: if (resultCode == RESULT_OK && intent.hasExtra("data")) {
                bitmap = (Bitmap) intent.getExtras().get("data");
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    image = InputImage.fromBitmap(bitmap, 0);}
            }
                break; } }


    private void TextRecognition(TextRecognizer recognizer){
        Intent intent3 = new Intent(OcrActivity.this, completeActivity.class);
        Task<Text> result = recognizer.process(image)
                // 이미지 인식에 성공하면 실행되는 리스너
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        Log.e("텍스트 인식", "성공");
                        // Task completed successfully
                        resultText = visionText.getText();
                        text_info.setText(resultText);// 인식한 텍스트를 TextView에 세팅
                        resultText = resultText.substring(0,9);
                        if(resultText!=null){

                            SharedPreferences.Editor editor = preferences.edit();
                            //putString(KEY,VALUE)
                            editor.putString("userId",resultText);
                            //항상 commit & apply 를 해주어야 저장이 된다.
                            editor.commit();
                            //메소드 호출
                            int i = 0;
                            while (i <list.size()){
                                if(list.get(i).equals(resultText)){
                                    startActivity(intent3);
                                    break;
                                }
                                i++;
                            }
                            if(i==list.size()){
                                Toast toast=Toast.makeText(OcrActivity.this,"인증 실패",Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }
                })
                // 이미지 인식에 실패하면 실행되는 리스너
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("텍스트 인식", "실패: " + e.getMessage());
                            }
                        });
    }

}