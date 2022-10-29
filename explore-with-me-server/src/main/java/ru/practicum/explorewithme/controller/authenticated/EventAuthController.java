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
import ru.practicum.explorewithme.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventAuthController {
    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping()
    public ResponseEntity<Object> getEvents(@PathVariable @NotNull Long userId,
                                            @RequestParam(required = false, defaultValue = "0") Integer from,
                                            @RequestParam(required = false, defaultValue = "10") Integer size)
            throws NotFoundException {
        log.info("Get events userId={}, from={}, size={}", userId, from, size);
        return ResponseEntity.ok(eventService.getEvents(userId, from, size));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Object> getEvent(@PathVariable @NotNull Long userId, @PathVariable @NotNull Long eventId)
            throws NotFoundException {
        log.info("Get event userId={}, eventId={}", userId, eventId);
        return ResponseEntity.ok(eventService.getEventByInitiator(userId, eventId));
    }

    @PatchMapping
    public ResponseEntity<Object> updateEvent(@PathVariable @NotNull Long userId,
                                              @RequestBody @Valid UpdateEventRequest updateEventRequest)
            throws NotFoundException, BadRequestException {
        log.info("Update event userId={}, updateEventRequest={}", userId, updateEventRequest);
        return ResponseEntity.ok(eventService.updateEvent(userId, updateEventRequest));
    }

    @PostMapping
    public ResponseEntity<Object> createEvent(@PathVariable @NotNull Long userId,
                                              @RequestBody @Valid NewEventDto newEventDto)
            throws NotFoundException {
        log.info("Create event userId={}, newEventDto={}", userId, newEventDto);
        return ResponseEntity.ok(eventService.createEvent(userId, newEventDto));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Object> cancelEvent(@PathVariable @NotNull Long userId,
                                              @PathVariable @NotNull Long eventId)
            throws NotFoundException, BadRequestException {
        log.info("Cancel event userId={}, eventId={}", userId, eventId);
        return ResponseEntity.ok(eventService.cancelEvent(userId, eventId));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<Object> getEventRequests(@PathVariable @NotNull Long userId,
                                                   @PathVariable @NotNull Long eventId) {
        log.info("Get event {} requests for user {}", eventId, userId);
        return ResponseEntity.ok(requestService.getEventRequests(userId, eventId));
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ResponseEntity<Object> confirmRequest(@PathVariable @NotNull Long userId,
                                                 @PathVariable @NotNull Long eventId,
                                                 @PathVariable @NotNull Long reqId)
            throws NotFoundException, BadRequestException {
        log.info("Confirm request userId={}, eventId={}, reqId={}", userId, eventId, reqId);
        return ResponseEntity.ok(requestService.confirmRequest(userId, eventId, reqId));
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ResponseEntity<Object> rejectRequest(@PathVariable @NotNull Long userId,
                                                @PathVariable @NotNull Long eventId,
                                                @PathVariable @NotNull Long reqId)
            throws NotFoundException, BadRequestException {
        log.info("Reject request userId={}, eventId={}, reqId={}", userId, eventId, reqId);
        return ResponseEntity.ok(requestService.rejectRequest(userId, eventId, reqId));
    }
}
