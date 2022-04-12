package com.ac.coin.dao.repository;

import com.ac.coin.po.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends Neo4jRepository<User,Long> {
    @Query("match (u:User) where u.name = $name return u")
    Optional<User> findByName(@Param("name") String name);
}
