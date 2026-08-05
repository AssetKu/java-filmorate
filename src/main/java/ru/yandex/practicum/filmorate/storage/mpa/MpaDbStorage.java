package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAll() {

        String sql = """
                SELECT MPA_ID,
                       MPA_NAME
                FROM MPAS
                ORDER BY MPA_ID
                """;

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new Mpa(
                        rs.getInt("MPA_ID"),
                        rs.getString("MPA_NAME")
                ));
    }

    @Override
    public Mpa getById(int id) {

        String sql = """
                SELECT MPA_ID,
                       MPA_NAME
                FROM MPAS
                WHERE MPA_ID = ?
                """;

        List<Mpa> mpas = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Mpa(
                        rs.getInt("MPA_ID"),
                        rs.getString("MPA_NAME")
                ),
                id
        );

        if (mpas.isEmpty()) {
            throw new NotFoundException("Рейтинг не найден");
        }

        return mpas.get(0);
    }
}
