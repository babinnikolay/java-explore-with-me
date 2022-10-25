package ru.practicum.explorewithme.model.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.model.dto.CompilationDto;
import ru.practicum.explorewithme.model.dto.NewCompilationDto;

import java.util.stream.Collectors;

@Component
public class CompilationMapper {
    private static ModelMapper mapper;

    @Autowired
    public CompilationMapper(ModelMapper modelMapper) {
        CompilationMapper.mapper = modelMapper;
    }

    public static Compilation toCompilation(NewCompilationDto compilationDto) {
        return mapper.map(compilationDto, Compilation.class);
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        TypeMap<Compilation, CompilationDto> typeMap = mapper.getTypeMap(Compilation.class, CompilationDto.class);
        if (typeMap == null) {
            typeMap = mapper.createTypeMap(Compilation.class, CompilationDto.class);
            typeMap.addMappings(m -> m.skip(CompilationDto::setEvents));
        }
        CompilationDto compilationDto = mapper.map(compilation, CompilationDto.class);
        compilationDto.setEvents(compilation.getEvents()
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList()));
        return compilationDto;
    }
}
