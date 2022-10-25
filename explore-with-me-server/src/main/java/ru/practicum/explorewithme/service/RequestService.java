package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.model.*;
import ru.practicum.explorewithme.model.mapper.RequestMapper;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.RequestRepository;
import ru.practicum.explorewithme.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;

    public ResponseEntity<Object> getRequests(Long userId) throws NotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id %s not found", userId));
        }
        Collection<EventRequest> requests = requestRepository.findAllByRequesterId(userId);

        return ResponseEntity.ok(requests.stream()
                .map(requestMapper::toParticipationRequestDto).collect(Collectors.toList()));
    }

    public ResponseEntity<Object> createRequest(Long userId, Long eventId) throws NotFoundException, BadRequestException {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id %s not found", userId)));
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new BadRequestException(String.format("Event request for userId=%s and eventId=% already exist",
                    userId, eventId));
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event eventId=%s is not exists", eventId)));
        if (event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException(String.format("User userId=%s is eventId=%s initiator",
                    userId, eventId));
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new BadRequestException(String.format("Event eventId=%s is not published", eventId));
        }

        if (!event.isAvailable()) {
            throw new BadRequestException(String.format("Event %s have Participant Limit", eventId));
        }

        EventRequest eventRequest = new EventRequest();
        eventRequest.setRequester(user);
        eventRequest.setEvent(event);
        eventRequest.setCreated(LocalDateTime.now());
        if (!event.getRequestModeration()) {
            event.setState(EventState.PUBLISHED);
        } else {
            eventRequest.setState(EventRequestState.PENDING);
        }
        event.setAvailable(event.getRequests().stream()
                .filter(r -> r.getState() == EventRequestState.CONFIRMED)
                .count() < event.getParticipantLimit()
        );
        EventRequest savedRequest = requestRepository.save(eventRequest);
        return ResponseEntity.ok(requestMapper.toParticipationRequestDto(savedRequest));
    }

    public ResponseEntity<Object> cancelRequest(Long userId, Long requestId) throws NotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id %s not found", userId));
        }
        EventRequest request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException(String.format("Request with id %s not found", requestId)));
        if (request.getState() == EventRequestState.CONFIRMED) {
            Event event = request.getEvent();
            boolean newAvailableStatus = event.getRequests().stream()
                    .filter(r -> r.getState() == EventRequestState.CONFIRMED)
                    .count() < event.getParticipantLimit();
            if (event.isAvailable() != newAvailableStatus) {
                event.setAvailable(newAvailableStatus);
                eventRepository.save(event);
            }
        }
        request.setState(EventRequestState.CANCELED);
        EventRequest savedRequest = requestRepository.save(request);
        return ResponseEntity.ok(requestMapper.toParticipationRequestDto(savedRequest));
    }

    public ResponseEntity<Object> getEventRequests(Long userId, Long eventId) {
        Collection<EventRequest> requests = requestRepository.findAllByRequesterIdAndEventId(userId, eventId);
        return ResponseEntity.ok(requests.stream()
                .map(requestMapper::toParticipationRequestDto).collect(Collectors.toList()));
    }

    public ResponseEntity<Object> confirmRequest(Long userId, Long eventId, Long reqId)
            throws NotFoundException, BadRequestException {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User %s not found", userId));
        }

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event %s not found", eventId)));

        EventRequest eventRequest = requestRepository.findById(reqId).orElseThrow(() ->
                new NotFoundException(String.format("Request %s not found", reqId)));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException(String.format("User %s is not initiator event %s", userId, eventId));
        }

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            eventRequest.setState(EventRequestState.CONFIRMED);
        } else {
            if (!event.isAvailable()) {
                throw new BadRequestException(String.format("Event %s has participant limit", eventId));
            }
            eventRequest.setState(EventRequestState.CONFIRMED);
            boolean newAvailableStatus = event.getRequests().stream()
                    .filter(r -> r.getState() == EventRequestState.CONFIRMED)
                    .count() <= event.getParticipantLimit();
            if (event.isAvailable() != newAvailableStatus) {
                event.setAvailable(newAvailableStatus);
            }
        }

        if (!event.isAvailable()) {
            event.getRequests().stream()
                    .filter(r -> r.getState() != EventRequestState.CONFIRMED)
                    .forEach(r -> r.setState(EventRequestState.REJECTED));
        }
        eventRepository.save(event);
        EventRequest savedRequest = requestRepository.save(eventRequest);
        return ResponseEntity.ok(requestMapper.toParticipationRequestDto(savedRequest));
    }

    public ResponseEntity<Object> rejectRequest(Long userId, Long eventId, Long reqId)
            throws NotFoundException, BadRequestException {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User %s not found", userId));
        }

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event %s not found", eventId)));

        EventRequest eventRequest = requestRepository.findById(reqId).orElseThrow(() ->
                new NotFoundException(String.format("Request %s not found", reqId)));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException(String.format("User %s is not initiator event %s", userId, eventId));
        }
        eventRequest.setState(EventRequestState.REJECTED);
        if (!event.isAvailable()) {
            event.setAvailable(true);
        }
        EventRequest savedRequest = requestRepository.save(eventRequest);
        return ResponseEntity.ok(requestMapper.toParticipationRequestDto(savedRequest));
    }
}
