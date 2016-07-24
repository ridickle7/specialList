package kr.co.yapp.speciallist.Comment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.co.yapp.speciallist.Detail.Spec_Detail_Comment;
import kr.co.yapp.speciallist.Helper.CustomerHelper;
import kr.co.yapp.speciallist.Helper.JSONParserHelper;
import kr.co.yapp.speciallist.Helper.NetworkHelper;
import kr.co.yapp.speciallist.MyApplication;
import kr.co.yapp.speciallist.R;

public class CommentActivity extends AppCompatActivity {
    Intent intent;
    String spec_Id;
    Context context;

    View mCommentFormView;
    View mProgressView;
    private CommentProgressBar commentProgressBar = null;

    Button addComment;
    EditText input_comment;
    RatingBar ratingBar;

    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<Spec_Detail_Comment> singleComments;
    private CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        context = CommentActivity.this;
        intent = getIntent();
        spec_Id = intent.getStringExtra(MyApplication.PARAMETER_SPECID);

        viewById();

        {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            final ActionBar ab = getSupportActionBar();

            ab.setDisplayShowTitleEnabled(false);
            ab.setDisplayUseLogoEnabled(true);
            ab.setLogo(getResources().getDrawable(R.mipmap.bar_title));
        }

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment(CustomerHelper.getInstance().get_Id(), input_comment.getText().toString(), "3.7");
            }
        });

        showProgress(true);
        new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
            @Override
            public void run() {
                // 실행할 동작 코딩
                commentProgressBar = new CommentProgressBar();
                commentProgressBar.execute((Void) null);
//                    mHandler.sendEmptyMessage(0);	// 실행이 끝난후 알림
            }
        }, 1000);
    }

    public void viewById() {
        recyclerView = (RecyclerView) findViewById(R.id.commentList);
        LinearLayoutManager layoutManager = new GridLayoutManager(context, 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        mCommentFormView = findViewById(R.id.comment_form);
        mProgressView = findViewById(R.id.comment_progress);

        addComment = (Button) findViewById(R.id.addComment);
        input_comment = (EditText) findViewById(R.id.input_comment);
        ratingBar = (RatingBar) findViewById(R.id.input_rating);
    }

    public class CommentProgressBar extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            // Simulate network access.
            getCommentList(spec_Id, "0");
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success.equals(true)) {
                showProgress(false);
//                mDlg.dismiss();
            } else {
                Toast.makeText(context, "치명적인 오류입니다. 앱을 재실행하여 주십시요.", Toast.LENGTH_LONG).show();
                finish();
            }
        }

        @Override
        protected void onCancelled() {
//            mDlg.dismiss();
            Toast.makeText(context, "치명적인 오류입니다. 앱을 재실행하여 주십시요.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mCommentFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mCommentFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCommentFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mCommentFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void getCommentList(final String spec_id, final String page) {
        // /user/add
        final String URL_address = MyApplication.MAIN_SERVER_ADDRESS + MyApplication.SERVER_COMMENT;
        //mQueue2 = Volley.newRequestQueue(this);       //싱글턴 사용 안할 시
        RequestQueue mQueue2 = NetworkHelper.getInstance().mQueue;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Response is: ", response.toString());
//                        Toast.makeText(ctx, response.toString(), Toast.LENGTH_LONG).show();
                        try {
                            String specId = JSONParserHelper.StringToJSONObject(response.toString()).getJSONObject("comments").getString(MyApplication.PARAMETER_SPECID);
                            int commentTotalCnt = JSONParserHelper.StringToJSONObject(response.toString()).getJSONObject("comments").getInt(MyApplication.PARAMETER_TOTALCOMMENTCNT);
                            singleComments = JSONParserHelper.JSONArray_getCommentList(response.toString());

                            adapter = new CommentAdapter(context, R.layout.list_comment, singleComments);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
                NetworkResponse networkResponse = error.networkResponse;
//                if (networkResponse != null && networkResponse.statusCode == 401) {
//                    // HTTP Status Code: 401 Unauthorized
//                }
//                else{
//                    Toast.makeText(context, MyApplication.network_fail1, Toast.LENGTH_SHORT).show();
//                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put(MyApplication.PARAMETER_LOGINID, CustomerHelper.getInstance().getUserId());
                map.put(MyApplication.PARAMETER_SPECID, spec_id);
                map.put(MyApplication.PARAMETER_PAGE, page);
                return map;
            }
        };
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        //mQueue2.add(stringRequest);   //싱글턴 사용 안할 시
        mQueue2.add(stringRequest);
    }

    public void addComment(final String spec_id, final String value, final String rating) {
        // /user/add
        final String URL_address = MyApplication.MAIN_SERVER_ADDRESS + MyApplication.SERVER_COMMENT_ADD;
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

                        ArrayList<Spec_Detail_Comment> temp_comment = JSONParserHelper.JSONArray_getCommentList(response.toString());
                        singleComments.add(temp_comment.get(0));
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == 401) {
                    // HTTP Status Code: 401 Unauthorized
                }
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put(MyApplication.PARAMETER_LOGINID, CustomerHelper.getInstance().getUserId());
                map.put(MyApplication.PARAMETER_SPECID, spec_id);
                map.put(MyApplication.PARAMETER_VALUE, value);
                map.put(MyApplication.PARAMETER_RATING, rating);
                return map;
            }
        };
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        //mQueue2.add(stringRequest);   //싱글턴 사용 안할 시
        mQueue2.add(stringRequest);
    }

}
