package ru.practicum.explorewithme.controller.open;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.service.CompilationService;

import javax.validation.constraints.NotNull;

@Controller
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationOpenController {
    private final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<Object> getCompilations(@RequestParam(required = false, defaultValue = "false") Boolean pinned,
                                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Get compilations request pinned={}, from={}, size={}", pinned, from, size);
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<Object> getCompilation(@NotNull @PathVariable Long compId) throws NotFoundException {
        log.info("Get compilation request compId={}", compId);
        return compilationService.getCompilation(compId);
    }
}
