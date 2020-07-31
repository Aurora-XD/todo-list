package com.thoughtworks.todo_list.repository.user;

import com.thoughtworks.todo_list.repository.user.entity.User;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class UserRepositoryImpl implements UserRepository {
    private UserDataSource dataSource;

    public UserRepositoryImpl(UserDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Maybe<User> findByName(String name) {
        return dataSource.findByName(name);
    }

    @Override
    public Completable save(User user) {
        return dataSource.save(user);
    }
}