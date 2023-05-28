package com.claudio.mensageria.rabbitmq.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.claudio.mensageria.rabbitmq.model.entity.game.GameEntity;
import com.claudio.mensageria.rabbitmq.repository.GameRepository;

@RestController
@RequestMapping("game")
public class GameController {
    private GameRepository gameRepository;

    @Autowired
    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }


    @GetMapping
    public List<GameEntity> getAll() {
        return gameRepository.findAll();
    }

    @PostMapping
    public ResponseEntity saveGame(GameEntity gameEntity) {
        gameRepository.save(gameEntity);
        return ResponseEntity.status(201).build();
    }
}
