package com.skyhope.expandcollapsecardview;

/*
 *  ****************************************************************************
 *  * Created by : Md Tariqul Islam on 7/4/2018 at 3:57 PM.
 *  * Email : tariqul@w3engineers.com
 *  *
 *  * Purpose:
 *  *
 *  * Last edited by : Md Tariqul Islam on 7/4/2018.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExpandCollapseCard extends CardView implements View.OnClickListener {

    /*
     *  This is our expandable card view title --> It is a textview
     * */
    private TextView textViewTitle;

    //Image button of collapse sign
    private ImageButton imageButtonCollapse;

    //Image button of Expand sign
    private ImageButton imageButtonExpand;

    private RelativeLayout titleBackgroundLayout;

    //Inner view
    private ViewStub viewStub;

    // Card view default title size
    private static final float DEFAULT_TITLE_SIZE = 5.0f;

    // Card view default title color
    private static final int DEFAULT_TITLE_COLOR = Color.rgb(128, 128, 128);

    // Card view default title background color
    private static final int DEFAULT_TITLE_BACKGROUND_COLOR = Color.rgb(255, 255, 255);

    //Card view expand tracker
    private boolean isExpand = false;

    //Card view custom text
    private String mTitle;

    //card view title custom size
    private float mTitleSize;

    //card view title custom color
    private int mTitleColor;

    //card view title custom background color
    private int mTitleBackgroundColor;

    //card view inner item
    private int mInterViewID;

    //get inner view item
    private View mInnerView;

    //card collapse icon
    private Drawable mCollapseIcon;

    //card expand icon
    private Drawable mExpandIcon;

    private ExpandCollapseListener mExpandCollapseListener;


    public ExpandCollapseCard(Context context) {
        super(context);
    }

    public ExpandCollapseCard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initAttrs(attrs);

        initView(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        textViewTitle = findViewById(R.id.text_view_card_title);
        imageButtonCollapse = findViewById(R.id.image_button_collapse);
        imageButtonExpand = findViewById(R.id.image_button_expand);
        viewStub = findViewById(R.id.view_stub);
        titleBackgroundLayout = findViewById(R.id.title_background);


        setTitle(mTitle);

        setTitleSize(mTitleSize);

        setTitleColor(mTitleColor);

        setInnerItem(mInterViewID);

        setIcon(mCollapseIcon, mExpandIcon);

        setTitleBackgroundColor(mTitleBackgroundColor);

        imageButtonCollapse.setOnClickListener(this);
        imageButtonExpand.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.image_button_collapse) {
            isExpand = true;
            imageButtonCollapse.setVisibility(GONE);
            imageButtonExpand.setVisibility(VISIBLE);

            mExpandCollapseListener.onExpandCollapseListener(isExpand);

           /* if (viewStub != null) {
                viewStub.setVisibility(VISIBLE);
            }*/

            if (mInnerView != null) {
                expand(mInnerView);
            }


        } else if (i == R.id.image_button_expand) {
            isExpand = false;
            imageButtonCollapse.setVisibility(VISIBLE);
            imageButtonExpand.setVisibility(GONE);

            mExpandCollapseListener.onExpandCollapseListener(isExpand);

            if (mInnerView != null) {
                collapse(mInnerView);
            }

           /* if (viewStub != null) {
                viewStub.setVisibility(GONE);
            }*/
        }
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandCollapseCard);

        mTitle = typedArray.getString(R.styleable.ExpandCollapseCard_title);

        mTitleSize = typedArray.getDimension(R.styleable.ExpandCollapseCard_title_size, DEFAULT_TITLE_SIZE);

        mTitleColor = typedArray.getColor(R.styleable.ExpandCollapseCard_title_color, DEFAULT_TITLE_COLOR);

        mTitleBackgroundColor = typedArray.getColor(R.styleable.ExpandCollapseCard_title_background_color, DEFAULT_TITLE_BACKGROUND_COLOR);

        mInterViewID = typedArray.getResourceId(R.styleable.ExpandCollapseCard_item_inner_view, View.NO_ID);

        mCollapseIcon = typedArray.getDrawable(R.styleable.ExpandCollapseCard_collapse_icon);

        mExpandIcon = typedArray.getDrawable(R.styleable.ExpandCollapseCard_expand_icon);

        typedArray.recycle();

    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.expand_card, this);
        }


    }


    private void setTitle(String title) {
        if (title != null) {
            textViewTitle.setText(title);
        }
    }


    private void setTitleSize(float titleSize) {
        mTitleSize = px2sp(titleSize);
        textViewTitle.setTextSize(mTitleSize);
    }

    private void setTitleColor(int color) {
        textViewTitle.setTextColor(color);
    }

    private void setInnerItem(int id) {
        viewStub.setLayoutResource(id);
        viewStub.setVisibility(View.GONE);
        if (id != View.NO_ID) {
            mInnerView = viewStub.inflate();
            mInnerView.setVisibility(GONE);
        }
        viewStub.getInflatedId();

    }

    private void setIcon(Drawable collapseIcon, Drawable expandIcon) {

        if (collapseIcon != null) {
            imageButtonCollapse.setImageDrawable(collapseIcon);
        }
        if (expandIcon != null) {
            imageButtonExpand.setImageDrawable(expandIcon);
        }
    }

    private void setTitleBackgroundColor(int color) {
        titleBackgroundLayout.setBackgroundColor(color);

    }

    /*
     * Animation section
     * */

    public void expand(final View v) {
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        //a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(100);
        v.startAnimation(a);
    }

    public void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        //a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(500);
        v.startAnimation(a);
    }

    public void slideUp(final View view) {
        //view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    // slide the view from its current position to below itself
    public void slideDown(final View view) {
        view.setVisibility(VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);

        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /*
     * User settable property
     * */

    public void setTitleTextColor(int color) {
        textViewTitle.setTextColor(color);
    }

    public void setTitleText(String text) {
        textViewTitle.setText(text);
    }

    /*
     * User gettable property
     * */

    public View getChildView() {
        if (mInnerView != null) return mInnerView;
        return null;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void initListener(ExpandCollapseListener listener) {
        this.mExpandCollapseListener = listener;
    }


    private float px2sp(float px) {
        return px / getResources().getDisplayMetrics().scaledDensity;
    }

    private float sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

}
