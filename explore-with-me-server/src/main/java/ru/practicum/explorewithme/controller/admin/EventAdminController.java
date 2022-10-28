package ru.practicum.explorewithme.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.model.dto.AdminUpdateEventRequest;
import ru.practicum.explorewithme.model.dto.FilterEventAdminRequest;
import ru.practicum.explorewithme.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventAdminController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<Object> getEvents(@Valid @ModelAttribute FilterEventAdminRequest filter) {
        log.info("Get admin events filter={}", filter);
        return ResponseEntity.ok(eventService.getEvents(filter));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable @NotNull Long eventId,
                                              @RequestBody @Valid AdminUpdateEventRequest eventRequest)
            throws NotFoundException {
        log.info("Edit admin event eventId={}, eventRequest={}", eventId, eventRequest);
        return ResponseEntity.ok(eventService.updateEvent(eventId, eventRequest));
    }

    @PatchMapping("/{eventId}/publish")
    public ResponseEntity<Object> publishEvent(@PathVariable @NotNull Long eventId)
            throws NotFoundException, BadRequestException {
        log.info("Publish admin event eventId={}", eventId);
        return ResponseEntity.ok(eventService.publishEvent(eventId));
    }

    @PatchMapping("/{eventId}/reject")
    public ResponseEntity<Object> rejectEvent(@PathVariable @NotNull Long eventId)
            throws NotFoundException, BadRequestException {
        log.info("Reject admin event eventId={}", eventId);
        return ResponseEntity.ok(eventService.rejectEvent(eventId));
    }
}
