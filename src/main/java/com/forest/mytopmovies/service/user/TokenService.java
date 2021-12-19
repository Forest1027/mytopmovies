package com.forest.mytopmovies.service.user;

import java.util.Map;

public interface TokenService {
    String generatePermanentToken(Map<String, String> attributes);

    String generateExpiringToken(Map<String, String> attributes);

    Map<String, String> verifyUntrusted(String token);

    Map<String, String> verify(String token);
}
