package com.bookstore.app.service;

public interface CacheService {
    void getAllCache();
    Object getCache(String value, Object key);
    String getTypeCache();
    void printCacheStats();
}
