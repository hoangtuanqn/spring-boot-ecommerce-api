package mst.local.mstsoftware.helpers;

import mst.local.mstsoftware.resources.ApiResource;
import mst.local.mstsoftware.resources.ErrorResource;
import mst.local.mstsoftware.resources.FieldErrorResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice // global exception handler
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class) // Khai báo method bên dưới sẽ bắt exception loại này.
    public ResponseEntity<ApiResource<Void>> handleValidException(MethodArgumentNotValidException ex) {
        List<FieldErrorResource> details = ex.getBindingResult() // Lấy kết quả validation - chứa toàn bộ lỗi
                .getFieldErrors()// Lấy danh sách lỗi theo từng field
                .stream()
                .map(fe -> FieldErrorResource.builder()
                        .field(fe.getField())
                        .message(fe.getDefaultMessage())
                        .build()
                ).toList();
        ErrorResource error = ErrorResource.builder()
                .code("VALIDATION_ERROR")
                .details(details)
                .build();
        return ResponseEntity.badRequest().body(ApiResource.error(error, "Xảy ra lỗi trong quá trình validation"));
    }

    // xử lý lỗi login
    @ExceptionHandler({BadCredentialsException.class, AuthenticationCredentialsNotFoundException.class})
    public ResponseEntity<?> handleCredentials(Exception ex) {
        ErrorResource error = ErrorResource.builder().code("UNAUTHORIZED").build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResource.error(error, ex.getMessage()));
    }
}
