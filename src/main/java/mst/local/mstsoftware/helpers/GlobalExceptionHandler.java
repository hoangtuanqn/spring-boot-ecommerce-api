package mst.local.mstsoftware.helpers;

import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.resources.ApiResource;
import mst.local.mstsoftware.resources.ErrorResource;
import mst.local.mstsoftware.resources.FieldErrorResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
 * Xử lý gồm 3 tầng:
 * 1. Validation lỗi input (@Valid)
 * 2. Lỗi parse JSON (sai kiểu dữ liệu, thiếu field bắt buộc, ...)
 * 3. Bắt tất cả các lỗi ko xác định
 * */
@ControllerAdvice // global exception handler
@Slf4j
public class GlobalExceptionHandler {
    // Tầng 1: Validation lỗi input (@Valid)
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

    // Tâầng 2: Lỗi parse JSON (sai kiểu dữ liệu, thiếu field bắt buộc, ...)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleNotReadable(HttpMessageNotReadableException ex) {
        List<FieldErrorResource> details = new ArrayList<>();

        Throwable cause = ex.getCause();

        if (cause instanceof tools.jackson.databind.exc.InvalidFormatException ife) {
            String fieldName = ife.getPath().stream()
                    .map(ref -> ((tools.jackson.core.JacksonException.Reference) ref).getPropertyName())
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("."));

            String expectedType = ife.getTargetType().getSimpleName();

            details.add(FieldErrorResource.builder()
                    .field(fieldName)
                    .message("Giá trị không hợp lệ, yêu cầu kiểu " + expectedType)
                    .build());

        } else if (cause instanceof tools.jackson.databind.exc.MismatchedInputException mie) {
            String fieldName = mie.getPath().stream()
                    .map(ref -> ((tools.jackson.core.JacksonException.Reference) ref).getPropertyName())
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("."));

            details.add(FieldErrorResource.builder()
                    .field(fieldName)
                    .message("Dữ liệu không đúng cấu trúc")
                    .build());

        } else {
            details.add(FieldErrorResource.builder()
                    .message("Request body không đúng định dạng JSON")
                    .build());
        }

        ErrorResource error = ErrorResource.builder()
                .code("INVALID_REQUEST_BODY")
                .details(details)
                .build();

        return ResponseEntity.badRequest()
                .body(ApiResource.error(error, "Dữ liệu không hợp lệ"));
    }

    // Tầng 3: Bắt tất cả các lỗi ko xác định
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(Exception ex) {
        ErrorResource error = ErrorResource.builder().code("INTERNAL_SERVER_ERROR").build();
        return ResponseEntity.internalServerError().body(ApiResource.error(error, ex.getMessage()));
    }
}
