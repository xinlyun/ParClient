package com.lin.parclient;

import android.os.Handler;
import android.os.Message;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by xinlyun on 16-1-12.
 */
public class ConnectService extends Thread{
    private String serviceIp;
    private ConnectCallBack connectCallBack;
    private MyThread.MyThreadCall myThreadCall;
    public ConnectService(String ip){
        this.serviceIp = ip;
    }
    public void trytoSign(){
        try {
            URL url = new URL("http://"+serviceIp+":8080/servlet/Test?cmd=sign");
            new MyThread(url,"sign",getThreadCall()).start();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    public void sendCmd(String cmd){
        try {
            URL url = new URL("http://"+serviceIp+":8080/servlet/Test?cmd="+cmd);
            new MyThread(url,cmd,getThreadCall()).start();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    private MyThread.MyThreadCall getThreadCall(){
        if(myThreadCall==null){
            myThreadCall = new MyThread.MyThreadCall() {
                @Override
                public void sendSuccess(String cmd) {
                    Message msg = msgHandler.obtainMessage();
                    msg.what = 1;
                    msg.obj = cmd;
                    msgHandler.sendMessage(msg);
                }
                @Override
                public void sendFail(String cmd) {
                    Message msg = msgHandler.obtainMessage();
                    msg.what = 0;
                    msg.obj = cmd;
                    msgHandler.sendMessage(msg);
                }
            };
        }
        return myThreadCall;
    }
    private Handler msgHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String cmd = (String) msg.obj;
            switch (msg.what) {
                case 0:
                    if(cmd.equals("sign")){
                        if(connectCallBack!=null)connectCallBack.connectFail();
                        return;
                    }
                    if(connectCallBack!=null)
                        connectCallBack.SendCmdFail(cmd);
                    break;
                case 1:
                    if(cmd.equals("sign")){
                        if(connectCallBack!=null)connectCallBack.connectSucceful();
                        return;
                    }
                    if(connectCallBack!=null)
                        connectCallBack.SendCmdSucceful(cmd);
                    break;


            }
        }
    };


    interface ConnectCallBack{
        void connectSucceful();
        void connectFail();
        void SendCmdSucceful(String cmd);
        void SendCmdFail(String cmd);
    }
    public void setConnectCallBack(ConnectCallBack connectCallBack){
        this.connectCallBack = connectCallBack;
    }
}
