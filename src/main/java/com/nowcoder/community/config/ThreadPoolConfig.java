package com.nowcoder.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author xuming
 * @Date 2019/11/13 15:13
 */
@Configuration
@EnableScheduling
@EnableAsync
public class ThreadPoolConfig {
}
