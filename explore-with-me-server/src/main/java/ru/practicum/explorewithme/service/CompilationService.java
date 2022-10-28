package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.dto.CompilationDto;
import ru.practicum.explorewithme.model.dto.NewCompilationDto;
import ru.practicum.explorewithme.model.mapper.CompilationMapper;
import ru.practicum.explorewithme.repository.CompilationRepository;
import ru.practicum.explorewithme.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        List<Compilation> compilations = compilationRepository.findAll(pinned, page);
        return compilations.stream().map(compilationMapper::toCompilationDto).collect(Collectors.toList());
    }

    public CompilationDto getCompilation(Long compId) throws NotFoundException {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Compilation %s not found", compId)));

        return compilationMapper.toCompilationDto(compilation);
    }

    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = compilationMapper.toCompilation(compilationDto);
        List<Event> events = eventRepository.findAllById(compilationDto.getEvents());
        compilation.setEvents(events);
        Compilation savedCompilation = compilationRepository.save(compilation);
        return compilationMapper.toCompilationDto(savedCompilation);
    }

    public void deleteCompilation(Long compId) throws NotFoundException {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException(String.format("Compilation with %s id not found", compId));
        }
        compilationRepository.deleteById(compId);
    }

    public CompilationDto deleteEvent(Long compId, Long eventId) throws BadRequestException, NotFoundException {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Compilation %s not found", compId)));

        Event event = compilation.getEvents()
                .stream()
                .filter(e -> e.getId().equals(eventId))
                .findAny()
                .orElseThrow(() ->
                        new BadRequestException(
                                String.format("Compilation %s does not have event %s", compId, eventId)));
        compilation.getEvents().remove(event);
        Compilation savedCompilation = compilationRepository.save(compilation);
        return compilationMapper.toCompilationDto(savedCompilation);
    }

    public CompilationDto addEvent(Long compId, Long eventId) throws NotFoundException {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Compilation %s not found", compId)));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event %s not found", eventId)));

        compilation.getEvents().add(event);
        Compilation savedCompilation = compilationRepository.save(compilation);
        return compilationMapper.toCompilationDto(savedCompilation);
    }

    public CompilationDto changePinState(Long compId, boolean pinState) throws NotFoundException {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Compilation %s not found", compId)));
        compilation.setPinned(pinState);
        Compilation savedCompilation = compilationRepository.save(compilation);
        return compilationMapper.toCompilationDto(savedCompilation);
    }
}
