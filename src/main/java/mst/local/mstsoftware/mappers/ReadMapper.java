package mst.local.mstsoftware.mappers;

import java.util.List;

public interface ReadMapper<Entity, Resource> {
    Resource toResource(Entity entity);

    List<Resource> toList(List<Entity> entities);
}
