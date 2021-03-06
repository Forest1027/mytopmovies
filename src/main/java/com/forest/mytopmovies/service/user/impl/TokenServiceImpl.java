package com.forest.mytopmovies.service.user.impl;

import com.forest.mytopmovies.properties.JwtProperties;
import com.forest.mytopmovies.service.user.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenServiceImpl implements Clock, TokenService {
    private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

    private final JwtProperties constants;

    private final java.time.Clock clock;

    public TokenServiceImpl(JwtProperties constants, java.time.Clock clock) {
        this.constants = constants;
        this.clock = clock;
    }

    @Override
    public String generateExpiringToken(Map<String, String> attributes) {
        return newToken(attributes, constants.expireSec);
    }

    @Override
    public Map<String, String> verify(String token) {
        JwtParser parser = jwtParser().setSigningKey(constants.secretKey);
        return parseClaims(parser, token);
    }

    private String newToken(Map<String, String> attributes, int expireInSec) {
        Claims claims = getClaims(expireInSec);
        claims.putAll(attributes);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, constants.secretKey)
                .compressWith(COMPRESSION_CODEC).compact();
    }

    private Claims getClaims(int expireInSec) {
        Date now = Date.from(clock.instant());
        Claims claims = Jwts.claims()
                .setIssuer(constants.issuer)
                .setIssuedAt(now);
        if (expireInSec > 0) {
            now.setTime(now.getTime() + (expireInSec * 1000));
            claims.setExpiration(now);
        }
        return claims;
    }

    private Map<String, String> parseClaims(JwtParser parser, String token) {
        Claims claims = parser.parseClaimsJws(token).getBody();
        return Map.copyOf(convertClaimsToMap(claims));
    }

    private Map<String, String> convertClaimsToMap(Claims claims) {
        Map<String, String> attributes = new HashMap<>();
        claims.forEach((key, val) -> attributes.put(key, String.valueOf(val)));
        return attributes;
    }

    @Override
    public Date now() {
        return Date.from(clock.instant());
    }

    private JwtParser jwtParser() {
        return Jwts.parser()
                .requireIssuer(constants.issuer)
                .setClock(this)
                .setAllowedClockSkewSeconds(constants.clockSkewSec);
    }
}
