package com.xy.rxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xy.rxjava.model.ScoreTip;
import com.xy.rxjava.model.User;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by xingyun on 2016/8/29.
 */
public class RxJavaMethodActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RxJavaMethodActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_method);

        findViews();
    }

    private void findViews() {
        Button btnCreate = (Button) findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(this);
        Button btnJust = (Button) findViewById(R.id.btn_just);
        btnJust.setOnClickListener(this);
        Button btnFrom = (Button) findViewById(R.id.btn_from);
        btnFrom.setOnClickListener(this);
        Button btnMap = (Button) findViewById(R.id.btn_map);
        btnMap.setOnClickListener(this);
        Button btnFlatMap = (Button) findViewById(R.id.btn_flatmap);
        btnFlatMap.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_create:
                create();
                break;
            case R.id.btn_just:
                just();
                break;
            case R.id.btn_from:
                from();
                break;
            case R.id.btn_map:
                map();
                break;
            case R.id.btn_flatmap:
                flatMap();
                break;
        }
    }

    /**
     * 一个用户对应多个积分方案
     * 需要把User->List<ScoreTip>，（一对多转化）
     * map就不能满足，需要使用flatMap
     * eg. 打印每个用户各自的积分方案
     */
    private void flatMap() {
        List<User> users = new ArrayList<User>();

        User user1 = new User("zhangsan");
        List<ScoreTip> scoreTips1 = new ArrayList<>();
        scoreTips1.add(new ScoreTip("vip_1"));
        scoreTips1.add(new ScoreTip("mvp_1"));
        user1.scoreTipList = scoreTips1;
        users.add(user1);

        User user2 = new User("lisi");
        List<ScoreTip> scoreTips2 = new ArrayList<>();
        scoreTips2.add(new ScoreTip("vip_2"));
        scoreTips2.add(new ScoreTip("mvp_2"));
        user2.scoreTipList = scoreTips2;
        users.add(user2);

        Observable.from(users)
                .flatMap(new Func1<User, Observable<ScoreTip>>() {
                    @Override
                    public Observable<ScoreTip> call(User user) {
                        return Observable.from(user.scoreTipList);
                    }
                })
                .subscribe(new Action1<ScoreTip>() {
                    @Override
                    public void call(ScoreTip scoreTip) {
                        Log.d(TAG, "subscribe...call==>scoretip name="+scoreTip.tipName);
                    }
                });
    }

    /**
     * 打印用户名
     * 把User对象转换成String（一对一转化）
     */
    private void map() {
        List<User> users = new ArrayList<User>();
        users.add(new User("zhangsan"));
        users.add(new User("lisi"));
        Observable.from(users)
                .map(new Func1<User, String>() {
                    @Override
                    public String call(User user) {
                        return user.name;
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, "subscribe call==>name="+s);
                    }
                });
    }

    /**
     * 传入的数组或 Iterable 拆分成具体对象后，依次发送出来
     * 对于参数Future，它会发射Future.get()方法返回的单个数据
     */
    private void from() {
        Integer[] items = {1,2,3};
        Observable observable = Observable.from(items);
        observable.subscribe(new Subscriber<Integer>() {
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
                Log.d(TAG, "onNext....i="+integer);
            }
        });
    }

    /**
     * 以数组形式一次性发送
     */
    private void just() {
        Observable.just(1,2,3).subscribe(new Subscriber<Integer>() {
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
                Log.d(TAG, "onNext....i="+integer);
            }
        });
    }

    private void create() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    for (int i = 0; i < 3; i++) {
                        subscriber.onNext(i);
                    }
                    subscriber.onCompleted();
                } catch (Exception e) {
                        subscriber.onError(e);
                }
            }
        }).subscribe(new Subscriber<Integer>() {
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
                Log.d(TAG, "onNext....i="+integer);
            }
        });
    }
}
