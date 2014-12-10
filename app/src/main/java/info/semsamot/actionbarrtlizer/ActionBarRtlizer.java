package info.semsamot.actionbarrtlizer;

import android.annotation.TargetApi;
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

    private static final String TAG = "info.semsamot.actionbarrtlizer";
    private final Activity mActivity;
    private OnRtlizeFinishedListener onRtlizeFinishedListener;

    private ViewGroup homeView, actionMenuView, homeViewContainer;

    public ActionBarRtlizer(Activity activity) {
        this.mActivity = activity;
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
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
