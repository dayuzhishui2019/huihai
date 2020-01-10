package com.dayu.response.handle;


import com.dayu.response.RunningError;
import com.dayu.response.annotation.IgnoreDecorate;
import com.dayu.response.exception.BusinessException;
import com.dayu.response.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.*;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;

@Slf4j
@ControllerAdvice
public class ResultHandler implements ResponseBodyAdvice<Object> {

    private Class[] include = new Class[]{
            RequestMapping.class,
            GetMapping.class,
            PostMapping.class,
            DeleteMapping.class,
            PutMapping.class,
            PatchMapping.class
    };

    private Class[] excludes = new Class[]{
            IgnoreDecorate.class
    };

    private Class[] unSupportClass = new Class[]{
            File.class, byte[].class, InputStream.class
    };

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        AnnotatedElement element = methodParameter.getAnnotatedElement();
        Class<?> returnClass = methodParameter.getMethod().getReturnType();
        return Arrays.stream(include).anyMatch(a -> a.isAnnotation() && element.isAnnotationPresent(a))
                && !Arrays.stream(excludes).anyMatch(a -> a.isAnnotation() && element.isAnnotationPresent(a))
                && !Arrays.stream(unSupportClass).anyMatch(s -> returnClass.isAssignableFrom(s));
    }

    @ResponseBody
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {

        Class<?> clz = methodParameter.getDeclaringClass();
        if (!clz.getName().startsWith("com.dayu")) {
            return o;
        }
        if (o instanceof Result) {
            return o;
        }
        if (mediaType != null && mediaType.includes(MediaType.APPLICATION_JSON)) {
            RunningError success = RunningError.SUCCESS;
            return Result.builder().code(success.getCode()).message(success.getMessage()).data(o).build();
        }
        return o;
    }


    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        RunningError error = e.getError();
        return Result.builder().code(error.getCode())
                .message(error.getMessage())
//                .exception(printStack(e))
                .build();
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        RunningError error = RunningError.FAIL;
        return Result.builder().code(error.getCode())
                .message(error.getMessage())
                .exception(printStack(e))
                .build();
    }


    private String printStack(Exception e) {
        ByteArrayOutputStream outputStream = null;
        PrintStream stream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            stream = new PrintStream(outputStream);
            e.printStackTrace(stream);
            return new String(outputStream.toByteArray(), "utf-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } finally {
            if (stream != null) {
                stream.close();
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                }
            }
        }
        return null;
    }
}
