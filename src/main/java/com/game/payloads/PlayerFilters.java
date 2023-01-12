package com.game.payloads;

import com.game.controller.PlayerOrder;
import com.game.entity.Profession;
import com.game.entity.Race;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class PlayerFilters {
    private String name;
    private String title;
    @Enumerated(EnumType.STRING)
    private Race race;
    @Enumerated(EnumType.STRING)
    private Profession profession;
    private Long after;
    private Long before;
    private Boolean banned;
    private Integer minExperience;
    private Integer maxExperience;
    private Integer minLevel;
    private Integer maxLevel;
    @Enumerated
    private PlayerOrder order;
    private Integer pageNumber;
    private Integer pageSize;

    public PlayerFilters(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel, PlayerOrder order, Integer pageNumber, Integer pageSize) {
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.after = after;
        this.before = before;
        this.banned = banned;
        this.minExperience = minExperience;
        this.maxExperience = maxExperience;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.order = order;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public PlayerFilters(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel) {
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.after = after;
        this.before = before;
        this.banned = banned;
        this.minExperience = minExperience;
        this.maxExperience = maxExperience;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
    }

    public PlayerOrder getOrder() {
        return order;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public Race getRace() {
        return race;
    }

    public Profession getProfession() {
        return profession;
    }

    public Long getAfter() {
        return after;
    }

    public Long getBefore() {
        return before;
    }

    public Boolean getBanned() {
        return banned;
    }

    public Integer getMinExperience() {
        return minExperience;
    }

    public Integer getMaxExperience() {
        return maxExperience;
    }

    public Integer getMinLevel() {
        return minLevel;
    }

    public Integer getMaxLevel() {
        return maxLevel;
    }

    public void setOrder(PlayerOrder order) {
        this.order = order;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public void setAfter(Long after) {
        this.after = after;
    }

    public void setBefore(Long before) {
        this.before = before;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public void setMinExperience(Integer minExperience) {
        this.minExperience = minExperience;
    }

    public void setMaxExperience(Integer maxExperience) {
        this.maxExperience = maxExperience;
    }

    public void setMinLevel(Integer minLevel) {
        this.minLevel = minLevel;
    }

    public void setMaxLevel(Integer maxLevel) {
        this.maxLevel = maxLevel;
    }
}
