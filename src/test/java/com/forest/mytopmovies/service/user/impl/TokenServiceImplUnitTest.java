package com.forest.mytopmovies.service.user.impl;

import com.forest.mytopmovies.config.ClockConfiguration;
import com.forest.mytopmovies.constants.JwtConstants;
import com.forest.mytopmovies.service.user.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplUnitTest {

    private TokenService underTest;

    private String permanentToken = "eyJhbGciOiJIUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAA_6tWyiwuVrJScssvSi0uUdJRykwsUbIyNDO2NDU0NTMw0FEqLU4tykvMTQUqSoMoqgUACLK2XTUAAAA.os-pt8_ARKMDomidBu8vvjxRjUCnhAm59yLjZQ0noGw";

    private String expireToken = "eyJhbGciOiJIUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAA_6tWyiwuVrJScssvSi0uUdJRykwsUbIyNDO2NDU0NTMw0FFKrSiACZibgARKi1OL8hJzU4G60iC6agG9GTFvRgAAAA.sprDk-_YhOlV89VUFdNCAzRv6i5smNhK5_bwXvRtIho";

    @BeforeEach
    void setUp() {
        JwtConstants constants = new JwtConstants();
        constants.issuer = "Forest";
        constants.clockSkewSec = 120;
        constants.expireSec = 1800;
        constants.secretKey = "test-key";
        ClockConfiguration config = new ClockConfiguration();
        underTest = new TokenServiceImpl(constants, config.testClock());
    }

    @Test
    void checkPermanent() {
        // given
        // when
        String token = underTest.generatePermanentToken(Map.of("username", "forest"));
        // then
        assertThat(token).isEqualTo(permanentToken);
    }

    @Test
    void checkExpiring() {
        // given
        // when
        String token = underTest.generateExpiringToken(Map.of("username", "forest"));
        // then
        assertThat(token).isEqualTo(expireToken);
    }

    @Test
    void checkUntrusted() {
        Map<String, String> result = underTest.verifyUntrusted(expireToken);
        assertThat(result).containsEntry("username", "forest").containsEntry("iss", "Forest");
        assertThat(Integer.valueOf(result.get("exp")) - Integer.valueOf(result.get("iat"))).isEqualTo(1800);
    }

    @Test
    void checkVerify() {
        Map<String, String> result = underTest.verify(expireToken);
        assertThat(result).containsEntry("username", "forest").containsEntry("iss", "Forest");
        assertThat(Integer.valueOf(result.get("exp")) - Integer.valueOf(result.get("iat"))).isEqualTo(1800);
    }

}