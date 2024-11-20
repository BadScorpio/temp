package com.trainAi.backend.utils;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class CustomCache<K, V>{

    // Using ConcurrentHashMap to store cache data
    private final ConcurrentHashMap<K, CacheItem<V>> cache = new ConcurrentHashMap<>();

    // Put data into the cache with an expiration time
    public void put(K key, V value, long expireAfter, TimeUnit timeUnit) {
        long expirationTime = System.currentTimeMillis() + timeUnit.toMillis(expireAfter);
        CacheItem<V> item = new CacheItem<>(value, expirationTime);
        cache.put(key, item);
    }

    // Get data from the cache. If the item is expired, return null
    public V get(K key) {
        CacheItem<V> item = cache.get(key);
        if (item == null || item.isExpired()) {
            cache.remove(key);  // If the cache item is expired, remove it
            return null;
        }
        return item.getValue();
    }

    // Remove a cache item
    public void remove(K key) {
        cache.remove(key);
    }

    // Clean up all expired cache items
    public void cleanUp() {
        for (K key : cache.keySet()) {
            CacheItem<V> item = cache.get(key);
            if (item != null && item.isExpired()) {
                cache.remove(key);
            }
        }
    }

    // CacheItem class that contains the value and expiration time
    private static class CacheItem<V> {
        private final V value;
        private final long expirationTime;

        public CacheItem(V value, long expirationTime) {
            this.value = value;
            this.expirationTime = expirationTime;
        }

        public V getValue() {
            return value;
        }

        // Check if the cache item has expired
        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }
}
