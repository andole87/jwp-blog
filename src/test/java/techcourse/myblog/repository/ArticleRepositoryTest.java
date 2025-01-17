package techcourse.myblog.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserEmail;
import techcourse.myblog.dto.ArticleDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.findByEmail(UserEmail.of("test@test.com")).get();
        Article article1 = new Article("a1", "b", "c", user);
        Article article2 = new Article("a2", "b", "c", user);
        Article article3 = new Article("a3", "b", "c", user);

        articleRepository.save(article1);
        articleRepository.save(article2);
        articleRepository.save(article3);
        articleRepository.flush();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void createTest() {
        long count = articleRepository.count();
        articleRepository.save(new Article("title", "contents", "coverUrl", user));
        assertThat(articleRepository.count()).isNotEqualTo(count);
    }

    @Test
    void findByIdTest() {
        assertThatThrownBy(() -> articleRepository
                .findById(100L).orElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findAllTest() {
        List<Article> list = articleRepository.findAll();

        assertThat(list.get(0).getTitle()).isEqualTo("a1");
        assertThat(list.get(1).getTitle()).isEqualTo("a2");
        assertThat(list.get(2).getTitle()).isEqualTo("a3");
    }

    @Test
    void UpdateTest() {
        Article article = new Article("test", "test", "test", user);
        articleRepository.save(article);
        ArticleDto articleDto = new ArticleDto("a100", "edit", "edit");
        long id = article.getId();
        article.update(articleDto.toEntity(user));
        Article post = articleRepository.findById(id).orElseThrow(IllegalAccessError::new);

        assertThat(post.getTitle()).isEqualTo(article.getTitle());
        assertThat(post.getContents()).isNotEqualTo("test");
        assertThat(post.getCoverUrl()).isEqualTo("edit");
    }

    @Test
    void deleteTest() {
        Article article = new Article("test", "test", "test", user);
        articleRepository.save(article);
        long id = article.getId();
        articleRepository.deleteById(id);
        assertThatThrownBy(() -> articleRepository.findById(id).orElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);
    }
}