package ru.practicum.explorewithme.model.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.model.dto.CompilationDto;
import ru.practicum.explorewithme.model.dto.NewCompilationDto;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final ModelMapper mapper;
    private final EventMapper eventMapper;

    public CompilationDto toCompilationDto(Compilation compilation) {
        TypeMap<Compilation, CompilationDto> typeMap = mapper.getTypeMap(Compilation.class, CompilationDto.class);
        if (typeMap == null) {
            typeMap = mapper.createTypeMap(Compilation.class, CompilationDto.class);
            typeMap.addMappings(m -> m.skip(CompilationDto::setEvents));
        }
        CompilationDto compilationDto = mapper.map(compilation, CompilationDto.class);
        compilationDto.setEvents(compilation.getEvents()
                .stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList()));
        return compilationDto;
    }

    public Compilation toCompilation(NewCompilationDto compilationDto) {
        return mapper.map(compilationDto, Compilation.class);
    }
}
