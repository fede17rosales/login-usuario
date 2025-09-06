package com.example.userservice.dto;

import lombok.*;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ErrorEnvelope {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ErrorItem {
        private Instant timestamp;
        private int codigo;
        private String detail;
    }

    private List<ErrorItem> error;

    public static ErrorEnvelope of(int code, String detail) {
        return new ErrorEnvelope(Collections.singletonList(
                ErrorItem.builder().timestamp(Instant.now()).codigo(code).detail(detail).build()
        ));
    }
}
