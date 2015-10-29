package com.tz.niceappdemo.fragment;

import com.tz.niceappdemo.R;
import com.tz.niceappdemo.bean.Card;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * User: shine
 * Date: 2015-03-13
 * Time: 09:32
 * Description:
 */
public class CardFragment extends Fragment {

    public static CardFragment getInstance(Card card) {
        CardFragment fragment = new CardFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("card", card);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card, null);
    }
}
