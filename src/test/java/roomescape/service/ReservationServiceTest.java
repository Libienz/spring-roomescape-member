package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationAddRequest;

class ReservationServiceTest {

    ReservationService reservationService;
    FakeReservationRepository fakeReservationDao;
    FakeReservationTimeRepository fakeReservationTimeDao;
    FakeThemeRepository fakeThemeDao;

    @BeforeEach
    void setUp() {
        fakeReservationDao = new FakeReservationRepository();
        fakeReservationTimeDao = new FakeReservationTimeRepository();
        fakeThemeDao = new FakeThemeRepository();
        reservationService = new ReservationService(fakeReservationDao, fakeReservationTimeDao, fakeThemeDao);
    }

    @DisplayName("존재하지 않는 예약시각으로 예약 시 예외가 발생합니다.")
    @Test
    void should_throw_IllegalArgumentException_when_reserve_non_exist_time() {
        LocalDate reservationDate = LocalDate.now().plusDays(2L);
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(reservationDate, "dodo", 1L, 1L);

        assertThatThrownBy(() -> reservationService.saveReservation(reservationAddRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약 날짜와 예약시각 그리고 테마 아이디가 같은 경우 예외를 발생합니다.")
    @Test
    void should_throw_IllegalArgumentException_when_reserve_date_and_time_duplicated() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 0));
        Theme theme = new Theme(1L, "dummy", "description", "url");

        Reservation reservation = new Reservation(null, "lib", LocalDate.now().plusDays(1), reservationTime, theme);
        ReservationTime savedTime = fakeReservationTimeDao.save(reservationTime);
        Theme savedTheme = fakeThemeDao.save(theme);
        Reservation savedReservation = fakeReservationDao.save(reservation);

        ReservationAddRequest conflictRequest = new ReservationAddRequest(LocalDate.now().plusDays(1), "dodo",
                savedTime.getId(), savedTheme.getId());

        assertThatThrownBy(() -> reservationService.saveReservation(conflictRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
