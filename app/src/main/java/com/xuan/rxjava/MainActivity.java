package com.xuan.rxjava;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.tv_content)
    TextView tv_content;

    @OnClick(R.id.tv_just)
    void just() {
        Observable.just(justData())
                .subscribe(new Observer<ArrayList<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArrayList<String> strings) {
                        tv_content.setText("");
                        StringBuilder sb = new StringBuilder();
                        for (String str : strings) {
                            sb.append(str).append("   ");
                        }
                        tv_content.setText(sb.toString());
                    }
                });
    }

    private ArrayList<String> justData() {
        ArrayList<String> itemList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            itemList.add("just（" + i + "）");
        }
        return itemList;
    }


    Subscription fromCallable;

    @OnClick(R.id.tv_fromCallable)
    void fromCallable() {
        fromCallable = Observable.fromCallable(new Func0<ArrayList<String>>() {
            @Override
            public ArrayList<String> call() {
                return fromCallableData();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArrayList<String> strings) {
                        tv_content.setText("");
                        StringBuilder sb = new StringBuilder();
                        for (String str : strings) {
                            sb.append(str).append("   ");
                        }
                        tv_content.setText(sb.toString());
                    }
                });
    }

    private ArrayList<String> fromCallableData() {
        ArrayList<String> itemList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            itemList.add("fromCallable（" + i + "）");
        }
        SystemClock.sleep(2000);
        return itemList;
    }


    PublishSubject publishSubject;
    int publishCount = 0;

    @OnClick(R.id.tv_publishSubject)
    void publishSubject() {
        if (publishSubject == null) {
            publishSubject = PublishSubject.create();
            publishSubject.subscribe(new Observer<Integer>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Integer integer) {
                    tv_content.setText("publishSubject   " + integer);
                }
            });


        }
        publishSubject.onNext(publishSubjectData());


    }

    private int publishSubjectData() {
        return ++publishCount;
    }


    PublishSubject mapDebounce;
    StringBuilder sbMap;

    @OnClick(R.id.tv_map_debounce)
    void map() {
        if (publishSubject == null) {

            mapDebounce = PublishSubject.create();

            mapDebounce.subscribeOn(Schedulers.io())
                    .map(new Func1<String, String>() {
                        @Override
                        public String call(String s) {
                            Log.d("print", "Func1   " + s);

                            return s;
                        }
                    })
                    .debounce(400, TimeUnit.MILLISECONDS)//400ms内的过滤掉
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onCompleted() {
                            tv_content.setText(sbMap.toString());
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(String string) {
                            sbMap.append(string).append("   ");
                        }
                    });
        }

        sbMap = new StringBuilder();
        mapStringData();
    }

    private void mapStringData() {
        for (int i = 0; i < 8; i++) {
            mapDebounce.onNext("过滤400ms以内的数据" + "(" + i + ")");
            SystemClock.sleep(i * 100);
        }
        mapDebounce.onCompleted();
    }

    /*
    1.Observable和Subscriber可以做任何事情Observable可以是一个数据库查询，
    Subscriber用来显示查询结果；Observable可以是屏幕上的点击事件，Subscriber用来响应点击事件；
    Observable可以是一个网络请求，Subscriber用来显示请求结果。

    2.Observable和Subscriber是独立于中间的变换过程的。
    在Observable和Subscriber中间可以增减任何数量的map。
    整个系统是高度可组合的，操作数据是一个很简单的过程。*/
    @OnClick(R.id.tv_map1)
    void map1() {
        Observable.just(getList())
                .subscribeOn(Schedulers.io())
                .map(new Func1<ArrayList<Integer>, String>() {

                    @Override
                    public String call(ArrayList<Integer> integer) {
                        StringBuilder sb = new StringBuilder();
                        for (Integer i : integer) {
                            sb.append(i).append(" - ");
                        }
                        return sb.toString();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        tv_content.setText(s);
                    }
                });
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//
//                    }
//                });
    }

    private ArrayList<Integer> getList() {
        ArrayList<Integer> itemList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            itemList.add(i);
        }
        return itemList;
    }

    @OnClick(R.id.tv_from)
    void from() {
        Observable.from(getList())
                .subscribeOn(Schedulers.io())
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        SystemClock.sleep(400);
                        return integer;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        tv_content.setText("integer = " + integer + "");
                    }
                });
    }



    @OnClick(R.id.tv_flatMap1)
    void flatMap1() {
        final StringBuilder sb=new StringBuilder();
        Observable.from(getStudent())
                .map(new Func1<Student, String>() {
                    @Override
                    public String call(Student student) {
                        return student.name;
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        tv_content.setText(sb.toString());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        sb.append(s).append("   ");
                    }
                });

    }

    @OnClick(R.id.tv_flatMap2)
    void flatMap2() {
        final StringBuilder sb=new StringBuilder();
        Observable.from(getStudent())
                .flatMap(new Func1<Student, Observable<String>>() {
                    @Override
                    public Observable<String> call(Student student) {
                        return Observable.from(student.course);
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        tv_content.setText(sb.toString());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        sb.append(s).append("   ");
                    }
                });

    }

    private class Student{
        public String name;
        public ArrayList<String> course;
    }

    public ArrayList<Student> getStudent(){
        ArrayList<Student> stus=new ArrayList<>();
        for (int i=0;i<5;i++){
            Student stu=new Student();
            stu.name="名字("+i+")";
            stu.course=new ArrayList<>();
            for (int k=0;k<3;k++){
                stu.course.add("课程("+k+")");
            }
            stus.add(stu);
        }
        return  stus;
    }


    @OnClick(R.id.tv_bitmap)
    void bitmap() {
        final ArrayList<Bitmap> item=new ArrayList<>();

        Observable.from(getFolders())
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {
                        return Observable.from(file.listFiles());
                    }
                })
                .filter(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File file) {
                        return file.getName().endsWith(".png");
                    }
                })
                .map(new Func1<File, Bitmap>() {
                    @Override
                    public Bitmap call(File file) {
                        return getBitmapFromFile(file);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        item.add(bitmap);
                    }
                });
    }

    /**
     * 全局缓存路径
     */
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mhome/";
    /**
     * 缓存路径
     */
    String cachePath = path + "cache/";
    /**
     * 相机路径
     */
    String cameraPath = path + "camera/";
    private ArrayList<File> getFolders() {
        ArrayList<File> item=new ArrayList<>();
        File file1=new File(path+cachePath);
        File file2=new File(path+cameraPath);

        item.add(file1);
        item.add(file2);
        return item;
    }
    private Bitmap getBitmapFromFile(File file) {
        return BitmapFactory.decodeFile(file.getPath());
    }


    @OnClick(R.id.tv_timer)void timer(){
        Observable.timer(2000,TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        tv_content.setText("打开首页 "+aLong);
                    }
                });

    }


    @OnClick(R.id.tv_combineLatest) void combineLatest(){
        final EditText editText1=new EditText(this);
        editText1.setText("123");
        final EditText editText2=new EditText(this);
        editText2.setText("");


        Observable<String> ob1=Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(editText1.getText().toString());
            }
        });

        Observable<String> ob2=Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(editText2.getText().toString());
            }
        });

        Observable.combineLatest(ob1, ob2, new Func2<String, String, Boolean>() {
            @Override
            public Boolean call(String s, String s2) {

                return !TextUtils.isEmpty(s) && !TextUtils.isEmpty(s2);
            }
        })
        .subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                tv_content.setText(aBoolean+"");
            }
        });


    }

//    BehaviorSubject<List<String>> cache;
//    private static final int DATA_SOURCE_MEMORY = 1;
//    private static final int DATA_SOURCE_DISK = 2;
//    private static final int DATA_SOURCE_NETWORK = 3;
//    private int dataSource;
//    @IntDef({DATA_SOURCE_MEMORY, DATA_SOURCE_DISK, DATA_SOURCE_NETWORK}) @interface DataSource {}
//    @OnClick(R.id.tv_behaviorSubject) void behaviorSubject(){
//
//    }
//    public Subscription subscribeData(@NonNull Observer<List<String>> observer) {
//        if (cache == null) {
//            cache = BehaviorSubject.create();
//            Observable.create(new Observable.OnSubscribe<List<String>>() {
//                @Override
//                public void call(Subscriber<? super List<String>> subscriber) {
//                    List<String> items = Database.getInstance().readItems();
//                    if (items == null) {
//                        setDataSource(DATA_SOURCE_NETWORK);
//                        loadFromNetwork();
//                    } else {
//                        setDataSource(DATA_SOURCE_DISK);
//                        subscriber.onNext(items);
//                    }
//                }
//            })
//                    .subscribeOn(Schedulers.io())
//                    .subscribe(cache);
//        } else {
//            setDataSource(DATA_SOURCE_MEMORY);
//        }
//        return cache.observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
//    }
//    private void setDataSource(@DataSource int dataSource) {
//        this.dataSource = dataSource;
//    }
//    public List<String> readItems() {
//        // Hard code adding some delay, to distinguish reading from memory and reading disk clearly
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            Reader reader = new FileReader(dataFile);
//            return gson.fromJson(reader, new TypeToken<List<String>>(){}.getType());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);

        if (fromCallable != null && !fromCallable.isUnsubscribed()) {
            fromCallable.unsubscribe();
        }
    }


}
