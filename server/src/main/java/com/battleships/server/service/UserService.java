package com.battleships.server.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.battleships.server.api.Exceptions.InvalidPasswordException;
import com.battleships.server.api.Exceptions.NoUserException;
import com.battleships.server.api.model.User;
import com.battleships.server.api.repository.UserRepository;

/**
 * Class responsible for managing active user user data.
 */
@Service
public class UserService {
    
    List<User> activeUsers;
    public final UserRepository userRepository;

    /**
     * UserService constructor. Creates new acrive user pool. 
     * @param userRepository - Interface for connecting to database - (Auto implemented by JPA)
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.activeUsers = new LinkedList<>();
    }

    /**
     * Method used to log in to server. Method checks if user exists in database,
     * and adds this user into active users pool.
     * @param login user login
     * @param passwd user password
     * @return User that has logged in
     * @throws NoUserException if given user does not exist in the database
     * @throws InvalidPasswordException if user exists but given password is incorrect
     */
    public User loginUser(String login, String passwd) throws NoUserException, InvalidPasswordException {
        Optional<User> optUser = userRepository.getUserByLogin(login);
        if(optUser.isPresent()) {
            System.out.println(optUser.get().toString());
        }

        if(optUser.isEmpty()) {
            throw new NoUserException("No Such User");
        }
        User u = optUser.get();

        if(passwd.equals(u.getPassword())) {
            activeUsers.add(u);
            /* DEBUG */ // System.out.println("USER " + u.getLogin() + " Loged in");
            return u;
        } else {
            throw new InvalidPasswordException("PASSWORD INVALID");
        }
    }

    /**
     * Method used to get active user reference by his ID
     * @param id id of user
     * @return User reference
     * @throws NoUserException when user with given id is not present in active users pool
     */
    public User getActiveUser(int id) throws NoUserException{
        for (User u : activeUsers) {
            if(u.getUid() == id) return u;
        }
        throw new NoUserException("User not logged in.");
    }

    /**
     * Method used to ger active user reference by his login
     * @param login user login
     * @return User reference
     * @throws NoUserException when user with given login is not present in active users pool
     */
    public User getActiveUser(String login) throws NoUserException{
        for (User u : activeUsers) {
            if(u.getLogin().equals(login)) return u;
        }
        throw new NoUserException("User not logged in.");
    }

    public User registerUser(String login, String passwd, Optional<String> email) throws Exception
    {   
        Optional<User> clone = userRepository.getUserByLogin(login);

        if(clone.isPresent()) {
            // TODO: Create Exeption for this
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login already used");
        }

        User user = new User();
        user.setLogin(login);
        user.setPassword(passwd);
        user.setEmail(email.isPresent() ? email.get() : "");
        user.setGamerScore(0);

        user = userRepository.save(user);
     
        /* DEBUG */ System.out.println("User registered: " + user);

        return user;
    }

    /**
     * Method used to log out given user from server. User is removed from active user pool
     * @param login user login
     * @param password user password
     * @return True if user has been logged out successfuly
     * @throws InvalidPasswordException when given password is incorrect
     */
    public Boolean logout(String login, String password) throws InvalidPasswordException
    {
        for(User u : activeUsers) {
            if(u.getLogin().equals(login) && !u.getPassword().equals(password)) {
                throw new InvalidPasswordException("INVALID PASSWORD");
            }

            if(u.getLogin().equals(login) && u.getPassword().equals(password)) {
                activeUsers.remove(u);
                return true;
            }
        }

        return false;
    }

    /**
     * Method that returns all users in database
     * @return List of User's saved in database
     */
    public List<User> getUsers()
    {
        return userRepository.findAll();
    }
    
}
