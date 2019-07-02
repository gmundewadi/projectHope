package io.projectHope;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mongodb.DB;
import com.mongodb.MongoClient;

@SpringBootApplication
public class ProjectHopeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectHopeApplication.class, args);
	}

}
