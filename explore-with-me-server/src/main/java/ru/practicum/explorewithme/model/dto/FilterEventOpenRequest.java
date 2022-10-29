package ru.practicum.explorewithme.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.explorewithme.model.EventState;
import ru.practicum.explorewithme.model.SortTypes;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class FilterEventOpenRequest {
    private String text;
    private Long[] categories;
    private Boolean paid;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeStart = LocalDateTime.now();
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private SortTypes sort;
    private Integer from = 0;
    private Integer size = 10;
    private EventState state = EventState.PUBLISHED;
}
