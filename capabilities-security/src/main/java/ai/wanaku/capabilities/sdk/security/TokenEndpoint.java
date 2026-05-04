package ai.wanaku.capabilities.sdk.security;

/**
 * Utility class for constructing OAuth2 token endpoint URLs.
 * Provides methods for creating endpoint URLs either directly or from base URLs.
 */
public final class TokenEndpoint {

    /**
     * Private constructor to prevent instantiation.
     */
    private TokenEndpoint() {}

    /**
     * Returns the provided URI directly as the token endpoint.
     *
     * @param uri The complete token endpoint URI.
     * @return The same URI.
     */
    public static String direct(String uri) {
        return uri;
    }

    /**
     * Constructs a token endpoint URL by appending the standard OpenID Connect path to a base URL.
     *
     * @param baseUrl The base URL of the authentication server.
     * @return The complete token endpoint URL.
     */
    public static String fromBaseUrl(String baseUrl) {
        return baseUrl + "/protocol/openid-connect/token";
    }

    /**
     * Constructs a token endpoint URL by appending the discovery OpenID Connect path to a base URL.
     *
     * @param baseUrl The base URL of the authentication server.
     * @return The complete token endpoint URL.
     */
    public static String forDiscovery(String baseUrl) {
        return baseUrl + "/q/oidc/";
    }

    private static final String OIDC_TOKEN_PATH = "/protocol/openid-connect/token";

    /**
     * Extracts the realm URL from a token endpoint URL by stripping the OIDC token path suffix.
     * If the URL does not end with the token path, it is returned unchanged.
     *
     * @param tokenEndpoint The token endpoint URL.
     * @return The realm URL.
     */
    public static String toRealmUrl(String tokenEndpoint) {
        if (tokenEndpoint != null && tokenEndpoint.endsWith(OIDC_TOKEN_PATH)) {
            return tokenEndpoint.substring(0, tokenEndpoint.length() - OIDC_TOKEN_PATH.length());
        }
        return tokenEndpoint;
    }

    /**
     * Automatically resolve the best URI to use
     * @param registrationUri
     * @param tokenEndpointUri
     * @return
     */
    public static String autoResolve(String registrationUri, String tokenEndpointUri) {
        if (tokenEndpointUri != null) {
            if (!tokenEndpointUri.isEmpty() && !tokenEndpointUri.matches(".*/realms/[^/]+/?$")) {
                return direct(tokenEndpointUri);
            }

            return fromBaseUrl(tokenEndpointUri);
        }

        return forDiscovery(registrationUri);
    }
}
