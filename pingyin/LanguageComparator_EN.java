package org.pingyin;

import java.util.Comparator;

public class LanguageComparator_EN implements Comparator<String> {

	/**
	 * compareToIgnoreCase���Ƚϣ����Դ�Сд
	 * ��������ַ������ڴ��ַ������򷵻� 0 ֵ��
	 * ������ֵ�˳����ַ���С���ַ����������򷵻�һ��С�� 0 ��ֵ��
	 * ������ֵ�˳����ַ��������ַ����������򷵻�һ������ 0 ��ֵ��
	 * �����ֵ��˳��0123456ABCabc ��д��ĸ��Сд��ĸǰ
	 */
	public int compare(String ostr1, String ostr2) {
		// TODO Auto-generated method stub
		return ostr1.compareToIgnoreCase(ostr2);
	}

}
