package com.example.restservice.controller;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.example.restservice.bean.Greeting;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/greeting")
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping
	public Greeting greeting(@RequestParam(value = "name"/*, defaultValue = "World"*/) String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	@GetMapping("/{id}")
	public Greeting greeting(@PathVariable long id) {
		return new Greeting(counter.incrementAndGet(), String.format(template, "1"));
	}

	@GetMapping("/all")
	public List<Greeting> greeting() {
		return Arrays.asList(
				new Greeting(counter.incrementAndGet(), String.format(template, "client0")),
				new Greeting(counter.incrementAndGet(), String.format(template, "client1")),
				new Greeting(counter.incrementAndGet(), String.format(template, "client2"))
		);
	}

}
