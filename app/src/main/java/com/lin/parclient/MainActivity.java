package com.lin.parclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;




public class MainActivity extends Activity implements View.OnClickListener{
    private SharedPreferences sh ;
    private Button mSignON,mSetIp ;
    private String serverIp;
    private ConnectService.ConnectCallBack connectCallBack;
    private ConnectService connectService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        sh              = getSharedPreferences("myown", Context.MODE_PRIVATE);


    }
    private void initView(){
        mSignON         = (Button) findViewById(R.id.sign_on);
        mSetIp          = (Button) findViewById(R.id.setip_enter);
    }
    private void initListener(){
        mSignON         .setOnClickListener(this);
        mSetIp          .setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        serverIp        = sh.getString("ip","192.168.0.33");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_on:
                connectService = new ConnectService(serverIp);
                connectService.setConnectCallBack(getConnectCallBack());
                connectService.trytoSign();
//                Intent intent = new Intent()
                break;
            case R.id.setip_enter:
                startActivity(new Intent(MainActivity.this,SettingIp.class));
                break;
            default:
                break;
        }
    }

    private ConnectService.ConnectCallBack getConnectCallBack(){
        if (connectCallBack==null){
            connectCallBack = new ConnectService.ConnectCallBack() {
                @Override
                public void connectSucceful() {
                    Bundle bundle = new Bundle();
                    bundle.putString("ip", serverIp);
                    Intent intent = new Intent(MainActivity.this,SendCmd.class);
                    intent.putExtra("myown",bundle);
                    startActivity(intent);
                    finish();
                }
                @Override
                public void connectFail() {
                    Toast.makeText(MainActivity.this,"登陆失败",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void SendCmdSucceful(String cmd) {

                }

                @Override
                public void SendCmdFail(String cmd) {

                }
            };
        }
        return connectCallBack;
    }

}
