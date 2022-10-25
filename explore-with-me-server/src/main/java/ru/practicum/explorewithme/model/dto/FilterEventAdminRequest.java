package ru.practicum.explorewithme.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.explorewithme.model.EventState;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class FilterEventAdminRequest {
    private long[] users;
    private EventState[] states;
    private long[] categories;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeEnd;
    private Integer from = 0;
    private Integer size = 10;
}
