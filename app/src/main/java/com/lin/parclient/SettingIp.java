package com.lin.parclient;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by xinlyun on 16-1-12.
 */
public class SettingIp extends Activity implements View.OnClickListener{
    private EditText mIpsetEt;
    private Button   mIpsetBt;
    private SharedPreferences sh;
    private SharedPreferences.Editor editor;
    private String ipStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_ip);
        initData();
        initView();
        initListener();
    }
    private void initData(){
        sh              = getSharedPreferences("myown", Context.MODE_PRIVATE);
        ipStr           = sh.getString("ip","");
    }

    private void initView(){
        mIpsetEt        = (EditText) findViewById(R.id.ip_text);
        mIpsetBt        = (Button) findViewById(R.id.ip_sure);
        if (ipStr.equals("")){
            editor      = sh.edit();
            editor      .putString("ip","192.168.0.33");
            editor      .commit();
            ipStr       = "192.168.0.33";
            mIpsetEt    .setHint(ipStr);
            editor      =null;
        }
    }
    private void initListener(){
        mIpsetBt        .setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ip_sure:
                if (mIpsetEt.getText().toString().equals("")) return;
                editor  = sh.edit();
                editor  .putString("ip",mIpsetEt.getText().toString());
                editor  .commit();
                finish();
                break;
        }
    }


}
