package com.example.URLAPP;

import java.nio.charset.StandardCharsets;

import org.apache.commons.validator.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.hash.Hashing;

@RequestMapping("/rest/url")
@RestController 
public class URLshortener {
	
    @Autowired
    StringRedisTemplate redisTemplate;
    
	@GetMapping("/{id}")
	public String getUrl(@PathVariable String id) {
		String url = redisTemplate.opsForValue().get(id);
		System.out.println("URL retrieved:" + url);
		return "url";
	}
	@PostMapping
	public String create(@RequestBody String url) {
		
		UrlValidator urlvalidator = new UrlValidator(new String[] {"http","https"});
		
		if (urlvalidator.isValid(url)){
			
			String id = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
			
			System.out.println("ID generated:" +id);
			redisTemplate.opsForValue().set(id, url);
			return id;
		}
		throw new RuntimeException("URL invalid");
		
	}
}
