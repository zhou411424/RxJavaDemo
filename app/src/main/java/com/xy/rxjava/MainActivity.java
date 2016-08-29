package com.xy.rxjava;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViews();
    }

    private void findViews() {
        Button btnMethod = (Button) findViewById(R.id.rxjava_method_btn);
        btnMethod.setOnClickListener(this);
        Button btnScheduler = (Button) findViewById(R.id.rxjava_scheduler_btn);
        btnScheduler.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rxjava_method_btn:
                Intent methodIntent = new Intent(MainActivity.this, RxJavaMethodActivity.class);
                MainActivity.this.startActivity(methodIntent);
                break;
            case R.id.rxjava_scheduler_btn:
                Intent schedulerIntent = new Intent(MainActivity.this, RxJavaSchedulerActivity.class);
                MainActivity.this.startActivity(schedulerIntent);
                break;
        }
    }
}
