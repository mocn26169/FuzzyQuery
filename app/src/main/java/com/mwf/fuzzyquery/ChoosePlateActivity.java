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
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        fakeSearchView.setOnSearchListener(this);
        mEditText = fakeSearchView.getSearch();

        SoftKeyBoardListener.setListener(ChoosePlateActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
//                Toast.makeText(ChoosePlateActivity.this, "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();
                mEditText.requestFocus();
            }

            @Override
            public void keyBoardHide(int height) {
//                Toast.makeText(ChoosePlateActivity.this, "键盘隐藏 高度" + height, Toast.LENGTH_SHORT).show();
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
                inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        super.onDestroy();
    }
}
