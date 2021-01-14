package com.labs.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Map;

@Repository
public class RedisRepositoryImpl implements RedisRepository {
    private static final String MAP_KEY = "File";
    private static final String RAW_KEY = "RAWS";

    @Autowired
    private StringRedisTemplate template;

    private HashOperations hashOperations;

    @PostConstruct
    private void init(){
        hashOperations = this.template.opsForHash();
    }

    public void addFile(final String filename, String status) {
        hashOperations.put(MAP_KEY, filename, status);
    }

    public Object findOneFile(final String id){
        return  hashOperations.get(MAP_KEY, id);
    }

    @Override
    public void addRaws(String counter, String raws) {
        hashOperations.put(RAW_KEY, counter, raws);
    }

    @Override
    public Map<Object, Object> findAllRaws() {
        return hashOperations.entries(RAW_KEY);
    }


    public Map<Object, Object> findAllFile(){
        return hashOperations.entries(MAP_KEY);
    }


}