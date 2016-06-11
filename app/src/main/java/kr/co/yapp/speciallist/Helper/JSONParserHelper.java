package kr.co.yapp.speciallist.Helper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by home on 2016-06-11.
 */
public class JSONParserHelper {
    public static JSONObject StringToJSONObject(String str) {
        JSONObject temp_obj = new JSONObject();
        try {
            temp_obj = new JSONObject(str);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return temp_obj;
    }
}
