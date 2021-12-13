package com.professional.telegram_hyecorn_bot.sleep_prevent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class Controller {

    @GetMapping("/sleepPrevent")
    public ResponseEntity<String> getMapping(){
        log.trace("get job request");
        return ResponseEntity.status(HttpStatus.OK)
                .body("OK");
    }
}
