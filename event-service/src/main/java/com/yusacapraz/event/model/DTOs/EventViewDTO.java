package com.yusacapraz.event.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EventViewDTO {
    private String eventId;
    private String eventName;
    private String eventDescription;
    private String eventTime;
    private String categoryName;
    private Integer participantQuote;
    private String addressId;
    private String organizationId;
}
