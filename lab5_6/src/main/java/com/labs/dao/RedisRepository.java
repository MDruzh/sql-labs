package com.labs.dao;

import java.util.Map;

public interface RedisRepository {
    Map<Object, Object> findAllFile();

    void addFile(String filename, String status);

    Object findOneFile(String id);

    void addRaws(String counter, String raws);

    Map<Object, Object> findAllRaws();


}