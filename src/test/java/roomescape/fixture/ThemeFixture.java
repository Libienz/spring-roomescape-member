package roomescape.fixture;

import roomescape.domain.Theme;
import roomescape.dto.request.ThemeAddRequest;

public class ThemeFixture {
    public static final ThemeAddRequest THEME_ADD_REQUEST = new ThemeAddRequest("name", "description", "thumb");

    public static final Theme THEME_1 = new Theme(1L, "name", "des", "thumb");
    public static final Theme THEME_2 = new Theme(2L, "name", "des", "thumb");
    public static final Theme THEME_3 = new Theme(3L, "name", "des", "thumb");
}
