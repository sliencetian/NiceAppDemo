package com.tz.niceappdemo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.extras.viewpager.PullToRefreshViewPager;
import com.tz.niceappdemo.R;
import com.tz.niceappdemo.bean.Card;
import com.tz.niceappdemo.control.CardPagerAdapter;
import com.tz.niceappdemo.control.IRhythmItemListener;
import com.tz.niceappdemo.control.RhythmAdapter;
import com.tz.niceappdemo.control.RhythmLayout;
import com.tz.niceappdemo.utils.AnimatorUtils;
import com.tz.niceappdemo.widget.ViewPagerScroller;

public class MainActivity extends FragmentActivity {

	/**
	 * ���ٲ���
	 */
	private RhythmLayout mRhythmLayout;

	/**
	 * ���ٲ��ֵ�������
	 */
	private RhythmAdapter mRhythmAdapter;

	/**
	 * ����PullToRefreshViewPager�е�ViewPager�ؼ�
	 */
	private ViewPager mViewPager;

	/**
	 * ���Բ���ˢ�µ�ViewPager����ʵ��һ��LinearLayout�ؼ�
	 */
	private PullToRefreshViewPager mPullToRefreshViewPager;

	/**
	 * ViewPager��������
	 */
	private CardPagerAdapter mPagerAdapter;

	/**
	 * ������View��Ϊ�����ñ�����ɫ��ʹ��
	 */
	private View mMainView;

	private List<Card> mCardList;

	/**
	 * ��¼��һ��ѡ�����ɫֵ
	 */
	private int mPreColor;

	private IRhythmItemListener iRhythmItemListener = new IRhythmItemListener() {
		@Override
		public void onSelected(final int position) {
			new Handler().postDelayed(new Runnable() {
				public void run() {
					mViewPager.setCurrentItem(position);
				}
			}, 100L);
		}
	};

	private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {
			int currColor = mCardList.get(position).getBackgroundColor();
			AnimatorUtils.showBackgroundColorAnimation(mMainView, mPreColor,
					currColor, 400);
			mPreColor = currColor;

			mMainView.setBackgroundColor(mCardList.get(position)
					.getBackgroundColor());
			mRhythmLayout.showRhythmAtPosition(position);
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		// ʵ�����ؼ�
		mMainView = findViewById(R.id.main_view);
		mRhythmLayout = (RhythmLayout) findViewById(R.id.box_rhythm);
		mPullToRefreshViewPager = (PullToRefreshViewPager) findViewById(R.id.pager);
		// ��ȡPullToRefreshViewPager�е�ViewPager�ؼ�
		mViewPager = mPullToRefreshViewPager.getRefreshableView();
		// ���ø��ٲ��ֵĸ߶� �߶�Ϊ���ٲ���item�Ŀ��+10dp
		int height = (int) mRhythmLayout.getRhythmItemWidth()
				+ (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
						10.0F, getResources().getDisplayMetrics());
		mRhythmLayout.getLayoutParams().height = height;

		((RelativeLayout.LayoutParams) this.mPullToRefreshViewPager
				.getLayoutParams()).bottomMargin = height;

		mCardList = new ArrayList<Card>();
		for (int i = 0; i < 30; i++) {
			Card card = new Card();
			// ���������ɫֵ
			card.setBackgroundColor((int) -(Math.random() * (16777216 - 1) + 1));
			mCardList.add(card);
		}
		// ����ViewPager��������
		mPagerAdapter = new CardPagerAdapter(getSupportFragmentManager(),
				mCardList);
		mViewPager.setAdapter(mPagerAdapter);
		// ���ø��ٲ��ֵ�������
		mRhythmAdapter = new RhythmAdapter(this, mCardList);
		mRhythmLayout.setAdapter(mRhythmAdapter);

		// ����ViewPager�Ĺ����ٶ�
		setViewPagerScrollSpeed(this.mViewPager, 400);

		// ���ÿؼ��ļ���
		mRhythmLayout.setRhythmListener(iRhythmItemListener);
		mViewPager.setOnPageChangeListener(onPageChangeListener);
		// ����ScrollView���������ӳ�ִ�е�ʱ��
		mRhythmLayout.setScrollRhythmStartDelayTime(400);

		// ��ʼ��ʱ����һ����ñ����,�����ñ�����ɫ
		mRhythmLayout.showRhythmAtPosition(0);
		mPreColor = mCardList.get(0).getBackgroundColor();
		mMainView.setBackgroundColor(mPreColor);
	}

	/**
	 * ����ViewPager�Ĺ����ٶȣ���ÿ��ѡ����л��ٶ�
	 * 
	 * @param viewPager
	 *            ViewPager�ؼ�
	 * @param speed
	 *            �����ٶȣ�����Ϊ��λ
	 */
	private void setViewPagerScrollSpeed(ViewPager viewPager, int speed) {
		try {
			Field field = ViewPager.class.getDeclaredField("mScroller");
			field.setAccessible(true);
			ViewPagerScroller viewPagerScroller = new ViewPagerScroller(
					viewPager.getContext(), new OvershootInterpolator(0.6F));
			field.set(viewPager, viewPagerScroller);
			viewPagerScroller.setDuration(speed);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
