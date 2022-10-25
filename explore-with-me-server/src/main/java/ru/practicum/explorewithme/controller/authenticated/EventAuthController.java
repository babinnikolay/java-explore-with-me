package ru.practicum.explorewithme.controller.authenticated;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.model.dto.NewEventDto;
import ru.practicum.explorewithme.model.dto.UpdateEventRequest;
import ru.practicum.explorewithme.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventAuthController {
    private final EventService eventService;

    @GetMapping("/{userId}/events")
    public ResponseEntity<Object> getEvents(@PathVariable @NotNull Long userId,
                                            @RequestParam(required = false, defaultValue = "0") Integer from,
                                            @RequestParam(required = false, defaultValue = "10") Integer size)
            throws NotFoundException {
        log.info("Get events userId={}, from={}, size={}", userId, from, size);
        return eventService.getEvents(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<Object> getEvent(@PathVariable @NotNull Long userId, @PathVariable @NotNull Long eventId)
            throws NotFoundException {
        log.info("Get event userId={}, eventId={}", userId, eventId);
        return eventService.getEventByInitiator(userId, eventId);
    }

    @PatchMapping("/{userId}/events")
    public ResponseEntity<Object> updateEvent(@PathVariable @NotNull Long userId,
                                              @RequestBody @Valid UpdateEventRequest updateEventRequest)
            throws NotFoundException, BadRequestException {
        log.info("Update event userId={}, updateEventRequest={}", userId, updateEventRequest);
        return eventService.updateEvent(userId, updateEventRequest);
    }

    @PostMapping("/{userId}/events")
    public ResponseEntity<Object> createEvent(@PathVariable @NotNull Long userId,
                                              @RequestBody @Valid NewEventDto newEventDto)
            throws NotFoundException {
        log.info("Create event userId={}, newEventDto={}", userId, newEventDto);
        return eventService.createEvent(userId, newEventDto);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<Object> cancelEvent(@PathVariable @NotNull Long userId,
                                              @PathVariable @NotNull Long eventId)
            throws NotFoundException, BadRequestException {
        log.info("Cancel event userId={}, eventId={}", userId, eventId);
        return eventService.cancelEvent(userId, eventId);
    }
}
