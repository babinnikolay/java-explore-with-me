package ru.practicum.explorewithme.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
public class UserDto {
    private Long id;
    @Email
    @NotNull
    private String email;
    @NotNull
    private String name;
}
