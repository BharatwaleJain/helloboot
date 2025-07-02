# Project Backend Setup

### 1. Clone the Repository

```
git clone https://github.com/BharatwaleJain/helloboot
```

### 2. Go to the Project Directory

```
cd helloboot
```

### 3. Configure Environment Variables

Create a file named `.env` in the project root and add:

```
DB_URL=db-url
DB_USER=user-name
DB_PASS=password
```

### 4. Compiles the Project

```
mvn clean install -DskipTests
```

### 5. Start the Backend Server

```
mvn spring--boot:run
```

### 6. API will start (by default) in Your Browser

```
http://localhost:3000
```

Once deployed, update `VITE_API_BASE` in the frontend `.env` file with your backend API URL.