package com.shifa.employee_management_api.exception;

public class DuplicateEmailException extends RuntimeException{
    public DuplicateEmailException(String meassage){
        super(meassage);
    }
}
