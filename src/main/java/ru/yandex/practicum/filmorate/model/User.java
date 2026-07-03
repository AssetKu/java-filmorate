package ru.yandex.practicum.filmorate.model;

import lombok.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

@Setter
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @NonNull
    private Long id;

    @NonNull
    @Email(message = "Email должен быть корректным адресом электронной почты")
    @NotNull(message = "Email не может быть null")
    private String email;

    @NonNull
    @NotBlank(message = "Логин не может состоять из пробелов или быть null")
    private String login;

    @NonNull
    private String name;

    @NonNull
    @NotNull(message = "Birthday не может быть null")
    @Past(message = "Дата рожденья не может быть в будущем или в настоящем")
    private LocalDate birthday;
}
