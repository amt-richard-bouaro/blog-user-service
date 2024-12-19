package com.amalitech.usermanagementservice.model.Mapper;


import com.amalitech.usermanagementservice.config.mapper.BlogModelMapper;
import com.amalitech.usermanagementservice.dto.response.UserDetailsResponsePayload;
import com.amalitech.usermanagementservice.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UseDetailsResponseModelMapper implements BlogModelMapper<User, UserDetailsResponsePayload> {

    private final ModelMapper modelMapper;

    @Override
    public User mapToEntity(UserDetailsResponsePayload userDetailsResponsePayload) {
        return modelMapper.map(userDetailsResponsePayload, User.class);
    }

    @Override
    public UserDetailsResponsePayload mapToDto(User user) {
        return modelMapper.map(user, UserDetailsResponsePayload.class);
    }
}
