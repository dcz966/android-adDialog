package com.uuch.adlibrary;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.uuch.adlibrary.anim.AnimSpring;

/**
 * 使用弹性动画
 */
public class AnimDialogUtils {
    private Activity context;
    private ViewGroup androidContentView;
    private View rootView;
    private RelativeLayout animBackView;
    private FrameLayout flContentContainer;
    private RelativeLayout animContainer;
    private ImageView ivClose;

    public static final String ANIM_DIALOG_TAG = "AnimDialogTag";

    private boolean isShowing = false;
    // 弹窗背景是否透明
    private boolean isAnimBackViewTransparent = false;
    // 弹窗是否可关闭
    private boolean isDialogCloseable = true;
    // 弹窗关闭点击事件
    private View.OnClickListener onCloseClickListener = null;
    // 设置弹窗背景颜色
    private int backViewColor = Color.parseColor("#bf000000");
    // 弹窗背景是否覆盖全屏幕
    private boolean isOverScreen = true;

    private AnimDialogUtils(Activity context) {
        this.context = context;
    }

    public static AnimDialogUtils getInstance(Activity context) {
        return new AnimDialogUtils(context);
    }


    /**
     * 初始化弹窗中的界面
     */
    public AnimDialogUtils initView(@LayoutRes int id) {
        ensureContainer();

        return this.initView(LayoutInflater.from(context).inflate(id, flContentContainer, false));
    }

    /**
     * 初始化弹窗中的界面，添加传入的customView界面，并监听关闭按钮点击事件
     */
    public AnimDialogUtils initView(final View customView) {
        ensureContainer();

        ViewGroup.LayoutParams contentParams = customView.getLayoutParams();
        if (contentParams == null) {
            contentParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        flContentContainer.addView(customView, contentParams);
        ivClose = (ImageView) rootView.findViewById(R.id.iv_close);

        return this;
    }

    private void ensureContainer() {
        if (flContentContainer != null) {
            return;
        }
        if (isOverScreen) {
            androidContentView = (ViewGroup) context.getWindow().getDecorView();
        } else {
            androidContentView =
                    (ViewGroup) context.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        }
        rootView = LayoutInflater.from(context).inflate(R.layout.anim_dialog_layout, androidContentView, false);
        rootView.setTag(ANIM_DIALOG_TAG);

        animBackView = (RelativeLayout) rootView.findViewById(R.id.anim_back_view);
        animContainer = (RelativeLayout) rootView.findViewById(R.id.anim_container);
        animContainer.setVisibility(View.INVISIBLE);
        flContentContainer = (FrameLayout) rootView.findViewById(R.id.fl_content_container);
    }

    /**
     * 开始执行弹窗的展示动画, 使用默认动画设置
     */
    public void show() {
        this.show(AdConstant.ANIM_ALPHA_IN, AdConstant.BOUNCINESS, AdConstant.SPEED);
    }

    /**
     * 开始执行弹窗的展示动画
     */
    public void show(int animType, double bounciness, double speed) {
        // 判断是否设置背景透明
        if (isAnimBackViewTransparent) {
            backViewColor = Color.TRANSPARENT;
        }
        // 判断背景颜色
        animBackView.setBackgroundColor(backViewColor);

        // 判断弹窗是否可关闭
        if (isDialogCloseable) {
            ivClose.setVisibility(View.VISIBLE);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onCloseClickListener != null) {
                        onCloseClickListener.onClick(view);
                    }
                    dismiss(AdConstant.ANIM_STOP_TRANSPARENT);
                }
            });
        } else {
            ivClose.setVisibility(View.GONE);
        }
        ViewGroup.LayoutParams params = rootView.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

        }
        androidContentView.addView(rootView, params);
        AnimSpring.getInstance().startAnim(animType, this, bounciness, speed);
        isShowing = true;
    }

    /**
     * 开始执行关闭动画的操作
     */
    public void dismiss(int animType) {
        AnimSpring.getInstance().stopAnim(animType, this);
    }

    /**
     * 设置背景组件颜色
     */
    public AnimDialogUtils setDialogBackViewColor(int color) {

        backViewColor = color;

        return this;
    }

    /**
     * 设置弹窗关闭按钮是否可见
     */
    public AnimDialogUtils setDialogCloseable(boolean dialogCloseable) {
        isDialogCloseable = dialogCloseable;

        return this;
    }

    public AnimDialogUtils setOnCloseClickListener(View.OnClickListener onCloseClickListener) {
        this.onCloseClickListener = onCloseClickListener;

        return this;
    }

    /**
     * 设置背景是否透明
     */
    public AnimDialogUtils setAnimBackViewTransparent(boolean animBackViewTransparent) {
        isAnimBackViewTransparent = animBackViewTransparent;

        return this;
    }

    /**
     * 设置弹窗背景是否覆盖全屏幕
     */
    public AnimDialogUtils setOverScreen(boolean overScreen) {
        isOverScreen = overScreen;

        return this;
    }

    // ################### get方法 ####################

    public RelativeLayout getAnimContainer() {
        return animContainer;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public ViewGroup getAndroidContentView() {
        return androidContentView;
    }

    public View getRootView() {
        return rootView;
    }

    public View getAnimBackView() {
        return animBackView;
    }

    public void setShowing(boolean showing) {
        isShowing = showing;
    }
}

