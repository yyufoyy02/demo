package com.vk.simpleutil;

import android.content.Context;

import com.vk.simpleutil.http.XSimpleHttpUtil;
import com.vk.simpleutil.library.XSimpleAlertDialog;
import com.vk.simpleutil.library.XSimpleApp;
import com.vk.simpleutil.library.XSimpleDensity;
import com.vk.simpleutil.library.XSimpleImage;
import com.vk.simpleutil.library.XSimpleLogger;
import com.vk.simpleutil.library.XSimpleResources;
import com.vk.simpleutil.library.XSimpleScreen;
import com.vk.simpleutil.library.XSimpleSharePreferences;
import com.vk.simpleutil.library.XSimpleText;
import com.vk.simpleutil.library.XSimpleToast;
import com.vk.simpleutil.library.XSimpleUtil;

import java.util.Map;

public class XSimpleBaseUtil {
	private XSimpleBaseUtil() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static void initConfig(Builder builder) {
		XSimpleDensity.init(builder.context, builder.pad);
		XSimpleAlertDialog.setTheme(builder.alertDialogTheme);
		XSimpleSharePreferences.init(builder.context);
		XSimpleText.init(builder.context);
		XSimpleToast.init(builder.context);
		XSimpleLogger.setDebugMode(builder.debug);
		XSimpleApp.init(builder.context);
		XSimpleHttpUtil.getInstance().init(builder.context);
		XSimpleHttpUtil.getInstance().setDafaultRequestParams(
				builder.defaultRequestParams);
		XSimpleUtil.init(builder.context);
		XSimpleScreen.init(builder.context);
		XSimpleResources.init(builder.context);
		XSimpleImage.init(builder.context);
	}

	public static class Builder {
		Context context;
		boolean pad;
		int alertDialogTheme;
		boolean debug;
		Map<String, ?> defaultRequestParams;

		public Builder initContext(Context context) {
			this.context = context;
			return this;
		}

		public Builder isPad(boolean pad) {
			this.pad = pad;
			return this;
		}

		public Builder alertDialogTheme(int alertDialogTheme) {
			this.alertDialogTheme = alertDialogTheme;
			return this;
		}

		public Builder isDebug(boolean debug) {
			this.debug = debug;
			return this;
		}

		public Builder initDefaultRequestParams(
				Map<String, ?> defaultRequestParams) {
			this.defaultRequestParams = defaultRequestParams;
			return this;
		}

		public Builder build() {
			return this;
		}
	}

}
