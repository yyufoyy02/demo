package com.vk.simpleutil.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;

/**
 * 文字工具类
 * 
 * @author rendongwei
 * 
 */
public class XSimpleText {
	private XSimpleText() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}
	static Context context;

	public static void init(Context contexts) {
		context = contexts.getApplicationContext();
	}

	/** 是否为空 */
	public static Boolean isEmpty(String str) {
		return TextUtils.isEmpty(str) || str.equals("null");

	}

	/** 为空输出默认值 */
	public static String isEmpty(String str, String defaultext) {
		if (TextUtils.isEmpty(str)) {
			return defaultext;
		} else {
			return str;
		}

	}

	/**
	 * 设置部分字体颜色
	 * 
	 */

	public static SpannableStringBuilder setColorText(Context mContext,
			String str, int begin, int end, int color) {
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		if (str != null) {
			if (begin >= 0 && end >= 0 && begin != end) {
				style.setSpan(new ForegroundColorSpan(mContext
						.getResources().getColor(color)), begin, end,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			}
		}
		return style;
	}

	public static SpannableStringBuilder setColorText(String str, int begin,
			int end, int color) {

		return setColorText(context, str, begin, end, color);
	}

	/**
	 * 设置部分字体颜色下划线
	 * 
	 * @param mContext
	 * @param str
	 * @param textview
	 * @param begin
	 * @param end
	 */
	public static SpannableStringBuilder setUnderColorlineText(
			Context mContext, String str, int begin, int end, int color) {
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		if (begin >= 0 && end >= 0 && begin != end) {

			style.setSpan(new UnderlineSpan(), begin, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			style.setSpan(new ForegroundColorSpan(mContext.getResources()
					.getColor(color)), begin, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		}
		return style;
	}

	public static SpannableStringBuilder setUnderColorlineText(String str,
			int begin, int end, int color) {

		return setUnderColorlineText(context, str, begin, end, color);
	}

	/**
	 * 设置部分字体下划线
	 * 
	 * @param mContext
	 * @param str
	 * @param textview
	 * @param begin
	 * @param end
	 */
	public static SpannableStringBuilder setUnderlineText(Context mContext,
			String str, int begin, int end) {
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		if (begin >= 0 && end >= 0 && begin != end) {

			style.setSpan(new UnderlineSpan(), begin, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return style;
	}

	public static SpannableStringBuilder setUnderlineText(String str,
			int begin, int end) {

		return setUnderlineText(context, str, begin, end);
	}

	/**
	 * 根据输入流转换成字符串
	 * 
	 * @param inputStream
	 *            文字输入流
	 * @return 文字字符串(String 类型)
	 */
	public static String readTextFile(InputStream inputStream) {
		String readedStr = "";
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String tmp;
			while ((tmp = br.readLine()) != null) {
				readedStr += tmp;
			}
			br.close();
			inputStream.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return readedStr;
	}

}
