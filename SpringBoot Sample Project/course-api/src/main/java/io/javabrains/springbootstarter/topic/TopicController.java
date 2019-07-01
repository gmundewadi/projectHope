package io.javabrains.springbootstarter.topic;

import java.util.List;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopicController {
	
	// Spring annotation that marks this member variable that
	// needs dependency injection.
	// this occurs when Spring creates a TopicContoller instance
	// and then reads this line and injects the Topic Service class
	// into this private member variable
	
	@Autowired
	private TopicService topicService;
	
	@RequestMapping("/topics")
	public List<Topic> getAllTopics() {
		return topicService.getAllTopics();
	}
	
	// {id} tells spring that that part of the http request is
	// a variable. That way /topics/foo or /topics/hello etc will all map
	// to the method getTopic(String id). @PathVariable tells 
	// spring that  {id} = String id 
	
	@RequestMapping("/topics/{id}")
	public Topic getTopic(@PathVariable String id) {
		return topicService.getTopic(id);
	}
	
	// Map this method to any request that is a post on /topics
	// when a post request happens to /topics the method addTopic()
	// will be called. RequestBody tells Spring MVC that your request payload
	// (you can use postman to define what this payload is)
	// will contain a JSON representation of a topic that is to be added
	// when /topics with a post request is requested
	
	@RequestMapping(method = RequestMethod.POST, value = "/topics")
	public void addTopic(@RequestBody Topic topic) {
		topicService.addTopic(topic);
	}
	
	// .PUT is a way to update information. Method searches list of all topics
	// and updates topic with given id with topic passes as parameter
	
	@RequestMapping(method = RequestMethod.PUT, value = "/topics/{id}")
	public void updateTopic(@RequestBody Topic topic, @PathVariable String id) {
		topicService.updateTopic(id, topic);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/topics/{id}")
	public void deleteTopic(@PathVariable String id) {
		topicService.deleteTopic(id);
	}
	
	
}
