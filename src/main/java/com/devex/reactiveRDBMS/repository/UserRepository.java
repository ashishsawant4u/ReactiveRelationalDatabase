package com.devex.reactiveRDBMS.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.devex.reactiveRDBMS.entity.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository("userRepository")
public interface UserRepository extends ReactiveCrudRepository<User, Integer>
{
	public Flux<User> findAll();
	
	public Mono<User> findById(Integer id);
	
	@Modifying
	@Query("UPDATE rx_users SET balance = balance - :amount WHERE balance >= :amount AND id = :userid")
	public Mono<Boolean> updateBalance(double amount,int userid);
}
