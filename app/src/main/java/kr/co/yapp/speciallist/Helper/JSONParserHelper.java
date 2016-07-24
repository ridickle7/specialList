package kr.co.yapp.speciallist.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.yapp.speciallist.Detail.Spec_Detail_Comment;
import kr.co.yapp.speciallist.Detail.Spec_Detail_flag;
import kr.co.yapp.speciallist.Main.MainTab1.Spec_mainList;
import kr.co.yapp.speciallist.Main.MainTab2.MainTab2ListAdapter;
import kr.co.yapp.speciallist.MyApplication;

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

    // getSpecItem (Flag)
    public static ArrayList<Spec_Detail_flag> JSONArray_getSpecDetail_Flag(JSONArray jData) {
        ArrayList<Spec_Detail_flag> abList = new ArrayList<>();
        try {
            Spec_Detail_flag temp_item;
            int item_num = jData.length();

            for (int i = 0; i < item_num; i++) {
                // 요소값 초기화
                temp_item = new Spec_Detail_flag();

                JSONObject jObj = jData.getJSONObject(i);

                String spec_id = jObj.getString("spec_id");
                temp_item.setSpec_detail_id(spec_id);

                String spec_flag = jObj.getString("spec_flag");
                temp_item.setSpec_flag(spec_flag);

                String spec_assign_start_date = jObj.getString("spec_assign_start_date");
                temp_item.setSpec_assign_start_date(spec_assign_start_date);

                String spec_assign_end_date = jObj.getString("spec_assign_end_date");
                temp_item.setSpec_assign_end_date(spec_assign_end_date);

                String spec_test_date = jObj.getString("spec_test_date");
                temp_item.setSpec_test_date(spec_test_date);

                String spec_result_date = jObj.getString("spec_result_date");
                temp_item.setSpec_result_date(spec_result_date);

                String spec_cost = jObj.getString("spec_cost");
                temp_item.setSpec_cost(spec_cost);

                abList.add(temp_item);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return abList;
    }

    public static String JSONArray_addSpec(String response, ArrayList<String> receive_list) {
        String msg = "";
        try {
            JSONArray jArray = StringToJSONObject(response).getJSONArray(receive_list.get(0));
            for (int i = 0; i < jArray.length(); i++) {
                if (jArray.getString(i).equals(MyApplication.PARAMETER_ADDSPEC_SUCCESS)) {
                    if (jArray.length() == 1) {   //단일 자격증만 있는 경우
                        msg = msg + "";
                    } else if (i == 0) {            //필기 추가 완료
                        msg = msg + "필기 ";
                    } else {                       //실기 추가 완료
                        msg = msg + "실기 ";
                        msg = msg + "일정 추가가 완료되었습니다.";
                    }
                } else {
                    if (jArray.length() == 1) {   // 필기 이미 추가된 자료
                        msg = msg + "이미 추가 되어있는 스펙입니다.";
                    } else if (i == 1 && msg.equals("")) {   // 실기 이미 추가된 자료
                        msg = msg + "이미 추가 되어있는 스펙입니다.";
                    } else if (i == 1 && !msg.equals("")) {   // 필기가 아미 추가되어있는 경우
                        msg = msg + "일정 추가가 완료되었습니다.";
                    }
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return msg;
    }

    // getSpecItem (Comment)
    public static Spec_Detail_Comment JSONArray_getSpecDetail_Comment(JSONObject jData) {
        Spec_Detail_Comment comment_item = new Spec_Detail_Comment();
        try {
            String comment_id = jData.getString("comment_id");
            comment_item.setCommentId(comment_id);

            String comment_writer = jData.getString("comment_writer");
            comment_item.setCommentWriterId(comment_writer);

            String comment_spec = jData.getString("comment_spec");
            comment_item.setCommentSpec(comment_spec);

            String comment_value = jData.getString("comment_value");
            comment_item.setCommentValue(comment_value);

            Boolean comment_isgood = jData.getBoolean("comment_isgood");
            comment_item.setCommentIsgood(comment_isgood);

            // 댓글이 0개일 경우
            if (jData.getString("comment_good").equals("null")) {
                comment_item.setCommentGood(0);
            } else {
                int comment_good = Integer.parseInt(jData.getString("comment_good"));
                comment_item.setCommentGood(comment_good);
            }

            String comment_time = jData.getString("comment_time");
            comment_item.setCommentTime(comment_time);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return comment_item;
    }

    public static ArrayList<Spec_Detail_Comment> JSONArray_getCommentList(String response) {
        ArrayList<Spec_Detail_Comment> abList = new ArrayList<>();
        try {
            JSONArray jArray = StringToJSONObject(response).getJSONObject("comments").getJSONArray(MyApplication.PARAMETER_SINGLECOMMENTS);
            for (int i = 0; i < jArray.length(); i++) {

                Spec_Detail_Comment commentItem = new Spec_Detail_Comment();
                JSONObject CommentData = jArray.getJSONObject(i);

                String _id = CommentData.getString("comment_id");
                commentItem.setCommentId(_id);

                float rating = (float) CommentData.getDouble("comment_rating");
                commentItem.setCommentRating(rating);

                String comment = CommentData.getString("comment_value");
                commentItem.setCommentValue(comment);

                String writerId = CommentData.getString("comment_writer");
                commentItem.setCommentWriterId(writerId);

                int likedCnt = CommentData.getInt("comment_good");
                commentItem.setCommentGood(likedCnt);

                String isBest = CommentData.getString("comment_isbest");
                commentItem.setCommentIsBest(isBest);

                String isLiking = CommentData.getString("comment_isgood");
                if(!isLiking.equals(""))
                    commentItem.setCommentIsgood(Boolean.parseBoolean(isLiking));
                else
                    commentItem.setCommentIsgood(null);

                String commentDate = CommentData.getString("comment_time");
                commentItem.setCommentTime(MyApplication.getDate(commentDate)[0] + " " + MyApplication.getDate(commentDate)[1]);

                abList.add(commentItem);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return abList;
    }

}
