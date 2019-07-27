package techcourse.myblog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserEmail;
import techcourse.myblog.domain.user.UserException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticlePageableRepository articleRepository;

    @Test
    void register_bad_case() {
        assertThatThrownBy(() -> new User("z", "b", "c")).isInstanceOf(UserException.class);
    }

    @Test
    void findByNameTest() {
        userRepository.save(new User("andole", "A!1bcdefg", "andole@gmail.com"));
        assertThatThrownBy(() -> userRepository.findByEmail(UserEmail.of("abc@gmail.com")).orElseThrow(UserException::new))
                .isInstanceOf(UserException.class);
    }

    @Test
    void slave() {
        User persistUser = userRepository.save(new User("andole", "A!1bccefg", "andole@gmail.com"));
        persistUser.addArticle(new Article("a", "b", "c"));
        assertThat(userRepository.findById(persistUser.getId()).get().getArticles().get(0).getTitle()).isEqualTo("a");
        assertThat(articleRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    void master() {
        Article persistArticle = articleRepository.save(new Article("a", "b", "c"));
        User user = userRepository.save(new User("andole", "A!1bcdefg", "andole@gmail.com"));
        user.updateNameAndEmail("ab", "a@a.com");
        persistArticle.setAuthor(user);
        assertThat(articleRepository.findById(persistArticle.getId()).get().getAuthor().getName()).isEqualTo("ab");
        assertThatThrownBy(() -> userRepository.findByEmail(UserEmail.of("andole@gmail.com")).orElseThrow(UserException::new))
                .isInstanceOf(UserException.class);
    }

    @Test
    void slave1() {
        User persistUser = userRepository.save(new User("andole", "A!1bcdefg", "andole@gmail.com"));
        Article article = new Article("a", "b", "c");
        persistUser.addArticle(article);
        article.update(new Article("z", "x", "c"));


        assertThat(userRepository.findById(persistUser.getId()).get().getArticles().get(0).getTitle()).isEqualTo("z");
        assertThat(articleRepository.findAll().size()).isEqualTo(0);

    }
}