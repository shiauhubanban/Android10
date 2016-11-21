package com.org.iii.shine10;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private TextView tv;
    private File sdroot,approot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.tv);
        sp = getSharedPreferences("gamedata",MODE_PRIVATE);
        editor = sp.edit();

        sdroot = Environment.getExternalStorageDirectory();
        //String state = Environment.getExternalStorageState();
        Log.v("shine",sdroot.getAbsolutePath());

        // 以下是檢查危險權限
        if(ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},123);
        }else {
            init();
        }

    }

    private void init(){
        approot=new File(sdroot,"Android/data/"+ getPackageName());
        if(!approot.exists())approot.mkdirs();
    }
    //callback
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResalt : grantResults) {
            if (grantResalt == PackageManager.PERMISSION_GRANTED) {
            Log.v("shine", "OK");
                init();
        }
      }
    }

    // 偏好設定 => save
   public void test1(View v){
       editor.putInt("stage",3);
       editor.putString("user","shine");
       editor.commit();
       Toast.makeText(this,"Save OK",Toast.LENGTH_SHORT);
   }

    // 偏好設定 => read
    public void test2(View v){
        int stage = sp.getInt("stage",0);
        String name = sp.getString("user","nobody");
        tv.setText("Stage: " + stage + "\n" +
                "User: " + name);
    }
    //內存 write
    public void test3(View v){
        // 在內存空間之下的 FileSystem
        try {
            FileOutputStream fout = openFileOutput("Shine.data",MODE_PRIVATE);
            fout.write("Hello Shine".getBytes());
            fout.flush();
            fout.close();
            Toast.makeText(this,"save ok",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("shine","test03:"+ e.toString());
        }
    }
    //內存 read
    public void test4(View v){
        tv.setText("");
        try {
            BufferedReader reader =
                    new BufferedReader(
                    new InputStreamReader(openFileInput("Shine.data")));
            String line;
            while ((line = reader.readLine())!=null) {
                tv.append(line + "\n");
            }
            reader.close();
        } catch (IOException e) {
            Log.v("shine","test04:"+ e.toString());
        }
    }
    //寫入
    public void test5(View v){
        try {
            FileOutputStream fout =
                    new FileOutputStream(new File(sdroot,"file2.txt"));
            fout.write("Hello1".getBytes());
            fout.flush();
            fout.close();
            Toast.makeText(this,"Save1 OK",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.v("shine","test5:"+e.toString());
        }

    }
    //寫入
    public void test6(View v){
        try {
            FileOutputStream fout =new FileOutputStream(new File(sdroot,"file2.txt"));
            fout.write("Hello2".getBytes());
            fout.flush();
            fout.close();
            Toast.makeText(this,"Save2 OK",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.v("shine","test6:"+e.toString());
        }

    }
}
