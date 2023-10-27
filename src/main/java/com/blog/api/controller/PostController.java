package com.blog.api.controller;

import com.blog.api.request.PostCreate;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class PostController {

    @PostMapping("/posts")
//    public Map<String ,String> post(@Valid @RequestBody PostCreate params , BindingResult result )  {
    public Map<String ,String> post(@Valid @RequestBody PostCreate params  )  {
//        log.info("params={}",params);
//        if(result.hasErrors()){
//            List<FieldError> fieldErrors = result.getFieldErrors();
//            FieldError firstFieldErrors = fieldErrors.get(0);
//            String fieldName = firstFieldErrors.getField();
//            String errorMessage = firstFieldErrors.getDefaultMessage();
//
//            HashMap<String , String> error = new HashMap<>();
//            error.put(fieldName, errorMessage);
//            return  error;
//        }
        return Map.of();
    }

}
