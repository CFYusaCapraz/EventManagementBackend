package com.yusacapraz.event.service;

import com.yusacapraz.event.mapper.EventMapper;
import com.yusacapraz.event.model.DTOs.EventViewDTO;
import com.yusacapraz.event.model.Event;
import com.yusacapraz.event.model.exception.EventNotFoundException;
import com.yusacapraz.event.repository.EventRepository;
import com.yusacapraz.event.response.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Autowired
    public EventService(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    public ResponseEntity<APIResponse<?>> getAllEvents() {
        try {
            List<Event> eventList = eventRepository.findAll();
            List<EventViewDTO> viewDTOS = new ArrayList<>();
            if (eventList.isEmpty()) {
                APIResponse<Object> response = APIResponse.error("No event information is found!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            for (Event event : eventList) {
                viewDTOS.add(eventMapper.viewMapper(event));
            }
            APIResponse<List<EventViewDTO>> response = APIResponse.successWithData(viewDTOS, "Here are all events.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> getEventById(String eventId) {
        try {
            UUID uuid = UUID.fromString(eventId);
            Event event = eventRepository.findById(uuid)
                    .orElseThrow(() -> new EventNotFoundException("Event of the given id not found!"));
            EventViewDTO viewDTO = eventMapper.viewMapper(event);
            APIResponse<EventViewDTO> response = APIResponse.successWithData(viewDTO, "Event of the given id is found.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            APIResponse<Object> response = APIResponse.error("Please enter a valid event id!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (EventNotFoundException e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
