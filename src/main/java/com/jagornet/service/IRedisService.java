package com.jagornet.service;

import redis.clients.jedis.Jedis;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @Author:LiuXinJian
 * @Description:
 * @Date:Created in 13:48 2018/2/25
 */
public interface IRedisService {

    public Jedis getResource();

    public void returnResource(Jedis jedis);

    public byte[] getStringDate(byte[] sname);

    public void setStringDate(String sname, String date);

    public void setStringDate(byte[] sname,byte[] date);

    public void delStringDate(String sname);

    public Map getHashDate(String sname);

    public String getHashValue(String sname, String key);

    public byte[] getHashValue(byte[] sname, byte[] key);

    public void delHashValue(String sname,String key);

    public void delHashValue(byte[] sname,byte[] key);

    public Set getZSetDate(String sname);

    public Double getZSetValue(String sname,String key);

    public Double getZSetValue(byte[] sname,byte[] key);

    public void delZSetValue(String sname,String key);

    public void delZSetValue(byte[] sname,byte[] key);

    public Set getSetDate(String sname);

    public long getSetSize(String sname);

    public void delSetValue(String sname,String value);

    public void delSetValue(byte[] sname,byte[] value);

    public Set getKeys(String keys);

    public void delKey(String key);

    public void delKeys(Set<String> keys);
}
