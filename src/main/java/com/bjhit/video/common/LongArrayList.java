package com.bjhit.video.common;

import java.util.Arrays;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午5:06:36
 * @version 1.0
 */
public class LongArrayList {

    private static final int DEFAULT_GROW_AMOUNT = 128;
    private long[] storage;
    private int size;
    private int growAmount;

    public LongArrayList() {
        this(DEFAULT_GROW_AMOUNT);
    }

    public LongArrayList(int growAmount) {
        this.growAmount = growAmount;
        this.storage = new long[growAmount];
    }

    public long[] toArray() {
        long[] result = new long[size];
        System.arraycopy(storage, 0, result, 0, size);
        return result;
    }

    public void add(long val) {
        if (size >= storage.length) {
            long[] ns = new long[storage.length + growAmount];
            System.arraycopy(storage, 0, ns, 0, storage.length);
            storage = ns;
        }
        storage[size++] = val;
    }
    
    public void push(long id) {
        this.add(id);
    }
    
    public void pop() {
        if (size == 0)
            return;
        size--;
    }

    public void set(int index, int value) {
        storage[index] = value;
    }

    public long get(int index) {
        return storage[index];
    }

    public void fill(int start, int end, int val) {
        if (end > storage.length) {
            long[] ns = new long[end + growAmount];
            System.arraycopy(storage, 0, ns, 0, storage.length);
            storage = ns;
        }
        Arrays.fill(storage, start, end, val);
        size = Math.max(size, end);
    }

    public int size() {
        return size;
    }

    public void addAll(long[] other) {
        if (size + other.length >= storage.length) {
            long[] ns = new long[size + growAmount + other.length];
            System.arraycopy(storage, 0, ns, 0, size);
            storage = ns;
        }
        System.arraycopy(other, 0, storage, size, other.length);
        size += other.length;
    }

    public void clear() {
        size = 0;
    }
    
    public boolean contains(long needle) {
        for (int i = 0; i < size; i++)
            if (storage[i] == needle)
                return true;
        return false;
    }
}
