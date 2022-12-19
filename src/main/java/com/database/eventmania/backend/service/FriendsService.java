package com.database.eventmania.backend.service;

import com.database.eventmania.backend.repository.FriendsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class FriendsService {

    FriendsRepository friendsRepository;

    @Autowired
    public FriendsService(FriendsRepository friendsRepository){
        this.friendsRepository = friendsRepository;
    }

    public boolean addFriend(String userId, String friendId) throws SQLException {
        return friendsRepository.addFriend(Long.valueOf(userId), Long.valueOf(friendId));
    }

    public boolean deleteFriend(String userId) throws SQLException{
        return friendsRepository.deleteFriend(Long.valueOf(userId));it branch
    }
}
