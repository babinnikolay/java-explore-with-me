package ru.practicum.explorewithme.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
public class NewCategoryDto {
    @NotNull
    private String name;
}
