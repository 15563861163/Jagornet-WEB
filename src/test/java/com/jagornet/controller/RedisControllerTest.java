package com.jagornet.controller;

import com.jagornet.service.IRedisService;
import com.jagornet.serviceImpl.RedisServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * @Author:LiuXinJian
 * @Description:
 * @Date:Created in 17:28 2018/2/26
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisControllerTest.class)
public class RedisControllerTest {

    @Test
    public void getFreeList() {
//        try {
//           assembleFreeList();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
    }

    private Map assembleFreeList() throws UnknownHostException {
        //先读取FreeList
        Jedis jedis = new Jedis("127.0.0.1",9180);
        Set<String> keys = jedis.keys("DHCP_FreeList*");

        Map<String,String> pools = new HashMap<String,String>();
        if (keys != null && !keys.isEmpty()) {
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {

                String[] strs = it.next().split(":");
                String[] range = strs[1].split("-");

                BigInteger a = new BigInteger(range[0]);
                BigInteger b = new BigInteger(range[1]);
                InetAddress ip1 = InetAddress.getByAddress(a.toByteArray());
                InetAddress ip2 = InetAddress.getByAddress(b.toByteArray());
                pools.put(ip1.toString(), ip2.toString());

            }

        }
        return pools;
    }

    @Test
    public void getDhcpTables(){
        Jedis jedis = new Jedis("127.0.0.1",9180);
        String IPADDRES_SET = "DHCP_IPADDRESS_TABLE";
        Set<String> keys = jedis.smembers(IPADDRES_SET);
        String IAID_HASH = "DHCP_IAID_TABLE";
        for (String key: keys) {
            System.out.println(jedis.hget(IAID_HASH,key));
        }

    }
}