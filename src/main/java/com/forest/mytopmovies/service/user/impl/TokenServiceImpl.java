package com.forest.mytopmovies.service.user.impl;

import com.forest.mytopmovies.constants.JwtConstants;
import com.forest.mytopmovies.service.user.TokenService;
import com.google.common.collect.ImmutableMap;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;

@Service
public class TokenServiceImpl implements Clock, TokenService {
    private static final String DOT = ".";
    private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

    @Autowired
    private JwtConstants constants;

    @Override
    public String permanent(Map<String, String> attributes) {
        return newToken(attributes, 0);
    }

    @Override
    public String expiring(Map<String, String> attributes) {
        return newToken(attributes, constants.expireSec);
    }

    @Override
    public Map<String, String> untrusted(String token) {
        JwtParser parser = Jwts.parser().requireIssuer(constants.issuer).setClock(this).setAllowedClockSkewSeconds(constants.clockSkewSec);
        String withoutSignature = token.substring(0, token.lastIndexOf(DOT)) + DOT;
        return parseClaims(() -> parser.parseClaimsJws(withoutSignature).getBody());
    }

    @Override
    public Map<String, String> verify(String token) {
        JwtParser parser = Jwts.parser().requireIssuer(constants.issuer).setClock(this).setAllowedClockSkewSeconds(constants.clockSkewSec).setSigningKey(constants.secretKey);
        return parseClaims(() -> parser.parseClaimsJws(token).getBody());
    }

    private String newToken(Map<String, String> attributes, int expireInSec) {
        DateTime now = new DateTime();
        Claims claims = Jwts.claims().setIssuer(constants.issuer).setIssuedAt(now.toDate());
        if (expireInSec > 0) {
            DateTime expireAt = now.plusSeconds(expireInSec);
            claims.setExpiration(expireAt.toDate());
        }
        claims.putAll(attributes);
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, constants.secretKey)
                .compressWith(COMPRESSION_CODEC).compact();
    }

    private static Map<String, String> parseClaims(Supplier<Claims> toClaims) {
        Claims claims = toClaims.get();
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        for (Map.Entry<String, Object> e : claims.entrySet()) {
            builder.put(e.getKey(), String.valueOf(e.getValue()));
        }
        return builder.build();
    }

    @Override
    public Date now() {
        return new DateTime().toDate();
    }
}
