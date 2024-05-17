package br.com.ebanx.demo.controller;

import br.com.ebanx.demo.dto.EventDto;
import br.com.ebanx.demo.model.Event;
import br.com.ebanx.demo.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    EventService eventService;


    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody EventDto eventDto) {
        Event event = eventService.createEvent(eventDto);
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.CREATED).body(event);
        }
    }

}
