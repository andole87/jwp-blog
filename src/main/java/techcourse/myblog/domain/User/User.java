package techcourse.myblog.domain.User;

import techcourse.myblog.domain.Article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.dto.UserDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Convert(converter = UserNameConverter.class)
    private UserName name;
    @Convert(converter = UserPasswordConverter.class)
    private UserPassword password;

    @Column(unique = true)
    @Convert(converter = UserEmailConverter.class)
    private UserEmail email;

    @OneToMany(mappedBy = "author")
    private List<Article> articles;

    @OneToMany(mappedBy = "author")
    private List<Comment> comments;

    protected User() {
    }

    public User(String name, String password, String email) {
        this.name = UserName.of(name);
        this.password = UserPassword.of(password);
        this.email = UserEmail.of(email);
    }

    public void updateNameAndEmail(String name, String email) {
        this.name.update(name);
        this.email.update(email);
    }

    public boolean isMatchPassword(UserDto dto) {
        return isMatchPassword(dto.getPassword());
    }

    public boolean isMatchPassword(String password) {
        return this.password.match(password);
    }

    public void setArticles(Article article) {
        if (articles == null) {
            articles = new ArrayList<>();
        }
        articles.add(article);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getUserName();
    }

    public String getPassword() {
        return password.getPassword();
    }

    public String getEmail() {
        return email.getEmail();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
