package guoke.sort_map_uitls;

import java.util.Comparator;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * 汉字排序比较
 * 排前面的返回负数,排后面的返回正数
 * */
public class KeyComparator implements Comparator<String> {
	

	public int compare(String ostr1, String ostr2) {

		for (int i = 0; i < ostr1.length() && i < ostr2.length(); i++) {

			//获得第i个字
			int codePoint1 = ostr1.charAt(i);
			int codePoint2 = ostr2.charAt(i);
			/**
			 * 确定指定的字符（Unicode代码点）是否是在增补字符范围内。
			 * 此方法返回true，如果指定的代码点之间MIN_SUPPLEMENTARY_CODE_POINT和MAX_CODE_POINT包容性，
			 * 否则返回false。
			 * 通俗的讲，增补字符集就是在基本数据类型的标准老版本基础上升级之后产生的新字符.增补字符使char拜年的复杂但功能更强大。特别是东南亚市场的商业需求。
			 * 如果你使用的Unicode2.0版本，中国的GB18030就属于其增补字符集。
			 * i+=2意味着占2个字节
			 */
			if (Character.isSupplementaryCodePoint(codePoint1)
					|| Character.isSupplementaryCodePoint(codePoint2)) {
				i++;//意味着占2个字节
			}
			if (codePoint1 != codePoint2) {
				if (Character.isSupplementaryCodePoint(codePoint1)
						|| Character.isSupplementaryCodePoint(codePoint2)) {
					return codePoint1 - codePoint2;
				}
				//获得汉字拼音的首字符
				String pinyin1 = pinyin((char) codePoint1);
				String pinyin2 = pinyin((char) codePoint2);

				if (pinyin1 != null && pinyin2 != null) { // 两个字符都是汉字
					if (!pinyin1.equals(pinyin2)) {
						//如果首字符不相同
						/**
						 * 如果按字典顺序此字符串小于字符串参数，则返回一个小于 0 的值；
	                     * 如果按字典顺序此字符串大于字符串参数，则返回一个大于 0 的值。
	                     * 即pinyin1在前面,返回负数
	                     * pinyin1在后面,返回整数
						 */
						return pinyin1.compareTo(pinyin2);
					}
				} else {
					//如果首字符相同
					//codePoint1大,返回正数
					//codePoint1小,返回负数
					return codePoint1 - codePoint2;
				}
			}
		}
		//如果两个字一样,短的排前面
		return ostr1.length() - ostr2.length();
	}

	// 获得汉字拼音的首字符
	private String pinyin(char c) {
		//PinyinHelper在引入的包里面
		String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(c);
		if (pinyins == null) {
			return null;
		}
		return pinyins[0];
	}

	

}