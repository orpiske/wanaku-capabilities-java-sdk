# SDK Usage Guide

This guide provides instructions on how to use the Wanaku Capabilities Java SDK to generate new capabilities.

## Generating a New Capability Project

To create a new project based on the Wanaku Capabilities Java SDK archetype, use the following Maven command. Remember to replace `ai.test` with your desired package, `groupId`, `artifactId`, and `name`.

```bash
mvn -B archetype:generate -DarchetypeGroupId=ai.wanaku.sdk \
  -DarchetypeArtifactId=capabilities-archetypes-java-tool \
  -DarchetypeVersion=${WANAKU_VERSION} \
  -DgroupId=ai.test \
  -Dpackage=ai.test \
  -DartifactId=test \
  -Dname=Test \
  -Dwanaku-sdk-version=${WANAKU_VERSION}
```

**Explanation of Parameters:**

*   `-DarchetypeGroupId`: The groupId of the archetype (always `ai.wanaku.sdk`).
*   `-DarchetypeArtifactId`: The artifactId of the archetype (always `capabilities-archetypes-java-tool`).
*   `-DarchetypeVersion`: The version of the archetype. Use the current version of the Wanaku SDK, typically passed as `${WANAKU_VERSION}`.
*   `-DgroupId`: Your project's group ID (e.g., `com.mycompany`).
*   `-Dpackage`: Your project's base package (e.g., `com.mycompany.mytool`).
*   `-DartifactId`: Your project's artifact ID (e.g., `my-awesome-tool`).
*   `-Dname`: A human-readable name for your capability (e.g., `My Awesome Tool`).
*   `-Dwanaku-sdk-version`: The version of the Wanaku SDK to be used in the generated project. This should match `${WANAKU_VERSION}`.

## Modifying the Generated Tool Class

After generating the project, navigate into the newly created project directory. You will find a class named `AppTool` in your main source folder (`src/main/java/...`).

Modify the `toolInvoke` method within this `AppTool` class to implement the actual logic of your capability. This method is the entry point for executing your tool's functionality when invoked by the Wanaku platform.

```java
package ai.test;

import ai.wanaku.core.exchange.ToolInvokeReply;
import ai.wanaku.core.exchange.ToolInvokeRequest;
import ai.wanaku.core.exchange.ToolInvokerGrpc;
import io.grpc.stub.StreamObserver;
import java.util.List;

public class AppTool extends ToolInvokerGrpc.ToolInvokerImplBase {

    public void invokeTool(ToolInvokeRequest request, StreamObserver<ToolInvokeReply> responseObserver) {

        try {
            // Here, write the code to actually invoke the tool, then set whatever is returned as the
            // response object (i.e; Object response = myToolCall();)
            Object response = null;

            // Build the response
            responseObserver.onNext(
                    ToolInvokeReply.newBuilder()
                            .setIsError(false)
                            .addAllContent(List.of(response.toString())).build());

            responseObserver.onCompleted();
        } finally {
            // cleanup
        }
    }
}
```

Replace the `// TODO: Implement your tool's logic here` comment with your specific business logic. 

The `ToolInvokeRequest` object provides access to various information and services relevant to the tool's execution request.

## Accessing Request Data

The `ToolInvokeRequest` provides several methods to access invocation data:

| Method | Description |
|--------|-------------|
| `getArgumentsMap()` | Tool arguments passed by the caller |
| `getHeadersMap()` | HTTP headers including metadata headers |
| `getBody()` | Request body content (from `wanaku_body` argument) |
| `getUri()` | The tool's configured URI |
| `getConfigurationURI()` | URI for external configuration |
| `getSecretsURI()` | URI for secrets/credentials |

## Accessing Metadata Headers

AI services can inject metadata into tool invocations using the `wanaku_meta_*` prefix convention. Arguments prefixed with `wanaku_meta_` are automatically:

1. Extracted from regular arguments
2. Prefix stripped to form the header name
3. Made available via `request.getHeadersMap()`

### Example: LangChain4j AI Service

```java
@RegisterAiService
public interface MyAIService {
    @McpToolBox("wanakutoolbox")
    String callTool(
        @Header("wanaku_meta_contextId") String contextId,
        @Header("wanaku_meta_userId") String userId,
        @UserMessage String message
    );
}
```

### Accessing Headers in Tool Implementation

```java
public void invokeTool(ToolInvokeRequest request, StreamObserver<ToolInvokeReply> responseObserver) {
    // Access metadata headers (prefix is stripped)
    Map<String, String> headers = request.getHeadersMap();
    String contextId = headers.get("contextId");  // from wanaku_meta_contextId
    String userId = headers.get("userId");        // from wanaku_meta_userId

    // Use headers for context-aware processing
    if (contextId != null) {
        // Process with context...
    }

    // Regular arguments (metadata args are filtered out)
    Map<String, String> args = request.getArgumentsMap();
    // ...
}
```

### Reserved Argument Names

The SDK provides constants for reserved argument names:

```java
import ai.wanaku.capabilities.sdk.api.util.ReservedArgumentNames;

// Body content argument
String bodyArg = ReservedArgumentNames.BODY;  // "wanaku_body"

// Metadata prefix for header injection
String prefix = ReservedArgumentNames.METADATA_PREFIX;  // "wanaku_meta_"
```

| Constant | Value | Purpose |
|----------|-------|---------|
| `BODY` | `wanaku_body` | Argument containing request body content |
| `METADATA_PREFIX` | `wanaku_meta_` | Prefix for arguments converted to headers |

# Learn More

- **[Client Registration Flow](client-registration-flow.md)** - Client Registration Flow