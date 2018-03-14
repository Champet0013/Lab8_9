package com.example.ist_mac_01.lab8_9;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String outputStr = "";
    Button runBtn;
    TextView tv1;
    ProgressBar pb;
    ImageView imv;
    ByteArrayOutputStream byteBuffer=new ByteArrayOutputStream();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imv = findViewById(R.id.imageView);
        runBtn = findViewById(R.id.button);
        tv1 = findViewById(R.id.textview);
        pb = findViewById(R.id.progressBar);
        runBtn.setOnClickListener(new View.OnClickListener(){
           public void onClick(View view){
               outputStr +="\n";
//               example1();
//               example2();
//               example3();
//               example4();

               example5();


           }
        });
    }
    private void example1() {
        for(int i =0;i<5;i++)
            outputStr += "A";
        for(int i = 0 ;i<5;i++)
            outputStr +="B";
        tv1.setText(outputStr);
    }
    private void example2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<5;i++)
                    outputStr += "A";
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i =0;i<5;i++)
                    outputStr += "B";
            }
        }).start();
        tv1.setText(outputStr);
    }
    private void example3() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=1;i<=100;i++) {
                    final int value = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv1.setText(""+value);
                        }
                    });
                    doFakeWork();
                }
            }
        }).start();
    }
    private void example4() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0 ; i<=100 ; i++){
                    final int value = i;
                    doFakeWork();
                    pb.post(new Runnable() {
                        @Override
                        public void run() {
                            tv1.setText("Updating");
                            pb.setProgress(value);

                        }
                    });

                }
            }
        }).start();
    }
    private void doFakeWork(){
        try{
            Thread.sleep(1000);}
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    private void example5() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL newurl = new URL("https://goo.gl/WhTo4g");
//                    final Bitmap bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());

                    HttpURLConnection con = (HttpURLConnection)newurl.openConnection();
                    InputStream is = con.getInputStream();
                    int imgSize = con.getContentLength();

                    byte[] buffer = new byte[1024];
                    int len = 0;
                    int sum = 0;
                    while((len =is.read (buffer))>0){
                        byteBuffer.write(buffer,0,len);
                        sum += len;
                        float percent = (sum*100.0f)/imgSize;
                        pb.setProgress((int)percent);
                    }
                    imv.post(new Runnable() {
                        public void run() {
                            tv1.setText("Updating");
                            Bitmap bitmap = BitmapFactory.decodeByteArray(byteBuffer.toByteArray(),0,byteBuffer.size());
                            imv.setImageBitmap(bitmap);
                        }
                    });
                } catch (Exception e){}
            }
        }).start();
    }
}
