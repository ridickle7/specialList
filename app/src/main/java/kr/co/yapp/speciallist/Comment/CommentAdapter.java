package kr.co.yapp.speciallist.Comment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.co.yapp.speciallist.Detail.Spec_Detail_Comment;
import kr.co.yapp.speciallist.Helper.CustomerHelper;
import kr.co.yapp.speciallist.Helper.JSONParserHelper;
import kr.co.yapp.speciallist.Helper.NetworkHelper;
import kr.co.yapp.speciallist.MyApplication;
import kr.co.yapp.speciallist.R;

/**
 * Created by home on 2016-06-21.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MainViewHolder> {

    Context context;
    int item_layout;
    ArrayList<Spec_Detail_Comment> dataSource;
    LayoutInflater mInflater;

    public CommentAdapter(Context ctx, int item_resId, ArrayList<Spec_Detail_Comment> data) {
        context = ctx;
        item_layout = item_resId;
        dataSource = data;

        mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(item_layout, null);
        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(MainViewHolder parameter_holder, final int position) {
        final Spec_Detail_Comment item = dataSource.get(position);

        final ViewHolder holder = (ViewHolder) parameter_holder;

        String[] dateList = MyApplication.getDate(item.getCommentTime());
        holder.comment_writer.setText(item.getCommentWriterId());
        holder.ratingBar.setMax(5);
        holder.ratingBar.setRating(item.getCommentRating());
        holder.comment_value.setText(item.getCommentValue());
        holder.comment_time.setText(dateList[0] + " " + dateList[1]);
        holder.comment_good_toogle.setChecked(item.getCommentIsgood());
        holder.comment_good_number.setText(item.getCommentGood() + "");

        holder.comment_good_toogle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    item.setCommentGood(item.getCommentGood() + 1);
                    holder.comment_good_number.setText(item.getCommentGood() + "");
                    comment_good(item.getCommentId());
                } else {
                    item.setCommentGood(item.getCommentGood() - 1);
                    holder.comment_good_number.setText(item.getCommentGood() + "");
                    comment_ungood(item.getCommentId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.dataSource.size();
    }


    public class ViewHolder extends MainViewHolder {
        TextView comment_writer;
        RatingBar ratingBar;
        TextView comment_value;
        TextView comment_time;
        ToggleButton comment_good_toogle;
        TextView comment_good_number;

        public ViewHolder(View convertView) {
            super(convertView);
            comment_writer = (TextView) convertView.findViewById(R.id.comment_writer);
            ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            comment_value = (TextView) convertView.findViewById(R.id.comment_value);
            comment_time = (TextView) convertView.findViewById(R.id.comment_time);
            comment_good_toogle = (ToggleButton) convertView.findViewById(R.id.comment_good_toogle);
            comment_time = (TextView) convertView.findViewById(R.id.comment_good_number);
            comment_good_number = (TextView) convertView.findViewById(R.id.comment_good_number);
        }
    }

    public class ViewHolder_null extends MainViewHolder {
        public ViewHolder_null(View convertView) {
            super(convertView);
        }

        ImageView image;
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        public MainViewHolder(View v) {
            super(v);
        }
    }


    public void comment_good(final String comment_Id) {
        // /user/add
        final String URL_address = MyApplication.MAIN_SERVER_ADDRESS + MyApplication.SERVER_COMMENT_GOOD;
        //mQueue2 = Volley.newRequestQueue(this);       //싱글턴 사용 안할 시
        RequestQueue mQueue2 = NetworkHelper.getInstance().mQueue;

        // Request a string response from the provided URL.
        // 2) Request Obejct인 StringRequest 생성
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Response is: ", response.toString());
                        //Toast.makeText(ctx, response.toString(), Toast.LENGTH_LONG).show();


                        JSONObject jObject = JSONParserHelper.StringToJSONObject(response.toString());
                        try {
                            String value = jObject.getString("msg");
                            Log.d("MainActivity 231 : ", value);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == 401) {
                    // HTTP Status Code: 401 Unauthorized
                    Toast.makeText(MyApplication.getAppContext(), "ID 혹은 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put(MyApplication.PARAMETER_USERID, CustomerHelper.getInstance().getUserId());
                map.put(MyApplication.PARAMETER_COMMENTID, comment_Id);
                return map;
            }
        };
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        //mQueue2.add(stringRequest);   //싱글턴 사용 안할 시
        mQueue2.add(stringRequest);
    }

    public void comment_ungood(final String comment_Id) {
        // /user/add
        final String URL_address = MyApplication.MAIN_SERVER_ADDRESS + MyApplication.SERVER_COMMENT_UNGOOD;
        //mQueue2 = Volley.newRequestQueue(this);       //싱글턴 사용 안할 시
        RequestQueue mQueue2 = NetworkHelper.getInstance().mQueue;

        // Request a string response from the provided URL.
        // 2) Request Obejct인 StringRequest 생성
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Response is: ", response.toString());
                        //Toast.makeText(ctx, response.toString(), Toast.LENGTH_LONG).show();


                        JSONObject jObject = JSONParserHelper.StringToJSONObject(response.toString());
                        try {
                            String value = jObject.getString("msg");
                            Log.d("MainActivity 231 : ", value);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == 401) {
                    // HTTP Status Code: 401 Unauthorized
                    Toast.makeText(MyApplication.getAppContext(), "ID 혹은 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put(MyApplication.PARAMETER_USERID, CustomerHelper.getInstance().getUserId());
                map.put(MyApplication.PARAMETER_COMMENTID, comment_Id);
                return map;
            }
        };
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        //mQueue2.add(stringRequest);   //싱글턴 사용 안할 시
        mQueue2.add(stringRequest);
    }
}
