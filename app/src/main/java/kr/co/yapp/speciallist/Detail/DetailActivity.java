package kr.co.yapp.speciallist.Detail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.co.yapp.speciallist.Comment.CommentActivity;
import kr.co.yapp.speciallist.Custom_Object.NewTextView;
import kr.co.yapp.speciallist.Helper.CustomerHelper;
import kr.co.yapp.speciallist.Helper.JSONParserHelper;
import kr.co.yapp.speciallist.Helper.NetworkHelper;
import kr.co.yapp.speciallist.MyApplication;
import kr.co.yapp.speciallist.R;

public class DetailActivity extends AppCompatActivity {

    Context context;

    Toolbar toolbar;
    private UpdateProgressBar updateProgressBar = null;
    LayoutInflater mInflater;

    Runnable mRunnable;
    Handler mHandler;
    Intent intent;
    int panbyel;

    String parameter_specId;
    View mDetailFormView;
    View mProgressView;

    String spec_name, spec_challenge_user, spec_instruction;
    ImageButton addSpec, commentList;
    NewTextView comment_good_number;
    Spec_Detail_Comment item_comment;

    ArrayList<String> date_list;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
            super.onCreate(savedInstanceState);
            setProgressBarIndeterminateVisibility(true);
            setContentView(R.layout.activity_detail);

            context = DetailActivity.this;
            date_list = new ArrayList<>();



//            mDlg = new ProgressDialog(context);
//            mDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            mDlg.setMessage("Start");
//            mDlg.show();

//        Toast.makeText(context, intent.getStringExtra(MyApplication.PARAMETER_SPECID) + " / " + intent.getStringExtra(MyApplication.PARAMETER_METHOD + ""), Toast.LENGTH_SHORT).show();

            viewById();
            showProgress(true);
            listener();


            {
                toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                final ActionBar ab = getSupportActionBar();

                ab.setDisplayShowTitleEnabled(false);
                ab.setDisplayUseLogoEnabled(true);
                ab.setLogo(getResources().getDrawable(R.mipmap.bar_title));
            }

            new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
                @Override
                public void run() {
                    // 실행할 동작 코딩
                    updateProgressBar = new UpdateProgressBar();
                    updateProgressBar.execute((Void)null);
//                    mHandler.sendEmptyMessage(0);	// 실행이 끝난후 알림
                }
            }, 1000);



        }

    void viewById() {
        mDetailFormView = findViewById(R.id.detail_form);
        mProgressView = findViewById(R.id.detail_progress);
        addSpec = (ImageButton) findViewById(R.id.addSpec);
        commentList = (ImageButton) findViewById(R.id.commentList);
    }

    void listener(){
        addSpec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailDialogFragment dialogFragment = DetailDialogFragment.newInstance();

                Bundle args = new Bundle();
                args.putString("specId", intent.getStringExtra("specId"));
                args.putString("specDetailId", intent.getStringExtra("specDetailId"));
                args.putStringArrayList("dateList", date_list);
                dialogFragment.setArguments(args);

                dialogFragment.setStyle(R.style.Theme_Dialog_Transparent, R.style.Theme_Dialog_Transparent);
                dialogFragment.show(getFragmentManager(), "TAG");
            }
        });

        commentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);

                intent.putExtra(MyApplication.PARAMETER_SPECID, parameter_specId);
                startActivity(intent);
            }
        });
    }

    // 여기 화면에서만 이용할 서버 통신이기 때문에 여기에서 구현
    public void getSpecItem(final String spec_id, final String spec_year, final String spec_year_number, final int panbyel) {
        // /user/add
        final String URL_address;
        if (panbyel == 0) {
            URL_address = MyApplication.MAIN_SERVER_ADDRESS + MyApplication.SERVER_SPEC_SEARCH_DETAIL;
        } else {
            URL_address = MyApplication.MAIN_SERVER_ADDRESS + MyApplication.SERVER_SPEC_RECENT_DETAIL;
        }
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

                        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                        MyApplication.Login_flag = 1;
                        JSONObject jObject = JSONParserHelper.StringToJSONObject(response.toString());
                        try {
                            spec_name = jObject.getJSONObject("spec_detail").getString("spec_name");
                            spec_challenge_user = jObject.getJSONObject("spec_detail").getString("spec_challenge_user") + "명 정복중";
                            spec_instruction = jObject.getJSONObject("spec_detail").getString("spec_instruction");

                            JSONArray array = jObject.getJSONObject("spec_detail").getJSONArray("spec_flag");
                            ArrayList<Spec_Detail_flag> itemList = JSONParserHelper.JSONArray_getSpecDetail_Flag(array);

                            LinearLayout flag_list = (LinearLayout) findViewById(R.id.flag_list);
                            LinearLayout cost_list = (LinearLayout) findViewById(R.id.cost_list);
                            LinearLayout.LayoutParams layoutParams_WW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            LinearLayout.LayoutParams layoutParams_WM = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            LinearLayout.LayoutParams layoutParams_MM = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


                            for (int i = 0; i < itemList.size(); i++) {
                                LinearLayout temp_layout = new LinearLayout(getApplicationContext());
                                temp_layout.setLayoutParams(layoutParams_MM);
                                flag_list.addView(temp_layout);

                                // 왼쪽 그림
                                LinearLayout left_layout = new LinearLayout(getApplicationContext());
                                left_layout.setLayoutParams(layoutParams_WM);
                                temp_layout.addView(left_layout);

                                ImageView left_image = new ImageView(getApplicationContext());
                                if (i == 0)
                                    left_image.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.icon1));
                                else
                                    left_image.setBackground(context.getResources().getDrawable(R.mipmap.icon2));
                                left_layout.addView(left_image);

                                // 오른쪽
                                LinearLayout right_layout = new LinearLayout(context);
                                right_layout.setLayoutParams(layoutParams_MM);
                                temp_layout.addView(right_layout);


                                View convertView = mInflater.inflate(R.layout.item_detail_date, null);

                                NewTextView tv_spec_assign_date = (NewTextView) convertView.findViewById(R.id.spec_assign_date);
                                NewTextView tv_spec_test_date = (NewTextView) convertView.findViewById(R.id.spec_test_date);
                                NewTextView tv_spec_result_date = (NewTextView) convertView.findViewById(R.id.spec_result_date);
                                NewTextView tv_spec_name = (NewTextView) findViewById(R.id.spec_name);
                                NewTextView tv_spec_challange_user = (NewTextView) findViewById(R.id.spec_challange_user);
                                NewTextView tv_spec_instruction = (NewTextView) findViewById(R.id.spec_instruction);

                                Spec_Detail_flag temp = itemList.get(i);
                                tv_spec_assign_date.setText(MyApplication.getDate(temp.getSpec_assign_start_date())[0] + " ~ " + MyApplication.getDate(temp.getSpec_assign_end_date())[0]);
                                tv_spec_test_date.setText(MyApplication.getDate(temp.getSpec_test_date())[0]);
                                tv_spec_result_date.setText(MyApplication.getDate(temp.getSpec_result_date())[0]);
                                tv_spec_name.setText(spec_name);
                                tv_spec_challange_user.setText(spec_challenge_user);
                                tv_spec_instruction.setText(spec_instruction);


                                date_list.add(MyApplication.getDate(temp.getSpec_assign_start_date())[0]);
                                date_list.add(MyApplication.getDate(temp.getSpec_assign_end_date())[0]);
                                date_list.add(MyApplication.getDate(temp.getSpec_test_date())[0]);
                                date_list.add(MyApplication.getDate(temp.getSpec_result_date())[0]);

                                layoutParams_MM.leftMargin = 5;
                                convertView.setLayoutParams(layoutParams_MM);
                                right_layout.addView(convertView);


                                // 시험 수수료 탭
                                ImageView image1 = new ImageView(context);
                                NewTextView text1 = new NewTextView(context);
                                if (i == 0) {
                                    image1.setBackground(context.getResources().getDrawable(R.mipmap.icon1));
                                    cost_list.addView(image1);

                                    layoutParams_WW.leftMargin = 10;
                                    text1.setLayoutParams(layoutParams_WW);
                                    text1.setText(temp.getSpec_cost());
                                    cost_list.addView(text1);
                                    MyApplication.SPEC_DETAIL_WRITTEN_ID = temp.getSpec_detail_id();
                                } else {
                                    image1.setBackground(context.getResources().getDrawable(R.mipmap.icon2));
                                    image1.setLayoutParams(layoutParams_WW);
                                    cost_list.addView(image1);

                                    text1.setLayoutParams(layoutParams_WW);
                                    text1.setText(temp.getSpec_cost());
                                    cost_list.addView(text1);
                                    MyApplication.SPEC_DETAIL_PRACTICAL_ID = temp.getSpec_detail_id();
                                }
                            }

                            // 댓글
                            item_comment = JSONParserHelper.JSONArray_getSpecDetail_Comment(jObject.getJSONObject("spec_detail").getJSONObject("spec_current_comment"));

                            LinearLayout comment_list = (LinearLayout) findViewById(R.id.comment_list);
                            if (item_comment.getCommentId().equals("null")) {

                                NewTextView text = new NewTextView(context);
                                text.setText("댓글이 없습니다.");
                                text.setGravity(Gravity.CENTER);
                                text.setTextSize(17);
                                comment_list.addView(text);

                            } else {
                                View convertView = mInflater.inflate(R.layout.list_comment, null);

                                NewTextView comment_writer = (NewTextView) convertView.findViewById(R.id.comment_writer);
                                NewTextView comment_value = (NewTextView) convertView.findViewById(R.id.comment_value);
                                NewTextView comment_time = (NewTextView) convertView.findViewById(R.id.comment_time);
                                ToggleButton comment_good_toogle = (ToggleButton) convertView.findViewById(R.id.comment_good_toogle);
                                NewTextView comment_good_number = (NewTextView) convertView.findViewById(R.id.comment_good_number);

                                comment_writer.setText(item_comment.getCommentWriterId());
                                comment_value.setText(item_comment.getCommentValue());
                                comment_time.setText(MyApplication.getDate(item_comment.getCommentTime())[0] + " " + MyApplication.getDate(item_comment.getCommentTime())[1]);
                                comment_good_toogle.setChecked(item_comment.getCommentIsgood());
                                comment_good_number.setText(item_comment.getCommentGood() + "");

                                comment_good_toogle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked) {
                                            comment_good(item_comment.getCommentId());
                                        } else {
                                            comment_ungood(item_comment.getCommentId());
                                        }
                                    }
                                });

                                comment_list.addView(convertView);
                            }

                            comment_list.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    CommentFragment commentFragment = CommentFragment.newInstance();
//                                    Bundle args = new Bundle();
//                                    args.putString(MyApplication.PARAMETER_SPECID, parameter_specId);
//                                    commentFragment.setArguments(args);
//                                    //showMenuSheet(MenuSheetView.MenuType.LIST);
//                                    commentFragment.show(getSupportFragmentManager(), R.id.bottomsheet);
                                }
                            });
                            //시험 후기 탭 (최근 댓글 1개만 보임)
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
                NetworkResponse networkResponse = error.networkResponse;
                Toast.makeText(MyApplication.getAppContext(), MyApplication.network_fail2, Toast.LENGTH_SHORT).show();

                mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (panbyel == 0) {  // 메인 리스트 아이템 클릭했을 경우
                            getSpecItem(intent.getStringExtra(MyApplication.PARAMETER_SPECID), intent.getStringExtra("specYear"), intent.getStringExtra("specYearNumber"), panbyel);
                        } else if (panbyel == 1) {  // 카테고리 탭 아이템 클릭했을 경우
                            getSpecItem(intent.getStringExtra(MyApplication.PARAMETER_SPECID), "", "", panbyel);
                        } else {
                            Toast.makeText(context, "치명적인 오류입니다. 앱을 재실행하여 주십시요.", Toast.LENGTH_LONG).show();
                        }
                    }
                };

                if (networkResponse != null && networkResponse.statusCode == 401) {
                    // HTTP Status Code: 401 Unauthorized
                    Toast.makeText(MyApplication.getAppContext(), "ID 혹은 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    mHandler = new Handler();
                    mHandler.postDelayed(mRunnable, 10000);
                }
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                if (panbyel == 0) {
                    map.put(MyApplication.PARAMETER_USERID, CustomerHelper.getInstance().get_Id());
                    map.put(MyApplication.PARAMETER_SPECID, spec_id);
                    map.put(MyApplication.PARAMETER_SPECYEAR, spec_year);
                    map.put(MyApplication.PARAMETER_SPECYEARNUMBER, spec_year_number);
                    Log.d("map : ", CustomerHelper.getInstance().get_Id() + " " + spec_id + " " + spec_year + " " + spec_year_number);
                    return map;
                } else {
                    map.put(MyApplication.PARAMETER_USERID, CustomerHelper.getInstance().get_Id());
                    map.put(MyApplication.PARAMETER_SPECID, spec_id);
                    Log.d("map : ", CustomerHelper.getInstance().get_Id() + " " + spec_id);
                    return map;
                }
            }
        };
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        //mQueue2.add(stringRequest);   //싱글턴 사용 안할 시
        mQueue2.add(stringRequest);
    }

    public void comment_good(final String comment_Id) {
        // /user/add
        final String URL_address = MyApplication.MAIN_SERVER_ADDRESS + MyApplication.SERVER_COMMENT_GOOD;
        //mQueue2 = Volley.newRequestQueue(this);       //싱글턴 사용 안할 시
        RequestQueue mQueue = NetworkHelper.getInstance().mQueue;

        // Request a string response from the provided URL.
        // 2) Request Obejct인 StringRequest 생성
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Response is: ", response.toString());
                        //Toast.makeText(ctx, response.toString(), Toast.LENGTH_LONG).show();


                        item_comment.setCommentGood(item_comment.getCommentGood() + 1);
                        comment_good_number.setText(item_comment.getCommentGood() + "");
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
        mQueue.add(stringRequest);
    }

    public void comment_ungood(final String comment_Id) {
        // /user/add
        final String URL_address = MyApplication.MAIN_SERVER_ADDRESS + MyApplication.SERVER_COMMENT_UNGOOD;
        //mQueue2 = Volley.newRequestQueue(this);       //싱글턴 사용 안할 시
        RequestQueue mQueue = NetworkHelper.getInstance().mQueue;


        // Request a string response from the provided URL.
        // 2) Request Obejct인 StringRequest 생성
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Response is: ", response.toString());
                        //Toast.makeText(ctx, response.toString(), Toast.LENGTH_LONG).show();


                        item_comment.setCommentGood(item_comment.getCommentGood() - 1);
                        comment_good_number.setText(item_comment.getCommentGood() + "");
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
        mQueue.add(stringRequest);
    }


    public class UpdateProgressBar extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            // Simulate network access.
            intent = getIntent();
            panbyel = intent.getIntExtra("method", -1);
            parameter_specId = intent.getStringExtra(MyApplication.PARAMETER_SPECID);

            if (panbyel == 0) {  // 메인 리스트 아이템 클릭했을 경우
                getSpecItem(intent.getStringExtra(MyApplication.PARAMETER_SPECID), intent.getStringExtra("specYear"), intent.getStringExtra("specYearNumber"), panbyel);
                return true;
            } else if (panbyel == 1) {  // 카테고리 탭 아이템 클릭했을 경우
                getSpecItem(intent.getStringExtra(MyApplication.PARAMETER_SPECID), "", "", panbyel);
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "치명적인 오류입니다. 앱을 재실행하여 주십시요.", Toast.LENGTH_LONG).show();
                return false;
            }
            // TODO: register the new account here.
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success.equals(true)) {
                showProgress(false);
//                mDlg.dismiss();
            }
            else{
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

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mDetailFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mDetailFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mDetailFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mDetailFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public void onPause() {
        Log.d(this.getClass().getSimpleName(), "onPause()");
        super.onPause();
        if(mHandler != null)
            mHandler.removeCallbacks(mRunnable);
    }


    @Override
    public void onRestart() {
        super.onRestart();
//        if (panbyel == 0) {  // 메인 리스트 아이템 클릭했을 경우
//            getSpecItem(intent.getStringExtra(MyApplication.PARAMETER_SPECID), intent.getStringExtra("specYear"), intent.getStringExtra("specYearNumber"), panbyel);
//        } else if (panbyel == 1) {  // 카테고리 탭 아이템 클릭했을 경우
//            getSpecItem(intent.getStringExtra(MyApplication.PARAMETER_SPECID), "", "", panbyel);
//        } else {
//            Toast.makeText(getApplicationContext(), "치명적인 오류입니다. 앱을 재실행하여 주십시요.", Toast.LENGTH_LONG).show();
//        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.SPEC_DETAIL_WRITTEN_ID = "";
        MyApplication.SPEC_DETAIL_PRACTICAL_ID = "";
        if (mHandler != null)
            mHandler.removeCallbacks(mRunnable);
    }
}
