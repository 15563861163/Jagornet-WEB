# Jagornet-WEB
用以管理Jagornet-redis的redis中保存的数据信息

localhost:8081/Jargornet-redis

1.登录 默认登录码为root 登录端口为8081    在src/main/resources/application.yml中保存修改
2.显示服务器配置文件 服务器配置文件以jagronet文档中xml格式为标准。上传更改后将清除redis相关信息，包括已分配出去的ip信息，正在使用的地址池信息
# Documentation
[Jagornet DHCP Server Community Edition v2.0.5 User Guide](http://www.jagornet.com/products/dhcp-server/docs)
# Downloads
[Jagornet DHCP Server Community Edition v2.0.5 Releases](https://github.com/jagornet/dhcp/releases)

  上传xml文件，将文件序列化后将其保存在redis中，获取的时候再将其反序列化。
  
  public String getServerConfigFromRedis() {
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
    两个必备包：xbean-2.5.jar   xmltypes.jar
    
 3.DHCPTables 显示已经分配出去的ip信息，备用手动删除功能。
 4.DHCPPool 显示正在使用分配的地址池
 
