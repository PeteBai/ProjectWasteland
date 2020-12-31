package org.liberty.j.wasteland.Exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.liberty.j.wasteland.Exception.ErrorEnum;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = DefinitionException.class)
    @ResponseBody
    public Result bizExceptionHandler(DefinitionException e) {
        return Result.defineError(e);
    }
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionHandler(Exception e) {
        e.printStackTrace();
        return Result.otherError(ErrorEnum.INTERNAL_SERVER_ERROR);
    }
}