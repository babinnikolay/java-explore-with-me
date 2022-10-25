package ru.practicum.explorewithme.model.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.model.dto.UserDto;
import ru.practicum.explorewithme.model.dto.UserShortDto;

@Component
public class UserMapper {

    private static ModelMapper mapper;

    public UserMapper(ModelMapper modelMapper) {
        UserMapper.mapper = modelMapper;
    }

    public static User toUser(UserDto userRequest) {
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setName(userRequest.getName());
        return user;
    }

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        return userDto;
    }

    public static UserShortDto toUserShortDto(User initiator) {
        return mapper.map(initiator, UserShortDto.class);
    }
}
