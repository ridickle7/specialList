package kr.co.yapp.speciallist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        context = DetailActivity.this;

        Intent intent = getIntent();
        Toast.makeText(context, intent.getStringExtra(MyApplication.PARAMETER_SPECID) + " / " + intent.getStringExtra(MyApplication.PARAMETER_METHOD + ""), Toast.LENGTH_SHORT).show();


    }
}
