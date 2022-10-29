package ru.practicum.explorewithme.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ru.practicum.explorewithme.validator.ValidNewEventDate;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class NewEventDto {
    @Length(min = 20, max = 2000)
    @NotNull
    private String annotation;
    @NotNull
    private Long category;
    @Length(min = 20, max = 7000)
    @NotNull
    private String description;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ValidNewEventDate
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    private boolean paid = false;
    private int participantLimit;
    private boolean requestModeration;
    @Length(min = 3, max = 120)
    @NotNull
    private String title;
}
