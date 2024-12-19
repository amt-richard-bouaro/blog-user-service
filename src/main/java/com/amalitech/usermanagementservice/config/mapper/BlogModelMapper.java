package com.amalitech.usermanagementservice.config.mapper;

public interface BlogModelMapper<Entity, Dto> {

    Entity mapToEntity(Dto dto);

    Dto mapToDto(Entity entity);
}
