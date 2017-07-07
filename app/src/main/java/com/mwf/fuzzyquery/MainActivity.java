package com.mwf.fuzzyquery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static int RESULT = 1000;

    @BindView(R.id.tv_result)
    TextView tv_result;

    @BindView(R.id.btn_choose)
    Button btn_choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, ChoosePlateActivity.class), RESULT);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            VehDataBean bean = data.getParcelableExtra("VehDataBean");
            if (bean != null) {
                tv_result.setText("结果：" + bean.getVehnumber());
            }
        }

    }


}
