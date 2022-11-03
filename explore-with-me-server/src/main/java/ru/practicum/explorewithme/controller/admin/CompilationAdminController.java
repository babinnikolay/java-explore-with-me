package ru.practicum.explorewithme.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.model.dto.NewCompilationDto;
import ru.practicum.explorewithme.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<Object> createCompilation(@RequestBody @Valid NewCompilationDto compilationDto) {
        log.info("New compilation {}", compilationDto);
        return ResponseEntity.ok(compilationService.createCompilation(compilationDto));
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Object> delResponseEntityeteCompilation(@PathVariable @NotNull Long compId) throws NotFoundException {
        log.info("Delete compilation compId={}", compId);
        compilationService.deleteCompilation(compId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public ResponseEntity<Object> deleteEvent(@PathVariable @NotNull Long compId,
                                              @PathVariable @NotNull Long eventId)
            throws BadRequestException, NotFoundException {
        log.info("Delete event {} from compilation {}", eventId, compId);
        return ResponseEntity.ok(compilationService.deleteEvent(compId, eventId));
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public ResponseEntity<Object> addEvent(@PathVariable @NotNull Long compId,
                                           @PathVariable @NotNull Long eventId) throws NotFoundException {
        log.info("Add event {} to compilation {}", eventId, compId);
        return ResponseEntity.ok(compilationService.addEvent(compId, eventId));
    }

    @DeleteMapping("/{compId}/pin")
    public ResponseEntity<Object> unpinCompilation(@PathVariable @NotNull Long compId) throws NotFoundException {
        log.info("Unpin compilation {}", compId);
        return ResponseEntity.ok(compilationService.changePinState(compId, false));
    }

    @PatchMapping("/{compId}/pin")
    public ResponseEntity<Object> pinCompilation(@PathVariable @NotNull Long compId) throws NotFoundException {
        log.info("Pin compilation {}", compId);
        return ResponseEntity.ok(compilationService.changePinState(compId, true));
    }
}
