package com.example.stack.welearn.fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.cleveroad.slidingtutorial.IndicatorOptions;
import com.cleveroad.slidingtutorial.OnTutorialPageChangeListener;
import com.cleveroad.slidingtutorial.PageOptions;
import com.cleveroad.slidingtutorial.Renderer;
import com.cleveroad.slidingtutorial.TutorialOptions;
import com.cleveroad.slidingtutorial.TutorialPageOptionsProvider;
import com.cleveroad.slidingtutorial.TutorialSupportFragment;
import com.example.stack.welearn.R;

/**
 * Created by stack on 2018/1/13.
 */

public class UserGuideFragment extends TutorialSupportFragment implements OnTutorialPageChangeListener {
    private static final String TAG=UserGuideFragment.class.getSimpleName();
    private static final int TOTAL_PAGES=6;
    private static final int PAGE=3;


    private final TutorialPageOptionsProvider mUserGuideOptionsProvider=new TutorialPageOptionsProvider() {
        @NonNull
        @Override
        public PageOptions provide(int position) {

        }
    };

    private int[] pageColors;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(pageColors==null){
            pageColors=new int[]{
                    ContextCompat.getColor(getContext(),android.R.color.holo_orange_dark),
                    ContextCompat.getColor(getContext(),android.R.color.holo_green_dark),
                    ContextCompat.getColor(getContext(),android.R.color.holo_blue_dark),
                    ContextCompat.getColor(getContext(),android.R.color.holo_red_dark),
                    ContextCompat.getColor(getContext(),android.R.color.holo_purple),
                    ContextCompat.getColor(getContext(),android.R.color.darker_gray),
            };
        }
        addOnTutorialPageChangeListener(this);
    }

    protected TutorialOptions provideTutorialOptions(){
        return newTutorialOptionsBuilder(getContext())
                .setUseInfiniteScroll(true)
                .setPagesColors(pageColors)
                .setPagesCount(TOTAL_PAGES)
                .setTutorialPageProvider(mUserGuideOptionsProvider)
                .setIndicatorOptions(IndicatorOptions.newBuilder(getContext())
                        .setElementSizeRes(R.dimen.indicator_size)
                        .setElementSpacingRes(R.dimen.indicator_spacing)
                        .setElementColorRes(android.R.color.darker_gray)
                        .setSelectedElementColor(Color.LTGRAY)
                        .setRenderer(MyRenderer.create())
                        .build())
                .setOnSkipClickListener((v)->{})
                .build();

    }

    @Override
    public void onPageChanged(int position) {

    }
}


class MyRenderer implements Renderer{
    public static MyRenderer create(){
        return new MyRenderer();
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull RectF elementBounds, @NonNull Paint paint, boolean isActive) {
        canvas.save();
        canvas.rotate(45f,elementBounds.centerX(),elementBounds.centerY());
        canvas.drawRect(elementBounds,paint);
        canvas.restore();
    }
}
