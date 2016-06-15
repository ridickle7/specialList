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
public class Tab2ParentView extends TextView {
    public Tab2ParentView(Context context){
        super(context);
        setType(context);
    }


    public Tab2ParentView(Context context, AttributeSet attrs){
        super(context, attrs);
        setType(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Tab2ParentView(Context context, AttributeSet attrs, int defStyleRes){
        super(context, attrs, defStyleRes);
        setType(context);
    }

    private void setType(Context context){
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Regular-Hestia.otf"));
    }
}

