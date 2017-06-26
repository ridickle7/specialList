package kr.co.yapp.speciallist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import kr.co.yapp.speciallist.Main.MainActivity;

/**
 * A login screen that offers login via id/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private static OAuthLogin mOAuthLoginInstance;
    private OAuthLoginButton mOAuthLoginButton;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
        // Set up the login form.
        initData();
    }

    private void initData() {
        MyApplication.setOAuthLoginInstance(context);
        mOAuthLoginInstance = MyApplication.getOAuthLoginInstance();

        OAuthLoginButton mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
        mOAuthLoginButton.setBgResourceId(R.mipmap.login_full_green);
    }


    /**
     * OAuthLoginHandler를 startOAuthLoginActivity() 메서드 호출 시 파라미터로 전달하거나 OAuthLoginButton
     객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다.
     */

    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginInstance.getAccessToken(context);
                String refreshToken = mOAuthLoginInstance.getRefreshToken(context);
                long expiresAt = mOAuthLoginInstance.getExpiresAt(context);
                String tokenType = mOAuthLoginInstance.getTokenType(context);

                context.startActivity(new Intent(context, MainActivity.class));
                Activity temp = (Activity)context;
                temp.finish();
            }

            else {
                String errorCode = mOAuthLoginInstance.getLastErrorCode(context).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(context);
                Toast.makeText(context, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        };
    };
}

