package com.ProjectHope.spring.mongo.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ProjectHope.spring.mongo.api.model.Article;

@Repository
public interface ArticleRepository extends MongoRepository<Article,String> {

}