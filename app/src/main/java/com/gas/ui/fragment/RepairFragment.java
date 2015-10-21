package com.property.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.property.adapter.CommonAdapter;
import com.property.adapter.ViewHolder;
import com.property.conf.Common;
import com.property.connector.HttpCallBack;
import com.property.connector.protocol.BusinessHttpProtocol;
import com.property.database.SharedPreferenceUtil;
import com.property.entity.RepairOrder;
import com.property.epiboly.MainActivity;
import com.property.epiboly.R;
import com.property.ui.activity.repairDetail;
import com.property.ui.common.BaseFragment;
import com.property.utils.TimeFormat;
import com.property.utils.Utils;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Heart on 2015/7/22.
 */
public class RepairFragment extends BaseFragment implements HttpCallBack {
    private int REQUEST_CODE_HISTORY = 0x00003;
    private int REQUEST_CODE_ACCEPT = 0x00002;
    private int REQUEST_CODE_UNACCEPT = 0x00001;

    private SharedPreferenceUtil sharedPreferenceUtil;


    //当前显示list
    private LinkedList<RepairOrder> currentList;

    private View loading_progress_layout;
    private List<RepairOrder> historyDatas = new LinkedList<>();
    private List<RepairOrder> accpetDatas = new LinkedList<>();
    private List<RepairOrder> unaccpetDatas = new LinkedList<>();

    private PullToRefreshListView accpetListView;
    private PullToRefreshListView historyListView;
    private PullToRefreshListView unaccpetListView;

    private CommonAdapter<RepairOrder> historyAdapter;
    private CommonAdapter<RepairOrder> accpetAdapter;
    private CommonAdapter<RepairOrder> unaccpetAdapter;

    private RadioGroup group_status_selector;
    protected View rootView;
    private Activity mActivity;

    private long FINISH_DOWN_FLAG = 0;
    private long FINISH_UP_FLAG = 0;
    private long ON_DOWN_FLAG = 0;
    private long ON_UP_FLAG = 0;
    private long NEW_DOWN_FLAG = 0;
    private long NEW_UP_FLAG = 0;
    private long EMPTY_FLAG = -1;
    private long DOWN_FLAG = 0;
    private long UP_FLAG = 0;
    private long referenceTime = 0;

    private String DOWN_STATE = "1";
    private String UP_STATE = "0";
    private int currentViewPosition = 0;  //0未接订单  1 已接订单  2 历史订单
    private Handler handler = new Handler();


    private View emptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delivery_order, container,
                false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = this.getActivity();
        rootView = getView();
        sharedPreferenceUtil = SharedPreferenceUtil.getInstance(mActivity);
        init();
        initListener();

        showListView(currentViewPosition);
        MainActivity.showLoading();
        referenceList(currentViewPosition);
    }


    //读取缓存
    public void getRecordToBuffer() {

        String historyJsonArray = SharedPreferenceUtil.getInstance(mActivity).getString(SharedPreferenceUtil.REPAIRORDER_HISTORY);
        String accpetJsonArray = SharedPreferenceUtil.getInstance(mActivity).getString(SharedPreferenceUtil.REPAIRORDER_ACCPET);
        String unaccpetyJsonArray = SharedPreferenceUtil.getInstance(mActivity).getString(SharedPreferenceUtil.REPAIRORDER_UNACCPET);

        List<RepairOrder> tempList = gson.fromJson(historyJsonArray, new TypeToken<List<RepairOrder>>() {
        }.getType());
        if (tempList != null)
            historyDatas.addAll(tempList);

        tempList = gson.fromJson(unaccpetyJsonArray, new TypeToken<List<RepairOrder>>() {
        }.getType());
        if (tempList != null)
            unaccpetDatas.addAll(tempList);

        tempList = gson.fromJson(accpetJsonArray, new TypeToken<List<RepairOrder>>() {
        }.getType());
        if (tempList != null)
            accpetDatas.addAll(tempList);

        if (currentViewPosition == 2)
            currentList = (LinkedList) historyDatas;
        else if (currentViewPosition == 1)
            currentList = (LinkedList) accpetDatas;
        else if (currentViewPosition == 0)
            currentList = (LinkedList) unaccpetDatas;
    }

    public void init() {
        getRecordToBuffer();
        emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.ly_empty_view, null);
        historyListView = (PullToRefreshListView) rootView.findViewById(R.id.pull_refresh_history_list);
        accpetListView = (PullToRefreshListView) rootView.findViewById(R.id.pull_refresh_accpet_list);
        unaccpetListView = (PullToRefreshListView) rootView.findViewById(R.id.pull_refresh_un_accpet_list);
        loading_progress_layout = rootView.findViewById(R.id.loading_progress_layout);
        unaccpetAdapter = new CommonAdapter<RepairOrder>(mActivity, unaccpetDatas, R.layout.item_delivery_order_history) {
            @Override
            public void convert(final ViewHolder helper, RepairOrder item) {
                helper.setText(R.id.customer_address, item.getAddress());
                helper.setText(R.id.customer_name, item.getClient_name());
                helper.setText(R.id.order_no, "订单序号：" + item.getId());

                helper.setText(R.id.service_date, TimeFormat.convertTimeLong2String(item.getRepair_date() * 1000, Calendar.DATE));
                helper.setText(R.id.service_time, item.getRepair_time());
                helper.setText(R.id.ctime, TimeFormat.convertTimeLong2String(item.getCtime() * 1000, Calendar.DATE));
                if (helper.getPosition() % 2 == 0) {
                    helper.getView(R.id.ly_item).setBackgroundColor(getResources().getColor(R.color.white));
                }
            }
        };
        accpetAdapter = new CommonAdapter<RepairOrder>(mActivity, accpetDatas, R.layout.item_delivery_order_history) {
            @Override
            public void convert(final ViewHolder helper, RepairOrder item) {
                helper.setText(R.id.customer_address, item.getAddress());
                helper.setText(R.id.customer_name, item.getClient_name());
                helper.setText(R.id.order_no, "订单序号：" + item.getId());

                helper.setText(R.id.service_date, TimeFormat.convertTimeLong2String(item.getRepair_date() * 1000, Calendar.DATE));
                helper.setText(R.id.service_time, item.getRepair_time());
                helper.setText(R.id.ctime, TimeFormat.convertTimeLong2String(item.getCtime() * 1000, Calendar.DATE));
                if (helper.getPosition() % 2 == 0) {
                    helper.getView(R.id.ly_item).setBackgroundColor(getResources().getColor(R.color.white));
                }
            }
        };
        historyAdapter = new CommonAdapter<RepairOrder>(mActivity, historyDatas, R.layout.item_delivery_order_history) {
            @Override
            public void convert(ViewHolder helper, RepairOrder item) {
                helper.setText(R.id.customer_address, item.getAddress());
                helper.setText(R.id.customer_name, item.getClient_name());
                helper.setText(R.id.order_no, "订单序号：" + item.getId());

                helper.setText(R.id.service_date, TimeFormat.convertTimeLong2String(item.getRepair_date() * 1000, Calendar.DATE));
                helper.setText(R.id.service_time, item.getRepair_time());
                helper.setText(R.id.ctime, TimeFormat.convertTimeLong2String(item.getCtime() * 1000, Calendar.DATE));
                if (helper.getPosition() % 2 == 0) {
                    helper.getView(R.id.ly_item).setBackgroundColor(getResources().getColor(R.color.white));
                }
            }
        };
        group_status_selector = (RadioGroup) rootView.findViewById(R.id.group_status_selector);

        unaccpetListView.setAdapter(unaccpetAdapter);
        unaccpetListView.setMode(PullToRefreshBase.Mode.BOTH);
        accpetListView.setAdapter(accpetAdapter);
        accpetListView.setMode(PullToRefreshBase.Mode.BOTH);
        historyListView.setAdapter(historyAdapter);
        historyListView.setMode(PullToRefreshBase.Mode.BOTH);
    }

    public void initListener() {
        unaccpetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                repairDetail.launchActivity(RepairFragment.this, REQUEST_CODE_UNACCEPT, unaccpetDatas.get(position - 1));
            }
        });
        accpetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                repairDetail.launchActivity(RepairFragment.this, REQUEST_CODE_ACCEPT, accpetDatas.get(position - 1));
            }
        });

        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                repairDetail.launchActivity(RepairFragment.this, REQUEST_CODE_HISTORY, historyDatas.get(position - 1));
            }
        });

        unaccpetListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Utils.log("down", "yeah");
                referenceTime = System.currentTimeMillis() / 1000;
                NEW_DOWN_FLAG = BusinessHttpProtocol.newRepairOrder(RepairFragment.this, Common.getInstance().user, 0, DOWN_STATE);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                referenceTime = System.currentTimeMillis() / 1000;
                if (currentList.size() > 0)
                    NEW_UP_FLAG = BusinessHttpProtocol.newRepairOrder(RepairFragment.this, Common.getInstance().user, currentList.getLast().getId(), UP_STATE);
            }
        });


        accpetListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Utils.log("down", "yeah");
                referenceTime = System.currentTimeMillis() / 1000;
                ON_DOWN_FLAG = BusinessHttpProtocol.onRepairOrder(RepairFragment.this, Common.getInstance().user, 0, DOWN_STATE);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Utils.log("down", "yeah");
                referenceTime = System.currentTimeMillis() / 1000;

                if (currentList.size() > 0)
                    ON_UP_FLAG = BusinessHttpProtocol.onRepairOrder(RepairFragment.this, Common.getInstance().user, currentList.getLast().getId(), UP_STATE);
            }
        });

        historyListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                referenceTime = System.currentTimeMillis() / 1000;
                FINISH_DOWN_FLAG = BusinessHttpProtocol.repairOrderHistory(RepairFragment.this, Common.getInstance().user, 0, DOWN_STATE);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                referenceTime = System.currentTimeMillis() / 1000;
                if (currentList.size() > 0)
                    FINISH_UP_FLAG = BusinessHttpProtocol.repairOrderHistory(RepairFragment.this, Common.getInstance().user, currentList.getLast().getId(), UP_STATE);
            }
        });

        group_status_selector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onRefreshComplete();
                switch (checkedId) {
                    case R.id.radio_un_accpet:
                        showListView(0);
                        break;
                    case R.id.radio_accpet:
                        showListView(1);
                        break;
                    case R.id.radio_history:
                        showListView(2);
                        break;
                }
            }
        });

        emptyView.findViewById(R.id.bt_again_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentViewPosition) {
                    case 0:
                        NEW_DOWN_FLAG = BusinessHttpProtocol.newRepairOrder(RepairFragment.this, Common.getInstance().user, 0, DOWN_STATE);
                        showProgressDialog(NEW_DOWN_FLAG);
                        EMPTY_FLAG = NEW_DOWN_FLAG;
                        break;
                    case 1:
                        ON_DOWN_FLAG = BusinessHttpProtocol.onRepairOrder(RepairFragment.this, Common.getInstance().user, 0, DOWN_STATE);
                        showProgressDialog(ON_DOWN_FLAG);
                        EMPTY_FLAG = ON_DOWN_FLAG;
                        break;
                    case 2:
                        FINISH_DOWN_FLAG = BusinessHttpProtocol.repairOrderHistory(RepairFragment.this, Common.getInstance().user, 0, DOWN_STATE);
                        showProgressDialog(FINISH_DOWN_FLAG);
                        EMPTY_FLAG = FINISH_DOWN_FLAG;
                        break;
                }
            }
        });
    }

    public void showListView(int position) {
        historyListView.setVisibility(View.GONE);
        unaccpetListView.setVisibility(View.GONE);
        accpetListView.setVisibility(View.GONE);

        switch (position) {
            case 0:
                currentViewPosition = 0;
                unaccpetListView.setVisibility(View.VISIBLE);
                group_status_selector.check(R.id.radio_un_accpet);
                unaccpetListView.setEmptyView(emptyView);
                currentList = (LinkedList) unaccpetDatas;
                break;
            case 1:
                currentViewPosition = 1;
                accpetListView.setVisibility(View.VISIBLE);
                group_status_selector.check(R.id.radio_accpet);
                accpetListView.setEmptyView(emptyView);
                currentList = (LinkedList) accpetDatas;
                break;
            case 2:
                currentViewPosition = 2;
                historyListView.setVisibility(View.VISIBLE);
                group_status_selector.check(R.id.radio_history);
                historyListView.setEmptyView(emptyView);
                currentList = (LinkedList) historyDatas;
                break;
        }
    }


    @Override
    public void onGeneralSuccess(final String result, final long flag) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.hidenLoading();
                onRefreshComplete();
                try {
                    JSONObject json = new JSONObject(result);

                    List<RepairOrder> tempList = new LinkedList<RepairOrder>();
                    tempList = gson.fromJson(json.getString("all"), new TypeToken<List<RepairOrder>>() {
                    }.getType());

                    if (EMPTY_FLAG == flag) {
                        dismissProgressDialog();

                        if (tempList.size() == 0)
                            Utils.toastMsg(getActivity(), "没有更多数据");
                    }


                    if (flag == FINISH_DOWN_FLAG) {
                        sharedPreferenceUtil.putString(SharedPreferenceUtil.REPAIRORDER_HISTORY, json.getString("all"));
                        historyDatas.clear();
                        historyDatas.addAll(tempList);
                        historyAdapter.notifyDataSetChanged();
                    } else if (flag == ON_DOWN_FLAG) {
                        sharedPreferenceUtil.putString(SharedPreferenceUtil.REPAIRORDER_ACCPET, json.getString("all"));
                        accpetDatas.clear();
                        accpetDatas.addAll(tempList);
                        accpetAdapter.notifyDataSetChanged();
                    } else if (flag == NEW_DOWN_FLAG) {
                        sharedPreferenceUtil.putString(SharedPreferenceUtil.REPAIRORDER_UNACCPET, json.getString("all"));
                        unaccpetDatas.clear();
                        unaccpetDatas.addAll(tempList);
                        unaccpetAdapter.notifyDataSetChanged();
                    }


                    if (flag == FINISH_UP_FLAG) {
                        historyDatas.addAll(tempList);
                        historyAdapter.notifyDataSetChanged();
                        if (historyListView.isShown() && tempList.size() == 0)
                            Utils.toastMsg(getActivity(), "没有更多数据");
                    } else if (flag == ON_UP_FLAG) {
                        accpetDatas.addAll(tempList);
                        accpetAdapter.notifyDataSetChanged();
                        if (accpetListView.isShown() && tempList.size() == 0)
                            Utils.toastMsg(getActivity(), "没有更多数据");
                    } else if (flag == NEW_UP_FLAG) {
                        unaccpetDatas.addAll(tempList);
                        unaccpetAdapter.notifyDataSetChanged();
                        if (accpetListView.isShown() && tempList.size() == 0)
                            Utils.toastMsg(getActivity(), "没有更多数据");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, ((System.currentTimeMillis() / 1000) - referenceTime) < 2000 ? 2000 : 100);


    }

    @Override
    public void onGeneralError(final String e, long flag) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.hidenLoading();
                dismissProgressDialog();
                onRefreshComplete();
                Utils.toastMsg(getActivity(), e);
            }
        }, ((System.currentTimeMillis() / 1000) - referenceTime) < 2000 ? 2000 : 100);

    }

    public void onRefreshComplete() {
        accpetListView.onRefreshComplete();
        historyListView.onRefreshComplete();
        unaccpetListView.onRefreshComplete();
    }


    public void referenceList(int position) {
        switch (position) {
            case 0:
                unaccpetListView.setRefreshing(false);
                break;
            case 1:
                accpetListView.setRefreshing(false);
                break;
            case 2:
                historyListView.setRefreshing(false);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Utils.log("czd", requestCode + "  " + resultCode);

        //2数据发生变化
        if (resultCode == 2) {
            RepairOrder deliveryOrder = data.getParcelableExtra("itemOrder");
            if (requestCode == REQUEST_CODE_UNACCEPT) {
                for (int i = 0; i < unaccpetDatas.size(); i++) {
                    RepairOrder temp = unaccpetDatas.get(i);
                    if (temp.getId() == deliveryOrder.getId()) {
                        unaccpetDatas.remove(temp);
                    }
                }
                unaccpetAdapter.notifyDataSetChanged();
                referenceList(1);
                return;
            } else if (requestCode == REQUEST_CODE_ACCEPT) {
                accpetDatas.remove(data.getParcelableExtra("itemOrder"));
                for (int i = 0; i < accpetDatas.size(); i++) {
                    RepairOrder temp = accpetDatas.get(i);
                    if (temp.getId() == deliveryOrder.getId()) {
                        accpetDatas.remove(temp);
                    }
                }
                accpetAdapter.notifyDataSetChanged();
                referenceList(2);
                return;
            } else if (requestCode == REQUEST_CODE_HISTORY) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        /**
         * order_type 1为送气；2为抢修； must_get 1为系统派送（不可拒绝）；0反之
         */

        if (!hidden) {
            if (Common.order_type == 2 && Common.must_get == 0) {
                currentViewPosition = 0;
                showListView(currentViewPosition);
                referenceList(currentViewPosition);
                Common.order_type = -1;
                Common.must_get = -1;
                Common.repairCount = 0;
                Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
                msgIntent.putExtra(MainActivity.KEY_MESSAGE, true);
                getActivity().sendBroadcast(msgIntent);
                //   EventBus.getInstatnce().post(true);
            } else if (Common.order_type == 2 && Common.must_get == 1) {
                currentViewPosition = 1;
                showListView(currentViewPosition);
                referenceList(currentViewPosition);
                Common.order_type = -1;
                Common.must_get = -1;
                Common.repairAccept = 0;
                //    EventBus.getInstatnce().post(true);

                Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
                msgIntent.putExtra(MainActivity.KEY_MESSAGE, true);
                getActivity().sendBroadcast(msgIntent);
            }
        }
    }
}
