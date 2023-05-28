package com.claudio.mensageria.rabbitmq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.claudio.mensageria.rabbitmq.model.entity.game.GameEntity;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long>{
    
}
