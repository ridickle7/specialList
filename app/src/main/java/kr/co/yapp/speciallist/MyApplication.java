package kr.co.yapp.speciallist;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.nhn.android.naverlogin.OAuthLogin;
import com.tsengvn.typekit.Typekit;

import java.util.StringTokenizer;

/**
 * Created by home on 2016-06-06.
 */
public class MyApplication extends Application {


    //서버 주소
    public static final String MAIN_SERVER_ADDRESS = "http://52.79.87.95:4000";

    //URL category
    public static final String SERVER_USER_EXERCISE = "/user/exercise";
    public static final String SERVER_USER_LOGOUT = "/user/logout";
    public static final String SERVER_USER_LOGIN = "/user/login";
    public static final String SERVER_USER_ADD = "/user/add";
    public static final String SERVER_USER_ADD_FOCUS = "/user/add/focus";
    public static final String SERVER_USER_ADD_SPEC = "/user/add/spec";
    public static final String SERVER_SPEC_ALL = "/spec/all";
    public static final String SERVER_SPEC_MYLIST = "/spec/myList";
    public static final String SERVER_SPEC_SEARCH_DETAIL = "/spec/search/detail";
    public static final String SERVER_SPEC_SEARCH_LIST = "/spec/search/list";
    public static final String SERVER_SPEC_RECENT_DETAIL = "/spec/recent/detail";
    public static final String SERVER_CATEGORY_VIEWALL = "/category/viewAll";
    public static final String SERVER_CATEGORY_VIEWSPEC = "/category/viewSpec";
    public static final String SERVER_CATEGORY_VIEWALL_SEARCH = "/category/viewAll/search";
    public static final String SERVER_COMMENT = "/comment";
    public static final String SERVER_COMMENT_ADD = "/comment/add";
    public static final String SERVER_COMMENT_GOOD = "/comment/good";
    public static final String SERVER_COMMENT_UNGOOD = "/comment/ungood";

    public static final String PARAMETER_TEXT = "text";
    public static final String PARAMETER_LOGINID = "loginId";
    public static final String PARAMETER_USERNAME = "username";
    public static final String PARAMETER_PASSWORD = "password";
    public static final String PARAMETER_DEVICEID = "deviceID";
    public static final String PARAMETER_OS = "os";
    public static final String PARAMETER_TOKEN = "token";
    public static final String PARAMETER_USERID = "userId";
    public static final String PARAMETER_SPECID = "specId";
    public static final String PARAMETER_COMMENTID = "commentId";
    public static final String PARAMETER_SPECDETAILID = "specDetailId";
    public static final String PARAMETER_SPECDETAILID2 = "specDetailId2";
    public static final String PARAMETER_SPECYEAR = "specYear";
    public static final String PARAMETER_SPECYEARNUMBER = "specYearNumber";
    public static final String PARAMETER_PAGE = "page";
    public static final String PARAMETER_VALUE = "value";
    public static final String PARAMETER_RATING = "rating";

    public static final String PARAMETER_SINGLECOMMENTS = "singleComments";
    public static final String PARAMETER_TOTALCOMMENTCNT = "totalCommentCnt";
    public static final String PARAMETER_ADDSPEC_SUCCESS = "addSpec_Success";

    public static final String PARAMETER_ID = "_id";
    public static final String PARAMETER_COMMENT = "comment";
    public static final String PARAMETER_WRITERID = "writerId";
    public static final String PARAMETER_LIKEDCNT = "likedCnt";
    public static final String PARAMETER_ISBEST = "isBest";
    public static final String PARAMETER_ISLIKING = "isLiking";
    public static final String PARAMETER_COMMENTDATE = "commentDate";


    // 안드로이드 내 사용 번수
    public static final String PARAMETER_METHOD = "method";
    public static final int METHOD_HOME_DETAIL = 0;
    public static final int METHOD_RECENT_DETAIL = 1;

    public static final String network_login_ok = "login_success";
    public static final String network_login_fail = "login_fail";
    public static final String network_fail1 = "인터넷 연결이 원활하지 않습니다. 인터넷 연결 확인 후 다시 시도하여 주십시요.";
    public static final String network_fail2 = "인터넷 연결이 원활하지 않습니다. 10초 후 새로고침합니다.";
    public static final String network_login_failMessage = "인터넷 연결이 원활하지 않습니다. 재로그인을 시도합니다.";


    private static OAuthLogin oAuthLoginInstance;

    /**
     * client 정보를 넣어준다.
     */
    private static String OAUTH_CLIENT_ID = "GQ0dkdh9_LlYEj3VLveh";
    private static String OAUTH_CLIENT_SECRET = "8rBicZqElA";
    private static String OAUTH_CLIENT_NAME = "SpecialList";


    public static final String textType = "NotoSansKR-Regular-Hestia.otf";
    public static final String textType_bold = "NotoSansKR-Black-Hestia.otf";

    // Push 관련 정보
    public static final String REGISTRATION_READY = "registrationReady";
    public static final String REGISTRATION_GENERATING = "registrationGenerating";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    // 폰 내의 정보
    public static Context context;
    public static String deviceID;
    public static String os = "android";
    public static Typeface typeface;
    public static Typeface typeface_bold;

    public static int Login_flag = 0;
    public static String SPEC_DETAIL_WRITTEN_ID = "";
    public static String SPEC_DETAIL_PRACTICAL_ID = "";

    @Override
    public void onCreate() {
        super.onCreate();

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        deviceID = tm.getDeviceId();
        context = this;

        typeface = Typekit.createFromAsset(this, "NotoSansKR-Regular-Hestia.otf");
        Typekit.getInstance()
                .addNormal(typeface)
                .addBold(Typekit.createFromAsset(this, "NotoSansKR-Black-Hestia.otf"));

        typeface_bold = Typeface
                .createFromAsset(this.getAssets(), "NotoSansKR-Black-Hestia.otf");


    }

    public static Context getAppContext() {
        return context;
    }

    public static void addExpandableFeatures(RecyclerView v) {
        v.getItemAnimator().setAddDuration(100);
        v.getItemAnimator().setRemoveDuration(100);
        v.getItemAnimator().setMoveDuration(200);
        v.getItemAnimator().setChangeDuration(100);
    }

    public static String[] getDate(String string) {
        Log.d("MyApplication 57 : ", string);
        String[] hour_minute;
        String[] return_string = {"ddd", "bbb"};

        StringTokenizer values = new StringTokenizer(string, "T");
        for (int x = 1; values.hasMoreElements(); x++) {
            return_string[x - 1] = values.nextToken();
        }

        hour_minute = return_string[1].split("Z");
        for (int x = 0; x < hour_minute.length; x++) {
            return_string[x + 1] = hour_minute[x];
        }

        return return_string;
    }

    // 2016-04-03 => {2016,4,3}
    public static String[] getYMD(String string) {
        Log.d("MyApplication 156 : ", string);
        String[] return_string = {"ddd", "bbb", "ccc"};

        StringTokenizer values = new StringTokenizer(string, "-");
        for (int x = 1; values.hasMoreElements(); x++) {
            return_string[x - 1] = values.nextToken();
        }

        return return_string;
    }


    public static OAuthLogin getOAuthLoginInstance() {
        return oAuthLoginInstance;
    }

    public static void setOAuthLoginInstance(Context ctx) {
        oAuthLoginInstance = OAuthLogin.getInstance();
        oAuthLoginInstance.init(
                ctx,
                OAUTH_CLIENT_ID
                ,OAUTH_CLIENT_SECRET
                ,OAUTH_CLIENT_NAME
                //,OAUTH_CALLBACK_INTENT
                // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT변수를 사용하지 않습니다.
        );
    }

}
