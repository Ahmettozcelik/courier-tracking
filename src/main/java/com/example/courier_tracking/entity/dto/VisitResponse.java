package com.example.courier_tracking.entity.dto;

import com.example.courier_tracking.util.DateTimeUtil;
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
    private String entryTime;

    public VisitResponse(String storeName, LocalDateTime entryTime) {
        this.storeName = storeName;
        this.entryTime = DateTimeUtil.format(entryTime);
    }

}