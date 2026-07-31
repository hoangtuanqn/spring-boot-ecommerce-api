package mst.local.mstsoftware.mappers;

public interface BaseMapper<Entity, Resource, CreateRequest, UpdateRequest>
        extends ReadMapper<Entity, Resource>,
        CreateMapper<Entity, CreateRequest>,
        UpdateMapper<Entity, UpdateRequest> {
}
