package ru.practicum.explorewithme.model.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.EventRequestState;
import ru.practicum.explorewithme.model.EventState;
import ru.practicum.explorewithme.model.dto.EventFullDto;
import ru.practicum.explorewithme.model.dto.EventShortDto;
import ru.practicum.explorewithme.model.dto.Location;
import ru.practicum.explorewithme.model.dto.NewEventDto;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final ModelMapper mapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    public Event toEvent(NewEventDto eventDto) {
        Event event = mapper.map(eventDto, Event.class);
        event.setCreatedOn(LocalDateTime.now());
        event.setLocationLat(eventDto.getLocation().getLat());
        event.setLocationLon(eventDto.getLocation().getLon());
        event.setState(EventState.PENDING);
        return event;
    }

    public EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = mapper.map(event, EventFullDto.class);
        eventFullDto.setCategory(categoryMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setInitiator(userMapper.toUserShortDto(event.getInitiator()));
        Location location = new Location();
        location.setLat(event.getLocationLat());
        location.setLon(event.getLocationLon());
        eventFullDto.setLocation(location);
        eventFullDto.setConfirmedRequests((int) event.getRequests().stream()
                .filter(r -> r.getState() == EventRequestState.CONFIRMED).count());
        return eventFullDto;
    }

    public EventShortDto toEventShortDto(Event event) {
        EventShortDto eventShortDto = mapper.map(event, EventShortDto.class);
        eventShortDto.setCategory(categoryMapper.toCategoryDto(event.getCategory()));
        eventShortDto.setInitiator(userMapper.toUserShortDto(event.getInitiator()));
        eventShortDto.setConfirmedRequests((int) event.getRequests().stream()
                .filter(r -> r.getState() == EventRequestState.CONFIRMED).count());
        return eventShortDto;
    }

}
