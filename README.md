# Java React JAR Project

A Java Spring Boot application with an embedded React 17 frontend, all packaged into a single executable JAR file.

## 🚀 Features

- **Java 11** backend with **Spring Boot**
- **React 17** frontend with routing and modern UI
- **Single JAR deployment** - no separate frontend/backend
- **Automatic build process** with Maven and frontend-maven-plugin
- **REST API integration** between React and Spring Boot
- **Modern UI** with responsive design and glassmorphism effects

## 📦 Project Structure

```
├── pom.xml                                    # Maven build configuration
├── src/
│   ├── main/
│   │   ├── java/com/example/javareactjar/    # Spring Boot application
│   │   │   ├── JavaReactJarApplication.java  # Main application class
│   │   │   ├── controller/ReactController.java # API endpoints & routing
│   │   │   └── config/WebConfig.java         # Static resource configuration
│   │   ├── resources/
│   │   │   └── application.properties        # Spring Boot configuration
│   │   └── webapp/                           # React frontend source
│   │       ├── package.json                  # React 17 dependencies
│   │       ├── webpack.config.js             # Webpack build configuration
│   │       ├── public/index.html             # HTML template
│   │       └── src/                          # React components and styles
└── README.md                                 # This file
```

## 🛠️ Build Requirements

- **Java 11** or higher
- **Maven 3.6+**
- **Internet connection** (for downloading Node.js and npm during build)

## 🏗️ Build Process

The Maven build automatically:

1. Downloads and installs Node.js and npm
2. Installs React dependencies (`npm install`)
3. Builds the React production bundle (`npm run build`)
4. Copies React build files to Spring Boot static resources
5. Compiles Java code and packages everything into a JAR

## 🚀 How to Build and Run

### 1. Build the Project
```bash
mvn clean package
```

This will create `target/java-react-jar-1.0.0.jar`

### 2. Run the Application
```bash
java -jar target/java-react-jar-1.0.0.jar
```

### 3. Access the Application
Open your browser and go to: http://localhost:8080

## 🌐 API Endpoints

- `GET /` - Serves the React application
- `GET /api/health` - Application health check
- `GET /api/info` - Application and technology information
- `GET /*` - SPA routing (forwards to React app)

## 🧪 Development

### Frontend Development
To work on the React frontend separately:

```bash
cd src/main/webapp
npm install
npm start
```

This will start the React dev server on http://localhost:3000 with proxy to the Java backend.

### Backend Development
Run the Spring Boot application:

```bash
mvn spring-boot:run
```

The backend will be available at http://localhost:8080

## 📋 Technologies Used

### Backend
- Java 11
- Spring Boot 2.7.18
- Maven 3.x

### Frontend  
- React 17.0.2
- React Router DOM 5.3.4
- Axios for HTTP requests
- Webpack 5 for bundling
- Babel for JavaScript transpilation

### Build Tools
- Maven Frontend Plugin for Node.js/npm integration
- Webpack for React bundling
- Maven Resources Plugin for file copying

## 🎯 Key Features Demonstrated

1. **Embedded Static Resources**: React build files are embedded in the JAR
2. **SPA Routing**: React Router works with Spring Boot routing
3. **API Integration**: React frontend communicates with Spring Boot backend
4. **Automatic Build**: Single `mvn package` command builds everything
5. **Production Ready**: Optimized React build with chunking and caching

## 🔧 Customization

- Modify React components in `src/main/webapp/src/`
- Add new API endpoints in `ReactController.java`
- Update styling in `src/main/webapp/src/App.css`
- Configure build process in `pom.xml` and `webpack.config.js`

## 📄 License

This project is provided as-is for educational and demonstration purposes.
