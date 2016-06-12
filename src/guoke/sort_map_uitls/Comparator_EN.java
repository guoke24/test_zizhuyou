package guoke.sort_map_uitls;



import guoke.model.MRegion;

import java.util.Comparator;

public class Comparator_EN implements Comparator<MRegion> {

	/**
	 * compareToIgnoreCase���Ƚϣ����Դ�Сд
	 * �������ַ���ڴ��ַ��򷵻� 0 ֵ��
	 * ����ֵ�˳����ַ�С���ַ�����򷵻�һ��С�� 0 ��ֵ��
	 * ����ֵ�˳����ַ�����ַ�����򷵻�һ������ 0 ��ֵ��
	 * ���ֵ��˳��0123456ABCabc ��д��ĸ��Сд��ĸǰ
	 */


	@Override
	public int compare(MRegion lhs, MRegion rhs) {
		// TODO Auto-generated method stub
		return lhs.getRname().compareToIgnoreCase(rhs.getRname());
	}

}
