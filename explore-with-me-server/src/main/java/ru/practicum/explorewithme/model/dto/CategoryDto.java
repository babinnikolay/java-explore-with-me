package ru.practicum.explorewithme.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
public class CategoryDto {
    @NotNull
    private Long id;
    @NotNull
    private String name;
}
