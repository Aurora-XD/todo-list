package com.thoughtworks.todo_list.repository.user;

import com.thoughtworks.todo_list.repository.user.entity.User;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface UserRepository {
    Maybe<User> findByName(String name);

    Completable save(User user);
}
