package mst.local.mstsoftware.modules.users.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

public interface BaseMapper<Entity, Resource, CreateRequest, UpdateRequest> {
    Resource toResource(Entity entity);

    List<Resource> toList(List<Entity> entities);

    
    Entity toEntity(CreateRequest createRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UpdateRequest updateRequest, @MappingTarget Entity entity);
}
