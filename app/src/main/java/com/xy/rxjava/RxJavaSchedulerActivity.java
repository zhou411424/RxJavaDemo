package com.xy.rxjava;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xingyun on 2016/8/29.
 */
public class RxJavaSchedulerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RxJavaSchedulerActivity";
    private ImageView localIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_scheduler);

        findViews();
    }

    private void findViews() {
        Button btnJust = (Button) findViewById(R.id.btn_just_scheduler);
        btnJust.setOnClickListener(this);
        Button btnSchedulerLocalImage = (Button) findViewById(R.id.btn_scheduler_local_image);
        btnSchedulerLocalImage.setOnClickListener(this);
        localIv = (ImageView) findViewById(R.id.iv_local);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_just_scheduler:
                just_scheduler();
                break;
            case R.id.btn_scheduler_local_image:
                scheduler_local_image();
                break;
        }
    }

    /**
     * 从本地资源文件中加载图片
     */
    private void scheduler_local_image() {
        Observable
                .create(new Observable.OnSubscribe<Drawable>() {
                    @Override
                    public void call(Subscriber<? super Drawable> subscriber) {
                        Log.d(TAG, "scheduler_local_image==>call....thread name=" + Thread.currentThread().getName() + ", thread id=" + Thread.currentThread().getId());
                        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
                        subscriber.onNext(drawable);
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io())// 读取文件放在io线程
                .observeOn(AndroidSchedulers.mainThread())// 显示图片放在主线程
                .subscribe(new Subscriber<Drawable>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted....");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError....error msg: " + e.getMessage());
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        localIv.setImageDrawable(drawable);
                        Log.d(TAG, "onNext....thread name=" + Thread.currentThread().getName() + ", thread id=" + Thread.currentThread().getId());
                    }
                });
    }

    /**
     * 以数组形式一次性发送
     * subscribeOn指定subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程(叫做事件生产线程)
     * observeOn指定Subscriber 所运行在的线程（叫做事件消费线程）
     */
    private void just_scheduler() {
        Observable.just(1,2,3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted....");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError....error msg: "+e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext....i="+integer+", thread name="+Thread.currentThread().getName()+", thread id="+Thread.currentThread().getId());
            }
        });
    }
}
