package kr.co.yapp.speciallist.Main.MainTab1;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.co.yapp.speciallist.Detail.DetailActivity;
import kr.co.yapp.speciallist.MyApplication;
import kr.co.yapp.speciallist.R;

/**
 * Created by home on 2016-06-15.
 */
public class MainTab1ListAdapter extends RecyclerView.Adapter<MainTab1ListAdapter.MainViewHolder> {
    Context context;
    int item_layout;
    View rootView;
    ArrayList<Spec_mainList> dataSource;
    LayoutInflater mInflater;

    public MainTab1ListAdapter(Context ctx, int item_resId, ArrayList<Spec_mainList> data) {
        context = ctx;
        item_layout = item_resId;
        dataSource = data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if(viewType == dataSource.size()-1){
            View convertView = mInflater.inflate(R.layout.list_maintab1fragment_null, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context.getApplicationContext(), "일단 만든다", Toast.LENGTH_SHORT).show();
                }
            });
            return new ViewHolder_null(convertView);
        }
        else {
            rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_maintab1fragment, null);
            return new ViewHolder(rootView);
        }
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(MainViewHolder parameter_holder, int position) {
        final Spec_mainList item = dataSource.get(position);

        if (parameter_holder.getItemViewType() == dataSource.size()-1) {
            View convertView = mInflater.inflate(R.layout.list_maintab1fragment_null, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context.getApplicationContext(), "일단 만든다", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            ViewHolder holder = (ViewHolder) parameter_holder;
            String panbyel = dataSource.get(position).getSpec_panbyel();

            holder.spec_panbyel.setBackground(context.getResources().getDrawable(R.drawable.main_fragment1_item_background));

            if (panbyel.equals("신청 시작일까지")) {
                //holder.spec_panbyel.setBackgroundColor(context.getResources().getColor(R.color.lightsteelblue));
                holder.spec_dDay.setBackgroundColor(context.getResources().getColor(R.color.flag_assign_start));
                //holder.spec_panbyel.setBackgroundColor(context.getResources().getColor(R.color.flag_assign_start));
                LayerDrawable bgDrawable = (LayerDrawable)holder.spec_panbyel.getBackground();
                GradientDrawable shape = (GradientDrawable)   bgDrawable.findDrawableByLayerId(R.id.shape_id);
                shape.setColor(0xff465966);

            } else if (panbyel.equals("시험 시작일까지")) {
                //holder.spec_panbyel.setBackgroundColor(context.getResources().getColor(R.color.outline_color_dialog_button));
                holder.spec_dDay.setBackgroundColor(context.getResources().getColor(R.color.flag_start));
                // holder.spec_panbyel.setBackgroundColor(context.getResources().getColor(R.color.flag_assign_start));
                LayerDrawable bgDrawable = (LayerDrawable)holder.spec_panbyel.getBackground();
                GradientDrawable shape = (GradientDrawable)   bgDrawable.findDrawableByLayerId(R.id.shape_id);
                shape.setColor(0xffff3535);
            } else if (panbyel.equals("결과 발표일까지")) {
                //holder.spec_panbyel.setBackgroundColor(context.getResources().getColor(R.color.bg_color_button_pressed));
                holder.spec_dDay.setBackgroundColor(context.getResources().getColor(R.color.flag_result_start));
                //holder.spec_panbyel.setBackgroundColor(context.getResources().getColor(R.color.flag_assign_start));
                LayerDrawable bgDrawable = (LayerDrawable)holder.spec_panbyel.getBackground();
                GradientDrawable shape = (GradientDrawable)   bgDrawable.findDrawableByLayerId(R.id.shape_id);
                shape.setColor(0xffffff35);
            }

            if (dataSource.get(position).getSpec_flag().equals("필기")) {
                holder.spec_flag.setBackground(context.getResources().getDrawable(R.mipmap.icon1));
            } else if (dataSource.get(position).getSpec_flag().equals("실기")) {
                holder.spec_flag.setBackground(context.getResources().getDrawable(R.mipmap.icon2));
            } else {
                holder.spec_flag.setBackground(context.getResources().getDrawable(R.mipmap.icon1));
            }

            holder.spec_panbyel.setText(panbyel);
            holder.spec_dDay.setText(dataSource.get(position).getDeadLine() + "");
            holder.spec_name.setText(dataSource.get(position).getSpec_name());
            holder.spec_test_date.setText(MyApplication.getDate(dataSource.get(position).getSpec_assign_start_date())[0]);
        }
    }

    @Override
    public int getItemCount() {
        return this.dataSource.size();
    }


    public class ViewHolder extends MainViewHolder {
        TextView spec_panbyel;
        TextView spec_dDay;
        ImageView spec_flag;
        TextView spec_name;
        TextView spec_test_date;

        public ViewHolder(View convertView) {
            super(convertView);
            spec_panbyel = (TextView) convertView.findViewById(R.id.spec_panbyel);
            spec_dDay = (TextView) convertView.findViewById(R.id.spec_dDay);
            spec_flag = (ImageView) convertView.findViewById(R.id.spec_flag);
            spec_name = (TextView) convertView.findViewById(R.id.spec_name);
            spec_test_date = (TextView) convertView.findViewById(R.id.spec_test_date);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(this., "출력됬다" + position, Toast.LENGTH_LONG);
                    int position = getPosition();
                    Log.v("출력됬다", position + "");
                    Intent intent = new Intent(context.getApplicationContext(), DetailActivity.class);
                    intent.putExtra("method", MyApplication.METHOD_HOME_DETAIL);
                    intent.putExtra("specId", dataSource.get(position).getSpec_id());
                    intent.putExtra("specDetailId", dataSource.get(position).getSpec_detail_id());
                    intent.putExtra("specYear", dataSource.get(position).getSpec_year());
                    intent.putExtra("specYearNumber", dataSource.get(position).getSpec_year_number());
                    intent.putExtra(MyApplication.PARAMETER_METHOD, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }

    public class ViewHolder_null extends MainViewHolder {
        public ViewHolder_null(View convertView){
            super(convertView);
        }
        ImageView image;
    }

    public class MainViewHolder extends  RecyclerView.ViewHolder {
        public MainViewHolder(View v) {
            super(v);
        }
    }

}
