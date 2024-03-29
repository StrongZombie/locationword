package com.example.locationword.locationword;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import com.example.locationword.locationword.event.MessageEvent;
import com.example.locationword.locationword.http.API;
import com.example.locationword.locationword.http.HttpUtil;
import com.example.locationword.locationword.myview.LoadingDialog;

import com.example.locationword.locationword.tool.Constant;
import com.example.locationword.locationword.tool.JSONChange;
import com.example.locationword.locationword.tool.PreferenceUtil;
import com.example.locationword.locationword.tool.ShowUtil;
import com.example.locationword.locationword.tool.SkipUtils;
import com.google.gson.JsonObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etTelphonenumber;
    private EditText etPassward;
    private Button btLogin;
    private TextView forgetpassword;
    private TextView register;
    private String TAG="LoginActivity";
    private LoadingDialog loadingDialog;
    private Handler handler=new Handler(){
        public void  handleMessage(Message m){
            switch (m.what){
                case 1000:
                    String s = (String)m.obj;
                    JsonObject jo = JSONChange.StringToJsonObject(s);
                    final String result=jo.get("message").getAsString();

                    Log.i(TAG,result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                             if(result.equals("密码错误")||result.equals("用户不存在")){
                                    loadingDialog.close();
                             }

                             ShowUtil.showText(LoginActivity.this,result);
                        }
                    });

                    if(result.equals("登录成功")){
                        final String userId=jo.get("userid").getAsString();
                        final String userPhone=jo.get("userphone").getAsString();
                        SharedPreferences preferences = getSharedPreferences(Constant.logindata,MODE_PRIVATE);
                        preferences.edit().putString(Constant.UserId,userId).putString(Constant.UserPhone,userPhone)
                                .commit();
                        LoginEMClient(userId);
                    }
                    break;
                case 1001:
                    Log.i(TAG,"SDSD");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.close();
                            ShowUtil.showText(LoginActivity.this,"网络异常");
                        }
                    });

                    break;
                case 101:
                    SharedPreferences preferences = getSharedPreferences(Constant.logindata,MODE_PRIVATE);
                    String userId = preferences.getString(Constant.UserId,"");

                    LoginEMClient(userId);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        //loadingDialog=new LoadingDialog(LoginActivity.this,"请稍候...");
        initView();
        onClicked();
        checkAutoLogin();
        // HttpUtil.getInstence().doPost("http://172.17.146.136:8082/MVCl_w/Login/checklogin",map);
    }
    public void onClicked(){
        btLogin.setOnClickListener(this);
        forgetpassword.setOnClickListener(this);
        register.setOnClickListener(this);
    }
    public void initView(){
        loadingDialog=new LoadingDialog(LoginActivity.this,"数据同步中...");
        etTelphonenumber = (EditText) findViewById(R.id.et_telphonenumber);
        etPassward = (EditText) findViewById(R.id.et_passward);
        btLogin = (Button) findViewById(R.id.bt_login);
        forgetpassword = (TextView) findViewById(R.id.forgetpassword);
        register = (TextView) findViewById(R.id.register);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_login:
                String phone = etTelphonenumber.getText().toString();
                String password = etPassward.getText().toString();
                if(phone.equals("")){
                    ShowUtil.showText(LoginActivity.this,"请输入账号");

                }else if (password.equals("")){
                    ShowUtil.showText(LoginActivity.this,"请输入密码");

                }else{
                    Log.i(TAG,"show");
                    loadingDialog.show();
                    Map<String,String> m = new HashMap<>();
                    m.put("phone",phone);
                    m.put("password",password);
                    HttpUtil.getInstence().doPost(API.checkLogin,m,handler);
                }

                break;
            case R.id.forgetpassword:
                SkipUtils.skipActivity(LoginActivity.this,ForgetPasswordActivity.class);
                break;
            case R.id.register:
                SkipUtils.skipActivity(LoginActivity.this,RegisterActivity.class);
                break;
        }
    }
    public void onStart(){
        super.onStart();
        if(EventBus.getDefault()==null){
            EventBus.getDefault().register(this);
        }

    }
    public void onDestroy(){
        super.onDestroy();

        loadingDialog.close();
        EventBus.getDefault().unregister(this);
    }
    public void LoginEMClient(final String userName){
        //Log.d("main", "登录聊天服务器成功！"+userName);
        EMClient.getInstance().login(userName,"123456",new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        EMClient.getInstance().chatManager().loadAllConversations();
//                        EMClient.getInstance().groupManager().loadAllGroups();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.close();
                            }
                        });
                        SkipUtils.skipActivity(LoginActivity.this,MainActivity.class);
                        LoginActivity.this.finish();
                    }
                });


            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
//                ShowUtil.showText(LoginActivity.this,"服务器异常");
                if(message.equals("User is already login")){
                    EMClient.getInstance().logout(true);
                    LoginEMClient(userName);
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            EMClient.getInstance().chatManager().loadAllConversations();
//                            EMClient.getInstance().groupManager().loadAllGroups();
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    loadingDialog.close();
//                                }
//                            });
//
//                            SkipUtils.skipActivity(LoginActivity.this,MainActivity.class);
//                            LoginActivity.this.finish();
//                        }
//                    }).start();
                      }
                Log.d("main", "登录聊天服务器失败！"+message);
            }
        });
    }
    protected void checkAutoLogin(){
        SharedPreferences preferences = getSharedPreferences(Constant.logindata,MODE_PRIVATE);
        Log.i("tag",preferences.getString(Constant.UserId,""));
        if (preferences!=null) {
            if (!preferences.getString(Constant.UserId, "").equals("")) {
                if (preferences.getBoolean(Constant.autoLogin,true)){
                    loadingDialog.show();
                    handler.sendEmptyMessage(101);
                }

            }
        }

    }

}
