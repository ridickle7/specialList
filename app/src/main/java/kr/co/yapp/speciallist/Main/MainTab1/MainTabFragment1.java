package kr.co.yapp.speciallist.Main.MainTab1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.yapp.speciallist.Helper.JSONParserHelper;
import kr.co.yapp.speciallist.Helper.NetworkHelper;
import kr.co.yapp.speciallist.MyApplication;
import kr.co.yapp.speciallist.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainTabFragment1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainTabFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainTabFragment1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    Context context;
    Handler mHandler;
    Runnable mRunnable;

    static RecyclerView recyclerView;
    ArrayList<Spec_mainList> spec_list;
    MainTab1ListAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MainTabFragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainTabFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static MainTabFragment1 newInstance(String param1, String param2) {
        MainTabFragment1 fragment = new MainTabFragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_main_tab_fragment1, container, false);

        recyclerView = (RecyclerView) convertView.findViewById(R.id.tab1_recyclerView);
        LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        spec_list = new ArrayList<>();

        return convertView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        playGetSpecList();
    }

    public void playGetSpecList(){
        NetworkHelper.getInstance().getSpecList(new NetworkHelper.VolleyCallback() {
            @Override
            public void onAction(String response) {
                try {
                    //Toast.makeText(ctx, response.toString(), Toast.LENGTH_LONG).show();
                    if (new JSONObject(response).getString("msg").equals(MyApplication.network_fail2)) {
                        mRunnable = new Runnable() {
                            @Override
                            public void run() {
                                playGetSpecList();
                            }
                        };
                        mHandler = new Handler();
                        mHandler.postDelayed(mRunnable, 10000);
                    }
                    else {
                        spec_list = JSONParserHelper.JSONArray_getSpecList(response.toString());
                        adapter = new MainTab1ListAdapter(context, R.layout.list_maintab1fragment, spec_list);
                        recyclerView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getActivity(), 0);
    }

    @Override
    public void onPause() {
        Log.d(this.getClass().getSimpleName(), "onPause()");
        super.onPause();
        if (mHandler != null)
            mHandler.removeCallbacks(mRunnable);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null)
            mHandler.removeCallbacks(mRunnable);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
