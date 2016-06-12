package guoke.sort_map_uitls;
/**
 * 
 * 主要职责:
 * 一.对两种数据的封装:
 * 1.歌曲名字首字母的集合-->keys
 * 2.每个首字母与相应歌曲集合的映射-->maps
 * 二.对这些数据的操作
 * 1.基于groupId和childId返回相应的实例
 * 2.对keys的排序
 * 3.对keys和maps的添加和移除
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
 * @param <K>代表一种数据类型
 * @param <V>代表一种数据类型,可以自定义
 */

public class HashList<K, V> {

	/**
	 * 键值集合
	 * */
	private List<K> keys = new ArrayList<K>();
	/**
	 * 键值对映射
	 * 一个K对应一个group
	 */	
	private HashMap<K, List<V>> map = new HashMap<K, List<V>>();
	/**
	 * 键值分类
	 */
	private KeySort<K, V> keySort;

	/**
	 * 构造函数
	 */
	public HashList(KeySort<K, V> keySort) {
		// TODO Auto-generated constructor stub
		this.keySort = keySort;
	}

	/**
	 * 根据value值返回key
	 * 返回首字母的大写字母
	 */	
	public K getKey(V v) {
		//在AssortPinyinList类里实现了KeySort接口的getkey()函数
		//返回名字的首字母的大写字母
		return keySort.getKey(v);
	}


	/** 
	 * 返回大写的首字母,根据int类型的id
	 */
	public K getKeyIndex(int key) {
		//key存储着每个group对应的id
		//一个group对应着一个key
		//key--group--id
		return keys.get(key);
	}
	
	/**
	 * 获取首字母的集合
	 */
	public List<K> getKeys(){
		return keys;
	}

	/** 
	 * 根据索引返回键值对,返回的是一组childitem
	 */

	public List<V> getChildList(int key) {
		//得到group的id,map存储着List<V>
		//源头还是从kerArr那里获得键值,即人名的首字母大写
		//在从map里去映射的list
		return map.get(getKeyIndex(key));
	}

	
	/**
	 * 取得V的实例
	 * key就是 group_id
	 * value就是 child_id
	 * @return 一个V类型的实例
	 */
	public V getChild(int key, int value) {
		//先得到group的id,再得到该group下的child的id
		return getChildList(key).get(value);

	}

	/**获取keys的长度*/
	public int size() {
		return keys.size();
	}
	
	/** 取得k的下标*/
	public int indexOfKey(K k)
	{
		return keys.indexOf(k);
	}

	/** 清除所有实例*/
	public void clear() {
		for (Iterator<K> it = map.keySet().iterator(); it.hasNext(); map.remove(it.next()));
	}

	/**
	 * 在这里实现了分类
	 * 把每一个名字分到对应的group里面
	 */
	public boolean add(Object object) {
		V v = (V) object;
		K key = getKey(v);
		//System.out.println("在add()里,key:"+key);
		//
		if (!map.containsKey(key)) {
			//该list存放人名
			List<V> list = new ArrayList<V>();
			//添加人名到list
			list.add(v);
			//添加该人名的首大写字母
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
	 * 移除 List<V>里的实例
	 */
	public boolean remove(Object v) {
		// TODO Auto-generated method stub
		//取得k
		K k=getKey((V)v);
		//取得k的下标
		int k_index=indexOfKey(k);
		
		//根据k的下标取得对应的子list,移除该对象
		boolean b=getChildList(k_index).remove(v);
		//如果k的下标对应的子list为空,移除k
		if(getChildList(k_index).isEmpty()==true){
			keys.remove(k);
		}
		return b;
	}
	
	/** 键值对排序 */
	public void sortKeyComparator(Comparator<K> comparator) {
		//对keyArr的元素以comparator定义的方式来排序
		//即对group对应的key进行排序
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
