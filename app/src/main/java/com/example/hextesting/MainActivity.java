package com.example.hextesting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    private TextView tv;
    private HexView hv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hv =  findViewById(R.id.hexView);
        tv = (TextView) findViewById(R.id.textView);

        hv.setTextView(tv);
    }

    public TextView getTv() {
        return tv;
    }
}