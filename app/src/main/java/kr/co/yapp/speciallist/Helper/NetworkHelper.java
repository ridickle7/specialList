package kr.co.yapp.speciallist.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kr.co.yapp.speciallist.MainActivity;
import kr.co.yapp.speciallist.MyApplication;

/**
 * Created by home on 2016-06-07.
 */
public class NetworkHelper {
    private static NetworkHelper instance;
    public static RequestQueue mQueue;
    private static Object obj = new Object();

    public static NetworkHelper getInstance() {
        synchronized (obj) {
            if (instance == null) {
                instance = new NetworkHelper();
                mQueue = Volley.newRequestQueue(MyApplication.getAppContext());
            }
            return instance;
        }
    }

    public static void login(final VolleyCallback callback, final Context ctx, final String user_id, final String user_pw) {
        // /user/add
        final String URL_address = MyApplication.MAIN_SERVER_ADDRESS + MyApplication.SERVER_USER_LOGIN;
        RequestQueue mQueue2 = NetworkHelper.getInstance().mQueue;

        // Request a string response from the provided URL.
        // 2) Request Obejct인 StringRequest 생성
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Response is: ", response.toString());
                        Toast.makeText(ctx, response.toString(), Toast.LENGTH_LONG).show();
                        if (callback != null)
                            callback.onAction(response.toString());
                        else {
                            JSONObject jObject = JSONParserHelper.StringToJSONObject(response);

                            try {
                                CustomerHelper.getInstance().set_id(jObject.getString("_id"));
                                CustomerHelper.getInstance().setUserId(user_id);
                                CustomerHelper.getInstance().setUserPassword(user_pw);

                                Intent intent = new Intent(ctx, MainActivity.class);
                                intent.setFlags(
                                        Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                ctx.getApplicationContext().startActivity(intent);
                                ((Activity) ctx.getApplicationContext()).finish();
                            }
                            catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());

                try {
                    if (callback != null) {
                        JSONObject jObject = new JSONObject();
                        jObject.put("msg", MyApplication.network_login_fail);
                        callback.onAction(jObject.toString());
                    } else {
                        Toast.makeText(ctx, MyApplication.network_login_failMessage, Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
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


    public interface VolleyCallback {
        void onAction(String result);
    }
}
