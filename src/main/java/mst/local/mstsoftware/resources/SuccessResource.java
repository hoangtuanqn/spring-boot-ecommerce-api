package mst.local.mstsoftware.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SuccessResource<T> {
    private String message;
    private T data;
}
