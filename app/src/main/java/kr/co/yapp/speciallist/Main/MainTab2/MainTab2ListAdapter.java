package kr.co.yapp.speciallist.Main.MainTab2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.co.yapp.speciallist.Custom_Object.Tab2ChildView;
import kr.co.yapp.speciallist.DetailActivity;
import kr.co.yapp.speciallist.MyApplication;
import kr.co.yapp.speciallist.R;

/**
 * Created by home on 2016-06-14.
 */
public class MainTab2ListAdapter extends RecyclerView.Adapter<MainTab2ListAdapter.Tab2ViewHolder> {
    public static final int HEADER = 0;
    public static final int CHILD = 1;

    Context ctx;
    private List<Item> data;
    LayoutInflater mInflate;

    public MainTab2ListAdapter(Context ctx, List<Item> data) {
        this.ctx = ctx;
        this.data = data;
        mInflate = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Tab2ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = null;
        float dp = ctx.getResources().getDisplayMetrics().density;
        int subItemPaddingLeft = (int) (18 * dp);
        int subItemPaddingTopAndBottom = (int) (5 * dp);

        switch (type) {
            case HEADER:
                view = mInflate.inflate(R.layout.list_maintab2fragment_parent, parent, false);
                Tab2ViewHolder header = new ListHeaderViewHolder(view);
                return header;
            case CHILD:
                view = mInflate.inflate(R.layout.list_maintab2fragment_child, null, false);
                Tab2ChildView itemTextView = (Tab2ChildView) view.findViewById(R.id.exp_section_title_child);
                itemTextView.setPadding(subItemPaddingLeft, subItemPaddingTopAndBottom, 0, subItemPaddingTopAndBottom);
                itemTextView.setTextColor(0x88cccccc);
                Tab2ViewHolder child = new ListChildViewHolder(view);
                return child;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(Tab2ViewHolder holder, int position) {
        final Item item = data.get(position);
        switch (item.type) {
            case HEADER:
                final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
                itemController.refferalItem = item;
                itemController.header_title.setText(item.name);
                if (item.invisibleChildren == null) {
//                    itemController.btn_expand_toggle.setImageResource(R.mipmap.icon_next);
                } else {
                    itemController.btn_expand_toggle.setImageResource(R.mipmap.icon_next);
                }

                itemController.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<Item>();
                            int count = 0;
                            int pos = data.indexOf(itemController.refferalItem);
                            while (data.size() > pos + 1 && data.get(pos + 1).type == CHILD) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            itemController.btn_expand_toggle.setRotation(0);
                        } else {
                            int pos = data.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for (Item i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            itemController.btn_expand_toggle.setRotation(90);

                            item.invisibleChildren = null;
                        }
                    }
                });
                break;

            case CHILD:
                final ListChildViewHolder itemController_child = (ListChildViewHolder) holder;
                final Tab2ChildView bindTextView = (Tab2ChildView) itemController_child.child_title;
                bindTextView.childId = data.get(position).id;
                bindTextView.setText(data.get(position).name);

                bindTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ctx, DetailActivity.class);
                        intent.putExtra(MyApplication.PARAMETER_SPECID, bindTextView.childId);
                        intent.putExtra(MyApplication.PARAMETER_METHOD, 1);
                        ctx.startActivity(intent);
                    }
                });

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ListHeaderViewHolder extends Tab2ViewHolder {
        public TextView header_title;
        public ImageView btn_expand_toggle;
        public Item refferalItem;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            header_title = (TextView) itemView.findViewById(R.id.exp_section_title);
            btn_expand_toggle = (ImageView) itemView.findViewById(R.id.exp_indication_arrow);
        }
    }

    public class ListChildViewHolder extends Tab2ViewHolder {
        public Tab2ChildView child_title;

        public ListChildViewHolder(View itemView) {
            super(itemView);
            child_title = (Tab2ChildView) itemView.findViewById(R.id.exp_section_title_child);
        }
    }


    public static class Item {
        public int type;
        public String id;
        public String name;
        public List<Item> invisibleChildren;

        public Item(int type, String id, String name) {
            this.type = type;
            this.id = id;
            this.name = name;
        }
    }


    public class Tab2ViewHolder extends  RecyclerView.ViewHolder {
        public Tab2ViewHolder(View v) {
            super(v);
        }
    }
}


