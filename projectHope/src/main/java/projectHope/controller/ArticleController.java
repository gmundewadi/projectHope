package projectHope.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import projectHope.model.article;
import projectHope.repository.ArticleRepository;

@RestController
public class ArticleController {

	@Autowired
	private ArticleRepository articleRepository;
	
	
	@GetMapping("/articles")
	public List<article> getAllArticles(){
		List<article> articles = new ArrayList<>();
		articleRepository.findAll().forEach(articles::add); 
		return articles;
	}
	
	@RequestMapping("/articles/{title}")
	public article getArticle(@PathVariable String title) {
		article a = articleRepository.findById(title).get();
		return a;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/articles/{title}")
	public void deleteArticle(@PathVariable String title) {
		articleRepository.deleteById(title);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/articles")
	public void addArticle(@RequestBody article a) {
		articleRepository.save((a));
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/articles/{title}")
	public void updateArticle(@RequestBody article a, @PathVariable String title) {
		articleRepository.save((a));
	}
		
}
