package com.project.HeartConnect.security;

import com.project.HeartConnect.utils.global.GlobalValidation;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Log4j2
@Service
@RequiredArgsConstructor
public class JwtService {

    private static final long TOKEN_VALIDITY = 86400000;

    private static final String SECRET_KEY = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";


    private final GlobalValidation globalValidation;


    public static String generateJwtToken(final String email) {
        // Claims of token
        final Map<String, Object> claims = new HashMap<>();
        // Create token
        return createToken(claims, email);
    }

    private static String createToken(final Map<String, Object> claims, final String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }


    public Boolean isTokenValid(final String token){
       return !isTokenExpired(token);
    }
    public static String extractUsernameFromToken(final String token){
        return extractClaim(token, Claims::getSubject);
    }


    private static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);

        // Extract claims from the parsed JWT
        return claimsJws.getBody();
    }

    private boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
