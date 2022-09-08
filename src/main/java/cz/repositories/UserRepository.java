package cz.repositories;

import cz.models.User;

import java.util.Collection;

public interface UserRepository {

    User findUserById(int id);

    User findUserByPhoneNumber(String phoneNumber);

    Collection<User> findAllUsers();

    void createUser(User user);

    void updateUser(User user);

    void deleteUser(int id);
}
