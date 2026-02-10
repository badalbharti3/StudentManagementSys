# Quick Start Guide - JWT Token Usage

## 🚀 How to Use JWT Tokens in Your Frontend

### Step 1: Start Your Backend
```bash
cd C:\Users\Dell\Downloads\Day3SMS\Day3SMS
mvnw spring-boot:run
```

### Step 2: Open Frontend Files
Navigate to the `Frontend` folder and open these files in your browser:
- `login.html` - Login page
- `register.html` - Registration page  
- `index.html` - Student list (requires authentication)
- `addStudent.html` - Add student form (requires authentication)

## 📝 Complete User Flow

### First Time User (Registration)
1. Open `register.html`
2. Enter:
   - Username
   - Email
   - Password
   - Confirm Password
3. Click "Register"
4. ✅ Token is automatically saved and you're redirected to student list

### Returning User (Login)
1. Open `login.html`
2. Enter:
   - Email
   - Password
3. Click "Login"
4. ✅ Token is automatically saved and you're redirected to student list

### Working with Students (Protected Actions)
All these actions automatically use your saved token:

**View Students:**
- Just open `index.html` - students load automatically

**Add Student:**
- Click "Add Student" button
- Fill in: ID, Name, Age, Email
- Click "Submit"
- ✅ Token is sent automatically in the request

**Update Student:**
- Click "Update" button on any student
- Modify fields
- Click "Update"
- ✅ Token is sent automatically in the request

**Delete Student:**
- Click "Delete" button on any student
- Confirm deletion
- ✅ Token is sent automatically in the request

### Logout
- Click "Logout" button in top-right corner
- Token is removed and you're redirected to login page

## 🔑 How Tokens Work Behind the Scenes

### When You Login/Register:
```javascript
// Backend sends this response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}

// Frontend automatically stores it:
localStorage.setItem('authToken', data.token);
```

### When You Make API Calls:
```javascript
// Frontend automatically adds token to every request:
fetch("http://localhost:8080/students", {
    headers: {
        'Authorization': `Bearer ${localStorage.getItem('authToken')}`
    }
})
```

### When You Logout:
```javascript
// Frontend removes token:
localStorage.removeItem('authToken');
localStorage.removeItem('userEmail');
```

## 🛡️ Security Features Implemented

✅ **Automatic Token Injection:** Every API call includes your token
✅ **Login Required:** Can't access student pages without logging in
✅ **Auto-Redirect:** Sends you to login if token is missing
✅ **Secure Storage:** Token saved in browser's localStorage
✅ **Easy Logout:** One-click to clear token and logout

## 🔍 Testing Your Token

### View Your Token
Open browser console (F12) and type:
```javascript
console.log(localStorage.getItem('authToken'));
```

### Test Token Removal
```javascript
localStorage.removeItem('authToken');
// Now try to access index.html - you'll be redirected to login
```

### Check if Logged In
```javascript
if (localStorage.getItem('authToken')) {
    console.log('You are logged in!');
} else {
    console.log('You are NOT logged in');
}
```

## 📋 API Endpoints Reference

### Public Endpoints (No Token Required)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/login` | Login and get token |
| POST | `/auth/register` | Register and get token |

### Protected Endpoints (Token Required)
| Method | Endpoint | Description | Auto-Token? |
|--------|----------|-------------|-------------|
| GET | `/students` | Get all students | ✅ Yes |
| POST | `/students` | Add new student | ✅ Yes |
| PATCH | `/students/{id}` | Update student | ✅ Yes |
| DELETE | `/students/{id}` | Delete student | ✅ Yes |

## 🎯 Key Points to Remember

1. **Token is Automatic:** You don't manually copy/paste tokens - it's handled automatically
2. **Login Once:** Token persists until you logout or close browser
3. **No Token = No Access:** Protected pages redirect you to login
4. **Logout Clears Token:** Must login again after logout
5. **Token in Header:** Always sent as `Authorization: Bearer <token>`

## 🐛 Troubleshooting

### "Please login first" alert
**Cause:** No token in localStorage  
**Solution:** Go to login.html and login again

### 401 Unauthorized error
**Cause:** Token is invalid or expired  
**Solution:** Logout and login again

### CORS errors
**Cause:** Backend not allowing requests  
**Solution:** Already fixed with `@CrossOrigin` annotation

### Token not saving
**Cause:** Browser privacy settings  
**Solution:** Check if localStorage is enabled

## 💡 What You DON'T Need to Do

❌ Copy/paste tokens manually  
❌ Add tokens to forms  
❌ Remember to send tokens  
❌ Manually check authentication  

## ✅ What Happens Automatically

✅ Token storage after login/register  
✅ Token retrieval for API calls  
✅ Token inclusion in request headers  
✅ Authentication checks on page load  
✅ Redirects when not authenticated  

## 🎓 Summary

**The system handles ALL token management for you!**

1. **Login/Register** → Token is saved automatically
2. **Use the app** → Token is sent automatically with every request
3. **Logout** → Token is removed automatically

**You just use the application normally - the JWT authentication works invisibly in the background!**

