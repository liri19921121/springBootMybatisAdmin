package com.admin.cache;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 缓存初始化(预热)
 * @author rstyro
 *
 */
@Component
public class RedisCache {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
//	@Autowired
//	private CacheService cacheService;
	
	@PostConstruct
	public void init(){
		try {
			System.out.println("预加载");
		} catch (Exception e) {
			log.error("cache init failed", e);
			e.printStackTrace();
		}
	}
	
	
}
