package com.onetool.server.member;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MemberServiceTest {

    private ExtractableResponse<Response> memberCreateResponse;

    @Test
    void create_member() {
        // given
        final Map<String, Object> params = Map.of(
                "email", "admin@example.com",
                "password", "1234",
                "name", "홍길동",
                "birthDate", "2001-03-26",
                "development_field", "백엔드",
                "phoneNum", "010-0000-0000",
                "isNative", true
        );

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/users/signup")
                .then().log().all()
                .extract();

        // then
        final JsonPath result = response.jsonPath();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(result.getString("email")).isEqualTo("admin@example.com"),
                () -> assertThat(result.getString("name")).isEqualTo("홍길동")
        );
    }

    @Test
    @DisplayName("멤버가 삭제되는지 확인")
    void memberDelete() {

        // given
        if (memberCreateResponse == null) {
            throw new IllegalStateException("회원이 생성되지 않았습니다.");
        }
        final Long userId = memberCreateResponse.jsonPath().getLong("id");

        // when
        final ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
                .when().delete("/users/" + userId)
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(deleteResponse.body().asString()).isEqualTo("회원 탈퇴가 완료되었습니다.")
        );
    }

    @Test
    @DisplayName("로그인 후 회원 정보 조회 성공 테스트")
    void loginAndGetMemberSuccess() {
        // given (회원 가입)
        final Map<String, Object> signupParams = Map.of(
                "email", "admin@example.com",
                "password", "1234",
                "name", "홍길동",
                "birthDate", "2001-03-26",
                "development_field", "백엔드",
                "phoneNum", "01000000000",
                "isNative", true
        );

        // 회원 가입 요청
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(signupParams)
                .when().post("/users/signup")
                .then().log().all()
                .statusCode(201);

        // given (로그인)
        final Map<String, String> loginParams = Map.of(
                "email", "admin@example.com",
                "password", "1234"
        );

        // 로그인 요청
        ExtractableResponse<Response> loginResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginParams)
                .when().post("/users/login")
                .then().log().all()
                .statusCode(200)
                .extract();

        // 로그인 응답에서 Authorization 헤더 추출
        String token = loginResponse.header("Authorization");
        assertThat(token).isNotNull();

        // 토큰에서 "Bearer " 부분 제거
        token = token.replace("Bearer ", "").trim();
        System.out.println("Extracted Token: " + token);

        // when (회원 정보 조회)
        ExtractableResponse<Response> memberResponse = RestAssured.given().log().all()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when().get("/users")
                .then().log().all()
                .statusCode(200)
                .extract();

        // 응답 본문 출력 (디버깅용)
        System.out.println("Member response body: " + memberResponse.body().asString());

        // then (조회 결과 검증)
        assertThat(memberResponse.jsonPath().getString("email")).isEqualTo("admin@example.com");
        assertThat(memberResponse.jsonPath().getString("name")).isEqualTo("홍길동");
        assertThat(memberResponse.jsonPath().getString("birthDate")).isEqualTo("2001-03-26");
        assertThat(memberResponse.jsonPath().getString("development_field")).isEqualTo("백엔드");
        assertThat(memberResponse.jsonPath().getString("phoneNum")).isEqualTo("01000000000");
        assertThat(memberResponse.jsonPath().getBoolean("isNative")).isTrue();
        assertThat(memberResponse.jsonPath().getString("user_registered_at")).isEqualTo(LocalDate.now().toString());
    }

    @Test
    @DisplayName("이름과 전화번호를 통한 이메일 찾기 성공 테스트")
    void findEmailSuccess() {
        // Step 1: Sign up the user
        final Map<String, Object> signupParams = Map.of(
                "email", "admin@example.com",
                "password", "1234",
                "name", "홍길동",
                "birthDate", "2001-03-26",
                "development_field", "백엔드",
                "phoneNum", "01000000000",
                "isNative", true
        );

        // 회원 가입 요청
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(signupParams)
                .when().post("/users/signup")
                .then().log().all()
                .statusCode(201);

        final Map<String, Object> findEmailParams = Map.of(
                "name", "홍길동",
                "phone_num", "01000000000"
        );

        // 이메일 찾기 요청
        ExtractableResponse<Response> findEmailResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(findEmailParams)
                .when().post("/users/email")
                .then().log().all()
                .statusCode(200)
                .extract();

        String expectedEmail = "admin@example.com";
        assertThat(findEmailResponse.body().asString()).isEqualTo(expectedEmail);

        System.out.println("Find email response body: " + findEmailResponse.body().asString());
    }

    @Test
    @DisplayName("로그인후 회원 정보 수정 성공 테스트")
    void updateMemberSuccess() {
        final Map<String, Object> signupParams = Map.of(
                "email", "admin@example.com",
                "password", "1234",
                "name", "홍길동",
                "birthDate", "2001-03-26",
                "development_field", "백엔드",
                "phoneNum", "01000000000",
                "isNative", true,
                "role", "USER" // 필드 추가
        );

        ExtractableResponse<Response> signupResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(signupParams)
                .when().post("/users/signup")
                .then().log().all()
                .statusCode(201)
                .extract();

        final Map<String, Object> loginParams = Map.of(
                "email", "admin@example.com",
                "password", "1234"
        );

        ExtractableResponse<Response> loginResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginParams)
                .when().post("/users/login")
                .then().log().all()
                .statusCode(200)
                .extract();

        String token = loginResponse.header("Authorization");
        assertThat(token).isNotNull();
        token = token.replace("Bearer ", "").trim();
        System.out.println("Extracted Token: " + token);

        final Map<String, Object> updateParams = Map.of(
                "developmentField", "프론트"
        );

        ExtractableResponse<Response> updateResponse = RestAssured.given().log().all()
                .header("Authorization", "Bearer " + token) // 인증 헤더 추가
                .contentType(ContentType.JSON)
                .body(updateParams)
                .when().patch("/users")
                .then().log().all()
                .statusCode(200)
                .extract();

        ExtractableResponse<Response> memberResponse = RestAssured.given().log().all()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when().get("/users")
                .then().log().all()
                .statusCode(200)
                .extract();

        assertThat(memberResponse.jsonPath().getString("email")).isEqualTo("admin@example.com");
        assertThat(memberResponse.jsonPath().getString("name")).isEqualTo("홍길동");
        assertThat(memberResponse.jsonPath().getString("birthDate")).isEqualTo("2001-03-26");
        assertThat(memberResponse.jsonPath().getString("development_field")).isEqualTo("프론트");
        assertThat(memberResponse.jsonPath().getString("phoneNum")).isEqualTo("01000000000");
        assertThat(memberResponse.jsonPath().getBoolean("isNative")).isTrue();
        assertThat(memberResponse.jsonPath().getString("user_registered_at")).isEqualTo(LocalDate.now().toString());
    }

    @Test
    @DisplayName("유저의 구매 내역을 조회하면 정상적으로 응답한다")
    void 유저의_구매내역을_조회하면_정상적으로_응답한다() {
        // given (회원 가입 및 로그인)
        final Map<String, String> loginParams = Map.of(
                "email", "admin@example.com",
                "password", "1234"
        );

        // 로그인 요청
        ExtractableResponse<Response> loginResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginParams)
                .when().post("/users/login")
                .then().log().all()
                .statusCode(200)
                .extract();

        // 로그인 응답에서 Authorization 헤더 추출
        String token = loginResponse.header("Authorization");
        assertThat(token).isNotNull();
        token = token.replace("Bearer ", "").trim();


        // when (구매 내역 조회 요청)
        ExtractableResponse<Response> purchaseResponse = RestAssured.given().log().all()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when().get("/users/1/myPurchase") // 적절한 userId를 사용하세요
                .then().log().all()
                .statusCode(200)
                .extract();

        // 응답 본문 출력
        System.out.println("Purchase response body: " + purchaseResponse.body().asString());

        // then
        assertThat(purchaseResponse.jsonPath().getBoolean("isSuccess")).isTrue();
        assertThat(purchaseResponse.jsonPath().getString("code")).isEqualTo("SUCCESS-0000");
        assertThat(purchaseResponse.jsonPath().getString("message")).isEqualTo("요청에 성공하였습니다.");
    }
}