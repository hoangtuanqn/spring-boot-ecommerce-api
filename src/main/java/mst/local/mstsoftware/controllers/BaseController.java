package mst.local.mstsoftware.controllers;

import mst.local.mstsoftware.resources.ApiResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {
    protected <T> ResponseEntity<ApiResource<T>> ok(T data, String message) {
        return ResponseEntity.ok(ApiResource.success(data, message));
    }

    protected <T> ResponseEntity<ApiResource<T>> created(T data, String message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResource.success(data, message));

    }
}
