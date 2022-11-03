package ru.practicum.explorewithme.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.explorewithme.model.PublicationStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentDto {
    private Long id;
    @NotBlank
    @Size(min = 3, max = 2000)
    private String text;
    private LocalDateTime created;
    @NotNull
    private Long event;
    private PublicationStatus status = PublicationStatus.PENDING;
}
