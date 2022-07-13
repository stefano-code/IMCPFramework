package com.android.imcplib;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MultiMap<K, V>
{
	private Map<K, Collection<V>> map;

	public MultiMap() 
	{
		map = new HashMap<K, Collection<V>>();
	}

	public Collection<V> getValues(K key) 
	{
		Collection<V> col = map.get(key);
		if (col == null) 
		{
			col = new HashSet<V>();
			map.put(key, col);
		}
		return col;
	}

	public boolean add(K key, V value) 
	{
		return getValues(key).add(value);
	}
	
	public Collection<V> get(K key)
	{
		return map.get(key);
	}

	public boolean remove(K key, V value) 
	{
		Collection<V> values = map.get(key);
		if (values == null) 
			return false;
		else
			return values.remove(value);
	}

	public void clear() 
	{
		map.clear();
	}
}
