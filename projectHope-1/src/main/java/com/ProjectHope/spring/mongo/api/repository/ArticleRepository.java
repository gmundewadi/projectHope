package com.ProjectHope.spring.mongo.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ProjectHope.spring.mongo.api.model.Article;

public interface ArticleRepository extends MongoRepository<Article,String> {

}
