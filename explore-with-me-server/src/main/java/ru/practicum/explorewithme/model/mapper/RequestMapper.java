package ru.practicum.explorewithme.model.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.model.EventRequest;
import ru.practicum.explorewithme.model.dto.ParticipationRequestDto;

@Component
public class RequestMapper {

    private static ModelMapper mapper;

    public RequestMapper(ModelMapper modelMapper) {
        RequestMapper.mapper = modelMapper;
    }

    public static ParticipationRequestDto toParticipationRequestDto(EventRequest request) {
        TypeMap<EventRequest, ParticipationRequestDto> typeMap = mapper.getTypeMap(EventRequest.class,
                ParticipationRequestDto.class);
        if (typeMap == null) {
            typeMap = mapper.createTypeMap(EventRequest.class, ParticipationRequestDto.class);
            typeMap.addMappings(m -> m.skip(ParticipationRequestDto::setRequester));
            typeMap.addMappings(m -> m.skip(ParticipationRequestDto::setEvent));
        }
        ParticipationRequestDto participationRequestDto = mapper.map(request, ParticipationRequestDto.class);
        participationRequestDto.setRequester(request.getRequester().getId());
        participationRequestDto.setEvent(request.getEvent().getId());
        participationRequestDto.setStatus(request.getState().name());
        return participationRequestDto;
    }
}
