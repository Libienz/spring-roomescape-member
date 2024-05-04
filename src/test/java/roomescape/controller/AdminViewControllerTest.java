package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AdminViewControllerTest {

    @DisplayName("어드민 메인 페이지 요청 테스트 - 200 OK")
    @Test
    void should_response_200_when_requested_admin_page() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 예약 페이지 요청 테스트 - 200 OK")
    @Test
    void should_response_200_when_requested_reservation_page() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 시간 관리 페이지 요청 테스트 - 200 OK")
    @Test
    void should_response_200_when_requested_time_page() {
        RestAssured.given().log().all()
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 테마 관리 페이지 요청 테스트 - 200 OK")
    @Test
    void should_response_200_when_requested_theme_page() {
        RestAssured.given().log().all()
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }
}
