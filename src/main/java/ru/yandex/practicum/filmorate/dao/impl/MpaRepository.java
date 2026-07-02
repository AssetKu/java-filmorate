package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.dao.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaRepository implements MpaDao {
    
    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mpaMapper;
    
    @Override
    public Mpa get(Long id) {
        String sqlQuery = "select " +
            "MPA_ID, " +
            "MPA_NAME, " +
            "MPA_DESCRIPTION " +
            "from MPAS where MPA_ID = ?";
        
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mpaMapper.makeMpa(rs), id)
            .stream()
            .findAny()
            .orElseThrow(() -> {
                throw new MpaNotFoundException(String
                    .format("MPA с id=%s не найден в базе.", id));
            });
    }
    
    @Override
    public List<Mpa> getAllMpas() {
        String sqlQuery = "select " +
            "MPA_ID, " +
            "MPA_NAME, " +
            "MPA_DESCRIPTION " +
            "from MPAS " +
            "order by MPA_ID";
        
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mpaMapper.makeMpa(rs));
    }
}
