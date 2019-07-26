package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article.Article;
import techcourse.myblog.domain.User.User;
import techcourse.myblog.domain.User.UserException;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.PageableArticleService;
import techcourse.myblog.service.UserService;
import techcourse.myblog.web.support.UserSessionInfo;

@Controller
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private final PageableArticleService articleService;
    private final UserService userService;

    public ArticleController(PageableArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Model model,
                        @PageableDefault(sort = {"id"}, size = 3, direction = Sort.Direction.DESC) Pageable pageable) {
        log.debug("Pageable : {}", pageable);
        model.addAttribute("articles", articleService.findAllPage(pageable));
        return "index";
    }

    @GetMapping("/writing")
    public String getArticleEditForm() {
        return "article-edit";
    }

    @GetMapping("/articles/{articleId}")
    public String getArticle(@PathVariable long articleId, Model model) {
        setArticleModel(model, articleService.findArticle(articleId));
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String getEditArticle(@PathVariable long articleId, Model model) {
        setArticleModel(model, articleService.findArticle(articleId));
        return "article-edit";
    }

    @PostMapping("/articles")
    public String saveArticle(ArticleDto dto, UserSessionInfo userSessionInfo) {
        User user = userService.findByEmail(userSessionInfo.getEmail()).orElseThrow(UserException::new);
        return "redirect:/articles/" + articleService.save(user, dto.toEntity());
    }

    @PutMapping("/articles/{articleId}")
    public String getModifiedArticle(@PathVariable long articleId, ArticleDto dto, Model model) {
        setArticleModel(model, articleService.update(articleId, dto));
        return "article";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable long articleId) {
        articleService.delete(articleId);
        return "redirect:/";
    }

    private void setArticleModel(Model model, Article article) {
        model.addAttribute("article", article);
    }
}