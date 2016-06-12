package guoke.sort_map_uitls;




import guoke.model.MRegion;

import java.util.Comparator;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * ��������Ƚ�
 * ��ǰ��ķ��ظ���,�ź���ķ�������
 * */
public class Comparator_CN implements Comparator<MRegion> {
	
	/** 
	 * ��ú���ƴ�������ַ�,�����Ǻ����򷵻ؿ�
	 */
	private String pinyin(char c) {
		//PinyinHelper������İ�����
		String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(c);
		if (pinyins == null) {
			return null;
		}
		return pinyins[0];
	}

	@Override
	public int compare(MRegion lhs, MRegion rhs) {
		// TODO Auto-generated method stub
		String ostr1=lhs.getRname();
		String ostr2=rhs.getRname();
		//System.out.println("------>�Ƚ�["+ostr1+"]��["+ostr2+"]");
		for (int i = 0; i < ostr1.length() && i < ostr2.length(); i++) {

			//��õ�i������
			int HanZi_1 = ostr1.charAt(i);
			int HanZi_2 = ostr2.charAt(i);
			//System.out.println("��õ�"+i+"����");
			/**
			 * ȷ��ָ�����ַ�Unicode����㣩�Ƿ����������ַ�Χ�ڡ�
			 * �˷�������true�����ָ���Ĵ����֮��MIN_SUPPLEMENTARY_CODE_POINT
			 * ��MAX_CODE_POINT�����ԣ�
			 * ���򷵻�false��
			 * ͨ�׵Ľ��������ַ�����ڻ�������͵ı�׼�ϰ汾������֮���������ַ�.
			 * �����ַ�ʹchar����ĸ��ӵ����ܸ�ǿ���ر��Ƕ������г�����ҵ����
			 * �����ʹ�õ�Unicode2.0�汾���й��GB18030�������������ַ�
			 * i+=2��ζ��ռ2���ֽ�
			 */
			if (Character.isSupplementaryCodePoint(HanZi_1)
					|| Character.isSupplementaryCodePoint(HanZi_2)) {
				i++;//��ζ��ռ2���ֽ�
			}
			if (HanZi_1 != HanZi_2) {
				if (Character.isSupplementaryCodePoint(HanZi_1)
						|| Character.isSupplementaryCodePoint(HanZi_2)) {
					//System.out.println("return-->Character.isSupplementaryCodePoint");
					return HanZi_1 - HanZi_2;
				}
				//��ú���ƴ�������ַ�
				String pinyin1 = pinyin((char) HanZi_1);
				String pinyin2 = pinyin((char) HanZi_2);

				if (pinyin1 != null && pinyin2 != null) { // �����ַ��Ǻ���
					if (!pinyin1.equals(pinyin2)) {
						//������ַ���ͬ
						/**
						 * ����ֵ�˳����ַ�С���ַ�����򷵻�һ��С�� 0 ��ֵ��
	                     * ����ֵ�˳����ַ�����ַ�����򷵻�һ������ 0 ��ֵ��
	                     * ��pinyin1��ǰ��,���ظ���
	                     * pinyin1�ں���,��������
						 */
						//System.out.println("return-->���ַ����, "+"[pinyin1:"+pinyin1+"][pinyin2:"+pinyin2+"], "+pinyin1+".compareTo("+pinyin2+")�Ľ��:"+pinyin1.compareTo(pinyin2));
						return pinyin1.compareTo(pinyin2);
					}
				}else {
					//���������һ�����Ǻ���
					//codePoint1��,��������
					//codePoint1С,���ظ���
					//System.out.println("return-->��ȫ���Ǻ���,codePoint1 - codePoint2���:"+(HanZi_1 - HanZi_2));
					return HanZi_1 - HanZi_2;
				}
			}
		}//forѭ������,�������ʾ������һ��
		//���������һ��,�̵���ǰ��
		//System.out.println("return-->������һ��,�̵���ǰ��,ostr1.length() - ostr2.length()���:"+(ostr1.length() - ostr2.length()));
		return ostr1.length() - ostr2.length();
		
	}

}
