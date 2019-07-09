package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return articles;
    }

    public void addBlog(Article article) {
        articles.add(article);
    }

    public Article find(int i) {
        articles.add(new Article("andole", "easy"));
        return articles.get(i - 1);
    }
}