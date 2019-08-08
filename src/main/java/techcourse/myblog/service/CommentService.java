package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentException;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.repository.CommentRepository;

import java.util.List;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ArticleGenericService articleGenericService;

    public CommentService(CommentRepository commentRepository, UserService userService, ArticleGenericService articleGenericService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.articleGenericService = articleGenericService;
    }

    public Comment add(long articleId, User author, CommentDto commentDto) {
        Article article = articleGenericService.findArticle(articleId, Article.class);
        User user = userService.getUserByEmail(author.getEmail());
        Comment comment = commentDto.toEntity(article, user);
        return commentRepository.save(comment);
    }

    public void delete(long commentId, User author) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException::new);
        if (!comment.isAuthor(author)) {
            throw new CommentException("당신은 죽을수도 있습니다.");
        }
        commentRepository.deleteById(commentId);
    }

    public Comment update(long commentId, User user, CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException::new);
        comment.updateContents(commentDto.getContents(), user);
        return comment;
    }

    public List<Comment> findByArticle(long articleId) {
        Article article = articleGenericService.findArticle(articleId, Article.class);
        return article.getComments();
    }
}
