package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.payloads.PlayerCreate;
import com.game.payloads.PlayerUpdate;
import com.game.payloads.PlayerFilters;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository repository;

    public Player createPlayer(PlayerCreate request) {

        if (request.getName() == null || request.getTitle() == null || request.getRace() == null ||
                request.getProfession() == null || request.getBirthday() == null || request.getExperience() == null)
            return null;

        if (request.getName().length() > 12 || request.getTitle().length() > 30)
            return null;

        if (request.getName().equals(""))
            return null;

        if (request.getExperience() < 0 || request.getExperience() > 10_000_000)
            return null;

        if (request.getBirthday() < 0)
            return null;

        Date birthday = new Date(request.getBirthday());

        if (birthday.getYear() < 100 || birthday.getYear() > 1100)
          return null;

        Boolean banned = request.getBanned();

        if (banned == null) {
            banned = false;
        }

        Player player = new Player();

        player.setName(request.getName());
        player.setTitle(request.getTitle());
        player.setRace(request.getRace());
        player.setProfession(request.getProfession());
        player.setBirthday(birthday);
        player.setBanned(banned);
        player.setExperience(request.getExperience());

        calcPlayer(player);
        repository.save(player);
        return player;
    }
    public ResponseEntity<Player> updatePlayer(String id, PlayerUpdate newPlayer) {

        if (checkId(id)) {
            Optional<Player> optPlayer = repository.findById(Long.parseLong(id));
            if (optPlayer.isPresent()) {
                Player player = optPlayer.get();

                if (newPlayer.getName() != null) {
                    if (newPlayer.getName().length() > 12 || newPlayer.getName().equals(""))
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    player.setName(newPlayer.getName());
                }

                if (newPlayer.getTitle() != null) {
                    if (newPlayer.getTitle().length() > 30)
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    player.setTitle(newPlayer.getTitle());
                }

                if (newPlayer.getRace() != null)
                    player.setRace(newPlayer.getRace());

                if (newPlayer.getProfession() != null)
                    player.setProfession(newPlayer.getProfession());

                if (newPlayer.getBirthday() != null) {
                    if (newPlayer.getBirthday() < 0)
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    player.setBirthday(new Date(newPlayer.getBirthday()));
                }

                if (newPlayer.getBanned() != null) {
                    player.setBanned(newPlayer.getBanned());
                }

                if (newPlayer.getExperience() != null) {
                    if (newPlayer.getExperience() < 0 || newPlayer.getExperience() > 10_000_000)
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    player.setExperience(newPlayer.getExperience());
                }

                calcPlayer(player);

                repository.save(player);

                return new ResponseEntity<>(player, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    public List<Player> getPlayers(PlayerFilters filters) {
        if (filters.getOrder() == null)
            filters.setOrder(PlayerOrder.ID);
        if (filters.getPageSize() == null)
            filters.setPageSize(3);
        if (filters.getPageNumber() == null)
            filters.setPageNumber(0);

        List<Player> players = findPlayers(filters);

        switch (filters.getOrder()) {
            case ID: players.sort(Comparator.comparing(Player::getId)); break;
            case NAME: players.sort(Comparator.comparing(Player::getName)); break;
            case LEVEL: players.sort(Comparator.comparing(Player::getLevel)); break;
            case BIRTHDAY: players.sort(Comparator.comparing(Player::getBirthday)); break;
            case EXPERIENCE: players.sort(Comparator.comparing(Player::getExperience)); break;
        }

        int startIndex = filters.getPageSize() * filters.getPageNumber();
        int lastIndex = startIndex + filters.getPageSize();

        if (players.size() - startIndex < filters.getPageSize())
            lastIndex = players.size();

        players = players.subList(startIndex, lastIndex);

        return players;
    }
    public ResponseEntity<Player> getPlayerById(String id) {
        if (checkId(id)) {
            Optional<Player> player = repository.findById(Long.parseLong(id));
            if (player.isPresent()) {
                return new ResponseEntity<>(player.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    public Integer getPlayersCount(PlayerFilters filters) {
        return findPlayers(filters).size();
    }
    public ResponseEntity<Player> deletePlayerById(String id) {
        if (checkId(id)) {
            Optional<Player> player = repository.findById(Long.parseLong(id));
            if (player.isPresent()) {
                repository.deleteById(Long.parseLong(id));
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // calculate new level and experience until next level
    private int calcLevel(Integer exp) {
        return (int) ((Math.sqrt(2500 + 200 * exp) - 50) / 100);
    }
    private int calcExperience(Integer lvl, Integer exp) {
        return 50 * (lvl + 1) * (lvl + 2) - exp;
    }
    private void calcPlayer(Player player) {
        player.setLevel(calcLevel(player.getExperience()));
        player.setUntilNextLevel(calcExperience(player.getLevel(), player.getExperience()));
    }
    private boolean checkId(String id) {
        long longId = -1;
        try {
            longId = Long.parseLong(id);
        } catch (NumberFormatException ex) {
            return false;
        }
        return longId > 0;
    }
    private List<Player> findPlayers(PlayerFilters filters) {
        if (filters != null) {
            List<Player> players = repository.findAll();

            if (filters.getName() != null) {
                players = players.stream().filter(p -> p.getName().contains(filters.getName())).collect(Collectors.toList());
            }
            if (filters.getTitle() != null) {
                players = players.stream().filter(p -> p.getTitle().contains(filters.getTitle())).collect(Collectors.toList());
            }
            if (filters.getRace() != null) {
                players = players.stream().filter(p -> p.getRace() == filters.getRace()).collect(Collectors.toList());
            }
            if (filters.getProfession() != null) {
                players = players.stream().filter(p -> p.getProfession() == filters.getProfession()).collect(Collectors.toList());
            }
            if (filters.getAfter() != null) {
                players = players.stream().filter(p -> p.getBirthday().after(new Date(filters.getAfter()))).collect(Collectors.toList());
            }
            if (filters.getBefore() != null) {
                players = players.stream().filter(p -> p.getBirthday().before(new Date(filters.getBefore()))).collect(Collectors.toList());
            }
            if (filters.getBanned() != null) {
                players = players.stream().filter(p -> p.getBanned().equals(filters.getBanned())).collect(Collectors.toList());
            }
            if (filters.getMinExperience() != null) {
                players = players.stream().filter(p -> p.getExperience() >= filters.getMinExperience()).collect(Collectors.toList());
            }
            if (filters.getMaxExperience() != null) {
                players = players.stream().filter(p -> p.getExperience() <= filters.getMaxExperience()).collect(Collectors.toList());
            }
            if (filters.getMinLevel() != null) {
                players = players.stream().filter(p -> p.getLevel() >= filters.getMinLevel()).collect(Collectors.toList());
            }
            if (filters.getMaxLevel() != null) {
                players = players.stream().filter(p -> p.getLevel() <= filters.getMaxLevel()).collect(Collectors.toList());
            }

            return players;
        }
        return repository.findAll();
    }
}
