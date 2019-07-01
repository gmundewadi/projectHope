package io.javabrains.springbootstarter.hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// annotation that indentifies that a class 
// is a rest controller. Rest controllers: have methods map to 
// url requests

@RestController
public class HelloController {
	
	// RequuestMapping maps only to GET methods
	// by default and the parameter 
	// specifies the url. When https .../hello is 
	//requested sayHi() executes. return type returned back 
	@RequestMapping("/hello")
	public String sayHi() {
		return "Hi";
	}
	
}
