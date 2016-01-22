package com.lin.parclient;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xinlyun on 16-1-12.
 */
public class MyThread extends Thread{
    URL url;
    HttpURLConnection connection;
    String str;
//    String nob;
    ObjectOutputStream objOutputStrm;
    MyThreadCall myThreadCall;
    MyThread(URL url,String msg,MyThreadCall myThreadCall){
        this.url = url;
        this.str = msg;
        this.myThreadCall = myThreadCall;
//        this.nob = nob;
    }

    @Override
    public void run() {
        super.run();
        try {
            //---数据填充至json数据中---
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("station", str);
//            jsonObject.put("bus",nob);

            //--转化成字符串----
            String content = String.valueOf(jsonObject);

            //---搭建连接---
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");


            OutputStream os = conn.getOutputStream();
            os.write(content.getBytes());
            os.flush();
            os.close();

            InputStream in = conn.getInputStream();
            byte[] back = new byte[512];
            int i = 5;
            int length=0;
            while (i!=0 && (length = in.read(back))==-1){
                i--;
            }
            String call = new String(back,0,length);
            if(call!=null){
                if(call.equals("ok"))
                    myThreadCall.sendSuccess(str);
                else
                    myThreadCall.sendFail(str);
            }
            else
                myThreadCall.sendFail(str);

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("MainActivity", "faild");
            myThreadCall.sendFail(str);
        } catch (JSONException e) {
            e.printStackTrace();
            myThreadCall.sendFail(str);
        } finally {
            if(connection!=null){
                connection.disconnect();
            }
        }
    }





    interface MyThreadCall{
        void sendSuccess(String cmd);
        void sendFail(String cmd);
    }

}