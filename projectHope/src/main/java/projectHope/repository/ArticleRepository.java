package projectHope.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import projectHope.model.article;

public interface ArticleRepository extends MongoRepository<article, String>{

}
