package info.semsamot.actionbarrtlizer;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by semsamot on 11/21/14.
 * Modified by maudem-airg on 7/24/15
 */
public class RtlizeEverything {

    public static void rtlize(ViewGroup container)
    {
        rtlize(container, false);
    }

    public static void rtlize(final ViewGroup container, final boolean recursive)
    {
        if (container == null) return;

        container.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                arrangeInRtl(container, recursive);

                if (Build.VERSION.SDK_INT < 16)
                    container.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                else
                    container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private static void arrangeInRtl(final ViewGroup container, boolean recursive)
    {
        int childCount = container.getChildCount();

        for (int i=0; i < childCount; i++)
        {
            final View child = container.getChildAt(i);

            if (recursive && child instanceof ViewGroup && ((ViewGroup)child).getChildCount() > 1)
                rtlize((ViewGroup)child, true);

            if ( !(child.getLeft() == 0 && child.getRight() == 0) )
            {
                mirrorViewPosition(container, child);
            } else {
                child.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        mirrorViewPosition(container, child);

                        if (Build.VERSION.SDK_INT < 16)
                            child.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        else
                            child.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void mirrorViewPosition(ViewGroup container, final View child)
    {
        int containerLeft = container.getLeft();
        int containerRight = containerLeft + container.getWidth();

        int childLeft = child.getLeft();
        int childRight = child.getRight();

        final int newChildRight = containerRight - childLeft;
        final int newChildLeft = newChildRight - child.getWidth();

        if (Build.VERSION.SDK_INT < 11)
        {
            //not supported/corrected
            if (child.isShown())
            {
                child.layout(newChildLeft, child.getTop(), newChildRight, child.getBottom());
                child.forceLayout();
            }else {
                child.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        child.layout(newChildLeft, child.getTop(), newChildRight, child.getBottom());
                        child.forceLayout();

                        if (Build.VERSION.SDK_INT < 16)
                            child.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        else
                            child.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        }else {
            child.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view,
                                           int i, int i2, int i3, int i4,
                                           int i5, int i6, int i7, int i8) {
                    if (!(child instanceof TextView))//cheat for the title to not been cut off
                        child.setLeft(newChildLeft);
                    child.setRight(newChildRight);
                    child.setLayoutParams(child.getLayoutParams());
                }
            });
            child.requestLayout();
        }
    }

    /**
     * change the position when the screen rotate.
     * (correct the bug of having the title of the ActionBar in the middle)
     * @param container the ActionBarView
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void rtlizeOnConfigurationChanged(final ViewGroup container){
        int childCount = container.getChildCount();

        for (int i=0; i < childCount; i++)
        {
            final View child = container.getChildAt(i);

            if (Build.VERSION.SDK_INT < 11) {
                //not supported/corrected
                if (child.isShown()) {
                    child.setLayoutParams(child.getLayoutParams());
                    child.forceLayout();
                } else {
                    child.getViewTreeObserver().addOnGlobalLayoutListener(
                            new ViewTreeObserver.OnGlobalLayoutListener() {
                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onGlobalLayout() {
                                    child.setLayoutParams(child.getLayoutParams());
                                    child.forceLayout();

                                    if (Build.VERSION.SDK_INT < 16)
                                        child.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                    else
                                        child.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                }
                            });
                }
            } else {
                child.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View view,
                                               int i, int i2, int i3, int i4,
                                               int i5, int i6, int i7, int i8) {
                        if (child instanceof ImageButton) {//for the home button
                            child.setLeft(container.getWidth() - child.getWidth());
                            child.setRight(container.getWidth());
                        } else if (child instanceof TextView) {//for the title
                            child.setRight(container.getWidth() - container.getHeight());
                        }
                        child.setLayoutParams(child.getLayoutParams());
                    }
                });
                child.requestLayout();
            }
        }
    }
}
