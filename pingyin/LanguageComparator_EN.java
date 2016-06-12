package org.pingyin;

import java.util.Comparator;

public class LanguageComparator_EN implements Comparator<String> {

	/**
	 * compareToIgnoreCase：比较，忽略大小写
	 * 如果参数字符串等于此字符串，则返回 0 值；
	 * 如果按字典顺序此字符串小于字符串参数，则返回一个小于 0 的值；
	 * 如果按字典顺序此字符串大于字符串参数，则返回一个大于 0 的值。
	 * 例：字典的顺序：0123456ABCabc 大写字母在小写字母前
	 */
	public int compare(String ostr1, String ostr2) {
		// TODO Auto-generated method stub
		return ostr1.compareToIgnoreCase(ostr2);
	}

}
