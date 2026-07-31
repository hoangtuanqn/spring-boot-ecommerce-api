package mst.local.mstsoftware.mappers;

public interface CreateMapper<Entity, CreateRequest> {
    Entity toEntity(CreateRequest createRequest);
}
