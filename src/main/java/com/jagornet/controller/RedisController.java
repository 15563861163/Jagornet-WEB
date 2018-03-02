package com.jagornet.controller;

import com.jagornet.Entity.DhcpTable;
import com.jagornet.dhcp.xml.DhcpServerConfigDocument;
import com.jagornet.dhcp.xml.DhcpServerConfigDocument.DhcpServerConfig;
import com.jagornet.service.IRedisService;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.*;

/**
 * @Author:LiuXinJian
 * @Description:
 * @Date:Created in 15:47 2018/2/25
 */
@Controller
@RequestMapping(value = "/Jargornet-redis")
public class RedisController {

    Logger logger = LoggerFactory.getLogger(RedisController.class);

    protected final static String SERVERCONFIG_STRING = "DHCPSERVERCONFIG_TABLE";
    protected final static String SERVER_HASH = "DHCP_SERVERIP_TABLE";
    protected final static String IPADDRES_SET = "DHCP_IPADDRESS_TABLE";
    protected final static String DUID_HASH = "DHCP_DUID_TABLE";
    protected final static String IATYPE_SORTEDSET = "DHCP_IATYPE_TABLE";
    protected final static String IAID_HASH = "DHCP_IAID_TABLE";
    protected final static String PREFIXLEN_HASH = "DHCP_PREFIXLEN_TABLE";
    protected final static String STATE_SORTEDSET = "DHCP_STATE_TABLE";
    protected final static String STARTTIME_HASH = "DHCP_STARTTIME_TABLE";
    protected final static String PREFERREDENDTIME_HASH = "DHCP_PREFERREDENDTIME_TABLE";
    protected final static String VALIDENDTIME_SORTEDSET = "DHCP_VALIDENDTIME_TABLE";
    protected final static String IAOPTIONS_HASH = "DHCP_IAOPTIONS_TABLE";
    protected final static String IAADDROPTIONS_HASH = "DHCP_IPADDROPTIONS_TABLE";

    @Autowired
    private IRedisService redisService;


    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String test(ModelMap map) {
        String config = getServerConfigFromRedis();
        map.addAttribute("config",config);
        return "index";
    }

    @RequestMapping(value = "/getDhcpServerConfig" ,method = RequestMethod.GET)
    public String getDhcpServerConfig(ModelMap map) {
        String config = getServerConfigFromRedis();
        map.addAttribute("config",config);

        return "index";
    }

    private String getServerConfigFromRedis() {
        byte[] serverConfig = redisService.getStringDate(SERVERCONFIG_STRING.getBytes());
        if (serverConfig == null || !(serverConfig.length > 0)) {
            //1.将文件读到redis中 DHCPSERVERCONFIG_TABLE_TEST
            //2.将标准文件读入
            try {
                DhcpServerConfig config = parseConfig("src/main/resources/static/dhcpserver-basic.xml");
                serverConfig = serialize(config);
                redisService.setStringDate(SERVERCONFIG_STRING.getBytes(),serialize(config));
            } catch (IOException | XmlException e) {
                System.out.println("file is error.路径问题（标准格式：src/main/resources/static/***.xml）或者是文件格式不符合");
                logger.debug(e.getMessage());
            }
        }
        Object objs = unserizlize(serverConfig);
        DhcpServerConfig dhcpServerConfig = null;
        if(objs instanceof DhcpServerConfig){
            dhcpServerConfig = (DhcpServerConfig)objs;
        }
        return dhcpServerConfig.toString();
    }
    @RequestMapping(value = "/setDhcpServerConfig",method = RequestMethod.POST)
    public String setDhcpServerConfig(@RequestParam(value = "xml") String xml) {
        DhcpServerConfig config = null;
        byte[] byt = xml.getBytes();
        if (byt != null && byt.length > 0) {
            /* 获取到了内容,将数据反序列化 */
            Object objs = unserizlize(byt);
            if (objs instanceof DhcpServerConfig) {
                logger.info("Get update configuration file.");
                //说明符合要求，可以保存到redis中
                config = (DhcpServerConfig) objs;
                redisService.setStringDate(SERVERCONFIG_STRING.getBytes(),serialize(config));
                logger.info("Configuration file update success.");
            } else {
                logger.error("Configuration file cannot update to redis.");
                //这里应该有内容，因为文件不符合要求
                return "返回提示错误信息";
            }
        } else {
            logger.error("Connot update xml because xml is null.");
        }
        return "redirect:/getDhcpServerConfig";
    }

    private DhcpServerConfig parseConfig(String filename) throws IOException, XmlException {
        DhcpServerConfig config = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
            config = DhcpServerConfigDocument.Factory.parse(fis).getDhcpServerConfig();

            ArrayList<XmlValidationError> validationErrors = new ArrayList<XmlValidationError>();
            XmlOptions validationOptions = new XmlOptions();
            validationOptions.setErrorListener(validationErrors);

            // During validation(验证), errors are added to the ArrayList
            boolean isValid = config.validate(validationOptions);
            if (!isValid) {
                StringBuilder sb = new StringBuilder();
                Iterator<XmlValidationError> iter = validationErrors.iterator();
                while (iter.hasNext())
                {
                    sb.append(iter.next());
                    sb.append('\n');
                }
                logger.debug(sb.toString());
            }
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return config;
    }
    /** redis保存数据方法之一
     * 序列化对象
     * @return byte[]
     * @param obj*/
    private byte [] serialize(Object obj){
        ObjectOutputStream obi=null;
        ByteArrayOutputStream bai=null;
        try {
            bai=new ByteArrayOutputStream();
            obi=new ObjectOutputStream(bai);
            obi.writeObject(obj);
            byte[] byt=bai.toByteArray();
            return byt;
        } catch (IOException e) {
            logger.debug("xml object serialize is fail.");
        }
        return null;
    }
    /**
     * 反序列化，将redis中保存的东西解出来
     * @param byt
     * @return obj
     * */
    private Object unserizlize(byte[] byt){
        ObjectInputStream oii=null;
        ByteArrayInputStream bis=null;
        bis=new ByteArrayInputStream(byt);
        try {
            oii=new ObjectInputStream(bis);
            Object obj=oii.readObject();
            return obj;
        } catch (Exception e) {
            logger.error("Object unserizlize error : " + e.getMessage());
        }
        return null;
    }

    @RequestMapping(value = "/serverIp",method = RequestMethod.GET)
    public String getServerIP() {
        Map date = redisService.getHashDate(SERVER_HASH);
        return "ServerIp";
    }

    @RequestMapping(value = "/getDhcpTables",method = RequestMethod.GET)
    public String getDhcpTables(ModelMap map) {
//        if (id == null) {
//            id = 1;
//        }
        map.addAttribute("dhcpTables",assembleDhcpTable());
//        Long size = getDhcpTableSize();
//        Long page;
//        if (size % 10 > 0) {
//            page = (size / 10) + 1;
//        } else {
//            page = size / 10;
//        }

        return "Tables";
    }

    private List<DhcpTable> assembleDhcpTable() {
        List<DhcpTable> dhcpTables = new ArrayList<DhcpTable>();
        Set<String> ipaddress = redisService.getSetDate(IPADDRES_SET);
//        List<String> ips = new ArrayList<String>(ipaddress);
        if (ipaddress != null && !ipaddress.isEmpty()) {
            //开始组合
//            for (int i = (id-1)*10; i < id * 10 && i < ips.size(); i++)
//            {
            Iterator<String> it = ipaddress.iterator();
            while (it.hasNext()) {
                //根据ip为主id到各个表中找数据
                DhcpTable dhcpTable = new DhcpTable();
//                String ip = ips.get(i);
                String ip = it.next();
                dhcpTable.setIpaddress(ip);
                dhcpTable.setDuid(redisService.getHashValue(DUID_HASH.getBytes(),ip.getBytes()));
                dhcpTable.setIatype(redisService.getZSetValue(IATYPE_SORTEDSET.getBytes(),ip.getBytes()));
                String iaid = redisService.getHashValue(IAID_HASH,ip);
                dhcpTable.setIaid(redisService.getHashValue(IAID_HASH,ip));
                dhcpTable.setPrefixlen(redisService.getHashValue(PREFIXLEN_HASH,ip));
                dhcpTable.setState(redisService.getZSetValue(STATE_SORTEDSET.getBytes(),ip.getBytes()));
                dhcpTable.setStarttime(redisService.getHashValue(STARTTIME_HASH,ip));
                dhcpTable.setPreferredEndTime(redisService.getHashValue(PREFERREDENDTIME_HASH,ip));
                dhcpTable.setValidendtime(redisService.getZSetValue(VALIDENDTIME_SORTEDSET,ip));
                dhcpTable.setIaDhcpOptions(redisService.getHashValue(IAOPTIONS_HASH.getBytes(),ip.getBytes()));
                dhcpTable.setIaAddrDhcpOptions(redisService.getHashValue(IAADDROPTIONS_HASH.getBytes(),ip.getBytes()));
                dhcpTables.add(dhcpTable);
            }
            logger.info("DhcpTable complete combination.");
        }
        return dhcpTables;
    }

    private long getDhcpTableSize() {
        return redisService.getSetSize(IPADDRES_SET);
    }
    @RequestMapping(value = "/delDHCPTable",method = RequestMethod.POST)
    @ResponseBody
    public String delDhcpTable(@RequestParam(value = "ipaddress") String ipaddress) {
        //根据ipaddress删除DhcpTable
        deleteDhcpTable(ipaddress);
        //跳转
        return "redirect:/Jargornet-redis/getDhcpTables";
    }

    private void deleteDhcpTable(String ipaddress) {
        redisService.delSetValue(IPADDRES_SET,ipaddress);
        redisService.delHashValue(DUID_HASH.getBytes(),ipaddress.getBytes());
        redisService.delZSetValue(IATYPE_SORTEDSET.getBytes(), ipaddress.getBytes());
        redisService.delHashValue(IAID_HASH, ipaddress);
        redisService.delHashValue(PREFIXLEN_HASH,ipaddress);
        redisService.delZSetValue(STATE_SORTEDSET.getBytes(),ipaddress.getBytes());
        redisService.delHashValue(STARTTIME_HASH,ipaddress);
        redisService.delHashValue(PREFERREDENDTIME_HASH,ipaddress);
        redisService.delZSetValue(VALIDENDTIME_SORTEDSET,ipaddress);
        redisService.delHashValue(IAOPTIONS_HASH.getBytes(),ipaddress.getBytes());
        redisService.delHashValue(IAADDROPTIONS_HASH.getBytes(),ipaddress.getBytes());
    }

    private void delAll() {
        Set<String> keys = redisService.getKeys("DHCP*");
        redisService.delKeys(keys);
    }
    @RequestMapping(value = "/getFreeList",method = RequestMethod.GET)
    public String getFreeList(ModelMap map) {
        Map<String,String> freelists =  assembleFreeList();
        map.addAttribute("freelists",freelists);
        return "FreeList";
    }

    private Map<String,String> assembleFreeList() {
        //先读取FreeList
        Set<String> keys = redisService.getKeys("DHCP_FreeList*");
        Map<String,String> pools = new HashMap<String,String>();
        if (keys != null && !keys.isEmpty()) {
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
     //           free = it.next().split(":");
                String[] strs = it.next().split(":");
                String[] range = strs[1].split("-");

                BigInteger a = new BigInteger(range[0]);
                BigInteger b = new BigInteger(range[1]);
                InetAddress ip1 = null;
                InetAddress ip2 = null;
                try {
                    ip1 = InetAddress.getByAddress(a.toByteArray());
                    ip2 = InetAddress.getByAddress(b.toByteArray());
                    pools.put(ip1.toString(), ip2.toString());
                } catch (UnknownHostException e) {
                    logger.debug(e.getMessage());
                }
            }
        } else {
            logger.debug("DHCP_FreeList* is null in redis.");
        }
        return pools;
    }

    @RequestMapping(value = "/delFreeList", method = RequestMethod.DELETE)
    public String delFreeList(@RequestParam(name = "ip") String ipInterval) {
        //添加一个手动删除，以防万一
        Set<String> keys = redisService.getKeys("*" + ipInterval + "*");
        if (keys != null && !keys.isEmpty()) {
            redisService.delKeys(keys);
        }
        return "redirect:/getFreeList";
    }

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
//    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file,ModelMap map) {
        if (!file.isEmpty()) {

            DhcpServerConfig config = null;
            try {
                config = DhcpServerConfigDocument.Factory.parse(file.getInputStream()).getDhcpServerConfig();

                ArrayList<XmlValidationError> validationErrors = new ArrayList<XmlValidationError>();
                XmlOptions validationOptions = new XmlOptions();
                validationOptions.setErrorListener(validationErrors);

                // During validation(验证), errors are added to the ArrayList
                boolean isValid = config.validate(validationOptions);
                if (!isValid) {
                    StringBuilder sb = new StringBuilder();
                    Iterator<XmlValidationError> iter = validationErrors.iterator();
                    while (iter.hasNext())
                    {
                        sb.append(iter.next());
                        sb.append('\n');
                    }
                    logger.debug(sb.toString());
                    map.addAttribute("msg","文件未验证通过");
                    return "error";
                }
                //验证通过后将其序列化保存到redis中
                byte[] serverConfig = serialize(config);
                redisService.setStringDate(SERVERCONFIG_STRING.getBytes(),serialize(config));
            } catch (FileNotFoundException e) {
                logger.debug(e.getMessage());
                String msg = "上传失败," + e.getMessage();
                map.addAttribute("msg",msg);
                return "error";
            } catch (IOException e) {
                logger.debug(e.getMessage());
                String msg = "上传失败," + e.getMessage();
                map.addAttribute("msg",msg);
                return "error";
            } catch (XmlException e) {
                logger.debug(e.getMessage());
                String msg = "文件格式错误，" + e.getMessage();
                map.addAttribute("msg",msg);
                return "error";
            }

        } else {
            map.addAttribute("msg","上传失败");
            return "error";
        }
        delAll();
        return "redirect:/Jargornet-redis/getDhcpServerConfig";
    }
}
