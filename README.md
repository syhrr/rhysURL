# Rhys' URL Shortener

This project is just for my learning and understanding. Nothing *too* serious.

## How does a URL shortener work?

### URL Shortening
The client sends a `POST` request with the long URL and then receives the short URL with error code `201`.

The URL shortening service (this Java SpringBoot application) receives requests and is responsible for creating and returning
shortened URLs. The three main functions are:
1. Generate a unique alias by either using a hashing algorithm or base-62 encoding
2. Store the mapping of long URLs to their short URLs in a database
3. Handle failure and manage collisions (ensuring that URLs are unique across all users)

### URL Redirection
When the user accesses the short URL, the service needs to then redirect them quickly to the OG URL


