package io.javabrains.springbootstarter.topic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// note: you don't need to initialize the database because
// apache derby database is embedded inside SpringBoot
// you will connect to an external database at another time

@Service
public class TopicService {
	
	// inject instance of class Topic Repository 
	// into topicRepository
	
	@Autowired
	private TopicRepository topicRepository;
	
	
	private List<Topic> topics = new ArrayList<>(Arrays.asList(
			new Topic("spring", "Spring Framework","Spring Framework Description"),
			new Topic("java", "Core Java","Core Java Description"),
			new Topic("javascript", "JavaScript","JavaScript Description")
			));
	
	
	public List<Topic> getAllTopics() {
		// REVIEW: Lambda expressions
		List<Topic> topics = new ArrayList<>();
		// need to convert findAll() into List. So you add into
		// topics through Lambda expressions
		topicRepository.findAll().forEach(topics::add); 
		return topics;

		
	}
	
	public Topic getTopic(String id) {
		Topic t = topicRepository.findById(id).get();
		return t;
	}
	
	public void addTopic(Topic topic) {
		// create in "CRUD" acrym.
		topicRepository.save(topic);
	}

	public void updateTopic(String id, Topic topic) {
		// Save can do both an add an update. Does replacements
		// if that row for that primary key (in this case id) 
		// does exisits, replacement occurs
		
		topicRepository.save(topic);
	}

	public void deleteTopic(String id) {
		topicRepository.deleteById(id);
	}

}
