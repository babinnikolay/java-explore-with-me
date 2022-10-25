package ru.practicum.explorewithme.model.mapper;

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
public class EventMapper {

    private static ModelMapper mapper;

    public EventMapper(ModelMapper modelMapper) {
        EventMapper.mapper = modelMapper;
    }

    public static Event toEvent(NewEventDto eventDto) {
        Event event = mapper.map(eventDto, Event.class);
        event.setCreatedOn(LocalDateTime.now());
        event.setLocationLat(eventDto.getLocation().getLat());
        event.setLocationLon(eventDto.getLocation().getLon());
        event.setState(EventState.PENDING);
        return event;
    }

    public static EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = mapper.map(event, EventFullDto.class);
        eventFullDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        Location location = new Location();
        location.setLat(event.getLocationLat());
        location.setLon(event.getLocationLon());
        eventFullDto.setLocation(location);
        eventFullDto.setConfirmedRequests((int) event.getRequests().stream()
                .filter(r -> r.getState() == EventRequestState.CONFIRMED).count());
        return eventFullDto;
    }

    public static EventShortDto toEventShortDto(Event event) {
        EventShortDto eventShortDto = mapper.map(event, EventShortDto.class);
        eventShortDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventShortDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventShortDto.setConfirmedRequests((int) event.getRequests().stream()
                .filter(r -> r.getState() == EventRequestState.CONFIRMED).count());
        return eventShortDto;
    }

}
