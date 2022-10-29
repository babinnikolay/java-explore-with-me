package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.model.*;
import ru.practicum.explorewithme.model.dto.*;
import ru.practicum.explorewithme.model.mapper.EventMapper;
import ru.practicum.explorewithme.repository.CategoryRepository;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.UserRepository;
import ru.practicum.explorewithme.statisticclient.StatisticClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EventService {
    private static final String SERVICE_NAME = "emw-server";
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatisticClient statisticClient;
    private final EventMapper eventMapper;

    public List<EventShortDto> getEvents(FilterEventOpenRequest filter, HttpServletRequest request) {
        String sortField = "id";
        if (filter.getSort() == SortTypes.EVENT_DATE) {
            sortField = "eventDate";
        } else if (filter.getSort() == SortTypes.VIEWS) {
            sortField = "views";
        }
        PageRequest page = PageRequest.of(filter.getFrom() / filter.getSize(),
                filter.getSize(), Sort.by(sortField));
        if (filter.getText() != null) {
            filter.setText(("%" + filter.getText() + "%"));
        }
        List<Event> events = eventRepository.findAllByFilter(filter, page);

        events.forEach(e -> {
            List<ViewStats> stats = statisticClient.stats(filter.getRangeStart(),
                    filter.getRangeEnd(),
                    List.of(String.format("/events/%s", e.getId())),
                    false);
            if (stats.isEmpty()) {
                e.setViews(0);
            } else {
                e.setViews(stats.get(0).getHits());
            }
        });

        if (filter.getSort() == SortTypes.VIEWS) {
            events.sort(Comparator.comparingInt(Event::getViews));
        }

        eventRepository.saveAll(events);
        saveStatisticHit(request);

        return events.stream().map(eventMapper::toEventShortDto).collect(Collectors.toList());
    }

    public List<EventFullDto> getEvents(FilterEventAdminRequest filter) {
        PageRequest page = PageRequest.of(filter.getFrom() / filter.getSize(), filter.getSize());
        List<Event> events = eventRepository.findAllByFilter(filter, page);

        return events.stream().map(eventMapper::toEventFullDto).collect(Collectors.toList());
    }

    public EventFullDto getEvent(Long id, HttpServletRequest request) throws NotFoundException {
        Event event = eventRepository.findByIdAndState(id, EventState.PUBLISHED).orElseThrow(() ->
                new NotFoundException(String.format("Event with id %s not found", id)));
        List<String> uris = List.of(request.getRequestURI());
        List<ViewStats> stats = statisticClient.stats(LocalDateTime.now(), LocalDateTime.now(), uris, true);
        int hits = stats.stream()
                .filter(s -> s.getApp().equals(SERVICE_NAME))
                .map(ViewStats::getHits).mapToInt(Integer::intValue).sum();

        event.setViews(hits);
        eventRepository.save(event);
        saveStatisticHit(request);
        return eventMapper.toEventFullDto(event);
    }

    public EventFullDto getEventByInitiator(Long userId, Long eventId) throws NotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with if %s not found", userId));
        }
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id %s not found", eventId)));
        return eventMapper.toEventFullDto(event);
    }

    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with if %s not found", userId)));

        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() ->
                new NotFoundException(String.format("Category with id=%s not found", newEventDto.getCategory())));

        Event event = eventMapper.toEvent(newEventDto);
        event.setInitiator(user);
        event.setCategory(category);
        event.setAvailable(true);
        Event newEvent = eventRepository.save(event);
        return eventMapper.toEventFullDto(newEvent);
    }

    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) throws NotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with if %s not found", userId));
        }
        PageRequest page = PageRequest.of(from / size, size);
        Collection<Event> events = eventRepository.findAllByInitiatorId(userId, page);
        return events.stream().map(eventMapper::toEventShortDto).collect(Collectors.toList());
    }

    public EventFullDto updateEvent(Long userId, UpdateEventRequest updateEventRequest)
            throws NotFoundException, BadRequestException {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id %s not found", userId));
        }
        Event event = eventRepository.findById(updateEventRequest.getEventId()).orElseThrow(() ->
                new NotFoundException(String.format("Event with id %s not found", updateEventRequest.getEventId())));
        if (event.getState() == EventState.PUBLISHED) {
            throw new BadRequestException(String.format("Event with id %d is published", updateEventRequest.getEventId()));
        }
        if (event.getState() == EventState.CANCELED) {
            event.setState(EventState.PENDING);
        }
        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventRequest.getCategory()).orElseThrow(() ->
                    new NotFoundException(String.format("Category with id %s not found",
                            updateEventRequest.getCategory())));
            event.setCategory(category);
        }
        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getEventDate() != null) {
            event.setEventDate(updateEventRequest.getEventDate());
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toEventFullDto(savedEvent);
    }

    public EventFullDto updateEvent(Long eventId, AdminUpdateEventRequest eventRequest) throws NotFoundException {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id %s not found", eventId)));
        event.setAnnotation(eventRequest.getAnnotation());
        Category category = categoryRepository.findById(eventRequest.getCategory()).orElse(null);
        event.setCategory(category);
        event.setDescription(eventRequest.getDescription());
        event.setEventDate(eventRequest.getEventDate());
        if (eventRequest.getLocation() != null) {
            event.setLocationLon(eventRequest.getLocation().getLon());
            event.setLocationLat(eventRequest.getLocation().getLat());
        } else {
            event.setLocationLat(null);
            event.setLocationLon(null);
        }
        event.setPaid(eventRequest.getPaid());
        if (event.getParticipantLimit() < eventRequest.getParticipantLimit()) {
            event.setAvailable(true);
        }
        event.setParticipantLimit(eventRequest.getParticipantLimit());
        event.setRequestModeration(eventRequest.getRequestModeration());
        event.setTitle(eventRequest.getTitle());
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toEventFullDto(savedEvent);
    }

    public EventFullDto cancelEvent(Long userId, Long eventId) throws NotFoundException, BadRequestException {
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event %s with initiator %s not found", eventId, userId)));
        if (event.getState() != EventState.PENDING) {
            throw new BadRequestException(String.format("Event %s is not PENDING state", eventId));
        }
        event.setState(EventState.CANCELED);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toEventFullDto(savedEvent);
    }

    public EventFullDto publishEvent(Long eventId) throws NotFoundException, BadRequestException {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id %s not found", eventId)));
        LocalDateTime now = LocalDateTime.now();
        if (event.getEventDate().isBefore(now.plusHours(1)) || event.getState() != EventState.PENDING) {
            throw new BadRequestException(String.format("Event %s is before or wrong state", eventId));
        }
        event.setPublishedOn(now);
        event.setState(EventState.PUBLISHED);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toEventFullDto(savedEvent);
    }

    public EventFullDto rejectEvent(Long eventId) throws NotFoundException, BadRequestException {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id %s not found", eventId)));
        if (event.getState() == EventState.PUBLISHED) {
            throw new BadRequestException(String.format("Event %s is published", eventId));
        }
        event.setState(EventState.CANCELED);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toEventFullDto(savedEvent);
    }

    private void saveStatisticHit(HttpServletRequest request) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp(SERVICE_NAME);
        endpointHit.setUri(request.getRemoteAddr());
        endpointHit.setIp(request.getRequestURI());
        endpointHit.setTimestamp(LocalDateTime.now());
        statisticClient.hit(endpointHit);
    }
}
