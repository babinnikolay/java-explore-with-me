package ru.practicum.explorewithme.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class NewCategoryDto {
    @NotNull
    private String name;
}
