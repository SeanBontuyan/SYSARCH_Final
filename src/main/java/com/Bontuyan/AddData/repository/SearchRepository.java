package com.Bontuyan.AddData.repository;

import com.Bontuyan.AddData.model.User;

import java.util.List;

public interface SearchRepository {

    List<User> findById(String string);
}
