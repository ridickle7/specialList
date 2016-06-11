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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import kr.co.yapp.speciallist.GCMService.RegistrationIntentService;
import kr.co.yapp.speciallist.Helper.CustomerHelper;
import kr.co.yapp.speciallist.Helper.JSONParserHelper;
import kr.co.yapp.speciallist.Helper.NetworkHelper;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

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

        // 토큰 아이디가 없을 경우
        if (CustomerHelper.getInstance().getUserToken().equals("")) {
            registBroadcastReceiver();

            getInstanceIdToken();
        }

        // 토큰 아이디가 있을 경우
        else {
            playLogin();
        }
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

                    playLogin();
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

    void playLogin() {
        NetworkHelper.login(new NetworkHelper.VolleyCallback() {
            @Override
            public void onAction(String result) {
                JSONObject jObject = JSONParserHelper.StringToJSONObject(result);
                // 문제가 없이 로그인 되었다. -> 바로 메인으로 간다.

                try {
                    if (jObject.getString("msg").equals(MyApplication.network_login_ok)) {
                        intent_Activity = MainActivity.class;
                    } else {
                        // 한 번도 로그인 안햇을 경우. + 인터넷 연결이 원활하지 않은 경우
                        if (jObject.getString("msg").equals(MyApplication.network_login_fail))
                        Toast.makeText(getApplicationContext(), MyApplication.network_login_failMessage, Toast.LENGTH_SHORT).show();


                        intent_Activity = LoginActivity.class;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 여기 intent가 있어도 문제가 없을까?
                        Intent intent = new Intent(getApplicationContext(), intent_Activity);
                        startActivity(intent);
                    }
                }, 5000);
            }
        }, getApplicationContext(), CustomerHelper.getInstance().getUserId(), CustomerHelper.getInstance().getUserPassword());
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

    /* for font */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

}
