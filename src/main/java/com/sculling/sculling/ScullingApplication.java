package com.sculling.sculling;

import com.alibaba.nacos.api.annotation.NacosProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ScullingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScullingApplication.class, args);
    }

}
