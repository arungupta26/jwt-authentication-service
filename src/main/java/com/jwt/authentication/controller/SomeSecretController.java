package com.jwt.authentication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/some/secret")
public class SomeSecretController {


	@GetMapping("/get")
	public ResponseEntity<String> getSecret(){
		return ResponseEntity.ok("Secret revealed");
	}

}
