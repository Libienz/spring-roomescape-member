package roomescape.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static roomescape.auth.fixture.TokenFixture.DUMMY_TOKEN;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.global.exception.InvalidTokenException;
import roomescape.global.exception.NoSuchRecordException;
import roomescape.global.exception.WrongPasswordException;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private MemberRepository memberRepository;


    @DisplayName("올바른 로그인 요청이 들어오면 토큰을 반환한다")
    @Test
    void should_return_token_when_valid_login_request_arrived() {
        Member member = new Member(1L, "리비", "aa@gmail.com", "123");

        when(memberRepository.findByEmail(any(String.class))).thenReturn(Optional.of(member));
        when(jwtTokenProvider.createToken(any(Member.class))).thenReturn(DUMMY_TOKEN.getAccessToken());

        LoginRequest request = new LoginRequest("aa@gmail.com", "123");
        assertThat(authService.login(request).getAccessToken()).isNotNull();
    }

    @DisplayName("로그인 요청 시 이메일에 해당하는 유저를 찾을 수 없을 경우 예외가 발생한다")
    @Test
    void should_throw_exception_when_email_not_exist() {
        when(memberRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        LoginRequest request = new LoginRequest("aa@gmail.com", "123");

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(NoSuchRecordException.class);
    }

    @DisplayName("로그인 요청 시 비밀번호가 틀리다면 예외가 발생한다")
    @Test
    void should_throw_exception_when_password_is_incorrect() {
        Member member = new Member(1L, "리비", "aa@gmail.com", "123");

        when(memberRepository.findByEmail(any(String.class))).thenReturn(Optional.of(member));
        LoginRequest request = new LoginRequest("aa@gmail.com", "1234");

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(WrongPasswordException.class);
    }

    @DisplayName("유효한 토큰으로 로그인 체크 시 이름을 반환한다")
    @Test
    void should_return_name_response_when_valid_token_check_performed() {
        Member member = new Member(1L, "리비", "aa@gmail.com", "123");

        when(jwtTokenProvider.isInvalidToken(any(String.class))).thenReturn(false);
        when(jwtTokenProvider.getPayload(DUMMY_TOKEN.getAccessToken())).thenReturn("1");
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));

        assertThat(authService.checkLogin(DUMMY_TOKEN.getAccessToken()).name()).isEqualTo("리비");
    }

    @DisplayName("로그인 체크 시 유효하지 않은 토큰이라면 예외가 발생한다")
    @Test
    void should_throw_exception_when_token_is_invalid() {
        when(jwtTokenProvider.isInvalidToken(any(String.class))).thenReturn(true);

        assertThatThrownBy(() -> authService.checkLogin(DUMMY_TOKEN.getAccessToken()))
                .isInstanceOf(InvalidTokenException.class);
    }

    @DisplayName("토큰에서 유저의 아이디를 추출했을 때 그 아이디에 해당하는 회원이 없으면 예외를 발생시킨다")
    @Test
    void should_throw_exception_when_token_member_no_exist() {
        when(jwtTokenProvider.isInvalidToken(any(String.class))).thenReturn(false);
        when(jwtTokenProvider.getPayload(DUMMY_TOKEN.getAccessToken())).thenReturn("1");
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.checkLogin(DUMMY_TOKEN.getAccessToken()))
                .isInstanceOf(NoSuchRecordException.class);
    }
}