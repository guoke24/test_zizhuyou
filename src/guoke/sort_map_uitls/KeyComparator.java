package guoke.sort_map_uitls;

import java.util.Comparator;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * ��������Ƚ�
 * ��ǰ��ķ��ظ���,�ź���ķ�������
 * */
public class KeyComparator implements Comparator<String> {
	

	public int compare(String ostr1, String ostr2) {

		for (int i = 0; i < ostr1.length() && i < ostr2.length(); i++) {

			//��õ�i����
			int codePoint1 = ostr1.charAt(i);
			int codePoint2 = ostr2.charAt(i);
			/**
			 * ȷ��ָ�����ַ���Unicode����㣩�Ƿ����������ַ���Χ�ڡ�
			 * �˷�������true�����ָ���Ĵ����֮��MIN_SUPPLEMENTARY_CODE_POINT��MAX_CODE_POINT�����ԣ�
			 * ���򷵻�false��
			 * ͨ�׵Ľ��������ַ��������ڻ����������͵ı�׼�ϰ汾����������֮����������ַ�.�����ַ�ʹchar����ĸ��ӵ����ܸ�ǿ���ر��Ƕ������г�����ҵ����
			 * �����ʹ�õ�Unicode2.0�汾���й���GB18030�������������ַ�����
			 * i+=2��ζ��ռ2���ֽ�
			 */
			if (Character.isSupplementaryCodePoint(codePoint1)
					|| Character.isSupplementaryCodePoint(codePoint2)) {
				i++;//��ζ��ռ2���ֽ�
			}
			if (codePoint1 != codePoint2) {
				if (Character.isSupplementaryCodePoint(codePoint1)
						|| Character.isSupplementaryCodePoint(codePoint2)) {
					return codePoint1 - codePoint2;
				}
				//��ú���ƴ�������ַ�
				String pinyin1 = pinyin((char) codePoint1);
				String pinyin2 = pinyin((char) codePoint2);

				if (pinyin1 != null && pinyin2 != null) { // �����ַ����Ǻ���
					if (!pinyin1.equals(pinyin2)) {
						//������ַ�����ͬ
						/**
						 * ������ֵ�˳����ַ���С���ַ����������򷵻�һ��С�� 0 ��ֵ��
	                     * ������ֵ�˳����ַ��������ַ����������򷵻�һ������ 0 ��ֵ��
	                     * ��pinyin1��ǰ��,���ظ���
	                     * pinyin1�ں���,��������
						 */
						return pinyin1.compareTo(pinyin2);
					}
				} else {
					//������ַ���ͬ
					//codePoint1��,��������
					//codePoint1С,���ظ���
					return codePoint1 - codePoint2;
				}
			}
		}
		//���������һ��,�̵���ǰ��
		return ostr1.length() - ostr2.length();
	}

	// ��ú���ƴ�������ַ�
	private String pinyin(char c) {
		//PinyinHelper������İ�����
		String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(c);
		if (pinyins == null) {
			return null;
		}
		return pinyins[0];
	}

	

}