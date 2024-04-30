package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationAddRequest;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<Reservation> findAllReservation() {
        return reservationDao.findAll();
    }

    public Reservation addReservation(ReservationAddRequest reservationAddRequest) {
//        reservationAddRequest.toEntity()
        return reservationDao.insert(reservationAddRequest);
    }

    public void removeReservation(Long id) {
        if (reservationDao.findById(id).isEmpty()) {
            throw new IllegalArgumentException("해당 id를 가진 예약이 존재하지 않습니다.");
        }
        reservationDao.deleteById(id);
    }
}
