package guoke.sort_map_uitls;


/***
 * 分类接口，根据V value返回K key
 *
 * @param <K>
 * @param <V>
 */

public interface KeySort<K, Mp3Info> {
	public K getKey(Mp3Info mp3Info);
}
