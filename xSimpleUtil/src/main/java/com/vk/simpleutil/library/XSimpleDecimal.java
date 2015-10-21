package com.vk.simpleutil.library;

import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 数值处理
 * 
 * @author Administrator
 * 
 */
public class XSimpleDecimal {
	private XSimpleDecimal() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 保留2位小数小数
	 * 
	 * @param decimal
	 * @return
	 */
	public static String FractionDigits(double decimal) {

		return FractionDigits(decimal, 2);

	}

	/**
	 * 保留指定位数小数
	 * 
	 * @param decimal
	 * @return
	 */
	public static String FractionDigits(double decimal, int num) {

		// return String.format("%." + num + "f", BigDecimal.valueOf(decimal));
		NumberFormat mDecimalFormat = DecimalFormat.getInstance();
		mDecimalFormat.setMaximumFractionDigits(num);
		mDecimalFormat.setMinimumFractionDigits(num);
		return mDecimalFormat.format(decimal);
	}

	/**
	 * 金额最多保留输入小数点后两位
	 *
	 */
	public static void editTextDecimalLimit(EditText edt, CharSequence s) {
		if (s.toString().contains(".")) {
			if (s.length() - 1 - s.toString().indexOf(".") > 2) {
				s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
				edt.setText(s);
				edt.setSelection(s.length());
			}
		}
		if (s.toString().trim().substring(0).equals(".")) {
			s = "0" + s;
			edt.setText(s);
			edt.setSelection(2);
		}

		if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
			if (!s.toString().substring(1, 2).equals(".")) {
				edt.setText(s.subSequence(0, 1));
				edt.setSelection(1);
				return;
			}
		}
	}

}
