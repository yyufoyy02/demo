package com.gas;

import android.app.Activity;

import com.gas.epiboly.MainActivity;
import com.gas.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class CloseAllActivity {

	public static final int EXIT_APPLICATION = 0x0001;
	private LinkedList<Activity> mActs;
	private static CloseAllActivity instance = null;

	private CloseAllActivity() {
		mActs = new LinkedList<Activity>();
	}

	public static CloseAllActivity getInstance() {
		if (instance == null) {
			synchronized (CloseAllActivity.class) {
				if (instance == null) {
					instance = new CloseAllActivity();
				}
			}
		}
		return instance;

	}

	public void addActivity(Activity act) {
		synchronized (CloseAllActivity.this) {
			mActs.addFirst(act);
		}
	}

	public void removeActivity(Activity act) {
		synchronized (CloseAllActivity.this) {
			if (mActs != null && mActs.indexOf(act) >= 0) {
				mActs.remove(act);
			}
		}
	}

	/**
	 * ����activityʹ�ñ�־:Intent.FLAG_ACTIVITY_REORDER_TO_FRONT, ʹ����������activity��ʽ��
	 * ���activity ջ������ڵ�activity�������� ��Ҫ��CloseActivity��Ӧ��activity�ƶ�������
	 */
	public void reorderActivityToFront(Activity act) {
		synchronized (CloseAllActivity.this) {
			if (mActs != null && mActs.indexOf(act) > 0) {
				mActs.remove(act);
				mActs.addFirst(act);
			}
		}
	}

	public Activity getTopActivity() {
		synchronized (CloseAllActivity.this) {
			return (mActs == null || mActs.size() <= 0) ? null : mActs.get(0);
		}
	}

	public Activity getSecondActivity() {
		synchronized (CloseAllActivity.this) {
			return (mActs == null || mActs.size() <= 1) ? null : mActs.get(1);
		}
	}

	public void close() {
		synchronized (CloseAllActivity.this) {
			Activity act;
			while (mActs.size() != 0) {
				act = mActs.poll();
				Utils.log("CloseActivity", "close():"
						+ act.getClass().getName());
				act.finish();
			}
		}
	}

	/**
	 * �ر�����activity��Ψ���ų�activityClassָ����activity
	 *
	 * @param activityClass
	 */
	public void closeExcept(Class<?> activityClass) {
		synchronized (CloseAllActivity.this) {
			Activity act;
			Iterator<Activity> activityIterator = mActs.iterator();
			while (activityIterator.hasNext()) {
				act = activityIterator.next( );
				if (!act.getClass().getName().equals(activityClass.getName())) {
					act.finish();
					activityIterator.remove();
				}
			}
		}
	}

	/**
	 * �ر�activityClassָ����activity
	 *
	 * @param activityClass
	 */
	public void closeTarget( Class< ? > activityClass )
	{
		synchronized ( CloseAllActivity.this )
		{
			Activity act;
			Iterator< Activity > activityIterator = mActs.iterator( );
			while ( activityIterator.hasNext( ) )
			{
				act = activityIterator.next( );
				if ( act.getClass( ).getName( ).equals( activityClass.getName( ) ) )
				{
					act.finish( );
					activityIterator.remove( );
				}
			}
		}
	}

	/**
	 * @Title: backToMainActivity
	 * @Description: �ص���ҳ�������صĽ���
	 */
	public void backToMainActivity( )
	{
		synchronized ( this )
		{
			while ( mActs.size( ) > 0 )
			{
				Activity activity = mActs.getFirst( );
				if ( activity instanceof MainActivity )
				{
					break;
				}
				else
				{
					activity.finish( );
					mActs.remove( activity );
				}
			}
		}
	}

	public ArrayList< Activity > getTargetActivity( Class< ? > activityClass )
	{
		ArrayList< Activity > activities = new ArrayList< Activity >( );
		synchronized ( CloseAllActivity.this )
		{
			Activity act;
			int size = mActs.size( );
			for ( int i = 0 ; i < size ; i++ )
			{
				act = mActs.get( i );
				if ( act.getClass( ).getName( ).equals( activityClass.getName( ) ) )
				{
					activities.add( act );
				}
			}
		}

		return activities;
	}
}
