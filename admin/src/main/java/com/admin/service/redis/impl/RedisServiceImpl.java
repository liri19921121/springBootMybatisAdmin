package com.admin.service.redis.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.admin.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service("redisService")
public class RedisServiceImpl implements RedisService {
	
	@Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Override
    public void deleteFromRedis(String key) {
        redisTemplate.delete(key);
    }
    
    @Override
    public void setExpireTime(String key,Long timeout){
    	redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }
    
    @Override
    public Boolean hashCheckHxists(String mapName, String field) {
        return redisTemplate.opsForHash().hasKey(mapName, field);
    }

    @Override
    public Object hashGet(String tableName, String hashKey) {
        return redisTemplate.opsForHash().get(tableName, hashKey);
    }
    
    @Override
    public Map<Object, Object> hashGetAll(String key) {
        return  redisTemplate.opsForHash().entries(key);
    }

    @Override
    public Long hashIncrementLongOfHashMap(String hKey, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(hKey, hashKey, delta);
    }

    @Override
    public Double hashIncrementDoubleOfHashMap(String hKey, String hashKey, Double delta) {
        return redisTemplate.opsForHash().increment(hKey, hashKey, delta);
    }

    @Override
    public void hashPushHashMap(String key, Object hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public Set<Object> hashGetAllHashKey(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    @Override
    public Long hashGetHashMapSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    @Override
    public List<Object> hashGetHashAllValues(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    @Override
    public Long hashDeleteHashKey(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    @Override
    public void listLeftPushList(String key, String value) {
        redisTemplate.opsForList().leftPush(key, value); 
    }

    @Override
    public Object listLeftPopList(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    @Override
    public Long listSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public List<Object> listRangeList(String key, Long start, Long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public Long listRemoveFromList(String key, long i, Object value) {
        return redisTemplate.opsForList().remove(key, i, value);
    }

    @Override
    public Object listIndexFromList(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    @Override
    public void listSetValueToList(String key, long index, String value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    @Override
    public void listTrimByRange(String key, Long start, Long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    @Override
    public void listRightPushList(String key, String value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public Object listRightPopList(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    @Override
    public Long setAddSetMap(String key, String... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    @Override
    public Long setGetSizeForSetMap(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public Set<Object> setGetMemberOfSetMap(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public Boolean setCheckIsMemberOfSet(String key, Object o) {
        return redisTemplate.opsForSet().isMember(key, o);
    }

    @Override
    public Integer stringAppendString(String key, String value) {
        return redisTemplate.opsForValue().append(key, value);
    }

    @Override
    public Object stringGetByKey(String key) {
        return  redisTemplate.opsForValue().get(key);
    }

    @Override
    public String stringGetSubStringFromString(String key, long start, long end) {
        return redisTemplate.opsForValue().get(key, start, end);
    }

    @Override
    public Long stringIncrementLongString(String key, Long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public Double stringIncrementDoubleString(String key, Double delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public void stringSetObject(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);        
    }

    @Override
    public Object stringGetAndSet(String key, Object value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    @Override
    public void stringSetValueAndExpireTime(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);        
    } 
}
