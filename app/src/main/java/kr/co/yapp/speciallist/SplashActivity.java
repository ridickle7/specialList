package kr.co.yapp.speciallist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.HashMap;
import java.util.Map;

import kr.co.yapp.speciallist.GCMService.RegistrationIntentService;
import kr.co.yapp.speciallist.Helper.CustomerHelper;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    RequestQueue mQueue2;
    Handler mHandler = new Handler(Looper.getMainLooper());
    Class intent_Activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 유저 아이디, 비밀번호가 등록 안 되어있는 경우   -> 처음 로그인 한 겁니다.
        // 토큰 아이디가 없는 경우                        -> 재 로그인해야 합니다.
        if (CustomerHelper.getInstance().getUserId().equals("") || CustomerHelper.getInstance().getUserPassword().equals("") || CustomerHelper.getInstance().getUserToken().equals("")) {
            registBroadcastReceiver();

            getInstanceIdToken();

            intent_Activity = LoginActivity.class;
        }

        // 문제가 없다!! -> 바로 메인으로 간다.
        else {
            intent_Activity = MainActivity.class;
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                // 여기 intent가 있어도 문제가 없을까?
                Intent intent = new Intent(getApplicationContext(), intent_Activity);
                startActivity(intent);
            }
        }, 1000);


    }


    /**
     * Instance ID를 이용하여 디바이스 토큰을 가져오는 RegistrationIntentService를 실행한다.
     */
    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        } else {
            Toast.makeText(getApplicationContext(), "no in regis Service", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * LocalBroadcast 리시버를 정의한다. 토큰을 획득하기 위한 READY, GENERATING, COMPLETE 액션에 따라 UI에 변화를 준다.
     */
    public void registBroadcastReceiver() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();


                if (action.equals(MyApplication.REGISTRATION_READY)) {
                    // 액션이 READY일 경우???

                } else if (action.equals(MyApplication.REGISTRATION_GENERATING)) {
                    // 토큰을 받고 있는 중인 경우

                } else if (action.equals(MyApplication.REGISTRATION_COMPLETE)) {
                    // 토큰을 성공적으로 받아왔을 경우
                    String token = intent.getStringExtra("token");      // 토큰을 받아왔다.
                    CustomerHelper.getInstance().setUserToken(token);
                }
            }
        };
    }

    /**
     * 앱이 실행되어 화면에 나타날때 LocalBoardcastManager에 액션을 정의하여 등록한다.
     */
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(MyApplication.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(MyApplication.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(MyApplication.REGISTRATION_COMPLETE));

    }

    /**
     * 앱이 화면에서 사라지면 등록된 LocalBoardcast를 모두 삭제한다.
     */
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    /**
     * Google Play Service를 사용할 수 있는 환경인지를 체크한다.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void login(final Context ctx, final String user_id, final String user_pw) {
        // /user/add
        final String URL_address = MyApplication.MAIN_SERVER_ADDRESS + MyApplication.SERVER_USER_LOGIN;
        mQueue2 = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        // 2) Request Obejct인 StringRequest 생성
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Response is: ", response.toString());
                        Toast.makeText(ctx, response.toString(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put(MyApplication.PARAMETER_USERNAME, user_id);
                map.put(MyApplication.PARAMETER_PASSWORD, user_pw);
                map.put(MyApplication.PARAMETER_DEVICEID, MyApplication.deviceID);
                map.put(MyApplication.PARAMETER_OS, "android");
                map.put(MyApplication.PARAMETER_TOKEN, CustomerHelper.getInstance().getUserToken());
                Log.d("map : ", user_id + user_pw + MyApplication.deviceID + "");
                return map;
            }
        };
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        //mQueue2.add(stringRequest);   //싱글턴 사용 안할 시
        mQueue2.add(stringRequest);
    }


    /* for font */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

}
