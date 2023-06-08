package com.devex.reactiveRDBMS.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table("rx_users")
public class User 
{
	@Id
	private Integer id;
	
	private String name;
	
	private double balance;
}
