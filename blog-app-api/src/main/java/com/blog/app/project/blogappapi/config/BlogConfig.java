package com.blog.app.project.blogappapi.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlogConfig {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
