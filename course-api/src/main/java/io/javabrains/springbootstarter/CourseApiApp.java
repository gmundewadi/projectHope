package io.javabrains.springbootstarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// Annotation tells SpringBoot this class is
// the starting point for our SpringBoot application
@SpringBootApplication 
public class CourseApiApp {

	public static void main(String[] args) {
		// calling static method spring app
		// passing the name of the class with the main method
		// args parameter is just the passthrough of the args
		SpringApplication.run(CourseApiApp.class, args);
	}

}
