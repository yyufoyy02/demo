package com.property.utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Heart on 2015/8/11.
 */
public class EventBus {
    private Handler mHandler;

    private static EventBus eventBus = new EventBus();

    /*
     * map，存储的方法
     */
    private static Map<Class, CopyOnWriteArrayList<SubscribeMethod>> mSubscribeMethodsByEventType = new HashMap<Class, CopyOnWriteArrayList<SubscribeMethod>>();

    private static ThreadLocal<PostingThread> mPostingThread = new ThreadLocal<PostingThread>() {
        @Override
        public PostingThread get() {
            return new PostingThread();
        }
    };

    public static EventBus getInstatnce() {
        return eventBus;
    }

    private EventBus() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void register(Object subscriber) {

        Class clazz = subscriber.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        CopyOnWriteArrayList<SubscribeMethod> subscribeMethods = null;
        /**
         * 遍历所有方法
         */
        for (Method method : methods) {
            String methodName = method.getName();
            /**
             * 判断方法是否以onEvent的开头
             */
            if (methodName.startsWith("onEvent")) {
                SubscribeMethod subscribeMethod = null;
                // 方法命中提前在什么线程运行。默认在UI线程
                String threadMode = methodName.substring("onEvent".length());
                ThreadMode mode = ThreadMode.UI;

                Class<?>[] parameterTypes = method.getParameterTypes();

                // 参数的个数为1
                if (parameterTypes.length == 1) {
                    Class<?> eventType = parameterTypes[0];

                    synchronized (this) {

                        if (mSubscribeMethodsByEventType.containsKey(eventType)) {
                            subscribeMethods = mSubscribeMethodsByEventType
                                    .get(eventType);
                        } else {
                            subscribeMethods = new CopyOnWriteArrayList<SubscribeMethod>();
                            mSubscribeMethodsByEventType.put(eventType,
                                    subscribeMethods);
                        }
                    }

                    if (threadMode.equals("Async")) {
                        mode = ThreadMode.Async;
                    }
                    // 提取出method，mode，方法所在类对象，存数的类型封装为SubscribeMethod
                    subscribeMethod = new SubscribeMethod(method, mode,
                            subscriber);
                    subscribeMethods.add(subscribeMethod);
                }
            }

        }
    }

    public void unregister(Object subscriber) {
        Class clazz = subscriber.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        List<SubscribeMethod> subscribeMethods = null;

        for (Method method : methods) {
            String methodName = method.getName();

            if (methodName.startsWith("onEvent")) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1) {
                    synchronized (this) {
                        mSubscribeMethodsByEventType.remove(parameterTypes);
                    }
                }
            }
        }

    }

    public void post(Object eventTypeInstance) {
        // 拿到该线程中的PostingThread对象
        PostingThread postingThread = mPostingThread.get();
        postingThread.isMainThread = Looper.getMainLooper() == Looper
                .myLooper();
        // 将事件加入事件队列
        List<Object> eventQueue = postingThread.mEventQueue;
        eventQueue.add(eventTypeInstance);
        // 防止多次调用
        if (postingThread.isPosting) {
            return;
        }
        postingThread.isPosting = true;
        // 取出所有事件进行调用
        while (!eventQueue.isEmpty()) {
            Object eventType = eventQueue.remove(0);
            postEvent(eventType, postingThread);
        }
        postingThread.isPosting = false;

    }

    private void postEvent(final Object eventType, PostingThread postingThread) {
        CopyOnWriteArrayList<SubscribeMethod> subscribeMethods = null;
        synchronized (this) {
            subscribeMethods = mSubscribeMethodsByEventType.get(eventType
                    .getClass());
        }

        for (final SubscribeMethod subscribeMethod : subscribeMethods) {

            if (subscribeMethod.threadMode == ThreadMode.UI) {
                if (postingThread.isMainThread) {
                    invokeMethod(eventType, subscribeMethod);
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            invokeMethod(eventType, subscribeMethod);
                        }
                    });
                }
            } else {
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        invokeMethod(eventType, subscribeMethod);
                        return null;
                    }
                };

            }

        }

    }

    private void invokeMethod(Object eventType, SubscribeMethod subscribeMethod) {
        try {
            subscribeMethod.method
                    .invoke(subscribeMethod.subscriber, eventType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

enum ThreadMode {
    UI, Async
}

class SubscribeMethod {
    Method method;
    ThreadMode threadMode;
    Object subscriber;

    public SubscribeMethod(Method method, ThreadMode threadMode,
                           Object subscriber) {
        this.method = method;
        this.threadMode = threadMode;
        this.subscriber = subscriber;
    }

}

class PostingThread {
    List<Object> mEventQueue = new ArrayList<Object>();
    boolean isMainThread;
    boolean isPosting;
}