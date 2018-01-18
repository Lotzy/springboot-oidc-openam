package com.lotzy.sample.ws;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 * Title: HomeController class
 * Description: RestController implementation
 * </pre>
 *
 * @author Lotzy
 * @version 1.0
 */
@RestController
public class HomeController {
	/**
	 * Simple REST method without params
	 * @return String representing the greeting
	 */
	@RequestMapping(value="/greet", method=RequestMethod.GET)
	public ResponseEntity<String> greet() {
		String greeting = "Hello world!";
		return new ResponseEntity<String>(greeting, HttpStatus.OK);
	}

	@RequestMapping("/user")
	public Principal user(Principal principal) {
		return principal;
	}
}
