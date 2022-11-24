package org.nschmidt.abalone.rest;

import org.nschmidt.abalone.playfield.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/abalone")
public class AbaloneController {

    private final AbaloneService service;
    
    @Autowired
    public AbaloneController(AbaloneService service) {
        this.service = service;
    }
    
    @GetMapping("/init")
    public ResponseEntity<Player[]> init() {
      return new ResponseEntity<>(service.initialField(), HttpStatus.ACCEPTED);
    }
    
    @PostMapping("/answer")
    public ResponseEntity<Player[]> answer(@RequestParam("player") Player player, @RequestBody() Player[] move) {
        return new ResponseEntity<>(service.answer(player, move), HttpStatus.ACCEPTED);
    }
}
