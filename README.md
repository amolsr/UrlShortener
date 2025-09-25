URL Shortener - Short.link

A sleek URL shortening service that transforms long web addresses into elegant, bite-sized aliases. Short links are easy to copy, share, and type, and they reduce the risk of user typos.

## Features

1. **Short Link Generation**

   * Generate a unique short link for any given URL.
   * Structure: `https://short.link/{uniqueId}`.

2. **Redirection**

   * Visiting a short link redirects the user to the original URL.

3. **Expiration**

   * Links expire after a default timespan.
   * Users can set a custom expiration time.

4. **Link Management**

   * Users can delete short links.

5. **Click Tracking**

   * Each short link tracks and displays the number of clicks.
   * Click count is displayed under each link:
     This link has been clicked 3 times.

     * Styled at 14px font size with `#9bb7f4` color.

6. **QR Code Generation**

   * Generate a QR code for each short link for easy sharing.
   * Displayed in the UI as a dropdown below the link.

7. **Responsive User Interface**

   * UI matches the design provided in the separate design file.

---

## Technology Stack

* **Backend**: Java 21, Spring Boot 3.5
* **Database**: PostgreSQL (containerized with Docker)
* **Frontend**: HTML, CSS, JavaScript
* **QR Code**: ZXing library
* **Containerization**: Docker & Docker Compose

---

## Deployment

### Using Docker Compose

Pull the backend image from Docker Hub:

```bash
docker pull ylliberisha/urlshortener:latest
```

Start the app and the database in one command:

```bash
docker compose up -d
```

The backend will be available at:

```
http://localhost:8080
```

**Notes:**

* QR codes fetch the current short link dynamically from the backend.
* Links are stored in PostgreSQL and tracked for clicks.
* Docker Compose sets up both the backend and PostgreSQL automatically.
* To use QR code linking from other devices, navigate to `http://<your-ip-address>:8080` instead of localhost.

