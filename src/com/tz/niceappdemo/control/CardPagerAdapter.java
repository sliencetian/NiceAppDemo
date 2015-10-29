package com.tz.niceappdemo.control;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tz.niceappdemo.bean.Card;
import com.tz.niceappdemo.fragment.CardFragment;

/**
 * User: shine
 * Date: 2015-03-13
 * Time: 09:21
 * Description:
 */
@SuppressWarnings("unchecked")
public class CardPagerAdapter extends FragmentStatePagerAdapter {

    private List<Card> mCardList;
	@SuppressWarnings("rawtypes")
	private List<Fragment> mFragments = new ArrayList();

    public CardPagerAdapter(FragmentManager fragmentManager, List<Card> cardList) {
        super(fragmentManager);
        //使用迭代器遍历List,
        @SuppressWarnings("rawtypes")
		Iterator iterator = cardList.iterator();
        while (iterator.hasNext()) {
            Card card = (Card) iterator.next();
            //实例化相应的Fragment并添加到List中
            mFragments.add(CardFragment.getInstance(card));
        }
        mCardList = cardList;
    }

    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    public List<Card> getCardList() {
        return mCardList;
    }

    public List<Fragment> getFragments() {
        return mFragments;
    }
}
