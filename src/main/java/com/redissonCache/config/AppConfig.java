package com.redissonCache.config;

import com.redissonCache.entity.Employee;
import com.redissonCache.repository.EmployeeRepository;
import org.redisson.Redisson;
import org.redisson.api.MapCacheOptions;
import org.redisson.api.MapOptions;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.MapWriter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.redisson.config.Config;

import java.util.Collection;

import java.util.Map;

import static org.redisson.api.MapCacheOptions.defaults;

@Configuration
public class AppConfig implements InitializingBean {

    private final String CACHE_NAME="test-cache";

    private RedissonClient redissonClient;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Value("${spring.redis.sentinel.master}")
    private String redisSentinelMaster;

    @Value("${spring.redis.sentinel.nodes}")
    private String redisSentinelNodes;



    @Bean
    public RMapCache<Long, Employee> rMapCache(){
        final RMapCache<Long, Employee> rMapCache=redissonClient.getMapCache(CACHE_NAME, MapCacheOptions.<Long, Employee>defaults()
                .writer(getMapWriter())
                .writeMode(MapOptions.WriteMode.WRITE_BEHIND));
        return rMapCache;
    }



    private MapWriter<Long, Employee> getMapWriter() {
        return new MapWriter<Long, Employee>() {

            @Override
            public void write(final Map<Long, Employee> map) {
                map.forEach((k, v) -> {
                    employeeRepository.save(v);
                });
            }

            @Override
            public void delete(Collection<Long> keys) {
                keys.stream().forEach(e -> {
                    employeeRepository.delete(employeeRepository.findById(e.intValue()));
                });
            }
        };
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        final Config config = new Config();
        // config.useSingleServer().setAddress("redis://127.0.1.1:6379");
        config.useSentinelServers()
                .setMasterName(redisSentinelMaster)
                .addSentinelAddress("redis://"+redisSentinelNodes) ;
        this.redissonClient = Redisson.create(config);
    }

}
