package guoke.sort_helper;


import guoke.model.MRegion;
import guoke.sort_map_uitls.HashList;
import guoke.sort_map_uitls.KeyComparator;
import guoke.sort_map_uitls.KeySort;
import guoke.sort_map_uitls.Comparator_CN;
import guoke.sort_map_uitls.Comparator_EN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * ��װ�˶�HashList��һ�¸��ӵĲ���,��Ҫ������
 * ���õ���ĵط���ʱ��ֻҪ�ı�AddrListItem������ͺ����ȡ���ֵ���Ӧ����Ϳ����ˡ�
 * */
public class NameSortHelper {
	
	private List<MRegion> addrList;
	
	private Comparator_CN mp3ListSort_CN = new Comparator_CN();
	private Comparator_EN mp3ListSort_EN= new Comparator_EN();
	private KeyComparator keySort = new KeyComparator();
	
	
	//1.������ʵ��HashList<String,String>�����
	private HashList<String,MRegion> hashList=new HashList<String,MRegion>
		(   //2.ͬʱʵ�� �ӿ�
			new KeySort<String,MRegion>()
			{    
				//3.ʵ���˽ӿڵĺ���
				public String getKey(MRegion mp3Info)
				{
					return getFirstChar(mp3Info.getRname());
				}
			}
		);
	
	/**
	 * ���캯��,������add_And_sortMp3()����
	 * ��mp3Infos��ӵ�hashList,ͬʱʵ��������
	 * */
	public NameSortHelper(List<MRegion> sList){
		this.addrList=sList;
		add_And_sortMp3();
	}
	
	/**
	 * ��Ӳ�����
	 * */
	private void add_And_sortMp3(){
		// ����
		//str ���������Ԫ�����ͣ��������е�һ��Ԫ�ؿ�ʼ����strList��?�����������
		for (MRegion addrInfo : addrList) {
			//���õ�add()��������ʵ���˷��๦��
			//ÿһ�����һ��str����Ը�str����
			//1.����������,������
			hashList.add(addrInfo);
			//System.out.println("title = "+addrInfo.getName()+" getFirst = "+getFirstChar(addrInfo.getName()));
		}
		hashList.sortKeyComparator(keySort);
		for(int i=0,length=hashList.size();i<length;i++)
		{   
			//������ִ������Ĵ���������
			//��ÿһ��group��item����,������Ӣ��,����������
			Collections.sort(hashList.getChildList(i),mp3ListSort_EN);
			Collections.sort(hashList.getChildList(i),mp3ListSort_CN);		
		}
	}
	
	//����ַ������ĸ ���ַ� ת����ƴ��
	public  String getFirstChar(String value) {
			// ���ַ�
			char firstChar = value.charAt(0);
			// ����ĸ����
			String first = null;
			// �Ƿ��ǷǺ���
			String[] print = PinyinHelper.toHanyuPinyinStringArray(firstChar);

			if (print == null) {
				// ��Сд��ĸ�ĳɴ�д
				if ((firstChar >= 97 && firstChar <= 122)) {
					firstChar -= 32;
				}
				if (firstChar >= 65 && firstChar <= 90) {
					first = String.valueOf((char) firstChar);
				} else {
					// ��Ϊ���ַ�Ϊ���ֻ��������ַ�
					first = "#";
				}
			} else {
				// ��������� �����д��ĸ
				first = String.valueOf((char)(print[0].charAt(0) -32));
			}
			if (first == null) {
				first = "?";
			}
			return first;
		}

	
	/**
	 * �������涨�� hashList����	
	 * */
	public HashList<String, MRegion> getHashList() {
			return hashList;
		}
	
	/**
	 * ����HashList�����ź���mp3Info��mp3Infos
	 * @return mp3Infos ���ܺõ��б�
	 * */
	public List<MRegion> getMp3InfosFromHashList(){
		List<MRegion> mp3Infos=new ArrayList<MRegion>();
		List<String> keys = hashList.getKeys();
		for(int i = 0;i<keys.size();i++){//����ÿһ������ĸ��Ӧ���б�
			List<MRegion> temps = hashList.getChildList(i);
			for(int j=0;j<temps.size();j++){//����ÿ���б�,������,��ȫ˳��
				mp3Infos.add(hashList.getChild(i, j)); 
			}
		}
		return mp3Infos;
	}
	
}
