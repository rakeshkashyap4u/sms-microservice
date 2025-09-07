package com.rakesh.sms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	@GetMapping("/test/{msg}")
	public String testSMS(@PathVariable String msg )
	{
		return "Test OK "+msg;
	}

}
