package com.hotnews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * Author: XianDaLi
 * Date: 2020/8/4 2:58
 * Remark:
 */
@SpringBootApplication
public class HotNewsApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(HotNewsApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(HotNewsApplication.class);
	}
}
