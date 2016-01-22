package com.lin.parclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by xinlyun on 16-1-12.
 */
public class SendCmd extends Activity implements View.OnClickListener {
    private EditText mCmdEt;
    private Button   mSendBt;
    private String   serverIp;
    private ConnectService mConnectService;
    private ConnectService.ConnectCallBack connectCallBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendcmd);
        initView();
        initListener();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("myown");
        serverIp = bundle.getString("ip","192.168.0.33");
        mConnectService = new ConnectService(serverIp);
        mConnectService.setConnectCallBack(getConnectCallBack());
    }
    private void initView(){
        mCmdEt          = (EditText) findViewById(R.id.edit_cmd);
        mSendBt         = (Button) findViewById(R.id.btn_send_cmd);
    }

    private void initListener(){
        mSendBt         .setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send_cmd:
                if(!mCmdEt.getText().toString().equals(""))
                    mConnectService.sendCmd(mCmdEt.getText().toString());
                break;
            default:
                break;

        }
    }
    private ConnectService.ConnectCallBack getConnectCallBack(){
        if(connectCallBack==null){
            connectCallBack = new ConnectService.ConnectCallBack() {
                @Override
                public void connectSucceful() {

                }

                @Override
                public void connectFail() {

                }

                @Override
                public void SendCmdSucceful(String cmd) {
                    Toast.makeText(SendCmd.this,"发送命令:"+cmd+" 成功",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void SendCmdFail(String cmd) {
                    Toast.makeText(SendCmd.this,"发送命令:"+cmd+" 失败",Toast.LENGTH_SHORT).show();
                }
            };
        }
        return connectCallBack;
    }

}
