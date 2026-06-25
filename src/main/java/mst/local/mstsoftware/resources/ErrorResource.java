package mst.local.mstsoftware.resources;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResource {
    private String message;
    private Map<String, String> errors;
}
