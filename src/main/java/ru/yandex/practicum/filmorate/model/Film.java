package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.annotation.AfterFirstFilm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Film {

    private Long id;

    @NonNull
    @NotBlank(message = "Необходимо указать имя")
    private String name;

    @NonNull
    @Size(max = 200, message = "Размер описания не должен быть больше 200 символов")
    private String description;

    @NonNull
    @AfterFirstFilm(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
    private LocalDate releaseDate;

    @Positive(message = "Длительность должна быть больше нуля")
    @NotNull(message = "Duration не может быть null")
    @NonNull
    private Integer duration;

    @NonNull
    private Integer rate;

    @NonNull
    private Mpa mpa;

    private List<Genre> genres;
}
