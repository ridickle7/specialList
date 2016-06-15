package kr.co.yapp.speciallist.Custom_Object;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by home on 2016-06-15.
 */
public class Tab2ChildView extends TextView {
    public String childId;
    public Tab2ChildView(Context context){
        super(context);
        setType(context);
    }


    public Tab2ChildView(Context context, AttributeSet attrs){
        super(context, attrs);
        setType(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Tab2ChildView(Context context, AttributeSet attrs, int defStyleRes){
        super(context, attrs, defStyleRes);
        setType(context);
    }

    private void setType(Context context){
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Regular-Hestia.otf"));
    }
}