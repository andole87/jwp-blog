package techcourse.myblog.interceptor;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import techcourse.myblog.web.AuthedWebTestClient;

@ActiveProfiles("test")
class LoginInterceptorTest extends AuthedWebTestClient {


    @Test
    void 인터셉터_동작() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".+/login");
    }

    @Test
    void 인터셉터_동작2() {
        webTestClient.get().uri("/articles/1")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".+/login");
    }


    @Test
    void 인터셉터_동작_제외() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 인터셉터_동작_제외2() {
        webTestClient.get().uri("/signup")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 인터셉터_동작_제외3() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 인터셉터_우회() {
        get("/users")
                .exchange()
                .expectStatus().isOk();
    }
}