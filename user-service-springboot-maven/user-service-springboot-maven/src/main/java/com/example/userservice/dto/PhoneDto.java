package com.example.userservice.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PhoneDto {
    private Long number;
    private Integer citycode;
    private String contrycode;
}
