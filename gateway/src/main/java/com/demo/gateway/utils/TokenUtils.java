package com.demo.gateway.utils;

import com.demo.gateway.constants.APIRequestHeaderConstants;
import com.demo.gateway.domain.Channel;
import com.demo.gateway.domain.Token;
import com.demo.gateway.dto.request.TokenGenerateRequest;
import com.demo.gateway.exception.AppException;
import com.demo.gateway.exception.code.AppErrorCode;
import io.jsonwebtoken.*;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class TokenUtils {

    public static final String ROLES = "AUTH_ROLES";
    public static final String CHANNEL = "channel_type";
    public static final String AUTH_HEADER = "Authorization";
    public static final String CIF = "cif";

    @Value("${rsa.private-key}")
    private String internalRSAPrivateKey;

    @Value("${rsa.public-key}")
    private String internalRSAPublicKey;


    public Claims getClaim(String accessToken) {
        return this.isValidToken(accessToken, internalRSAPublicKey);
    }

    private Tuple3<Boolean, String, Claims> verifyToken(String accessToken, String publicKey) {
        return this.verifyTokenInternal(accessToken, publicKey);
    }

    private Tuple3<Boolean, String, Claims> verifyTokenInternal(String accessToken, String publicKey) {
        String msg = AppErrorCode.AUTH_INVALID_TOKEN.getMessage();
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SecurityUtils.readPublicKey(publicKey))
                    .parseClaimsJws(accessToken);
            Claims body = claimsJws.getBody();
            return Tuple.of(Boolean.TRUE, "Valid token.", body);
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            msg = "Invalid access token.";
            log.error(msg);
        } catch (ExpiredJwtException ex) {
            msg = "Access token is expired.";
            log.error(msg);
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return Tuple.of(Boolean.FALSE, msg, null);
    }

    private String genRefreshToken(TokenGenerateRequest tokenCreate) {
        return this.createToken(tokenCreate.getId(), tokenCreate.getRefreshTokenExpired(),
                tokenCreate.getClientId(), null, null,
                tokenCreate.getChannel(), tokenCreate.getDeviceId());
    }

    public Token generateToken(TokenGenerateRequest tokenCreate) {
        String accessToken = this.createToken(tokenCreate.getId(), tokenCreate.getAccessTokenExpired(),
                tokenCreate.getClientId(), tokenCreate.getRoles(), tokenCreate.getCif(),
                tokenCreate.getChannel(), tokenCreate.getDeviceId());
        Token token = new Token();
        token.setType(Token.TOKEN_TYPE);
        token.setAccessToken(accessToken);
        token.setRefreshToken(this.genRefreshToken(tokenCreate));
        return token;
    }

    public Token generateOnlyAccessToken(TokenGenerateRequest tokenCreate) {
        String accessToken = this.createToken(tokenCreate.getId(), tokenCreate.getAccessTokenExpired(),
                tokenCreate.getClientId(), tokenCreate.getRoles(), tokenCreate.getUserId(),
                tokenCreate.getChannel(), tokenCreate.getDeviceId());
        Token token = new Token();
        token.setType(Token.TOKEN_TYPE);
        token.setAccessToken(accessToken);
        return token;
    }

    private String createToken(
            String id,
            Long tokenExpired, String clientId,
            String roles, String userId, Channel channel, String deviceId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenExpired);
        JwtBuilder jwtBuilder = Jwts.builder()
                .setIssuer(clientId)
                .setId(id)
                .claim(ROLES, roles)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim(CHANNEL, channel)
                .claim(CIF, userId)
                .claim(APIRequestHeaderConstants.DEVICE_ID, deviceId)
                .signWith(SignatureAlgorithm.RS256, SecurityUtils.readPrivateKey(internalRSAPrivateKey));
        return jwtBuilder.compact();
    }

    public String getAuthorizationToken(ServerHttpRequest request) {
        if (!request.getHeaders().containsKey(AUTH_HEADER)) {
            throw new AppException(AppErrorCode.AUTH_INVALID_HEADER);
        }
        return request.getHeaders()
                .getFirst(AUTH_HEADER)
                .replace(Token.TOKEN_TYPE, "").trim();
    }


    public Claims isValidToken(String accessToken, String publicKey) {
        Tuple3<Boolean, String, Claims> isValid =
                this.verifyToken(accessToken, publicKey);
        if (!isValid._1) {
            String msg = isValid._2;
            throw new AppException(AppErrorCode.AUTH_INVALID_TOKEN.getCode(), msg);
        }
        return isValid._3();
    }

    public static class ClaimUtils {
        public static String getCif(Claims claims) {
            return claims.getOrDefault(CIF, "").toString();
        }
        public static Channel getChannel(Claims claims) {
            return Channel
                    .valueOf(claims.getOrDefault(CHANNEL, Channel.INVALID_CHANNEL).toString());
        }
    }
}
