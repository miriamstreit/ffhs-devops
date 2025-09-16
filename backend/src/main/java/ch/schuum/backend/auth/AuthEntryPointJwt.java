package ch.schuum.backend.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Die Klasse AuthEntryPointJwt implementiert {@link org.springframework.security.web.AuthenticationEntryPoint}
 * und stellt mit der überschriebenen Methode einen Handler für Authentisierungsfehler zur Verfügung
 *
 * @author Miriam Streit
 *
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    /**
     * <p>übernimmt das Fehler-Handling bei der Authentisierung</p>
     * @param request HTTP-Request, der betroffen ist
     * @param response HTTP-Response, die zurückgesendet wird
     * @param authException genauer Fehler, der mit der Authentisierung aufgetreten ist
     * @throws IOException bei Problemen mit der zu überschreibenden HTTP-Response
     * @author Miriam Streit
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        logger.error("Unauthorized error: {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Fehler: nicht autorisiert");
    }

}