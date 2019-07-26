package techcourse.myblog.domain.comment;

import techcourse.myblog.domain.Article.Article;
import techcourse.myblog.domain.User.User;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;


    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private User author;

    @ManyToOne
    @JoinColumn(name = "article_id", foreignKey = @ForeignKey(name = "fk_comment_to_article"))
    private Article article;


    public String getComment() {
        return comment;
    }

    public User getAuthor() {
        return author;
    }

    public Article getArticle() {
        return article;
    }
}
