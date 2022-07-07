package com.example.kmong_assignment.repository;

import com.example.kmong_assignment.domain.Order;
import com.example.kmong_assignment.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    public List<Order> findAllByUser(User user);
}
