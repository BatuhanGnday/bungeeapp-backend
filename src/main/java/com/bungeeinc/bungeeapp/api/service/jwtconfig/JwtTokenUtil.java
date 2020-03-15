package com.bungeeinc.bungeeapp.api.service.jwtconfig;

import java.io.Serializable;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    private String secret = "3604e30e01135d5e00821287ac91586206e2a8b5a8f8cd6084eafe52648f30c618198cee62cbfc4131856b4e8520299697260ad1033f9151766499bf5bde6b6e6fe67f6db3ccf8c55068af3759eeaeea34e6c4c6cbca4fa623b2d7c633074c96b684394344a55a852abfff2c844af280ee13162d2ddc63af9f32ffca6c1ed1c9b810d765944b664552e5e56399968eb70e7a11d013d00ade89bf8c4090";
    //private String secret = "secret";

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     *
     * @param token jwt token
     * @return Expiration Date
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     *
     * @param token jwt token
     * @param claimsResolver claim resolver
     * @param <T> generic
     * @return claims
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, user.getUsername());
    }

    private Claims getAllClaimsFromToken(String token) {
/*        String raw = new String(token.getBytes());
        String encoded = raw.replace("_", "+").replace("-", "/");
        String decoded = String.valueOf(Base64.getDecoder().decode(encoded));*/

        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token) // token yerine decoded'ı vermiştim
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        //byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY  * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                //.signWith(SignatureAlgorithm.HS512, secret) // deprecated
                .compact();
    }

    public boolean validateToken(String token, UserDetails user) {
        final String username = getUsernameFromToken(token);

        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

}
