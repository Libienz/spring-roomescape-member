package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeRepository {

    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    List<Theme> findTopOrderByReservationCount();

    Theme save(Theme theme);

    void deleteById(Long id);
}
