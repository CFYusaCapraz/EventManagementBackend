package com.yusacapraz.auth.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class APIResponse<T> {
    private boolean status;
    private String message;
    private T data;

    public static <T> APIResponse<T> success(String message) {
        APIResponse<T> response = new APIResponse<>();
        response.setStatus(true);
        response.setMessage(message);
        return response;
    }

    public static <T> APIResponse<T> successWithData(T data, String message) {
        APIResponse<T> response = new APIResponse<>();
        response.setStatus(true);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static <T> APIResponse<T> error(String message) {
        APIResponse<T> response = new APIResponse<>();
        response.setStatus(false);
        response.setMessage(message);
        return response;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public T getData() {
        return data;
    }
}
