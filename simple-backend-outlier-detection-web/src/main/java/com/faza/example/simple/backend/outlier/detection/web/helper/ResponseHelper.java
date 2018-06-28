package com.faza.example.simple.backend.outlier.detection.web.helper;

import com.faza.example.simple.backend.outlier.detection.web.model.response.ApiResponse;
import org.springframework.http.HttpStatus;

import java.util.Optional;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

public class ResponseHelper {

    private static <T> ApiResponse<T> httpStatus(HttpStatus httpStatus) {
        return ApiResponse.<T>builder()
                .code(httpStatus.value())
                .status(httpStatus.getReasonPhrase())
                .build();
    }

    private static <T> ApiResponse<T> ok(T data) {
        ApiResponse<T> response = httpStatus(HttpStatus.OK);
        response.setData(data);

        return response;
    }

    private static <T> ApiResponse<T> internalServerError() {
        return httpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> ApiResponse<T> createResponse(T response) {
        if (Optional.ofNullable(response).isPresent())
            return ResponseHelper.ok(response);
        else
            return ResponseHelper.internalServerError();
    }
}
