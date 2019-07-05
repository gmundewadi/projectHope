package com.ProjectHope.spring.mongo.api.controller;


import com.ProjectHope.spring.mongo.api.repository.ArticleRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	public List<Article> getAllArticles(){
		System.out.println("HERE");
		List<Article> articles = new ArrayList<>();
		repository.findAll().forEach(articles::add); 
		return articles;
	}
	
	@GetMapping("/articles/{title}")
	public Article getArticle(@PathVariable String title) {
		Article a = repository.findById(title).get();
		return a;
	}
	
	@DeleteMapping("/articles/{title}")
	public void deleteArticle(@PathVariable String title) {
		repository.deleteById(title);
	}
	
	@PostMapping(value = "/articles")
	public void addArticle(@RequestBody Article a) {
		repository.save((a));
	}
	
	@PutMapping("/articles/{title}")
	public void updateArticle(@RequestBody Article a, @PathVariable String title) {
		repository.save((a));
	}
}
