package io.javabrains.springbootstarter.topic;

import javax.persistence.Entity;
import javax.persistence.Id;

// @Entity and @Id JPA knows how to convert a row instance into
// a Topic instance and a Topic into a row in the database


// @Entity tells JPA (Java persistence API) to create a table with
// an id col, name col, and description col. 
@Entity
public class Topic {

	// Every table should have
	// a primary key in a relational database. 
	// Mark ID to be the primary key in this relational database
	@Id
	private String id;
	private String name;
	private String description;
	

	public Topic() {
		
	}
	
	
	public Topic(String id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
