package com.example.courier_tracking.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
public class VisitResponse {
    private String storeName;
    private LocalDateTime entryTime;

}