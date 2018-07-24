package info.semsamot.actionbarrtlizer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by semsamot on 8/2/2014.
 */
public class ActionBarRtlizer {

    private static final String TAG = "info.semsamot.actionbar-rtlizer";
    private final Activity mActivity;
    private final String mActionBarIdentifierName;

    public ActionBarRtlizer(Activity activity) {
        this(activity, "action_bar");
    }

    public ActionBarRtlizer(Activity activity, String actionBarIdentifierName) {
        this.mActivity = activity;
        this.mActionBarIdentifierName = actionBarIdentifierName;
    }

    public ViewGroup getActionBarView() {
        int resId;
        ViewGroup actionBarView;

        Window window = mActivity.getWindow();
        View view = window.getDecorView();

        resId = mActivity.getResources().getIdentifier(
                mActionBarIdentifierName, "id", mActivity.getPackageName());
        actionBarView = (ViewGroup) view.findViewById(resId);

        if (actionBarView == null)
        {
            resId = mActivity.getResources().getIdentifier(mActionBarIdentifierName, "id", "android");
            actionBarView = (ViewGroup) view.findViewById(resId);
        }

        return actionBarView;
    }

    public View findViewByClass(String className, View parent)
    {
        int childCount = parent instanceof ViewGroup ? ((ViewGroup)parent).getChildCount() : -1;

        if (parent == null)
            return null;

        if (parent.getClass().toString().contains(className))
            return parent;


        if (childCount < 1)
            return null;

        for (int i=0; i < childCount; i++)
        {
            View child = ((ViewGroup)parent).getChildAt(i);

            View target = findViewByClass(className, child);

            if (target != null)
                return target;
        }

        return null;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void flipActionBarUpIconIfAvailable(ViewGroup homeView)
    {
        if (homeView == null || homeView.getChildCount() < 2) return;

        ImageView upIcon = (ImageView) homeView.getChildAt(0);

        if (Build.VERSION.SDK_INT >= 11)
            upIcon.setRotationX(180);
        else
        {
            Animation hFlip = AnimationUtils.loadAnimation(mActivity, R.anim.flip_horizontal);
            upIcon.setAnimation(hFlip);
        }
    }

    public View getHomeView()
    {
        return findViewByClass("HomeView", getActionBarView());
    }

    public View getActionMenuView()
    {
        return findViewByClass("MenuView", getActionBarView());
    }

    public View getHomeViewContainer() {
        if (Build.VERSION.SDK_INT >= 17)
            return getActionBarView().getChildAt(0);
        else
            return null;
    }
}
