package com.shifa.employee_management_api.exception;

public class DuplicateException extends RuntimeException{
    public DuplicateException(String meassage){
        super(meassage);
    }
}
