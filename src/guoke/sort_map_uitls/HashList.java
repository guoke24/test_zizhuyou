package guoke.sort_map_uitls;
/**
 * 
 * ��Ҫְ��:
 * һ.���������ݵķ�װ:
 * 1.������������ĸ�ļ���-->keys
 * 2.ÿ������ĸ����Ӧ�������ϵ�ӳ��-->maps
 * ��.����Щ���ݵĲ���
 * 1.����groupId��childId������Ӧ��ʵ��
 * 2.��keys������
 * 3.��keys��maps����Ӻ��Ƴ�
 * 
 * */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

import java.util.List;


/***
 * @param <K>����һ����������
 * @param <V>����һ����������,�����Զ���
 */

public class HashList<K, V> {

	/**
	 * ��ֵ����
	 * */
	private List<K> keys = new ArrayList<K>();
	/**
	 * ��ֵ��ӳ��
	 * һ��K��Ӧһ��group
	 */	
	private HashMap<K, List<V>> map = new HashMap<K, List<V>>();
	/**
	 * ��ֵ����
	 */
	private KeySort<K, V> keySort;

	/**
	 * ���캯��
	 */
	public HashList(KeySort<K, V> keySort) {
		// TODO Auto-generated constructor stub
		this.keySort = keySort;
	}

	/**
	 * ����valueֵ����key
	 * ��������ĸ�Ĵ�д��ĸ
	 */	
	public K getKey(V v) {
		//��AssortPinyinList����ʵ����KeySort�ӿڵ�getkey()����
		//�������ֵ�����ĸ�Ĵ�д��ĸ
		return keySort.getKey(v);
	}


	/** 
	 * ���ش�д������ĸ,����int���͵�id
	 */
	public K getKeyIndex(int key) {
		//key�洢��ÿ��group��Ӧ��id
		//һ��group��Ӧ��һ��key
		//key--group--id
		return keys.get(key);
	}
	
	/**
	 * ��ȡ����ĸ�ļ���
	 */
	public List<K> getKeys(){
		return keys;
	}

	/** 
	 * �����������ؼ�ֵ��,���ص���һ��childitem
	 */

	public List<V> getChildList(int key) {
		//�õ�group��id,map�洢��List<V>
		//Դͷ���Ǵ�kerArr�����ü�ֵ,������������ĸ��д
		//�ڴ�map��ȥӳ���list
		return map.get(getKeyIndex(key));
	}

	
	/**
	 * ȡ��V��ʵ��
	 * key���� group_id
	 * value���� child_id
	 * @return һ��V���͵�ʵ��
	 */
	public V getChild(int key, int value) {
		//�ȵõ�group��id,�ٵõ���group�µ�child��id
		return getChildList(key).get(value);

	}

	/**��ȡkeys�ĳ���*/
	public int size() {
		return keys.size();
	}
	
	/** ȡ��k���±�*/
	public int indexOfKey(K k)
	{
		return keys.indexOf(k);
	}

	/** �������ʵ��*/
	public void clear() {
		for (Iterator<K> it = map.keySet().iterator(); it.hasNext(); map.remove(it.next()));
	}

	/**
	 * ������ʵ���˷���
	 * ��ÿһ�����ֵַ���Ӧ��group����
	 */
	public boolean add(Object object) {
		V v = (V) object;
		K key = getKey(v);
		//System.out.println("��add()��,key:"+key);
		//
		if (!map.containsKey(key)) {
			//��list�������
			List<V> list = new ArrayList<V>();
			//���������list
			list.add(v);
			//��Ӹ��������״�д��ĸ
			keys.add(key);
			//
			map.put(key, list);
		} else {
			//
			map.get(key).add(v);
		}
		return false;
	}
		
	/**
	 * �Ƴ� List<V>���ʵ��
	 */
	public boolean remove(Object v) {
		// TODO Auto-generated method stub
		//ȡ��k
		K k=getKey((V)v);
		//ȡ��k���±�
		int k_index=indexOfKey(k);
		
		//����k���±�ȡ�ö�Ӧ����list,�Ƴ��ö���
		boolean b=getChildList(k_index).remove(v);
		//���k���±��Ӧ����listΪ��,�Ƴ�k
		if(getChildList(k_index).isEmpty()==true){
			keys.remove(k);
		}
		return b;
	}
	
	/** ��ֵ������ */
	public void sortKeyComparator(Comparator<K> comparator) {
		//��keyArr��Ԫ����comparator����ķ�ʽ������
		//����group��Ӧ��key��������
		Collections.sort(keys, comparator);
		if(keys.contains("#")){
			keys.remove("#");
			keys.add((K) "#");
		}
	}
	
	/*
	 	public boolean contains(Object object) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public Object remove(int location) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean remove(Object object) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean removeAll(Collection arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean retainAll(Collection arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public Object setkey(int location, Object object) {
		// TODO Auto-generated method stub
		return keys.set(location, (K) object);
	}

	public List subList(int start, int end) {
		// TODO Auto-generated method stub
		return keys.subList(start, end);
	}

	public Object[] toArray() {
		// TODO Auto-generated method stub
		return keys.toArray();
	}

	public Object[] toArray(Object[] array) {
		return keys.toArray(array);
	}
	 
	 
	 */
}
