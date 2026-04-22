package com.games.final_projest_java.repository;

import com.games.final_projest_java.model.GameRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRecordRepository extends MongoRepository<GameRecord, String> {
    List<GameRecord> findByUsername(String username);
    List<GameRecord> findByGameType(String gameType);
    List<GameRecord> findByResult(String result);
    List<GameRecord> findByUsernameAndGameType(String username, String gameType);
    List<GameRecord> findByUsernameAndResult(String username, String result);
    List<GameRecord> findByGameTypeAndResult(String gameType, String result);
    List<GameRecord> findByUsernameAndGameTypeAndResult(String username, String gameType, String result);
    List<GameRecord> findAllByOrderByPlayedAtDesc();
}
