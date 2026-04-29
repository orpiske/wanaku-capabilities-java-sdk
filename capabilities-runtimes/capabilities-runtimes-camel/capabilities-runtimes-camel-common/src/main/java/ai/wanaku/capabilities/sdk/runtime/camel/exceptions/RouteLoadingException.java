package ai.wanaku.capabilities.sdk.runtime.camel.exceptions;

import ai.wanaku.capabilities.sdk.api.exceptions.WanakuException;

/**
 * Thrown when Camel routes cannot be loaded from a given resource path.
 *
 * @see ai.wanaku.capabilities.sdk.runtime.camel.util.WanakuRoutesLoader
 */
public class RouteLoadingException extends WanakuException {

    /**
     * Creates an exception indicating that routes failed to load from the specified path.
     *
     * @param path the resource path that could not be loaded
     */
    public RouteLoadingException(String path) {
        super("Failed to load routes from " + path);
    }

    /**
     * Creates an exception indicating that routes failed to load, wrapping the underlying cause.
     *
     * @param path  the resource path that could not be loaded
     * @param cause the underlying exception thrown during route loading
     */
    public RouteLoadingException(String path, Throwable cause) {
        super(path, cause);
    }

    /**
     * Creates an exception wrapping the underlying cause.
     *
     * @param cause the underlying exception thrown during route loading
     */
    public RouteLoadingException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates an exception with full control over message, cause, suppression, and stack trace writability.
     *
     * @param message            the detail message
     * @param cause              the underlying cause
     * @param enableSuppression  whether suppression is enabled
     * @param writableStackTrace whether the stack trace is writable
     */
    public RouteLoadingException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
