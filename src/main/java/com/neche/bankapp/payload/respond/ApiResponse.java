package com.neche.bankapp.payload.respond;


import com.neche.bankapp.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ApiResponse<T> {

    private String message;
    private T data;
    private String responseTime;

    public ApiResponse(String message, T data){
        this.message = message;
        this.data = data;
        this.responseTime = DateUtils.dateToString(LocalDateTime.now());

    }

    public ApiResponse(String message){
        this.message = message;
        this.responseTime = DateUtils.dateToString(LocalDateTime.now());
    }
}
