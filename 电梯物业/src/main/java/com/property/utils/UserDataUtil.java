package com.property.utils;

import android.content.Context;

import com.property.model.UserModel;
import com.vk.simpleutil.http.XSimpleHttpUtil;
import com.vk.simpleutil.library.XSimpleACache;
import com.vk.simpleutil.webview.XSimpleWebView;

public class UserDataUtil {
    private Context mContext;
    private UserModel mOwenUser;
    private final static String mCache_mUser = "Cache_User";

    private static class Holder {
        private static UserDataUtil instance = new UserDataUtil();
    }

    private UserDataUtil() {
    }

    public static UserDataUtil getInstance() {
        return Holder.instance;
    }

    /**
     * 用户信息初始化
     */
    public void initData(Context mContexts) {
        mContext = mContexts.getApplicationContext();
        initUser();
    }

    private void initUser() {
        mOwenUser = getUserCache();
    }

    /**
     * 更新用户缓存
     */
    private void putUserCache() {
        XSimpleACache.get(mContext).put(mCache_mUser, mOwenUser);
    }

    /**
     * 获取用户缓存
     */
    private UserModel getUserCache() {
        return (UserModel) XSimpleACache.get(mContext).getAsObject(mCache_mUser);
    }

    /**
     * 退出登录
     */
    public void loginOut() {
//        Util.setLoginStatus(false);
        XSimpleHttpUtil.getInstance().clearCookie();
        mOwenUser = null;
        XSimpleACache.get(mContext).remove(mCache_mUser);

    }

    /**
     * 登录
     */
    public void login(UserModel mOwenUser) {

//        Util.setLoginStatus(true);
        XSimpleWebView.initCookies(mContext, XSimpleHttpUtil.getInstance().getCookie()
                .getCookies());
        setUserData(mOwenUser);

    }

    /**
     * 设置个人用户信息
     */
    public void setUserData(UserModel mOwenUser) {
        if (mOwenUser != null) {
            this.mOwenUser = mOwenUser;
            putUserCache();
        }
    }

    /**
     * 获取个人用户信息
     */
    public UserModel getUserData() {
        if (mOwenUser != null) {
            return mOwenUser;
        } else {
            return new UserModel();
        }
    }

    public String getStaff_id() {
        if (mOwenUser != null) {
            return mOwenUser.getStaff_id();
        } else {
            return "";
        }
    }


    public interface UserIsLoginInterface {
        void LoginSuccess();
    }


}
