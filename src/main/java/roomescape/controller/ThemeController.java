package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeAddRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<Theme>> getThemeList() {
        return ResponseEntity.ok(themeService.findAllTheme());
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> addTheme(@Valid @RequestBody ThemeAddRequest themeAddRequest) {
        ThemeResponse saveResponse = themeService.saveTheme(themeAddRequest);
        URI createdUri = URI.create("/themes/" + saveResponse.id());
        return ResponseEntity.created(createdUri).body(saveResponse);
    }

    @GetMapping("/themes/popular")
    public ResponseEntity<List<ThemeResponse>> getPopularTheme() {
        return ResponseEntity.ok(themeService.findPopularTheme());
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable("id") Long id) {
        themeService.removeTheme(id);
        return ResponseEntity.noContent().build();
    }
}
