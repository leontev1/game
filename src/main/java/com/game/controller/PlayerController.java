package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.payloads.PlayerCreate;
import com.game.payloads.PlayerUpdate;
import com.game.payloads.PlayerFilters;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/players")
public class PlayerController {

    @Autowired
    private PlayerService service;

    @PostMapping
    ResponseEntity<Player> postPlayer(@RequestBody PlayerCreate request) {

        Player response = service.createPlayer(request);

        if (response == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(response, HttpStatus.OK);}

    @GetMapping
    public ResponseEntity<List<Player>> getPlayers(@RequestParam(name = "name", required = false) String name,
                                                   @RequestParam(name = "title", required = false) String title,
                                                   @RequestParam(name = "race", required = false) Race race,
                                                   @RequestParam(name = "profession", required = false) Profession profession,
                                                   @RequestParam(name = "after", required = false) Long after,
                                                   @RequestParam(name = "before", required = false) Long before,
                                                   @RequestParam(name = "banned", required = false) Boolean banned,
                                                   @RequestParam(name = "minExperience", required = false) Integer minExperience,
                                                   @RequestParam(name = "maxExperience", required = false) Integer maxExperience,
                                                   @RequestParam(name = "minLevel", required = false) Integer minLevel,
                                                   @RequestParam(name = "maxLevel", required = false) Integer maxLevel,
                                                   @RequestParam(name = "order", required = false) PlayerOrder order,
                                                   @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
                                                   @RequestParam(name = "pageSize", required = false) Integer pageSize){
        PlayerFilters filters = new PlayerFilters(name, title, race, profession, after, before,
                banned, minExperience, maxExperience, minLevel, maxLevel, order, pageNumber, pageSize);
        List<Player> players = service.getPlayers(filters);
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable String id) {
        return service.getPlayerById(id);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getPlayersCount(@RequestParam(name = "name", required = false) String name,
                                                   @RequestParam(name = "title", required = false) String title,
                                                   @RequestParam(name = "race", required = false) Race race,
                                                   @RequestParam(name = "profession", required = false) Profession profession,
                                                   @RequestParam(name = "after", required = false) Long after,
                                                   @RequestParam(name = "before", required = false) Long before,
                                                   @RequestParam(name = "banned", required = false) Boolean banned,
                                                   @RequestParam(name = "minExperience", required = false) Integer minExperience,
                                                   @RequestParam(name = "maxExperience", required = false) Integer maxExperience,
                                                   @RequestParam(name = "minLevel", required = false) Integer minLevel,
                                                   @RequestParam(name = "maxLevel", required = false) Integer maxLevel) {

       PlayerFilters filters = new PlayerFilters(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
       return new ResponseEntity<>(service.getPlayersCount(filters), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Player> deletePlayerById(@PathVariable String id) {
        return service.deletePlayerById(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@RequestBody PlayerUpdate newPlayer, @PathVariable String id) {
        return service.updatePlayer(id, newPlayer);
    }
}
