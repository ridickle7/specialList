package kr.co.yapp.speciallist.Detail;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import kr.co.yapp.speciallist.Helper.Alarm.AlarmBroadCast;
import kr.co.yapp.speciallist.Helper.CustomerHelper;
import kr.co.yapp.speciallist.Helper.JSONParserHelper;
import kr.co.yapp.speciallist.Helper.NetworkHelper;
import kr.co.yapp.speciallist.MyApplication;
import kr.co.yapp.speciallist.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link DetailDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DATALIST = "dateList";
    private static final String INTENT_ACTION = "kr.co.yapp.speciallist.alarm";

    // TODO: Rename and change types of parameters
    int start;
    int end;

    ToggleButton written, practical;
    LinearLayout flag_layout, cancelButton, OkButton;
    RequestQueue mQueue2;
    ArrayList<String> date_list;

    public DetailDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailDialogFragment newInstance() {
        DetailDialogFragment fragment = new DetailDialogFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            date_list = getArguments().getStringArrayList(ARG_DATALIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail_dialog, container, false);
        cancelButton = (LinearLayout) v.findViewById(R.id.dialog_layout_button_cancel);
        OkButton = (LinearLayout) v.findViewById(R.id.dialog_layout_button_ok);

        flag_layout = (LinearLayout) v.findViewById(R.id.flag);
        LinearLayout.LayoutParams layoutParams_MW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        flag_layout.setLayoutParams(layoutParams_MW);
        flag_layout.setGravity(Gravity.CENTER);

        written = new ToggleButton(getActivity());
        practical = new ToggleButton(getActivity());
        written.setChecked(false);
        flag_layout.addView(written);

        if(!TextUtils.isEmpty(MyApplication.SPEC_DETAIL_PRACTICAL_ID)){
            practical.setChecked(false);
            flag_layout.addView(practical);
        }

        final String spec_id = getArguments().getString("specId");

        //취소버튼
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //확인 버튼
        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(), "" + getArguments(), Toast.LENGTH_LONG).show();
                // 아무것도 체크 안함
                if(!written.isChecked() && !practical.isChecked()){
                    Toast.makeText(getActivity(), "종류를 최소 1가지 이상 선택하여 주십시요.", Toast.LENGTH_SHORT).show();
                }
                else if(written.isChecked() && !practical.isChecked()){    //필기만 선택
                    addSpecList(spec_id, MyApplication.SPEC_DETAIL_WRITTEN_ID, "");
                }
                else if(!written.isChecked() && practical.isChecked()){    //실기만 선택
                    addSpecList(spec_id, "", MyApplication.SPEC_DETAIL_PRACTICAL_ID);
                }
                else{    //필기만 선택
                    addSpecList(spec_id, MyApplication.SPEC_DETAIL_WRITTEN_ID, MyApplication.SPEC_DETAIL_PRACTICAL_ID);
                }

            }
        });

        return v;
    }


    public void addSpecList(final String spec_id, final String spec_detail_id, final String spec_detail_id2) {
        // /user/add
        final String URL_address = MyApplication.MAIN_SERVER_ADDRESS + MyApplication.SERVER_USER_ADD_SPEC;
        //mQueue2 = Volley.newRequestQueue(this);       //싱글턴 사용 안할 시
        mQueue2 = NetworkHelper.getInstance().mQueue;

        // Request a string response from the provided URL.
        // 2) Request Obejct인 StringRequest 생성
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Response is: ", response.toString());
                        //Toast.makeText(ctx, response.toString(), Toast.LENGTH_LONG).show();

                        ArrayList<String> receive_list = new ArrayList<>();
                        receive_list.add("msg");
                        String msg = JSONParserHelper.JSONArray_addSpec(response.toString(), receive_list);

                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        MyApplication.SPEC_DETAIL_WRITTEN_ID = "";
                        MyApplication.SPEC_DETAIL_PRACTICAL_ID = "";

                        if(spec_detail_id.equals(""))
                            start = 4;
                        else
                            start = 0;

                        if(spec_detail_id2.equals(""))
                            end = 3;
                        else
                            end = 7;


                        int original_start = (0 == start) ? 4:0;
                        int original_end = (7 == end) ? 3:7;

                        for (int i = start; i < end; i++) {
                            AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                            String[] YMD = MyApplication.getYMD(date_list.get(i));
                            //알람시간 calendar에 set해주기
                            Intent intent = new Intent(getActivity(), AlarmBroadCast.class);
                            PendingIntent sender = PendingIntent.getBroadcast(getActivity(), i, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Integer.parseInt(YMD[0]), Integer.parseInt(YMD[1])-1, Integer.parseInt(YMD[2]), 0, 0, 0);
//                            calendar.set(2016, 6, 2, 21, 41, 0);

                            //알람 예약
                            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                        }

                        // 데이터가 4개인 경우
                        if(date_list.size() == 4){
                            original_end = 4;
                        }
                        for (int i = original_start; i < original_end; i++) {
                            AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                            String[] YMD = MyApplication.getYMD(date_list.get(i));
                            //알람시간 calendar에 set해주기
                            Intent intent = new Intent(getActivity(), AlarmBroadCast.class);

                            PendingIntent sender = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);

                            //알람 예약
                            am.cancel(sender);
                            sender.cancel();
                        }

                        dismiss();
                        getActivity().finish();
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
                else{
                    Toast.makeText(getActivity(), MyApplication.network_fail1, Toast.LENGTH_SHORT).show();
                }
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(MyApplication.PARAMETER_USERID, CustomerHelper.getInstance().get_Id());
                map.put(MyApplication.PARAMETER_SPECID, spec_id);
                map.put(MyApplication.PARAMETER_SPECDETAILID, spec_detail_id);
                map.put(MyApplication.PARAMETER_SPECDETAILID2, spec_detail_id2);
                return map;
            }
        };
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        //mQueue2.add(stringRequest);   //싱글턴 사용 안할 시
        mQueue2.add(stringRequest);
    }


}
