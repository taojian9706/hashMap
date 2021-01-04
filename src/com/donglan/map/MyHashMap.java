package com.donglan.map;

import java.util.ArrayList;

/**
 * @author TAOJIAN
 * @version 1.0
 * @since 2021-01-04 14:04:10
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int defaultInitSize;

    private float defaultLoadFactor;

    private int entryUseSize;

    private Entry<K, V>[] table = null;

    class Entry<K, V> implements MyMap.Entry<K, V> {

        private K key;

        private V value;

        private Entry<K, V> next;

        public Entry() {
        }

        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }
    }

    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int defaultInitialCapacity, float defaultLoadFactor) {

        if (defaultInitialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity：" + defaultInitialCapacity);
        }
        if (defaultLoadFactor <= 0 || Float.isNaN(defaultLoadFactor)) {
            throw new IllegalArgumentException("Illegal load factor:" + defaultLoadFactor);
        }
        this.defaultInitSize = defaultInitialCapacity;
        this.defaultLoadFactor = defaultLoadFactor;
        this.table = new Entry[this.defaultInitSize];
    }

    @Override
    public V put(K k, V v) {
        V oldValue = null;
        // 检测是否需要扩容
        if (entryUseSize >= defaultInitSize * defaultLoadFactor) {
            resize(entryUseSize * 2);
        }
        int index = hash(k) & (defaultInitSize - 1);
        // 如果是第一次存放
        if (table[index] == null) {
            table[index] = new Entry<>(k, v, null);
        }
        else {// table[index] 不是第一次存放
            Entry<K, V> entry = table[index];

            //遍历单项链表
            Entry<K, V> e = entry;
            while (e != null) {
                if (e.getKey() == k || e.getKey().equals(k)) {// 如果key 相同 执行更新操作
                    oldValue = e.getValue();
                    e.value = v;
                    return oldValue;
                }
                e = e.next;
            }
            // 放入数组中 并且让next指向之前的entry
            table[index] = new Entry<>(k, v, entry);
        }
        ++entryUseSize;
        return oldValue;
    }

    @Override
    public V get(K k) {
        int index = hash(k) & (defaultInitSize - 1);
        if (table[index] == null) {
            return null;
        }
        else {
            Entry<K, V> entry = table[index];
            do {
                if (k == entry.getKey() || k.equals(entry.getKey())) {
                    return entry.getValue();
                }
                entry = entry.next;
            }
            while (entry != null);
        }
        return null;
    }

    /**
     *@Description 数组扩容
     *@Param [i]
     *@Return void
     *@Author TAOJIAN
     *@Date 2021/1/4
     *@Time 19:58
     */
    private void resize(int i) {
        Entry[] newTable = new Entry[i];
        defaultInitSize = i;
        entryUseSize = 0;
        rehash(newTable);
    }

    /**
     *@Description 重新散列
     *@Param [newTable]
     *@Return void
     *@Author TAOJIAN
     *@Date 2021/1/4
     *@Time 19:58
     */
    private void rehash(Entry[] newTable) {
        ArrayList<Entry<K, V>> entries = new ArrayList<>();
        for (Entry<K, V> entry : table) {
            if (entry != null) {
                do {
                    entries.add(entry);
                    entry = entry.next;
                }
                while (entry != null);
            }
        }
        table = newTable.length > 0 ? newTable : table;
        for (Entry<K, V> entry : entries) {
            put(entry.getKey(), entry.getValue());
        }
    }

    private int hash(K k) {
        int hashCode = k.hashCode();
        hashCode ^= (hashCode >>> 20) ^ (hashCode >>> 12);
        return hashCode ^ (hashCode >>> 7) ^ (hashCode >>> 4);
    }

    public static void main(String[] args) {
        MyHashMap<String, String> map = new MyHashMap<>();
        for (int i = 0; i < 100000; i++) {
            map.put("key" + i, "value" + i);
        }
        for (int i = 0; i < 100000; i++) {
            System.out.println(map.get("key" + i));
        }
        System.out.println(map.entryUseSize);
        System.out.println(map.table.length);
    }
}
