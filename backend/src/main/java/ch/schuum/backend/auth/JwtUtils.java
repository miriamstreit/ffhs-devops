package ch.schuum.backend.auth;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Die Klasse JwtUtils stellt Funktionen für den Umgang mit JSON-Web-Tokens zur Vefügung.
 *
 * @author Miriam Streit
 *
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${schuum.config.jwtSecret}")
    private String jwtSecret;

    @Value("${schuum.config.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * <p>generiert ein neues JSON-Web-Token mit der E-Mail-Adresse, dem Erstellungsdatum, dem Ablaufdatum und einer Signatur</p>
     * @param authentication Authentication Objekt
     * @return das generierte JSON Web Token
     * @author Miriam Streit
     */
    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder().setSubject((userPrincipal.getEmail())).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * <p>liest E-Mail-Adresse aus dem JSON-Web-Token aus</p>
     * @param token auszulesendes Token
     * @return die ausgelesene E-Mail-Adresse
     * @author Miriam Streit
     */
    public String getEmailFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * <p>validiert das JSON-Web-Token</p>
     * @param authToken zu validierendes Token
     * @return true, wenn Token gültig, false wenn ungültig
     * @author Miriam Streit
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}