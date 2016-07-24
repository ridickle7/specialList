package kr.co.yapp.speciallist.Main;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.yapp.speciallist.Helper.CustomerHelper;
import kr.co.yapp.speciallist.Helper.JSONParserHelper;
import kr.co.yapp.speciallist.Helper.NetworkHelper;
import kr.co.yapp.speciallist.Main.MainTab1.MainTabFragment1;
import kr.co.yapp.speciallist.Main.MainTab2.MainTab2ListAdapter;
import kr.co.yapp.speciallist.Main.MainTab2.MainTabFragment2;
import kr.co.yapp.speciallist.MyApplication;
import kr.co.yapp.speciallist.R;

public class MainActivity extends AppCompatActivity {
    public static final String TAB_INDEX_ON_SAVEDINST = "tabindex";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    private boolean firstBackKey = false;
    private Timer timer;

    private DrawerLayout dlDrawer;
    Toolbar toolbar;

    TabHost tabHost;
    ViewPager pager;
    TabsAdapter mAdapter;
    SearchView searchView;
    NavigationView navigationView;

    // Tab2의 recyclerview
    RecyclerView recyclerView;

    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        // STEP1> Setup Toolbar

        //setSupportActionBar((Toolbar) findViewById(R.id.toolbar));        //아래 두 줄과 같은 말
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_drawer);

        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayUseLogoEnabled(true);
        ab.setLogo(getResources().getDrawable(R.mipmap.bar_title));

        ab.setDisplayHomeAsUpEnabled(true);

        // STEP2> Setup Drawer
        {
            dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);

            View header = navigationView.getHeaderView(0);

            LinearLayout layout_home = (LinearLayout) header.findViewById(R.id.nav_home);
            layout_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "홈 클릭", Toast.LENGTH_SHORT).show();
                    dlDrawer.closeDrawers();
                }
            });

            LinearLayout layout_history = (LinearLayout) header.findViewById(R.id.nav_history);
            layout_history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "기록 클릭", Toast.LENGTH_SHORT).show();
                    dlDrawer.closeDrawers();
                }

            });


            LinearLayout layout_setting = (LinearLayout) header.findViewById(R.id.nav_setting);
            layout_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "세팅 클릭", Toast.LENGTH_SHORT).show();
                    dlDrawer.closeDrawers();
                }


            });

            LinearLayout layout_logout = (LinearLayout) header.findViewById(R.id.nav_logout);
            layout_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NetworkHelper.getInstance().logout(null, context, CustomerHelper.getInstance().getUserId(), CustomerHelper.getInstance().getUserPassword());
                    dlDrawer.closeDrawers();
                }
            });
        }

        // STEP3> Setup View Pager
        {
            tabHost = (TabHost) findViewById(R.id.tabHost);
            tabHost.setup(); // setup the tabhost.
            pager = (ViewPager) findViewById(R.id.pager);

            mAdapter = new TabsAdapter(this, getSupportFragmentManager(), tabHost, pager);
            addFragmentToPager(savedInstanceState);
        }


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
    }


    private void addFragmentToPager(Bundle savedInstanceState) {
        Resources res = getResources();

        ArrayList<View> tab_Indicator = new ArrayList<>();
        View temp;

        temp = LayoutInflater.from(this).inflate(R.layout.tab_icon, null);
        TextView temp_textView = (TextView) temp.findViewById(R.id.tab_title);
        temp_textView.setText("홈");
        temp_textView.setTypeface(MyApplication.typeface_bold);
        tab_Indicator.add(temp);

        temp = LayoutInflater.from(this).inflate(R.layout.tab_icon, null);
        temp_textView = (TextView) temp.findViewById(R.id.tab_title);
        temp_textView.setText("카테고리");
        temp_textView.setTypeface(MyApplication.typeface_bold);
        tab_Indicator.add(temp);

        mAdapter.addTab(tabHost.newTabSpec("first").setIndicator(tab_Indicator.get(0)),
                MainTabFragment1.class, null);


        mAdapter.addTab(tabHost.newTabSpec("second").setIndicator(tab_Indicator.get(1)),
                MainTabFragment2.class, null);

        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.WHITE);
        }

        if (savedInstanceState != null) {
            tabHost.setCurrentTab(savedInstanceState.getInt(TAB_INDEX_ON_SAVEDINST));
            mAdapter.onRestoreInstanceState(savedInstanceState);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//      noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {// handle click event on home icon(drawer icon)
            dlDrawer.openDrawer(GravityCompat.START);
            return true;
        }

        if (id == R.id.menu_reflash) {// handle click event on home icon(drawer icon)
            // MainTabFragment1.newInstance("", "").getSpecList(getApplicationContext(), 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        EditText searchView_setting = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        ImageView ivIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);

        searchView_setting.setTextSize((float) 13.333);                                                          // 검색뷰 글자 크기
        searchView_setting.setHintTextColor(getResources().getColor(R.color.searchView_textHint_Color));     // 검색뷰 힌트글자색
        searchView_setting.setTextColor(getResources().getColor(R.color.searchView_text_Color));              // 검색뷰 일반글자색

        searchView_setting.setBackground(getResources().getDrawable(R.drawable.searchview_back));
//        LayerDrawable bgDrawable = (LayerDrawable) searchView_setting.getBackground();
//        GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.shape_id);
//        shape.setColor(0xff6bc6ff);

        ivIcon.setImageResource(R.mipmap.icon_search);
        ivIcon.setRight(3);

        searchView.setQueryHint("어떤 스펙을 정복해볼까?");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                //Cursor search = dba.getRecBySearch(arg0);
                Toast.makeText(getApplicationContext(), arg0 + "", Toast.LENGTH_SHORT).show();

                pager.setCurrentItem(1);


                NetworkHelper.getInstance().SearchCategoryList(new NetworkHelper.VolleyCallback() {
                    @Override
                    public void onAction(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Response is: ", response.toString());
                        ArrayList<MainTab2ListAdapter.Item> temp;

                        //Toast.makeText(ctx, response.toString(), Toast.LENGTH_LONG).show();
                        try {
                            temp = JSONParserHelper.JSONArray_getCategoryList(new JSONObject(response.toString()));

                            recyclerView = (RecyclerView) findViewById(R.id.tab2_recyclerView);

                            MainTab2ListAdapter Tab2Adapter = new MainTab2ListAdapter(getApplicationContext(), temp);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(Tab2Adapter);
//                        recyclerView.setBackgroundColor(Color.parseColor("#ffffff"));
                            MyApplication.addExpandableFeatures(recyclerView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, context, arg0);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                return false;
            }
        });
        // 검색필드를 항상 표시하고싶을 경우false, 아이콘으로 보이고 싶을 경우 true
        return true;
    }


    //네비게이션 드로우어
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void onBackPressed() {
        if (firstBackKey == false) {
            Toast.makeText(this, "한 번 더 누를 시 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
            firstBackKey = true;


            TimerTask second = new TimerTask() {
                @Override
                public void run() {
                    timer.cancel();
                    timer = null;
                    firstBackKey = false;
                }
            };
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            timer = new Timer();
            timer.schedule(second, 2000);
        } else {
            super.onBackPressed();
        }
    }

    /* for font */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
