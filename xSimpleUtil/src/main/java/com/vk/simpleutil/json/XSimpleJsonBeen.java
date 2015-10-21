package com.vk.simpleutil.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class XSimpleJsonBeen {
	/**
	 * 将java对象转换成json字符串
	 * 
	 * @param obj
	 *            准备转换的对象
	 * @return json字符串
	 * @throws Exception
	 */
	public static String bean2Json(Object obj) {
		return GsonUtils.objectToJson(obj);
	}

	/**
	 * json字符串转化为 JavaBean
	 * 
	 * @param <T>
	 * @param content
	 * @param valueType
	 * @return
	 */
	public static <T> T json2JavaBean(String content, Class<T> valueBean) {
		T t = null;
		try {
			t = GsonUtils.jsonToBean(content, valueBean);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if (t == null)
			try {
				return valueBean.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return t;
	}

	public static <T> T json2JavaBean(JSONObject mJsonObject, Class<T> valueBean) {
		return json2JavaBean(mJsonObject.toString(), valueBean);
	}

	/**
	 * json字符串转化为list
	 * 
	 * @param <T>
	 * @param content
	 * @param valueType
	 * @return
	 * @throws IOException
	 */
	public static <T> List<T> json2JavaBeanList(String content,
			Class<T> typeReference) {
		List<JsonObject> jsonObjs = null;
		try {
			jsonObjs = GsonUtils.jsonToList(content,
					new TypeToken<ArrayList<JsonObject>>() {
					}.getType());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		if (jsonObjs == null)
			return new ArrayList<T>();
		ArrayList<T> listOfT = new ArrayList<T>();
		for (JsonObject jsonObj : jsonObjs)
			listOfT.add(json2JavaBean(jsonObj.toString(), typeReference));
		jsonObjs.clear();
		jsonObjs = null;
		return listOfT;
	}

	public static <T> List<T> json2JavaBeanList(JSONObject mJsonObject,
			Class<T> typeReference) {
		return json2JavaBeanList(mJsonObject.toString(), typeReference);
	}
}
