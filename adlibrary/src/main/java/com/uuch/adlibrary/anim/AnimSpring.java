package com.uuch.adlibrary.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.uuch.adlibrary.AdConstant;
import com.uuch.adlibrary.AnimDialogUtils;
import com.uuch.adlibrary.utils.DisplayUtil;

/**
 * Created by aaron on 16/8/3.
 * 弹性动画操作类
 */
public class AnimSpring {

    public static AnimSpring animSpring = null;
    public static SpringSystem springSystem = null;
    public SpringConfig springConfig = SpringConfig.fromBouncinessAndSpeed(8, 2);

    public static AnimSpring getInstance() {
        if (springSystem == null) {
            springSystem = SpringSystem.create();
        }
        if (animSpring == null) {
            animSpring = new AnimSpring();
        }

        return animSpring;
    }

    // #################### 弹窗打开时的动画效果 #############################

    /**
     * 弹窗打开时的动画效果
     */
    public void startAnim(final int animType, final AnimDialogUtils animDialogUtils,
            double bounciness, double speed) {

        springConfig = SpringConfig.fromBouncinessAndSpeed(bounciness, speed);
        // 常量类型动画效果
        final RelativeLayout animContainer = animDialogUtils.getAnimContainer();
        if (AdConstant.isConstantAnim(animType)) {
            startConstantAnim(animType, animContainer);
        } else if (AdConstant.isCircleAnim(animType)) {
            startCircleAnim(animType, animContainer);
        } else if (AdConstant.isAlphaAnim(animType)) {
            startAlphaAnim(animType, animDialogUtils);
        } else {
            animContainer.setVisibility(View.VISIBLE);
        }
    }

    public void startAlphaAnim(final int animType, final AnimDialogUtils animDialogUtils) {
        if (animType == AdConstant.ANIM_ALPHA_IN) {
            final ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
            animator.setDuration(500);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float alpha = (float) animation.getAnimatedValue();
                    animDialogUtils.getAnimContainer().setAlpha(alpha);
                    animDialogUtils.getAnimBackView().setAlpha(alpha);
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    animDialogUtils.getAnimContainer().setVisibility(View.VISIBLE);
                }
            });
            animator.start();

            //animContainer.setAlpha(0f);
            //animContainer.animate()
            //        .alpha(1)
            //        .setDuration(500)
            //        .setInterpolator(new DecelerateInterpolator())
            //        .setListener(new AnimatorListenerAdapter() {
            //
            //            @Override
            //            public void onAnimationStart(Animator animation) {
            //                super.onAnimationStart(animation);
            //                animContainer.setVisibility(View.VISIBLE);
            //            }
            //
            //            @Override
            //            public void onAnimationEnd(Animator animation) {
            //            }
            //        })
            //        .start();
        }
    }

    /**
     * 开始动画-定义动画开始角度
     */
    public void startCircleAnim(final int animType, final RelativeLayout animContainer) {
        final int widthPx = DisplayUtil.getScreenWidth(animContainer.getContext());
        final int heightPx = DisplayUtil.getScreenHeight(animContainer.getContext());
        double radius = Math.sqrt(heightPx * heightPx + widthPx * widthPx);
        double heightY = -Math.sin(Math.toRadians(animType)) * radius;
        double widthX = Math.cos(Math.toRadians(animType)) * radius;

        Spring tranSpring = springSystem.createSpring();
        Spring tranSpring1 = springSystem.createSpring();
        tranSpring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringActivate(Spring spring) {
                animContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSpringUpdate(Spring spring) {
                animContainer.setTranslationX((float) spring.getCurrentValue());
            }
        });

        tranSpring1.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringActivate(Spring spring) {
                animContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSpringUpdate(Spring spring) {
                animContainer.setTranslationY((float) spring.getCurrentValue());
            }
        });

        tranSpring.setSpringConfig(springConfig);
        tranSpring1.setSpringConfig(springConfig);
        tranSpring.setCurrentValue(widthX);
        tranSpring.setEndValue(0);
        tranSpring1.setCurrentValue(heightY);
        tranSpring1.setEndValue(0);
    }

    /**
     * 开始动画-固定类型动画
     */
    public void startConstantAnim(final int animType, final RelativeLayout animContainer) {

        if (animType == AdConstant.ANIM_DOWN_TO_UP) {
            startCircleAnim(270, animContainer);
        } else if (animType == AdConstant.ANIM_UP_TO_DOWN) {
            startCircleAnim(90, animContainer);
        } else if (animType == AdConstant.ANIM_LEFT_TO_RIGHT) {
            startCircleAnim(180, animContainer);
        } else if (animType == AdConstant.ANIM_RIGHT_TO_LEFT) {
            startCircleAnim(0, animContainer);
        } else if (animType == AdConstant.ANIM_UPLEFT_TO_CENTER) {
            startCircleAnim(135, animContainer);
        } else if (animType == AdConstant.ANIM_UPRIGHT_TO_CENTER) {
            startCircleAnim(45, animContainer);
        } else if (animType == AdConstant.ANIM_DOWNLEFT_TO_CENTER) {
            startCircleAnim(225, animContainer);
        } else if (animType == AdConstant.ANIM_DOWNRIGHT_TO_CENTER) {
            startCircleAnim(315, animContainer);
        }
    }

    // ############################## 弹窗关闭时的动画效果 ##################################

    /**
     * 弹窗退出时的动画
     */
    public void stopAnim(final int animType, final AnimDialogUtils animDialogUtils) {
        if (animDialogUtils == null) {
            return;
        }

        if (animType == AdConstant.ANIM_STOP_TRANSPARENT) {
            final ValueAnimator animator = ValueAnimator.ofFloat(1f, 0f);
            animator.setDuration(300);
            animator.setInterpolator(new AccelerateInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float alpha = (float) animation.getAnimatedValue();
                    animDialogUtils.getAnimContainer().setAlpha(alpha);
                    animDialogUtils.getAnimBackView().setAlpha(alpha);
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animDialogUtils.getAndroidContentView()
                            .removeView(animDialogUtils.getRootView());
                    animDialogUtils.setShowing(false);
                }
            });
            animator.start();
        } else {
            animDialogUtils.getAndroidContentView().removeView(animDialogUtils.getRootView());
            animDialogUtils.setShowing(false);
        }
    }
}
