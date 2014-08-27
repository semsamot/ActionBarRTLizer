package info.semsamot.actionbarrtlizer;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by semsamot on 8/2/2014.
 */
public class ActionBarRtlizer {

    private static final String TAG = "com.semsamot.actionbarrtlizer";
    private final Activity mActivity;
    private OnRtlizeFinishedListener onRtlizeFinishedListener;

    private ViewGroup homeView, actionMenuView, homeViewContainer;

    public ActionBarRtlizer(Activity activity) {
        this.mActivity = activity;
    }

    public void rtlize()
    {
        final ViewGroup actionBarView = getActionBarView();

        actionBarView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout()
            {
                arrangeViewGroups();

                if (Build.VERSION.SDK_INT < 16)
                    actionBarView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                else
                    actionBarView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public ViewGroup getActionBarView() {
        int resId;
        ViewGroup actionBarView;

        Window window = mActivity.getWindow();
        View view = window.getDecorView();

        resId = mActivity.getResources().getIdentifier(
                "action_bar", "id", mActivity.getPackageName());
        actionBarView = (ViewGroup) view.findViewById(resId);

        if (actionBarView == null)
        {
            resId = mActivity.getResources().getIdentifier("action_bar", "id", "android");
            actionBarView = (ViewGroup) view.findViewById(resId);
        }

        return actionBarView;
    }

    private void arrangeViewGroups()
    {
        Log.d(TAG, mActivity.getClass().toString());

        homeView = null;
        actionMenuView = null;
        homeViewContainer = null;

        ViewGroup actionBarView = getActionBarView();
        ViewGroup homeViewContainer = null;
        boolean hasHomeViewContainer = false;

        homeView = (ViewGroup) findViewByClass("HomeView", actionBarView);
        actionMenuView = (ViewGroup) findViewByClass("MenuView", actionBarView);

        // if homeView is not set (API LEVEL >= 17)
        if (homeView == null)
        {
            hasHomeViewContainer = true;
            homeViewContainer = (ViewGroup) actionBarView.getChildAt(0);
            this.homeViewContainer = homeViewContainer;

            // find & set homeView
            homeView = (ViewGroup) findViewByClass("HomeView", homeViewContainer);
        }

        // re-arrange ActionBarView
        arrangeInRtl(actionBarView, true);

        // re-arrange HomeViewContainer (api >= 17)
        if (hasHomeViewContainer)
            arrangeInRtl(homeViewContainer);

        // re-arrange HomeView in RTL direction
        if (homeView != null)
        {
            arrangeInRtl(homeView);
            flipActionBarUpIconIfAvailable(homeView);
        }

        // re-arrange menu view too
        if (actionMenuView != null)
            arrangeInRtl(actionMenuView);


        // call OnRtlizeFinishedListener if set
        if (onRtlizeFinishedListener != null)
            onRtlizeFinishedListener.onRtlizeFinished();
    }

    private View findViewByClass(String className, ViewGroup parent)
    {
        for (int i=0; i < parent.getChildCount(); i++)
        {
            View child = parent.getChildAt(i);
            if (child.getClass().toString().contains(className))
                return child;
        }

        return null;
    }

    private void arrangeInRtl(ViewGroup viewGroup)
    {
        arrangeInRtl(viewGroup, false);
    }

    private void arrangeInRtl(ViewGroup viewGroup, boolean isInActionBarViewGroup)
    {
        final int childCount = viewGroup.getChildCount();
        final int childPadding = 0;
        int rightMost = viewGroup.getWidth();

        // if its not in actionBar and childCount < 2 don't bother...!
        if (!isInActionBarViewGroup && childCount < 2) return;

        View[] childs = new View[childCount];
        for (int i = 0; i < childCount; i++)
        {
            childs[i] = viewGroup.getChildAt(i);

            if (isInActionBarViewGroup && childs[i] == this.homeView && i != 0)
            {
                // put homeView at start of childs array
                View temp = childs[0];
                childs[0] = childs[i];
                childs[i] = temp;
            }
        }

        for (int i = 0; i < childCount - 1; i++) {
            final View child = childs[i];

            if (child.getVisibility() == View.GONE) continue;

            final int childRight = rightMost;
            final int childLeft = childRight - child.getWidth();

            if (Build.VERSION.SDK_INT < 11)
            {
                child.layout(childLeft, child.getTop(), childRight, child.getBottom());
                child.forceLayout();
            }else {
                child.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(
                            View view, int i, int i2, int i3, int i4,
                            int i5, int i6, int i7, int i8)
                    {
                        child.setLeft(childLeft);
                        child.setRight(childRight);
                    }
                });
                child.requestLayout();
            }

            rightMost -= child.getWidth() - childPadding;
        }

        final View lastChild = childs[childCount - 1];

        // if last child is menuView in actionBar, move it to the left otherwise append to right
        int childLeft, childRight;
        if ( isInActionBarViewGroup && lastChild.getClass().toString().contains("MenuView") )
        {
            childLeft = 0;
            childRight = childLeft + lastChild.getWidth();
        } else {
            childRight = rightMost;
            childLeft = childRight - lastChild.getWidth();
        }

        final int lastChildLeft = childLeft;
        final int lastChildRight = childRight;


        if (Build.VERSION.SDK_INT < 11)
        {
            lastChild.layout(
                    lastChildLeft, lastChild.getTop(), lastChildRight, lastChild.getBottom());
            lastChild.forceLayout();
        }else {
            lastChild.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(
                        View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    lastChild.setLeft(lastChildLeft);
                    lastChild.setRight(lastChildRight);
                }
            });
            lastChild.requestLayout();
        }
    }

    private void flipActionBarUpIconIfAvailable(ViewGroup homeView)
    {
        if (homeView.getChildCount() < 2) return;

        ImageView upIcon = (ImageView) homeView.getChildAt(0);

        if (Build.VERSION.SDK_INT >= 11)
            upIcon.setRotation(180);
        else
        {
            Animation hFlip = AnimationUtils.loadAnimation(mActivity, R.anim.flip_horizontal);
            upIcon.setAnimation(hFlip);
        }
    }

    public ViewGroup getHomeView()
    {
        return homeView;
    }

    public ViewGroup getActionMenuView()
    {
        return actionMenuView;
    }

    public ViewGroup getHomeViewContainer() {
        return homeViewContainer;
    }

    public OnRtlizeFinishedListener getOnRtlizeFinishedListener() {
        return onRtlizeFinishedListener;
    }

    public void setOnRtlizeFinishedListener(OnRtlizeFinishedListener onRtlizeFinishedListener) {
        this.onRtlizeFinishedListener = onRtlizeFinishedListener;
    }

    public interface OnRtlizeFinishedListener{
        public void onRtlizeFinished();
    }
}
