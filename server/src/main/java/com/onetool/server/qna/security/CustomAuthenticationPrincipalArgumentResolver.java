//package com.onetool.server.qna.security;
//
//import com.onetool.server.global.exception.BaseException;
//import com.onetool.server.global.exception.codes.ErrorCode;
//import org.springframework.core.MethodParameter;
//import org.springframework.core.annotation.AnnotationUtils;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.util.ClassUtils;
//import org.springframework.web.bind.support.WebDataBinderFactory;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.method.support.ModelAndViewContainer;
//
//import java.lang.annotation.Annotation;
//
//public final class CustomAuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
//
//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//        return findMethodAnnotation(AuthUser.class, parameter) != null;
//    }
//
//    @Override
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
//                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null) {
//            // Custom Exception 을 통한 예외 처리
//            throw new BaseException(ErrorCode.NON_AUTHORIZED_USER);
//        }
//        Object principal = authentication.getPrincipal();
//        AuthUser annotation = findMethodAnnotation(AuthUser.class, parameter);
//        if (principal == "anonymousUser") {
//            // Custom Exception 을 통한 예외 처리
//        }
//        findMethodAnnotation(AuthUser.class, parameter);
//        if (principal != null && !ClassUtils.isAssignable(parameter.getParameterType(), principal.getClass())) {
//            if (annotation.errorOnInvalidType()) {
//                throw new ClassCastException(principal + " is not assignable to " + parameter.getParameterType());
//            }
//        }
//
//        return principal;
//    }
//
//    private <T extends Annotation> T findMethodAnnotation(Class<T> annotationClass, MethodParameter parameter) {
//        T annotation = parameter.getParameterAnnotation(annotationClass);
//        if (annotation != null) {
//            return annotation;
//        }
//        Annotation[] annotationsToSearch = parameter.getParameterAnnotations();
//        for (Annotation toSearch : annotationsToSearch) {
//            annotation = AnnotationUtils.findAnnotation(toSearch.annotationType(), annotationClass);
//            if (annotation != null) {
//                return annotation;
//            }
//        }
//        return null;
//    }
//}
