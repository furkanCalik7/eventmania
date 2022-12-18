package com.database.eventmania.backend.service;

import com.database.eventmania.backend.entity.Rating;
import com.database.eventmania.backend.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.SQLException;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Rating getRatingById(Integer ratingId) throws SQLException {
        return ratingRepository.getRatingById(ratingId);
    }
    public boolean createRating(Integer eventId, Integer userId, Integer point, String topic, String ratingComment) throws SQLException {
        return ratingRepository.createRating(eventId, userId, point, topic, ratingComment);
    }
    public boolean deleteRating(Integer ratingId) throws SQLException {
        return ratingRepository.deleteRating(ratingId);
    }
    public boolean updateRating(Integer eventId, Integer ratingId, Integer point, String topic, String ratingComment) throws SQLException {
        return ratingRepository.updateRating(eventId, ratingId, point, topic, ratingComment);
    }




}
