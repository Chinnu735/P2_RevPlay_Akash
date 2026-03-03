# RevPlay — Premium Music Streaming Platform

RevPlay is a modern, full-stack music streaming and podcast platform designed for both listeners and artists. It features a sleek, dark-themed UI with a focus on visual excellence, seamless audio playback, and comprehensive artist analytics.

---

## 🚀 Key Features

### 🎵 For Listeners
- **Seamless Playback**: Persistent audio player bar that follows you throughout the application.
- **Discovery**: Browse music by Genre, Artist, or Album.
- **Playlists**: Create, manage, and follow public or private playlists.
- **Podcasts**: Stream podcast episodes with a dedicated episode management view.
- **Favorites & History**: Save your favorite tracks and track your listening journey.
- **Profile Management**: Customize your profile with an avatar, bio, and real-time updates.

### 🎨 For Artists
- **Artist Dashboard**: Comprehensive analytics (Plays, Favorites, Listeners) shown in real-time.
- **Content Upload**: Upload songs, create albums, and manage podcast series.
- **Social Integration**: Link your Instagram and YouTube profiles directly.
- **Top Fans**: See your most active listeners and popular releases at a glance.

---

## 🛠️ Technology Stack

**Backend:**
- **Java 21** & **Spring Boot 3.x**
- **Spring Security** (Session-based Authentication)
- **Spring Data JPA** (Hibernate)
- **Oracle SQL** (Enterprise-grade database)
- **Maven** (Build & Dependency Management)

**Frontend:**
- **Vanilla JavaScript** (Modern ES6+, AJAX)
- **Vanilla CSS** (Custom Design System, Glassmorphism, Micro-animations)
- **Thymeleaf** (Server-side Templating)
- **HTML5 Audio API** (Core Playback Engine)

**Testing:**
- **JUnit 5** & **Mockito** (Full coverage for Controller and Service layers)

---

## 📂 Project Structure

```text
RevPlay/
├── src/main/java/com/revplay/app/
│   ├── config/         # Security, Web, and App configurations
│   ├── controller/     # REST Controllers for API endpoints
│   ├── dto/           # Data Transfer Objects
│   ├── entity/         # JPA Entities
│   ├── repository/     # Data Access Layer
│   └── service/        # Business Logic Layer
├── src/main/resources/
│   ├── static/         # CSS, JS, and Images
│   ├── templates/      # Thymeleaf HTML views
│   └── application.properties
├── uploads/            # Local storage for music, podcasts, and avatars
└── pom.xml             # Project configuration and dependencies
```

---

## ⚙️ Getting Started

### Prerequisites
- JDK 21+
- Apache Maven
- Oracle Database (configured in `application.properties`)

### Installation & Run
1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/RevPlay.git
   cd RevPlay
   ```

2. **Configure Database**:
   Update `src/main/resources/application.properties` with your Oracle DB credentials.

3. **Build and Run**:
   ```bash
   mvn spring-boot:run
   ```

4. **Access the App**:
   Open your browser and navigate to `http://localhost:8088`.

---

## 🧪 Testing
Run the suite of JUnit and Mockito tests for the service and controller layers:
```bash
mvn test
```

---

## ✨ Developed with Excellence
RevPlay focuses on a **premium user experience**, utilizing modern design principles like vibrant accents, consistent spacing, and dynamic UI updates without the overhead of heavy frameworks.
