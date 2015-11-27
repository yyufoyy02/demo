package com.property.activity;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.property.api.StatisticsApi;
import com.property.base.BaseActivity;
import com.property.http.MyJsonDataResponseCacheHandler;
import com.property.model.CountModel;
import com.vk.simpleutil.library.XSimpleTime;

import java.util.Calendar;

import butterknife.InjectView;
import butterknife.annotation.event.OnClick;

public class StatisticsActivity extends BaseActivity {


    @InjectView(R.id.tv_statistics_begintime)
    TextView tvStatisticsBegintime;
    @InjectView(R.id.tv_statistics_endtime)
    TextView tvStatisticsEndtime;
    @InjectView(R.id.tv_statistics_maintenancecount)
    TextView tvStatisticsMaintenancecount;
    @InjectView(R.id.tv_statistics_faultcount)
    TextView tvStatisticsFaultcount;
    DatePickerDialog datePickerDialog;
    int beginYear, beginMouth, beginDay;
    int endYear, endMouth, endDay;

    @Override
    public int onCreateViewLayouId() {
        return R.layout.statistics_activity;
    }

    @Override
    public void initAllData() {
        setTitle("数据统计");
        initView();
        getStatistics(0, 0);
    }

    private void initView() {
        beginYear = Calendar.getInstance().get(Calendar.YEAR);
        beginMouth = Calendar.getInstance().get(Calendar.MONTH);
        beginDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        endYear = beginYear;
        endMouth = beginMouth;
        endDay = beginDay;
        int mouth = beginMouth + 1;
        tvStatisticsBegintime.setText(beginYear + "-" + mouth + "-" + beginDay);
        tvStatisticsEndtime.setText(endYear + "-" + mouth + "-" + endDay);
    }

    @OnClick({R.id.tv_statistics_begintime, R.id.tv_statistics_endtime, R.id.tv_statistics_timesubmit})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_statistics_begintime:
                mContext.setTheme(R.style.AppTheme);
                datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        beginYear = i;
                        beginMouth = i1;
                        beginDay = i2;
                        int mouth = beginMouth + 1;
                        tvStatisticsBegintime.setText(beginYear + "-" + mouth + "-" + beginDay);
                        if (XSimpleTime.getTimestampFromString(tvStatisticsBegintime.getText().toString(), "yyyy-MM-dd") >
                                (double) XSimpleTime.getTimestampFromString(tvStatisticsEndtime.getText().toString(), "yyyy-MM-dd")) {
                            endYear = beginYear;
                            endMouth = beginMouth;
                            endDay = beginDay;
                            tvStatisticsEndtime.setText(beginYear + "-" + mouth + "-" + beginDay);
                        }
                    }
                }, beginYear, beginMouth, beginDay);
                datePickerDialog.show();
                break;
            case R.id.tv_statistics_endtime:
                mContext.setTheme(R.style.AppTheme);
                datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        endYear = i;
                        endMouth = i1;
                        endDay = i2;
                        int mouth = endMouth + 1;
                        tvStatisticsEndtime.setText(endYear + "-" + mouth + "-" + endDay);
                        if (XSimpleTime.getTimestampFromString(tvStatisticsBegintime.getText().toString(), "yyyy-MM-dd") >
                                (double) XSimpleTime.getTimestampFromString(tvStatisticsEndtime.getText().toString(), "yyyy-MM-dd")) {
                            endYear = beginYear;
                            endMouth = beginMouth;
                            endDay = beginDay;
                            tvStatisticsEndtime.setText(beginYear + "-" + mouth + "-" + beginDay);
                        }
                    }
                }, endYear, endMouth, endDay);
                datePickerDialog.show();
                break;
            case R.id.tv_statistics_timesubmit:
                getStatistics((double) XSimpleTime.getTimestampFromString(tvStatisticsBegintime.getText().toString(), "yyyy-MM-dd")
                        , (double) XSimpleTime.getTimestampFromString(tvStatisticsEndtime.getText().toString(), "yyyy-MM-dd"));
                break;
        }
    }

    void initStatisticsView(CountModel countModel) {
        tvStatisticsMaintenancecount.setText(countModel.getMaintenance_count() + "");
        tvStatisticsFaultcount.setText(countModel.getFault_count() + "");
    }

    void getStatistics(double begin_time, double end_time) {
        showProgressDialog();
        StatisticsApi.getInstance().getStatistics(mContext, begin_time, end_time, new MyJsonDataResponseCacheHandler<CountModel>(CountModel.class,
                begin_time == 0 && end_time == 0 ? true : false) {
            @Override
            public void onHttpComplete() {
                super.onHttpComplete();
                dismissProgressDialog();
            }

            @Override
            public void onJsonDataSuccess(CountModel object) {

                initStatisticsView(object);
            }

            @Override
            public boolean onJsonCacheData(boolean has) {
                return false;
            }
        });
    }

    @Override
    public void initListener() {

    }


}
