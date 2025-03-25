package com.bookstore.app.service.impl;

import com.bookstore.app.dto.response.UserResponse;
import com.bookstore.app.service.CacheService;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class CacheServiceImpl implements CacheService {

    private CacheManager cacheManager;
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void getAllCache() {
        ArrayList<UserResponse> userResponses = (ArrayList<UserResponse>) redisTemplate.opsForValue().get("users::all");
        if (userResponses != null) userResponses.forEach(System.out::println);
    }

    @Override
    public Object getCache(String value, Object key) {
        Object object = redisTemplate.opsForValue().get(value + "::" + key);
        System.out.println(object);
        return object;
    }

    @Override
    public String getTypeCache() {
        return cacheManager.getClass().getName();
    }

    @Override
    public void printCacheStats() {
//        CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache("users");
//        System.out.println(caffeineCache.getNativeCache().stats());
    }
}
