package com.alten.shop.exception;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter

public class ErrorMessage {

	@JsonInclude(JsonInclude.Include.NON_NULL) 
	private int code;
	
	@JsonInclude(JsonInclude.Include.NON_NULL) 
	private Date timestamp;
	
	@JsonInclude(JsonInclude.Include.NON_NULL) 
	private String message;
	
	@JsonInclude(JsonInclude.Include.NON_NULL) 
	private String description;

	public ErrorMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}

}