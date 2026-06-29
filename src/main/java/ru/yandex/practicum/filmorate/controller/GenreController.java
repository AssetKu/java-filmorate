import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private static final List<Genre> GENRES = List.of(
            new Genre(1, "Комедия"),
            new Genre(2, "Драма"),
            new Genre(3, "Мультфильм"),
            new Genre(4, "Триллер"),
            new Genre(5, "Документальный"),
            new Genre(6, "Боевик")
    );

    @GetMapping
    public List<Genre> getAll() {
        return GENRES;
    }

    @GetMapping("/{id}")
    public Genre getById(@PathVariable int id) {
        return GENRES.stream()
                .filter(g -> g.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Жанр не найден"));
    }
}