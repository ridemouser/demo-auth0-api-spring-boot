package org.example;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * This is a Sample Controller to demonstrate how Auth0 can be used to
 * authorize rest endpoints and build a secure microservice
 */
@Slf4j
@RestController
public class LandingPageController {

    /**
     * Method to expose public endpoint that is open to everyone and not authenticated
     * @param principal Jwt
     * @return
     */
    @GetMapping("/api/public/")
    public ResponseEntity<String> testApiPublic(@AuthenticationPrincipal Jwt principal) {
        return new ResponseEntity<>("Hello Public", HttpStatus.OK);
    }

    /**
     * Method to expose the /api/internal/ endpoint that has an explicit
     * denyAll() and not allowed to access by any client
     * @param model
     * @param principal
     * @param authentication
     * @return  The authenticated principal after authentication
     */
    @GetMapping("/api/internal/")
    public ResponseEntity<Object> testApiInternal(Model model, @AuthenticationPrincipal Jwt principal
            , JwtAuthenticationToken authentication) {
        if (principal != null) {
            System.out.println("Entered testApiInternal");
            Authentication authDetails = SecurityContextHolder.getContext().getAuthentication();
            return new ResponseEntity<>(authDetails.getPrincipal(), HttpStatus.OK);
        }
        return null;
    }

    /**
     * Method to expose the /api/internal-scoped/read/ endpoint
     * that can only accessed by a client that has
     * scope = read:securities
     * @param model
     * @param principal
     * @return  The authenticated principal after authentication
     */
    @GetMapping("/api/internal-scoped/read/")
    @PreAuthorize("hasAuthority('SCOPE_read:securities')")
    public ResponseEntity<Object> testApiInternalReadScoped(Model model
            , @AuthenticationPrincipal Jwt principal) {
        if (principal != null) {
            System.out.println("Entered testApiInternalReadScoped");
            Authentication authDetails = SecurityContextHolder.getContext().getAuthentication();
            return new ResponseEntity<>(authDetails.getPrincipal(), HttpStatus.OK);
        }
        return null;
    }

    /**
     * Method to expose the /api/internal-scoped/write/ endpoint
     * that can only accessed by a client that has
     * scope = write:securities
     * @param model
     * @param principal
     * @return  The authenticated principal after authentication
     */
    @GetMapping("/api/internal-scoped/write/")
    //@PreAuthorize("hasAuthority('SCOPE_write:securities')")
    public ResponseEntity<Object> testApiInternalWriteScoped(Model model
            , @AuthenticationPrincipal Jwt principal) {
        log.info("entered scope test" + principal.getClaims().get("scope"));
        if (principal != null) {
            System.out.println("Entered testApiInternalWriteScoped");
            Authentication authDetails = SecurityContextHolder.getContext().getAuthentication();
            return new ResponseEntity<>(authDetails.getPrincipal(), HttpStatus.OK);
        }
        return null;
    }


}