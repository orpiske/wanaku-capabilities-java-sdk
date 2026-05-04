package ai.wanaku.capabilities.sdk.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenEndpointTest {

    @Test
    void directReturnsProvidedUri() {
        String uri = "https://auth.example.com/oauth/token";
        assertEquals(uri, TokenEndpoint.direct(uri));
    }

    @Test
    void fromBaseUrlAppendsOpenIdConnectPath() {
        String baseUrl = "https://auth.example.com/realms/wanaku";
        String expected = "https://auth.example.com/realms/wanaku/protocol/openid-connect/token";

        assertEquals(expected, TokenEndpoint.fromBaseUrl(baseUrl));
    }

    @Test
    void forDiscoveryAppendsOidcPath() {
        String baseUrl = "https://service.example.com";
        String expected = "https://service.example.com/q/oidc/";

        assertEquals(expected, TokenEndpoint.forDiscovery(baseUrl));
    }

    @Test
    void autoResolveUsesDirectWhenTokenEndpointDoesNotEndWithRealmsWanaku() {
        String registrationUri = "https://service.example.com";
        String tokenEndpointUri = "https://auth.example.com/oauth/token";

        String result = TokenEndpoint.autoResolve(registrationUri, tokenEndpointUri);

        assertEquals(tokenEndpointUri, result);
    }

    @Test
    void autoResolveUsesFromBaseUrlWhenTokenEndpointEndsWithRealmsWanaku() {
        String registrationUri = "https://service.example.com";
        String tokenEndpointUri = "https://auth.example.com/realms/wanaku/";

        String result = TokenEndpoint.autoResolve(registrationUri, tokenEndpointUri);
        String expected = "https://auth.example.com/realms/wanaku//protocol/openid-connect/token";

        assertEquals(expected, result);
    }

    @Test
    void autoResolveUsesFromBaseUrlWhenTokenEndpointEndsWithCustomRealm() {
        String registrationUri = "https://service.example.com";
        String tokenEndpointUri = "https://auth.example.com/realms/my-custom-realm/";

        String result = TokenEndpoint.autoResolve(registrationUri, tokenEndpointUri);
        String expected = "https://auth.example.com/realms/my-custom-realm//protocol/openid-connect/token";

        assertEquals(expected, result);
    }

    @Test
    void autoResolveUsesFromBaseUrlWhenTokenEndpointEndsWithRealmNoTrailingSlash() {
        String registrationUri = "https://service.example.com";
        String tokenEndpointUri = "https://auth.example.com/realms/production";

        String result = TokenEndpoint.autoResolve(registrationUri, tokenEndpointUri);
        String expected = "https://auth.example.com/realms/production/protocol/openid-connect/token";

        assertEquals(expected, result);
    }

    @Test
    void autoResolveUsesFromBaseUrlWhenTokenEndpointIsEmpty() {
        String registrationUri = "https://service.example.com";
        String tokenEndpointUri = "";

        String result = TokenEndpoint.autoResolve(registrationUri, tokenEndpointUri);
        String expected = "/protocol/openid-connect/token";

        assertEquals(expected, result);
    }

    @Test
    void autoResolveUsesDiscoveryWhenTokenEndpointIsNull() {
        String registrationUri = "https://service.example.com";

        String result = TokenEndpoint.autoResolve(registrationUri, null);
        String expected = "https://service.example.com/q/oidc/";

        assertEquals(expected, result);
    }
}
