package com.tz.niceappdemo.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import com.tz.niceappdemo.R;
import com.tz.niceappdemo.bean.Card;

/**
 * User: shine
 * Date: 2015-01-14
 * Time: 13:32
 * Description:
 */
public class RhythmAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;

    /**
     * item�Ŀ��
     */
    private float itemWidth;
    /**
     * ����Դ
     */
    private List<Card> mCardList;

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public RhythmAdapter(Context context, List<Card> cardList) {
        this.mContext = context;
        this.mCardList = new ArrayList();
        this.mCardList.addAll(cardList);
        if (context != null)
            this.mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return this.mCardList.size();
    }

    public Object getItem(int position) {
        return this.mCardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * ����ÿ��item�Ŀ��
     */
    public void setItemWidth(float width) {
        this.itemWidth = width;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout relativeLayout = (RelativeLayout) this.mInflater.inflate(R.layout.adapter_rhythm_icon, null);
        //����item���ֵĴ�С�Լ�Y���λ��
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams((int) itemWidth, mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_item_height)));
        relativeLayout.setTranslationY(itemWidth);

        //���õڶ���RelativeLayout���ֵĿ�͸�
        RelativeLayout childRelativeLayout = (RelativeLayout) relativeLayout.getChildAt(0);
        int relativeLayoutWidth = (int) itemWidth - 2 * mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_icon_margin);
        childRelativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(relativeLayoutWidth, mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_item_height) - 2 * mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_icon_margin)));

        ImageView imageIcon = (ImageView) relativeLayout.findViewById(R.id.image_icon);
        //����ImageView�Ĵ�С
        int iconSize = (relativeLayoutWidth - 2 * mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_icon_margin));
        ViewGroup.LayoutParams iconParams = imageIcon.getLayoutParams();
        iconParams.width = iconSize;
        iconParams.height = iconSize;
        imageIcon.setLayoutParams(iconParams);
        //���ñ���ͼƬ
        imageIcon.setBackgroundResource(R.drawable.ic_launcher);

        return relativeLayout;
    }
}


//  RelativeLayout localRelativeLayout2 = (RelativeLayout) relativeLayout.getChildAt(0);
//int i = (int) this.itemWidth - 2 * this.mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_icon_margin);
//  localRelativeLayout2.setLayoutParams(new RelativeLayout.LayoutParams(i, this.mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_item_height) - 2 * mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_icon_margin)));