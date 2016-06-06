package kr.co.yapp.speciallist.Helper;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import kr.co.yapp.speciallist.MyApplication;

/**
 * Created by home on 2016-06-07.
 */
public class CustomerHelper {
    private static CustomerHelper instance;
    private static Object obj = new Object(); // 여러쓰레드에서 접근할 필요 없을 시 삭제 고려

    // 생성자에서 SingleTon 방식을 이용 -> 이건 나중에 심층적으로 설명
    public static CustomerHelper getInstance() {
        synchronized (obj) {                    // 동기화 작업 (다른 곳에서 CustomerHelper을 호출할 수 있기 때문에 lock을 걸어 제어권이 넘어가지 않도록 한다.)
            if (instance == null) {              // instance가 생성되지 않았을 경우 쉐어드프리프런스 객체를 생성한다.
                instance = new CustomerHelper();
            }
            return instance;                     // null이 아닌 instance 값을 반환한다.
        }
    }

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    public CustomerHelper() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());   //최초 클래스 생성시에 해주고 계속적으로 사용
        mEditor = mPrefs.edit();                                                                //프리퍼런스 에디터를 변수에 매치
    }

    private static final String _ID = "_id";
    private static final String USER_ID = "userId";
    private static final String USER_PASSWORD = "password";
    private static final String USER_DEVICEID = "deviceID";
    private static final String USER_TOKEN = "token";
    private static final String USER_OS = "os";


    //[START] SETTER
    public void set_id(String _id) {
        mEditor.putString(_ID, _id);
        mEditor.commit();
    }


    public void setUserId(String ID) {
        mEditor.putString(USER_ID, ID);
        mEditor.commit();
    }

    public void setUserPassword(String ID) {
        mEditor.putString(USER_PASSWORD, ID);
        mEditor.commit();
    }

    public void setUserDevice_Id(String deviceID) {
        mEditor.putString(USER_DEVICEID, deviceID);
        mEditor.commit();
    }

    public void setUserToken(String token) {
        mEditor.putString(USER_TOKEN, token);
        mEditor.commit();
    }

    public void setUserOs(String os) {
        mEditor.putString(USER_OS, os);
        mEditor.commit();
    }
    //[END] SETTER


    //[START] GETTER
    public String get_Id() {
        return mPrefs.getString(_ID, "");
    }

    public String getUserId() {
        return mPrefs.getString(USER_ID, "");
    }

    public String getUserPassword() {
        return mPrefs.getString(USER_PASSWORD, "");
    }

    public String getUserDevice_id() {
        return mPrefs.getString(USER_DEVICEID, "");
    }

    public String getUserToken() {
        return mPrefs.getString(USER_TOKEN, "");
    }

    public String getUserOs() {
        return mPrefs.getString(USER_OS, "");
    }

    //[END] GETTER
}

