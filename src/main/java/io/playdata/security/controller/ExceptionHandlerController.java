package io.playdata.security.controller;

import io.playdata.security.exception.UniqueUsernameException;
import io.playdata.security.exception.UsernameLengthException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // 보조형 Controller
public class ExceptionHandlerController {

    @ExceptionHandler(Exception.class) // Exception으로 발생되는 것들을 처리하겠다 (모든 에러는 Exception타입)
    public String handleException(Exception ex, Model model) {
        // ex.getMessage() -> new Exception(message)
        // throw new Exception("중복된 Username 입니다");
//        model.addAttribute("msg",ex.getMessage());
        return "error";
    }

    @ExceptionHandler(UsernameLengthException.class)
    public String handleException(UsernameLengthException ex) {
        return "redirect:/join";
    }
    @ExceptionHandler(UniqueUsernameException.class)
    public String handleException(UniqueUsernameException ex) {
        return "redirect:/login";
    }
}
