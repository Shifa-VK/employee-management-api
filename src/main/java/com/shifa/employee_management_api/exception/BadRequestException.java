package com.shifa.employee_management_api.exception;

/**
 * Author: Shifa VK
 * Created: 04-08-2026
 */
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message){
        super(message);
    }
}
