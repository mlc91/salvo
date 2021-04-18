package com.codeoftheweb.salvo.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import java.util.HashMap;
import java.util.Map;

public class Util {
    //----------------------------------------IS GUEST----------------------------------------------------------------//
    public static boolean isGuest(Authentication authentication){
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }
    //----------------------------------------MAKE MAP----------------------------------------------------------------//
    public static Map<String, Object> makeMap(String key, Object value){
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);

        return map;
    }
}
