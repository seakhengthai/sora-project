package com.demo.gateway.exception.code;

public interface ErrorMessageCode {

     String getCode();
     String getMessage();
     default String prefix(){
          return "GW-";
     }

}
