package com.forest.mytopmovies.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("constants.jwt")
public class JwtConstants {
    public String issuer;
    public int expireSec;
    public long clockSkewSec;
    public String secretKey;

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public int getExpireSec() {
        return expireSec;
    }

    public void setExpireSec(int expireSec) {
        this.expireSec = expireSec;
    }

    public long getClockSkewSec() {
        return clockSkewSec;
    }

    public void setClockSkewSec(long clockSkewSec) {
        this.clockSkewSec = clockSkewSec;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
