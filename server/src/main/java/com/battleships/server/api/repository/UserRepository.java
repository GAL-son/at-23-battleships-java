package com.battleships.server.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.battleships.server.api.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Interface that defined methods used to access database.
 */
public interface UserRepository extends JpaRepository<User, Integer>{
    /**
     * Returns Optional containing User instance from database with given login
     * @param login user login
     * @return Optional containing User
     */
    public Optional<User> getUserByLogin(String login);
    
    /**
     * Gets all users from database
     * @return List of Users in database
     */
    public List<User> findAll();
}
