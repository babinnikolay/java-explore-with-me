package ru.practicum.explorewithme.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Location {
    private Float lat;
    private Float lon;
}
