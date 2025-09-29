# URL Shortener

A sleek URL shortening service that transforms long web addresses into elegant, bite-sized aliases. Short links are easy to copy, share, and type, and they reduce the risk of user typos.

## Features

1. **Short Link Generation**

   * Generate a unique short link for any given URL.
   * Structure: `http://localhost:8080/{uniqueId}`.

2. **Redirection**

   * Visiting a short link redirects the user to the original URL.

3. **Expiration**

   * Links expire after a default timespan.
   * Users can set a custom expiration time.

4. **Link Management**

   * Users can delete short links.

5. **Click Tracking** ✅

   * Each short link tracks and displays the number of clicks in real-time.
   * Click count is displayed under each link: "This link has been clicked X times."
   * Styled at 14px font size with `#9bb7f4` color.
   * **Performance Optimized**: Asynchronous click tracking for better performance.
   * **Race Condition Safe**: Uses atomic database operations to prevent data corruption.
   * **User Experience**: Optimistic UI updates provide immediate feedback.

6. **QR Code Generation**

   * Generate a QR code for each short link for easy sharing.
   * Displayed in the UI as a dropdown below the link.

7. **Responsive User Interface**

   * UI matches the design provided in the separate design file.

---

## Technology Stack

* **Backend**: Java 21, Spring Boot 3.5
* **Database**: PostgreSQL, Redis (containerized with Docker)
* **Frontend**: HTML, CSS, JavaScript
* **QR Code**: ZXing library
* **Containerization**: Docker & Docker Compose

---

## Deployment

To run the application using Docker Compose, follow these steps:

### 1. Clone the Repository

```bash
git clone https://github.com/amolsr/UrlShortener.git
cd urlShortener
```

### 3. Start the Application and Database

```bash
docker compose up -d
```

* This command will start both the backend service and the PostgreSQL database automatically.
* The backend will be accessible at: `http://localhost:8080`

### Notes

* QR codes fetch the current short link dynamically from the backend.
* Links are stored in PostgreSQL and tracked for clicks.
* Make sure your device can access the backend if scanning QR codes from another device. Use your machine’s IP address in the URL if needed.

