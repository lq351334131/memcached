package com.etocrm.memcached.config;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class MemcachedConfig {

    private static Logger logger = LoggerFactory.getLogger(MemcachedConfig.class);

    @Autowired
    private MemcachedProperties memcachedProperties;

    @Bean(name = "memcachedClientBuilder")
    public MemcachedClientBuilder getBuilder() {
        String[] servers = memcachedProperties.getServer().split(" ");
        List<InetSocketAddress> inetSocketAddressList = new ArrayList<>();
        for (int i=0;i<servers.length;i++){
            String host = servers[i].substring(0, servers[i].lastIndexOf(":"));
            Integer port = Integer.valueOf(servers[i].substring((servers[i].lastIndexOf(":") + 1),servers[i].length()));
            InetSocketAddress inetSocketAddress = new InetSocketAddress(host,port);
            inetSocketAddressList.add(inetSocketAddress);
        }

        MemcachedClientBuilder memcachedClientBuilder = new XMemcachedClientBuilder(inetSocketAddressList);
        // 内部采用一致性哈希算法
        memcachedClientBuilder.setSessionLocator(new KetamaMemcachedSessionLocator());
        // 操作的超时时间
        memcachedClientBuilder.setOpTimeout(memcachedProperties.getOpTimeout());
        // 设置连接池的大小
        memcachedClientBuilder.setConnectionPoolSize(memcachedProperties.getPoolSize());
        // 是否开起失败模式
        memcachedClientBuilder.setFailureMode(memcachedProperties.isFailureMode());
       // memcachedClientBuilder.setTranscoder(new TokyoTyrantTranscoder());
        return memcachedClientBuilder;
    }

    /**
     * 由Builder创建memcachedClient对象，并注入spring容器中
     * @param memcachedClientBuilder
     * @return
     */
    @Bean(name = "memcachedClient")
    public MemcachedClient getClient(@Qualifier("memcachedClientBuilder") MemcachedClientBuilder memcachedClientBuilder) {
        MemcachedClient client = null;
        try {
            client =  memcachedClientBuilder.build();
        } catch(Exception e) {
            logger.info("exception happens when bulid memcached client{}",e.toString());
        }
        return client;
    }


}
