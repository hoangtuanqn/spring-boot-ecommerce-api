package mst.local.mstsoftware.resources;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ErrorResource {
    private String code;
    @Builder.Default
    private List<FieldErrorResource> details = List.of();
}
