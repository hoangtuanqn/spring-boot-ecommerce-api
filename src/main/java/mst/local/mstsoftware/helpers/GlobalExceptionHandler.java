package mst.local.mstsoftware.helpers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import mst.local.mstsoftware.resources.ErrorResource;

@ControllerAdvice // global exception handler
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class) // Khai báo method bên dưới sẽ bắt exception loại này.
    public ResponseEntity<?> handleValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult() // Lấy kết quả validation - chứa toàn bộ lỗi
                .getFieldErrors() // Lấy danh sách lỗi theo từng field
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        ErrorResource errorResource = new ErrorResource("Xảy ra lỗi trong quá trình validation", errors);
        return ResponseEntity.badRequest().body(errorResource);
    }
}