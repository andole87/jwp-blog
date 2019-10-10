package techcourse.myblog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@SpringBootTest
public class FooTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void name() {
        User[] users = new User[100000];
        for (int i = 0; i < 100000; i++) {
            String temp = UUID.randomUUID().toString();
            users[i] = new User("test", "A!1bcdefg", temp);
        }
        userRepository.saveAll(Arrays.asList(users));
    }

    @Test
    @Transactional
    void a2() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            Article[] articles = new Article[10];
            for (int i = 0; i < 10; i++) {
                articles[i] = new Article(getRandomPassword(10), "contents", "", user);
            }
            articleRepository.saveAll(Arrays.asList(articles));
        }
    }

    public String getRandomPassword(int length) {
        char[] charaters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer sb = new StringBuffer();
        Random rn = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(charaters[rn.nextInt(charaters.length)]);
        }
        return sb.toString();
    }
}
