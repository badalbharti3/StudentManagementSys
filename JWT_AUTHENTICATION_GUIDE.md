# JWT Token Authentication Guide

## Overview
This guide explains how JWT (JSON Web Token) authentication is implemented in your Student Management System, connecting the backend Spring Boot API with the frontend HTML pages.

## How It Works

### 1. Backend Token Generation
When a user logs in or registers, the backend generates a JWT token:

**Endpoints:**
- `POST /auth/login` - Login with email and password
- `POST /auth/register` - Register a new user

**Response Format:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 2. Frontend Token Storage
The frontend receives the token and stores it in browser's `localStorage`:

```javascript
localStorage.setItem('authToken', data.token);
```

**Why localStorage?**
- Persists across browser sessions
- Accessible from JavaScript
- Survives page refreshes

### 3. Using Token in API Requests
For all protected endpoints, the token is sent in the `Authorization` header:

```javascript
headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
}
```

**Format:** `Bearer <token>`

### 4. Backend Token Validation
The backend verifies the token on each request:
- Extracts token from `Authorization` header
- Validates signature and expiration
- Returns 401 Unauthorized if invalid

## File-by-File Breakdown

### Frontend Files

#### login.html
**Purpose:** User login page

**Key Features:**
1. Accepts email and password
2. Sends POST request to `/auth/login`
3. On success:
   - Stores token: `localStorage.setItem('authToken', data.token)`
   - Redirects to index.html
4. On failure: Shows error message
5. Auto-redirects if already logged in

**Code Snippet:**
```javascript
fetch("http://localhost:8080/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password })
})
.then(response => response.json())
.then(data => {
    localStorage.setItem('authToken', data.token);
    window.location.href = "index.html";
});
```

#### register.html
**Purpose:** New user registration

**Key Features:**
1. Accepts username, email, password, and confirmation
2. Validates passwords match
3. Sends POST request to `/auth/register`
4. On success: Stores token and redirects
5. Validates minimum password length (6 characters)

#### index.html (Student List)
**Purpose:** Main dashboard showing all students

**Key Features:**
1. **Authentication Check:** Redirects to login if no token
```javascript
window.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('authToken');
    if (!token) {
        window.location.href = 'login.html';
    }
});
```

2. **Logout Function:**
```javascript
function logout() {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userEmail');
    window.location.href = 'login.html';
}
```

3. **Helper Function for Auth Headers:**
```javascript
function getAuthHeaders() {
    const token = localStorage.getItem('authToken');
    return {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
    };
}
```

4. **Protected API Calls:**
   - GET `/students` - Fetch all students
   - PATCH `/students/{id}` - Update student
   - DELETE `/students/{id}` - Delete student

All include: `headers: getAuthHeaders()`

#### addStudent.html
**Purpose:** Add new student form

**Key Features:**
1. Authentication check on page load
2. POST request to `/students` with token
3. Redirects to index.html on success

### Backend Files

#### AuthController.java
**Endpoints:**
- `POST /auth/login` - Returns JWT token
- `POST /auth/register` - Returns JWT token

**CORS:** `@CrossOrigin(origins = "*")` - Allows requests from any origin

#### StudentController.java
**Protected Endpoints:**
- `GET /students` - List all students
- `POST /students` - Add student
- `PATCH /students/{id}` - Update student
- `DELETE /students/{id}` - Delete student

**Token Validation:**
```java
private void checkToken(String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        throw new RuntimeException("Unauthorized! Token not provided!");
    }
    String token = authHeader.substring(7);
    jwtUtil.validateTokenAndGetEmail(token);
}
```

Each method receives: `@RequestHeader("Authorization") String authHeader`

#### JwtUtil.java
**Responsibilities:**
- Generate JWT tokens
- Validate tokens
- Extract user information from tokens

## Authentication Flow Diagram

```
1. User enters credentials
   ↓
2. Frontend sends to /auth/login
   ↓
3. Backend validates and generates JWT
   ↓
4. Frontend receives token
   ↓
5. Frontend stores in localStorage
   ↓
6. Future requests include token in header
   ↓
7. Backend validates token on each request
   ↓
8. Backend processes request if valid
```

## Testing the Implementation

### Step 1: Register a New User
1. Open `register.html`
2. Fill in username, email, password
3. Click "Register"
4. Should redirect to `index.html`

### Step 2: View Students
1. Should see student list (if authenticated)
2. Check browser console: token should be in localStorage
   ```javascript
   console.log(localStorage.getItem('authToken'));
   ```

### Step 3: Add a Student
1. Click "Add Student" button
2. Fill in form
3. Submit
4. Should redirect to student list

### Step 4: Logout and Login
1. Click "Logout" button
2. Should redirect to `login.html`
3. Enter credentials
4. Should redirect to `index.html`

### Step 5: Test Token Expiration
1. Manually remove token from localStorage:
   ```javascript
   localStorage.removeItem('authToken');
   ```
2. Try to access `index.html`
3. Should redirect to `login.html`

## Common Issues and Solutions

### Issue 1: CORS Errors
**Problem:** Browser blocks requests
**Solution:** `@CrossOrigin(origins = "*")` added to controllers

### Issue 2: 401 Unauthorized
**Problem:** Token not being sent
**Solution:** Check `getAuthHeaders()` function is used in fetch calls

### Issue 3: Token Expired
**Problem:** JWT has expiration time
**Solution:** User must login again

### Issue 4: Token Not Persisting
**Problem:** localStorage might be disabled
**Solution:** Check browser privacy settings

## Security Best Practices

### Current Implementation
✅ Tokens stored in localStorage
✅ HTTPS recommended for production
✅ Authorization header used
✅ Token validated on each request

### Recommendations for Production
- Set token expiration time (e.g., 24 hours)
- Implement refresh tokens
- Use HTTPS only
- Add CSRF protection
- Implement rate limiting
- Add token refresh mechanism
- Store sensitive data on backend only

## API Request Examples

### Login Request
```javascript
fetch("http://localhost:8080/auth/login", {
    method: "POST",
    headers: {
        "Content-Type": "application/json"
    },
    body: JSON.stringify({
        email: "user@example.com",
        password: "password123"
    })
})
```

### Authenticated Request (Get Students)
```javascript
fetch("http://localhost:8080/students", {
    headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`
    }
})
```

### Add Student with Token
```javascript
fetch("http://localhost:8080/students", {
    method: "POST",
    headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('authToken')}`
    },
    body: JSON.stringify({
        id: "S001",
        name: "John Doe",
        age: 20,
        email: "john@example.com"
    })
})
```

## Token Structure (JWT)

A JWT token consists of three parts separated by dots:
```
header.payload.signature
```

**Example:**
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNTE2MjM5MDIyfQ.
SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

**Decoded:**
- Header: `{"alg":"HS256","typ":"JWT"}`
- Payload: `{"sub":"user@example.com","iat":1516239022}`
- Signature: Verified using secret key

## Conclusion

Your application now has complete JWT authentication:
- ✅ Users can register and login
- ✅ Tokens are generated and stored
- ✅ All API calls include authentication
- ✅ Protected routes check for valid tokens
- ✅ Users can logout and clear tokens

**Next Steps:**
1. Test all functionality
2. Add token expiration handling
3. Implement refresh tokens
4. Add user profile management
5. Deploy with HTTPS enabled

