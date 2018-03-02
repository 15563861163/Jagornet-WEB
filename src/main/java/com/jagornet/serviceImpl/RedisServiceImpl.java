package com.jagornet.serviceImpl;

import com.jagornet.service.IRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

/**
 * @Author:LiuXinJian
 * @Description:
 * @Date:Created in 15:13 2018/2/25
 */
@Service
public class RedisServiceImpl implements IRedisService{

    private static Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    private JedisPool jedisPool;

    @Override
    public Jedis getResource() {
        return jedisPool.getResource();
    }

    @Override
    public void returnResource(Jedis jedis) {
        if (jedis != null) jedis.close();
    }

    @Override
    public byte[] getStringDate(byte[] sname) {
        Jedis jedis = null;
        byte[] date;
        try {
            jedis = getResource();
            date = jedis.get(sname);
        } finally {
            returnResource(jedis);
        }
        return date;
    }

    @Override
    public void setStringDate(String sname, String date) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.set(sname, date);
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public void setStringDate(byte[] sname, byte[] date) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.set(sname, date);
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public Map getHashDate(String sname) {
        Jedis jedis = null;
        Map date = new HashMap();
        try {
            jedis = getResource();
            date = jedis.hgetAll(sname);
        } finally {
            returnResource(jedis);
        }
        return date;
    }

    @Override
    public Set getZSetDate(String sname) {
        Jedis jedis = null;
        Set date = new HashSet();
        try {
            jedis = getResource();
            date = jedis.zrange(sname,0,-1);
        } finally {
            returnResource(jedis);
        }
        return date;
    }

    @Override
    public Set getSetDate(String sname) {
        Jedis jedis = null;
        Set date = new HashSet();
        try {
            jedis = getResource();
            date = jedis.smembers(sname);
        } finally {
            returnResource(jedis);
        }
        return date;
    }

    @Override
    public long getSetSize(String sname) {
        Jedis jedis = null;
        long size;
        try {
            jedis = getResource();
            size = jedis.scard(sname);
        } finally {
            returnResource(jedis);
        }
        return size;
    }

    @Override
    public String getHashValue(String sname, String key) {
        Jedis jedis = null;
        String value;
        try {
            jedis = getResource();
            value = jedis.hget(sname,key);
        }finally {
            returnResource(jedis);
        }
        return value;
    }

    @Override
    public byte[] getHashValue(byte[] sname, byte[] key) {
        Jedis jedis = null;
        byte[] value ;
        try {
            jedis = getResource();
            value = jedis.hget(sname, key);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    @Override
    public Double getZSetValue(String sname, String key) {
        Jedis jedis = null;
        Double value;
        try {
            jedis = getResource();
            value = jedis.zscore(sname, key);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    @Override
    public Double getZSetValue(byte[] sname, byte[] key) {
        Jedis jedis = null;
        Double value;
        try {
            jedis = getResource();
            value = jedis.zscore(sname, key);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    @Override
    public Set getKeys(String keys) {
        Jedis jedis = null;
        Set date = new HashSet();
        try {
            jedis = getResource();
            date = jedis.keys(keys);
        } finally {
            returnResource(jedis);
        }
        return date;
    }

    @Override
    public void delStringDate(String sname) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.del(sname);
            logger.info("finish delete String:" + sname);
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public void delHashValue(String sname, String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.hdel(sname,key);
            logger.info("finish delete Hash value:Hash:"+ sname + " - key:" + key);
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public void delHashValue(byte[] sname, byte[] key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.hdel(sname,key);
            logger.info("finish delete Hash value:Hash:"+ sname + " - key:" + key);
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public void delZSetValue(byte[] sname, byte[] key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.zrem(sname,key);
            logger.info("finish delete ZSet value:ZSet:"+ sname + " - key:" + key);
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public void delSetValue(byte[] sname, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.srem(sname,value);
            logger.info("finish delete Set value:Set"+ sname + " - value:" + value);
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public void delZSetValue(String sname, String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.zrem(sname,key);
            logger.info("finish delete ZSet value:ZSet:"+ sname + " - key:" + key);
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public void delSetValue(String sname, String value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.srem(sname,value);
            logger.info("finish delete Set value:Set"+ sname + " - value:" + value);
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public void delKey(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.del(key);
            logger.info("finish delete key:" + key);
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public void delKeys(Set<String> keys) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (keys != null && !keys.isEmpty()) {
                Iterator<String> it = keys.iterator();
                while (it.hasNext()) {
                    jedis.del(it.next());
                }
                logger.info("finish delete keys:" + keys);
            }
        } finally {
            returnResource(jedis);
        }
    }
}
