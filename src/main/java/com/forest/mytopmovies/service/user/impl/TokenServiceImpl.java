package com.forest.mytopmovies.service.user.impl;

import com.forest.mytopmovies.constants.JwtConstants;
import com.forest.mytopmovies.service.user.TokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Service
public class TokenServiceImpl implements Clock, TokenService {
    private static final String DOT = ".";
    private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

    private final JwtConstants constants;

    private final java.time.Clock clock;

    public TokenServiceImpl(JwtConstants constants, java.time.Clock clock) {
        this.constants = constants;
        this.clock = clock;
    }

    @Override
    public String generatePermanentToken(Map<String, String> attributes) {
        return newToken(attributes, 0);
    }

    @Override
    public String generateExpiringToken(Map<String, String> attributes) {
        return newToken(attributes, constants.expireSec);
    }

    @Override
    public Map<String, String> verifyUntrusted(String token) {
        JwtParser parser = jwtParser();
        String withoutSignature = token.substring(0, token.lastIndexOf(DOT)) + DOT;
        return parseClaims(() -> parser.parseClaimsJwt(withoutSignature).getBody());
    }

    @Override
    public Map<String, String> verify(String token) {
        JwtParser parser = jwtParser().setSigningKey(constants.secretKey);
        return parseClaims(() -> parser.parseClaimsJws(token).getBody());
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

    private Map<String, String> parseClaims(Supplier<Claims> toClaims) {
        Claims claims = toClaims.get();
        Map<String, String> attributes = new HashMap<>();
        claims.forEach((key, val) -> attributes.put(key, String.valueOf(val)));
        return Map.copyOf(attributes);
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
