package techcourse.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.domain.article.Article;

public interface ArticlePageableRepository extends JpaRepository<Article, Long> {

}
