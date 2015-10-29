package com.tz.niceappdemo.utils;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * User: shine
 * Date: 2015-01-15
 * Time: 09:41
 * Description:
 */
public class AnimatorUtils {
    /**
     * @param view                ��Ҫ���ö�����view
     * @param translationY        ƫ����
     * @param animatorTime        ����ʱ��
     * @param isStartAnimator     �Ƿ���ָʾ��
     * @param isStartInterpolator �Ƿ�ʼ����
     * @return ƽ�ƶ���
     */
    public static Animator showUpAndDownBounce(View view, int translationY, int animatorTime, boolean isStartAnimator, boolean isStartInterpolator) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", translationY);
        if (isStartInterpolator) {
            objectAnimator.setInterpolator(new OvershootInterpolator());
        }
        objectAnimator.setDuration(animatorTime);
        if (isStartAnimator) {
            objectAnimator.start();
        }
        return objectAnimator;
    }

    /**
     * �ƶ�ScrollView��x��
     *
     * @param view      Ҫ�ƶ���ScrollView
     * @param toX       Ҫ�ƶ�����X������
     * @param time      ��������ʱ��
     * @param delayTime �ӳٿ�ʼ������ʱ��
     * @param isStart   �Ƿ�ʼ����
     * @return
     */
    public static Animator moveScrollViewToX(View view, int toX, int time, int delayTime, boolean isStart) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(view, "scrollX", new int[]{toX});
        objectAnimator.setDuration(time);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.setStartDelay(delayTime);
        if (isStart)
            objectAnimator.start();
        return objectAnimator;
    }

    /**
     * ��View�ı�����ɫ���ģ�ʹ������ɫת������г�Ĺ��ɶ���
     * @param view   Ҫ�ı䱳����ɫ��View
     * @param preColor  �ϸ���ɫֵ
     * @param currColor ��ǰ��ɫֵ
     * @param duration  ��������ʱ��
     */
    public static void showBackgroundColorAnimation(View view, int preColor, int currColor, int duration) {
        ObjectAnimator localObjectAnimator = ObjectAnimator.ofInt(view, "backgroundColor", new int[]{preColor, currColor});
        localObjectAnimator.setDuration(duration);
        localObjectAnimator.setEvaluator(new ArgbEvaluator());
        localObjectAnimator.start();
    }
}
