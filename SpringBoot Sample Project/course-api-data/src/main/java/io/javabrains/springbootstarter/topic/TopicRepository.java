package io.javabrains.springbootstarter.topic;

import org.springframework.data.repository.CrudRepository;

// Make this an interface because methods that create/read/update/delete information
// in the database is given by the Spring data JPA framework
// Found in the CrudRepository. CrudRepository is a generic type
// you need to identify the entity class you are working with, 
// id the entity class has

public interface TopicRepository extends CrudRepository<Topic, String>{

	
	
	
}
