package com.gas.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Heart on 2015/7/16.
 */
/**
 * 一个简单的定时器，使用Handler实现，比Timer更实用 也可用CountDownTimer来代替
 */
public abstract class LightTimer
{
    private Handler mHandler = new Handler( Looper.getMainLooper( ) );
    private LightRunnable mRunnable = new LightRunnable( );
    private int mDelayTime;
    private int mLoopTime;
    private boolean mIsRun;
    private int mRunBout;// 可运行次数
    private int mRunCount;// 已运行次数

    /**
     * 延时开始计时，单位毫秒
     *
     * @param delaytime
     *            延时开始执行
     * @param looptime
     *            循环执行等待时间
     */
    public void startTimerDelay( int delaytime , int looptime )
    {
        start( delaytime , looptime , Integer.MAX_VALUE );
    }

    /**
     * 延时开始计时，单位毫秒
     *
     * @param delaytime
     *            延时开始执行
     * @param looptime
     *            循环执行等待时间
     * @param bout
     *            循环执行次数
     */
    public void startTimerDelay( int delaytime , int looptime , int bout )
    {
        start( delaytime , looptime , bout );
    }

    /**
     * 延时delaytime后，执行一次
     *
     * @param delaytime
     */
    public void startTimerDelay( int delaytime )
    {
        start( delaytime , 0 , 1 );
    }

    /**
     * 延时开始计时，单位毫秒
     *
     * @param looptime
     *            循环执行等待时间
     */
    public void startTimer( int looptime )
    {
        start( 0 , looptime , Integer.MAX_VALUE );
    }

    /**
     * 延时开始计时，单位毫秒
     *
     * @param looptime
     *            循环执行等待时间
     * @param runbout
     *            循环执行次数
     */
    public void startTimer( int looptime , int runbout )
    {
        start( 0 , looptime , runbout );
    }

    /**
     * 延时开始计时，单位毫秒
     *
     * @param delaytime
     *            延时开始执行
     * @param looptime
     *            循环执行等待时间
     * @param runbout
     *            循环执行次数
     */
    private void start( int delaytime , int looptime , int runbout )
    {
        if ( delaytime < 0 || looptime < 0 || runbout < 1 )
        {
            return;
        }

        if ( !mIsRun )
        {
            mIsRun = true;
            mRunBout = runbout;
            mDelayTime = delaytime;
            mLoopTime = looptime;
            mRunnable.shouldRun = true;
            start( );
        }
    }

    private void start( )
    {
        mRunCount = 0;
        mHandler.postDelayed( mRunnable , mDelayTime );
    }

    /**
     * 终止计时器
     */
    public void stop( )
    {
        if ( mIsRun )
        {
            mRunnable.shouldRun = false;
            mHandler.removeCallbacks( mRunnable );
            mIsRun = false;
        }
    }

    /**
     * 获取已经运行的次数
     */
    public int getRunCount( )
    {
        return mRunCount;
    }

    private class LightRunnable implements Runnable
    {
        public boolean shouldRun = true;

        public void run( )
        {
            if ( shouldRun && mRunCount++ < mRunBout )
            {
                LightTimer.this.run( LightTimer.this );
            }else {
                mHandler.removeCallbacks( this );
                return;
            }
            mHandler.removeCallbacks( this );
            mHandler.postDelayed( this , mLoopTime );
        }
    }

    /**
     * 循环执行方法
      *            已经运行的次数
     */
    public abstract void run( LightTimer timer );

}