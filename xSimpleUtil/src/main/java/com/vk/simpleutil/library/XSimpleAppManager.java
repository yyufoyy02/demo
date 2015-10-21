package com.vk.simpleutil.library;

import android.app.Activity;

import java.util.LinkedList;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class XSimpleAppManager {

    private LinkedList<Activity> mActivityList = new LinkedList<>();

    private XSimpleAppManager() {
    }

    // 单例模式实例化类
    private static class Holder {
        private static XSimpleAppManager instance = new XSimpleAppManager();
    }

    public static XSimpleAppManager getInstance() {
        return Holder.instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            mActivityList.remove(activity);
            if (!activity.isFinishing())
                activity.finish();
        }
    }
    public void removeActivityList(Activity activity) {
        if (activity != null) {
            mActivityList.remove(activity);
        }
    }
    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        while (mActivityList.size() > 0) {
            Activity activity = mActivityList.get(mActivityList.size() - 1);
            mActivityList.remove(mActivityList.size() - 1);
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }

    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
            System.exit(0);
        } catch (Exception e) {
        }
    }
}