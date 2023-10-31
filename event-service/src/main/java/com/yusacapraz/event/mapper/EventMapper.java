package com.yusacapraz.event.mapper;

import com.yusacapraz.event.model.DTOs.EventViewDTO;
import com.yusacapraz.event.model.Event;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class EventMapper {
    public EventViewDTO viewMapper(Event event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm 'GMT'xxx");
        return EventViewDTO.builder()
                .eventId(event.getEventId().toString())
                .eventName(event.getEventName())
                .eventDescription(event.getEventDetails().getEventDescription())
                .eventTime(event.getEventDetails().getEventTime().format(formatter))
                .categoryName(event.getEventDetails().getCategory().getCategoryName())
                .participantQuote(event.getEventDetails().getParticipantQuote())
                .addressId(event.getEventDetails().getEventDetailsId().toString())
                .organizationId(event.getOrganization().getOrgId().toString())
                .build();
    }
}
