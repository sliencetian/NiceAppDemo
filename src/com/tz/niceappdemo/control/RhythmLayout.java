package com.tz.niceappdemo.control;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.tz.niceappdemo.R;
import com.tz.niceappdemo.utils.AnimatorUtils;

/**
 * User: shine
 * Date: 2015-01-14
 * Time: 11:50
 * Description:
 */
@SuppressWarnings("unchecked")
public class RhythmLayout extends HorizontalScrollView {
    /**
     * ScrollView���ӿؼ�
     */
    private LinearLayout mLinearLayout;
    /**
     * item�Ŀ�ȣ�Ϊ��Ļ��1/7
     */
    private float mItemWidth;
    /**
     * ��Ļ���
     */
    private int mScreenWidth;
    /**
     * ��ǰ��ѡ�еĵ�Item��λ��
     */
    private int mCurrentItemPosition;
    /**
     * ������
     */
    private RhythmAdapter mAdapter;
    /**
     * item��Y��λ�Ƶĵ�λ�������ֵΪ������ʼ����ʽλ�ƶ���
     */
    private int mIntervalHeight;
    /**
     * item��Y��λ�����ĸ߶�
     */
    private int mMaxTranslationHeight;
    /**
     * ÿ��ͼ���������2�߱߾�ĳߴ�
     */
    private int mEdgeSizeForShiftRhythm;
    /**
     * ������Ļ��ʱ��
     */
    private long mFingerDownTime;

    /**
     * ��������������ָ�뿪��Ļʱ��λ��
     */
    private IRhythmItemListener mListener;

    /**
     * ��һ����ѡ�е�item��λ��
     */
    private int mLastDisplayItemPosition;


    /**
     * ScrollView���������ӳ�ִ�е�ʱ��
     */
    private int mScrollStartDelayTime;

    private Context mContext;
    private Handler mHandler;
    private ShiftMonitorTimer mTimer;

    public RhythmLayout(Context context) {
        this(context, null);
    }

    public RhythmLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }


    private void init() {
        //�����Ļ��С
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        //��ȡItem�Ŀ�ȣ�Ϊ��Ļ���߷�֮һ
        mItemWidth = mScreenWidth / 7;
        //��ʼ��ʱ����ָ��ǰ���ڵ�λ����Ϊ-1
        mCurrentItemPosition = -1;
        mMaxTranslationHeight = (int) mItemWidth;
        mIntervalHeight = (mMaxTranslationHeight / 6);
        mEdgeSizeForShiftRhythm = getResources().getDimensionPixelSize(R.dimen.rhythm_edge_size_for_shift);
        mFingerDownTime = 0;
        mHandler = new Handler();
        mTimer = new ShiftMonitorTimer();
        mTimer.startMonitor();

        mLastDisplayItemPosition = -1;
        mScrollStartDelayTime = 0;
    }

    public void setAdapter(RhythmAdapter adapter) {
        this.mAdapter = adapter;
        //�����ȡHorizontalScrollView�µ�LinearLayout�ؼ�
        if (mLinearLayout == null) {
            mLinearLayout = (LinearLayout) getChildAt(0);
        }
        //ѭ����ȡadapter�е�View������item�Ŀ�Ȳ���add��mLinearLayout��
        for (int i = 0; i < this.mAdapter.getCount(); i++) {
            mAdapter.setItemWidth(mItemWidth);
            mLinearLayout.addView(mAdapter.getView(i, null, null));
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE://�ƶ�
                mTimer.monitorTouchPosition(ev.getX(), ev.getY());
                updateItemHeight(ev.getX());
                break;
            case MotionEvent.ACTION_DOWN://����
                mTimer.monitorTouchPosition(ev.getX(), ev.getY());
                //�õ�����ʱ��ʱ���
                mFingerDownTime = System.currentTimeMillis();
                updateItemHeight(ev.getX());
                break;
            case MotionEvent.ACTION_UP://̧��
                actionUp();
                break;
        }
        return true;
    }

    //����Сͼ��ĸ߶�
    private void updateItemHeight(float scrollX) {
        //�õ���Ļ�Ͽɼ���7��Сͼ�����ͼ
		@SuppressWarnings("rawtypes")
		List viewList = getVisibleViews();
        //��ǰ��ָ���ڵ�item
        int position = (int) (scrollX / mItemWidth);
        //�����ָλ��û�з����仯���ߴ���childCount���������������ټ���ִ��
        if (position == mCurrentItemPosition || position >= mLinearLayout.getChildCount())
            return;
        mCurrentItemPosition = position;
        makeItems(position, viewList);
    }

    /**
     * �õ���ǰ�ɼ���7��Сͼ��
     */
    private List<View> getVisibleViews() {
        @SuppressWarnings("rawtypes")
		ArrayList arrayList = new ArrayList();
        if (mLinearLayout == null)
            return arrayList;
        //��ǰ�ɼ��ĵ�һ��Сͼ���λ��
        int firstPosition = getFirstVisibleItemPosition();
        //��ǰ�ɼ������һ��Сͼ���λ��
        int lastPosition = firstPosition + 7;
        if (mLinearLayout.getChildCount() < 7) {
            lastPosition = mLinearLayout.getChildCount();
        }
        //ȡ����ǰ�ɼ���7��Сͼ��
        for (int i = firstPosition; i < lastPosition; i++)
            arrayList.add(mLinearLayout.getChildAt(i));
        return arrayList;
    }

    /**
     * ���firstPosition-1 �� lastPosition +1 �ڵ�ǰ�ɼ���7���ܹ�9��Сͼ��
     *
     * @param isForward  �Ƿ��ȡfirstPosition - 1 λ�õ�Сͼ��
     * @param isBackward �Ƿ��ȡlastPosition + 1 λ�õ�Сͼ��
     * @return
     */
    private List<View> getVisibleViews(boolean isForward, boolean isBackward) {
        @SuppressWarnings("rawtypes")
		ArrayList viewList = new ArrayList();
        if (this.mLinearLayout == null)
            return viewList;
        int firstPosition = getFirstVisibleItemPosition();
        int lastPosition = firstPosition + 7;
        if (mLinearLayout.getChildCount() < 7) {
            lastPosition = mLinearLayout.getChildCount();
        }
        if ((isForward) && (firstPosition > 0))
            firstPosition--;
        if ((isBackward) && (lastPosition < mLinearLayout.getChildCount()))
            lastPosition++;
        for (int i = firstPosition; i < lastPosition; i++)
            viewList.add(mLinearLayout.getChildAt(i));

        return viewList;
    }

    /**
     * �õ��ɼ��ĵ�һ��Сͼ���λ��
     */
    public int getFirstVisibleItemPosition() {
        if (mLinearLayout == null) {
            return 0;
        }
        //��ȡСͼ�������
        int size = mLinearLayout.getChildCount();
        for (int i = 0; i < size; i++) {
            View view = mLinearLayout.getChildAt(i);
            //������Сͼ���x��ȵ�ǰScrollView��x���ʱ�����Сͼ����ǵ�ǰ�ɼ��ĵ�һ��
            if (getScrollX() < view.getX() + mItemWidth / 2.0F)
                return i;
        }
        return 0;
    }

    /**
     * ���������Сͼ����Ҫ�ĸ߶Ȳ���ʼ����
     */
    private void makeItems(int fingerPosition, List<View> viewList) {
        if (fingerPosition >= viewList.size()) {
            return;
        }
        int size = viewList.size();
        for (int i = 0; i < size; i++) {
            //����Сͼ���λ�ü������Y����Ҫλ�ƵĴ�С

            int translationY = Math.min(Math.max(Math.abs(fingerPosition - i) * mIntervalHeight, 10), mMaxTranslationHeight);
            //λ�ƶ���
            updateItemHeightAnimator(viewList.get(i), translationY);
        }

    }

    /**
     * ���ݸ�����ֵ����Y��λ�ƵĶ���
     *
     * @param view
     * @param translationY
     */
    private void updateItemHeightAnimator(View view, int translationY) {
        if (view != null)
            AnimatorUtils.showUpAndDownBounce(view, translationY, 180, true, true);
    }

    /**
     * ��ָ̧��ʱ������Сͼ�����£����õ���ʼλ��
     */
	private void actionUp() {
        mTimer.monitorTouchPosition(-1.0F, -1.0F);
        if (mCurrentItemPosition < 0) {
            return;
        }
        int firstPosition = getFirstVisibleItemPosition();
        int lastPosition = firstPosition + mCurrentItemPosition;
        @SuppressWarnings("rawtypes")
		final List viewList = getVisibleViews();
        int size = viewList.size();
        //����ǰСͼ���Ҫ���µ�ViewList��ɾ��
        if (size > mCurrentItemPosition) {
            viewList.remove(mCurrentItemPosition);
        }
        if (firstPosition - 1 >= 0) {
            viewList.add(mLinearLayout.getChildAt(firstPosition - 1));
        }
        if (lastPosition + 1 <= mLinearLayout.getChildCount()) {
            viewList.add(mLinearLayout.getChildAt(lastPosition + 1));
        }
        //200�����ִ�ж���
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                for (int i = 0; i < viewList.size(); i++) {
                    View downView = (View) viewList.get(i);
                    shootDownItem(downView, true);
                }
            }
        }, 200L);
        //��������
        if (mListener != null)
            mListener.onSelected(lastPosition);
        mCurrentItemPosition = -1;
        //ʹ�豸��
        vibrate(20L);
    }

    /**
     * λ�Ƶ�Y��'���'�Ķ���
     *
     * @param view    ��Ҫִ�ж�������ͼ
     * @param isStart �Ƿ�ʼ����
     * @return
     */
    public Animator shootDownItem(View view, boolean isStart) {
        if (view != null)
            return AnimatorUtils.showUpAndDownBounce(view, mMaxTranslationHeight, 350, isStart, true);
        return null;
    }

    /**
     * λ�Ƶ�Y��'���'�Ķ���
     *
     * @param viewPosition view��λ��
     * @param isStart      �Ƿ�ʼ����
     * @return
     */
    public Animator shootDownItem(int viewPosition, boolean isStart) {
        if ((viewPosition >= 0) && (mLinearLayout != null) && (mLinearLayout.getChildCount() > viewPosition))
            return shootDownItem(mLinearLayout.getChildAt(viewPosition), isStart);
        return null;
    }

    /**
     * @param position   Ҫ�ƶ�����view��λ��
     * @param duration   ��������ʱ��
     * @param startDelay �ӳٶ�����ʼʱ��
     * @param isStart    �����Ƿ�ʼ
     * @return
     */
    public Animator scrollToPosition(int position, int duration, int startDelay, boolean isStart) {
        int viewX = (int) mLinearLayout.getChildAt(position).getX();
        return smoothScrollX(viewX, duration, startDelay, isStart);
    }

    /**
     * ScrollView��������X��λ��
     *
     * @param position   view��λ��
     * @param startDelay �ӳٶ�����ʼʱ��
     * @param isStart    �����Ƿ�ʼ
     * @return
     */
    public Animator scrollToPosition(int position, int startDelay, boolean isStart) {
        int viewX = (int) mLinearLayout.getChildAt(position).getX();
        return smoothScrollX(viewX, 300, startDelay, isStart);
    }

    private Animator smoothScrollX(int position, int duration, int startDelay, boolean isStart) {
        return AnimatorUtils.moveScrollViewToX(this, position, duration, startDelay, isStart);
    }


    /**
     * λ�Ƶ�Y��'���'�Ķ���
     *
     * @param viewPosition view��λ��
     * @param isStart      �Ƿ�ʼ����
     * @return
     */
    public Animator bounceUpItem(int viewPosition, boolean isStart) {
        if (viewPosition >= 0)
            return bounceUpItem(mLinearLayout.getChildAt(viewPosition), isStart);
        return null;
    }

    public Animator bounceUpItem(View view, boolean isStart) {
        if (view != null)
            return AnimatorUtils.showUpAndDownBounce(view, 10, 350, isStart, true);
        return null;
    }

    /**
     * ���ƶ��豸��
     *
     * @param l �𶯵�ʱ��
     */
    private void vibrate(long l) {
   //     ((Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(new long[]{0L, l}, -1);
    }

    /**
     * ��ʱ����ʵ����¥��Ч��
     */
    class ShiftMonitorTimer extends Timer {
        private TimerTask timerTask;
        /**
         *
         */
        private boolean canShift = false;
        private float x;
        @SuppressWarnings("unused")
		private float y;

        void monitorTouchPosition(float x, float y) {
            this.x = x;
            this.y = y;
            //������λ���ڵ�һ�������һ������x<0,y<0ʱ��canShiftΪfalse��ʹ��ʱ���߳��еĴ��벻��ִ��
            if ((x < 0.0F) || ((x > mEdgeSizeForShiftRhythm) && (x < mScreenWidth - mEdgeSizeForShiftRhythm)) || (y < 0.0F)) {
                mFingerDownTime = System.currentTimeMillis();
                canShift = false;
            } else {
                canShift = true;
            }
        }

        void startMonitor() {
            if (this.timerTask == null) {
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        long duration = System.currentTimeMillis() - mFingerDownTime;
                        //����ʱ�����1�룬�Ұ��µ��ǵ�һ���������һ����ʽ����
                        if (canShift && duration > 1000) {
                            int firstPosition = getFirstVisibleItemPosition();
                            int toPosition = 0; //Ҫ�ƶ�����Сͼ���λ��
                            boolean isForward = false; //�Ƿ��ȡ��firstPosition-1��Сͼ��
                            boolean isBackward = false;//�Ƿ��ȡ��lastPosition+1��Сͼ��
                            final List<View> localList;
                            if (x <= mEdgeSizeForShiftRhythm && x >= 0.0F) {//��һ��
                                if (firstPosition - 1 >= 0) {
                                    mCurrentItemPosition = 0;
                                    toPosition = firstPosition - 1;
                                    isForward = true;
                                    isBackward = false;
                                }
                            } else if (x > mScreenWidth - mEdgeSizeForShiftRhythm) {//���һ��
                                if (mLinearLayout.getChildCount() >= 1 + (firstPosition + 7)) {
                                    mCurrentItemPosition = 7;
                                    toPosition = firstPosition + 1;
                                    isForward = false;
                                    isBackward = true;
                                }
                            }
                            //�����µ��ǵ�һ����ʱ��isForwardΪtrue�����һ��ʱisBackwardΪtrue
                            if (isForward || isBackward) {
                                localList = getVisibleViews(isForward, isBackward);
                                final int finalToPosition = toPosition;
                                mHandler.post(new Runnable() {
                                    public void run() {
                                        makeItems(mCurrentItemPosition, localList);//����ÿ��Item�ĸ߶�
                                        scrollToPosition(finalToPosition, 200, 0, true);//����ScrollView��x�������
                                        vibrate(10L);
                                    }
                                });
                            }
                        }
                    }
                };
            }
            //200����֮��ʼִ�У�ÿ��250����ִ��һ��
            schedule(timerTask, 200L, 250L);
        }
    }

    /**
     * λ�Ƶ���ѡ�е�itemλ�ã���������Ӧ�Ķ���
     *
     * @param position ǰ����itemλ��
     */
    public void showRhythmAtPosition(int position) {
        //�����Ҫ�ƶ���λ�ú���һ��һ�����˳�����
        if (this.mLastDisplayItemPosition == position)
            return;
        //ScrollView�Ĺ�����λ�ƶ���
        Animator scrollAnimator;
        //item�ĵ��𶯻�
        Animator bounceUpAnimator;
        //item�Ľ��¶���
        Animator shootDownAnimator;

        if ((this.mLastDisplayItemPosition < 0) || (mAdapter.getCount() <= 7) || (position <= 3)) {
            //��ǰҪλ�Ƶ���λ��Ϊǰ3��ʱ�����ܵ�item����С��7��
            scrollAnimator = scrollToPosition(0, mScrollStartDelayTime, false);
        } else if (mAdapter.getCount() - position <= 3) {
            //��ǰҪλ�Ƶ���λ��Ϊ���3��
            scrollAnimator = scrollToPosition(mAdapter.getCount() - 7, mScrollStartDelayTime, false);
        } else {
            //��ǰλ�Ƶ���λ�üȲ���ǰ3��Ҳ���Ǻ�3��
            scrollAnimator = scrollToPosition(position - 3, mScrollStartDelayTime, false);
        }
        //��ȡ��Ӧitem���𶯻�
        bounceUpAnimator = bounceUpItem(position, false);
        //��ȡ��Ӧitem���¶���
        shootDownAnimator = shootDownItem(mLastDisplayItemPosition, false);
        //�����ϼ� ���𶯻��ͽ��¶��������
        AnimatorSet animatorSet1 = new AnimatorSet();
        if (bounceUpAnimator != null) {
            animatorSet1.playTogether(bounceUpAnimator);
        }
        if (shootDownAnimator != null) {
            animatorSet1.playTogether(shootDownAnimator);
        }
        //3�����������
        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.playSequentially(new Animator[]{scrollAnimator, animatorSet1});
        animatorSet2.start();
        mLastDisplayItemPosition = position;
    }


    /*
     * �õ�ÿ����ð���ؼ����Ŀ��
     */
    public float getRhythmItemWidth() {
        return mItemWidth;
    }

    /**
     * ���ü�����
     */
    public void setRhythmListener(IRhythmItemListener listener) {
        mListener = listener;
    }

    /**
     * ���ù��������ӳ�ִ��ʱ��
     *
     * @param scrollStartDelayTime �ӳ�ʱ�����Ϊ��λ
     */
    public void setScrollRhythmStartDelayTime(int scrollStartDelayTime) {
        this.mScrollStartDelayTime = scrollStartDelayTime;
    }
}
