package ru.practicum.explorewithme.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned = false;
    @NotNull
    @Length(min = 3, max = 120)
    private String title;
}
