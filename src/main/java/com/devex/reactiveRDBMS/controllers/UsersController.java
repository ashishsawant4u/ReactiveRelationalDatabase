package com.devex.reactiveRDBMS.controllers;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devex.reactiveRDBMS.entity.User;
import com.devex.reactiveRDBMS.repository.UserRepository;

import io.r2dbc.spi.Parameter.In;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UsersController 
{
	@Resource(name = "userRepository")
	UserRepository userRepository;
	
	@GetMapping("/all")
	public Flux<User> getAll()
	{
		return userRepository.findAll();
	}
	
	
	@GetMapping("/get/{id}")
	public Mono<ResponseEntity<User>> findById(@PathVariable String id)
	{
		return userRepository.findById(Integer.parseInt(id))
							 .map(u -> new ResponseEntity<>(u,HttpStatus.OK))
							 .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@PostMapping("/save")
	public Mono<ResponseEntity<User>> saveUser( @RequestBody Mono<User> newUser)
	{
		return newUser
					  .flatMap(u -> userRepository.save(u))
					  .map(p -> new ResponseEntity<>(p, HttpStatus.ACCEPTED));
	}
	
	@PutMapping("/update/{id}")
	public Mono<ResponseEntity<User>> updateUser(@PathVariable String id ,@RequestBody Mono<User> updateUser) 
	{
		return userRepository.findById(Integer.parseInt(id))
		              .map(u -> Optional.of(u))
		              .defaultIfEmpty(Optional.empty())
		              .flatMap(exUser ->{
		            	  
		            	  if(exUser.isPresent())
		            	  {
		            		  return updateUser
		            				  		.flatMap(u ->{
		            				  			u.setId(Integer.parseInt(id));
		            				  			return userRepository.save(u);
		            				  		})
		            				  		.map(u -> new ResponseEntity<>(u,HttpStatus.ACCEPTED));
		            	  }
		            	  
		            	 return Mono.just(ResponseEntity.notFound().build()); 
		            	  
		              });
	}
	
	@DeleteMapping("/delete/{id}")
	public Mono<Void> deleteUser(@PathVariable String id)
	{
		return userRepository.findById(Integer.parseInt(id))
							 .flatMap(u -> userRepository.delete(u));
	}
	
	@PutMapping("/pay/{userid}/{amount}")
	public Mono<ResponseEntity<String>> updateUser(@PathVariable("userid") String userid,@PathVariable("amount") String amount)
	{
		return userRepository.findById(Integer.parseInt(userid))
							 .map(u -> Optional.of(u))
							 .defaultIfEmpty(Optional.empty())
							 .flatMap(exUser ->{
								 
								 if(exUser.isPresent())
								 {
									 return userRepository.updateBalance(Double.parseDouble(amount), Integer.parseInt(userid))
								 			  .filter(b -> b.booleanValue())
								 			  .map(u -> new ResponseEntity<>("payment done",HttpStatus.ACCEPTED))
								 			  .defaultIfEmpty(ResponseEntity.badRequest().build());
								 }
								 
								 return Mono.just(ResponseEntity.notFound().build()); 
								 
							 });
	}
	
}
