package kr.co.yapp.speciallist.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.yapp.speciallist.Main.MainTab1.Spec_mainList;
import kr.co.yapp.speciallist.Main.MainTab2.MainTab2ListAdapter;

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

    public static ArrayList<MainTab2ListAdapter.Item> JSONArray_getCategoryList(JSONObject jData) {
        ArrayList<MainTab2ListAdapter.Item> abList = new ArrayList<>();

        try {
            // 1. categoryList를 뽑아낸다.
            JSONArray jCategoryData = jData.getJSONArray("category_list");


            // 검색 결과가 없을경우
            if (jCategoryData.getJSONObject(0).getString("category_name").equals("null")) {
                abList.add(new MainTab2ListAdapter.Item(MainTab2ListAdapter.HEADER, "", "null"));
                return abList;
            }

            // 검색 결과가 있을경우
           else {
                for (int i = 0; i < jCategoryData.length(); i++) {
                    // 2. 해당 카테고리에 있는 specItem들을 뽑아낸다.
                    JSONObject jCategoryItem = jCategoryData.getJSONObject(i);
                    MainTab2ListAdapter.Item cItem = new MainTab2ListAdapter.Item(MainTab2ListAdapter.HEADER, "", jCategoryItem.getString("category_name"));    // 카테고리는 id 필요 없다.
                    cItem.invisibleChildren = new ArrayList<>();

                    JSONArray jSpecList = jCategoryItem.getJSONArray("spec_item");
                    for (int j = 0; j < jSpecList.length(); j++) {
                        JSONObject jSpecItem = jSpecList.getJSONObject(j);
                        MainTab2ListAdapter.Item sItem = new MainTab2ListAdapter.Item(MainTab2ListAdapter.CHILD, jSpecItem.getString("spec_id"), jSpecItem.getString("spec_name"));     // 스펙아이템은 아이디가 필요.
                        cItem.invisibleChildren.add(sItem);
                    }
                    abList.add(cItem);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return abList;
    }

    // getSpecList
    public static ArrayList<Spec_mainList> JSONArray_getSpecList(String string) {
        ArrayList<Spec_mainList> abList = new ArrayList<>();
        try {
            Spec_mainList temp_item;
            JSONArray jData = new JSONObject(string).getJSONArray("spec_list");
            int item_num = jData.length();
            for (int i = 0; i < item_num; i++) {
                // 요소값 초기화
                temp_item = new Spec_mainList();

                JSONObject jObj = jData.getJSONObject(i);

                String setSpec_id = jObj.getString("setSpec_id");
                temp_item.setSpec_id(setSpec_id);

                String setSpec_detail_id = jObj.getString("setSpec_detail_id");
                temp_item.setSpec_detail_id(setSpec_detail_id);

                String setSpec_panbyel = jObj.getString("setSpec_panbyel");
                temp_item.setSpec_panbyel(setSpec_panbyel);

                int setDeadLine = jObj.getInt("setDeadLine");
                temp_item.setDeadLine(setDeadLine);

                String setSpec_name = jObj.getString("setSpec_name");
                temp_item.setSpec_name(setSpec_name);

                String setSpec_year = jObj.getString("setSpec_year");
                temp_item.setSpec_year(setSpec_year);

                String setSpec_year_number = jObj.getString("setSpec_year_number");
                temp_item.setSpec_year_number(setSpec_year_number);

                String setSpec_start_date = jObj.getString("setSpec_start_date");
                temp_item.setSpec_assign_start_date(setSpec_start_date);

                String setSpec_end_date = jObj.getString("setSpec_end_date");
                temp_item.setSpec_assign_end_date(setSpec_end_date);

                String setSpec_flag = jObj.getString("setSpec_flag");
                temp_item.setSpec_flag(setSpec_flag);

                abList.add(temp_item);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return abList;
    }

}
