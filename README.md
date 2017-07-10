#仿微信模糊查询

1、可对列表数据进行模糊查询
2、添加人性化功能：进入界面时EditText不自动弹出键盘，键盘收起的时候隐藏光标，键盘打开的时候退出界面隐藏键盘

![这里写图片描述](http://img.blog.csdn.net/20170710094016873?aHR0cDovL2Jsb2cuY3Nkbi5uZXQvaXJvbWtvZWFy/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

####1、进入界面时EditText不自动弹出键盘

EditText添加属性：
```
android:focusable="true"
```

EditText的上一层添加属性：
```
android:focusable="true"
android:focusableInTouchMode="true"
```

####2、键盘收起的时候隐藏光标，键盘打开的时候退出界面隐藏键盘

创建监听键盘的Listener，SoftKeyBoardListener：
```
package com.mwf.fuzzyquery;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

public class SoftKeyBoardListener {
    
    //activity的根视图
    private View rootView;
    
    //纪录根视图的显示高度
    int rootViewVisibleHeight;
    
    private OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener;

    public SoftKeyBoardListener(Activity activity) {
        //获取activity的根视图
        rootView = activity.getWindow().getDecorView();

        //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获取当前根视图在屏幕上显示的大小
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);

                int visibleHeight = r.height();
//                System.out.println("" + visibleHeight);
                if (rootViewVisibleHeight == 0) {
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }

                //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
                if (rootViewVisibleHeight == visibleHeight) {
                    return;
                }

                //根视图显示高度变小超过200，可以看作软键盘显示了
                if (rootViewVisibleHeight - visibleHeight > 200) {
                    if (onSoftKeyBoardChangeListener != null) {
                        onSoftKeyBoardChangeListener.keyBoardShow(rootViewVisibleHeight - visibleHeight);
                    }
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }

                //根视图显示高度变大超过200，可以看作软键盘隐藏了
                if (visibleHeight - rootViewVisibleHeight > 200) {
                    if (onSoftKeyBoardChangeListener != null) {
                        onSoftKeyBoardChangeListener.keyBoardHide(visibleHeight - rootViewVisibleHeight);
                    }
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }

            }
        });
    }

    private void setOnSoftKeyBoardChangeListener(OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        this.onSoftKeyBoardChangeListener = onSoftKeyBoardChangeListener;
    }

    public interface OnSoftKeyBoardChangeListener {
        void keyBoardShow(int height);

        void keyBoardHide(int height);
    }

    public static void setListener(Activity activity, OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        SoftKeyBoardListener softKeyBoardListener = new SoftKeyBoardListener(activity);
        softKeyBoardListener.setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener);
    }
}

```

界面代码：

```
package com.mwf.fuzzyquery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mwf.fuzzyquery.fakesearchview.FakeSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoosePlateActivity extends AppCompatActivity implements FakeSearchView.OnSearchListener {

    @BindView(R.id.car_list)
    ListView cars;

    @BindView(R.id.fakeSearchView)
    FakeSearchView fakeSearchView;

    @BindView(R.id.tv_cancel)
    TextView tv_cancel;

    @BindView(R.id.activity_main)
    LinearLayout activity_main;

    private CarAdapter mCarAdapter;
    private InputMethodManager inputMethodManager;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_plate);

        ButterKnife.bind(this);
        //初始化键盘
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        fakeSearchView.setOnSearchListener(this);
        mEditText = fakeSearchView.getSearch();

        SoftKeyBoardListener.setListener(ChoosePlateActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                mEditText.requestFocus();
            }

            @Override
            public void keyBoardHide(int height) {
                mEditText.clearFocus();

            }
        });

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mCarAdapter = new CarAdapter(this, getCars());
        cars.setAdapter(mCarAdapter);
        mCarAdapter.setOnViewClickListener(mViewClickListener);

    }

    CarAdapter.OnViewClickListener mViewClickListener = new CarAdapter.OnViewClickListener() {
        @Override
        public void OnItemClick(View view, VehDataBean bean) {
            //隐藏键盘
            inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            Intent intent = new Intent();
            intent.putExtra("VehDataBean", bean);
            setResult(MainActivity.RESULT, intent);
            finish();
        }

    };


    @Override
    public void onSearch(FakeSearchView fakeSearchView, CharSequence constraint) {
        ((CarAdapter) cars.getAdapter()).getFilter().filter(constraint);
    }

    @Override
    public void onSearchHint(FakeSearchView fakeSearchView, CharSequence constraint) {
        ((CarAdapter) cars.getAdapter()).getFilter().filter(constraint);
    }

    List<VehDataBean> getCars() {

        List<VehDataBean> cars = new ArrayList<>();
        cars.add(new VehDataBean("粤BG3750"));
        cars.add(new VehDataBean("粤BG323"));
        cars.add(new VehDataBean("粤BG50"));
        cars.add(new VehDataBean("粤BGgg0"));
        cars.add(new VehDataBean("川BG3750"));
        cars.add(new VehDataBean("桂erssrari"));
        cars.add(new VehDataBean("桂erhhrari1"));
        cars.add(new VehDataBean("桂grrrrarig"));
        cars.add(new VehDataBean("京errerari"));
        cars.add(new VehDataBean("京errari"));
        cars.add(new VehDataBean("京erg6rari"));
        cars.add(new VehDataBean("京erruari"));
        cars.add(new VehDataBean("京err7ari"));
        cars.add(new VehDataBean("京erirari"));
        cars.add(new VehDataBean("京err9ari"));


        return cars;
    }

    @OnClick({R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                //隐藏键盘
                inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        //隐藏键盘
        inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        super.onDestroy();
    }
}

```

XML:
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/activity_main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center_vertical"
        android:orientation="horizontal">
        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_margin="3dp"
            android:layout_weight="1"
            android:focusable="true"
            android:focusableInTouchMode="true"
            android:background="@drawable/border_btn_gray"
        >
            <com.mwf.fuzzyquery.fakesearchview.FakeSearchView
                android:id="@+id/fakeSearchView"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"

            />

        </LinearLayout>
        <TextView
            android:id="@+id/tv_cancel"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_margin="8dp"
            android:text="取消"
            android:textColor="@color/colorPrimary"
            android:textSize="16dp"
        />
    </LinearLayout>


    <ListView
        android:id="@+id/car_list"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:overScrollMode="never"
        android:scrollbars="none"/>
</LinearLayout>
```

更多详细代码请git查看：[仿微信模糊查询](https://github.com/mocn26169/FuzzyQuery)
