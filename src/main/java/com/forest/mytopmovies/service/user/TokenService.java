package com.forest.mytopmovies.service.user;

import java.util.Map;

public interface TokenService {
    String generateExpiringToken(Map<String, String> attributes);

    Map<String, String> verify(String token);
}
