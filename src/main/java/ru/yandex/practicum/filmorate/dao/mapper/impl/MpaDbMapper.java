package ru.yandex.practicum.filmorate.dao.mapper.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaDbMapper implements MpaMapper {
    
    @Override
    public Mpa makeMpa(ResultSet rs) throws SQLException {
        
        Long id = rs.getLong("MPA_ID");
        String name = rs.getString("MPA_NAME");
        String description = rs.getString("MPA_DESCRIPTION");
        Mpa mpa = new Mpa(id);
        mpa.setName(name);
        mpa.setDescription(description);
        
        return mpa;
    }
}
