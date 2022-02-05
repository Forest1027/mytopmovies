package com.forest.mytopmovies.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("constants.jwt")
@Data
public class JwtProperties {
    public String issuer;
    public int expireSec;
    public long clockSkewSec;
    public String secretKey;
}
