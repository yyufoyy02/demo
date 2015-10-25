package com.property.adapter;

import android.content.Context;
import android.view.View;

import com.property.activity.R;
import com.property.model.StatisticsModel;
import com.vk.simpleutil.adapter.XSimpleAdapter;

import java.util.List;

/**
 * Created by Administrator on 2015/10/25.
 */
public class StatisticsAdapter extends XSimpleAdapter<StatisticsModel> {
    public StatisticsAdapter(Context context, List<StatisticsModel> mData) {
        super(context, mData, R.layout.statistics_item);
    }

    @Override
    public View convert(View convertView, StatisticsModel item, int position) {
        return convertView;
    }
}
