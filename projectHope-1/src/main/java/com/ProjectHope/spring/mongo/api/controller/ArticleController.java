package com.ProjectHope.spring.mongo.api.controller;


import com.ProjectHope.spring.mongo.api.repository.ArticleRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ProjectHope.spring.mongo.api.model.Article;


@RestController
public class ArticleController {

	@Autowired
	private ArticleRepository repository;
	
	@GetMapping("/articles")
	@CrossOrigin(origins = "http://localhost:3000")
	public List<Article> getAllArticles(){
		List<Article> articles = new ArrayList<>();
		repository.findAll().forEach(articles::add); 
		return articles;
	}
	
	@GetMapping("/articles/{link}")
	@CrossOrigin(origins = "http://localhost:3000")
	public Article getArticle(@PathVariable String link) {
		Article a = repository.findById(link).get();
		return a;
	}
	
	
	// Methods written below for testing REST API 
	// endpoints. Not needed in final projectHope
	// application
	
	@DeleteMapping("/articles/{link}")
	@CrossOrigin(origins = "http://localhost:3000")
	public void deleteArticle(@PathVariable String link) {
		repository.deleteById(link);
	}
	
	@PostMapping(value = "/articles")
	@CrossOrigin(origins = "http://localhost:3000")
	public void addArticle(@RequestBody Article a) {
		repository.save((a));
	}
	
	@PutMapping("/articles/{link}")
	@CrossOrigin(origins = "http://localhost:3000")
	public void updateArticle(@RequestBody Article a, @PathVariable String link) {
		repository.save((a));
	}
}
