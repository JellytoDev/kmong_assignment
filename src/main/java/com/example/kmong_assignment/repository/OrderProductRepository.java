package com.example.kmong_assignment.repository;

import com.example.kmong_assignment.domain.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface OrderProductRepository extends JpaRepository<OrderProduct,Long> {

}
