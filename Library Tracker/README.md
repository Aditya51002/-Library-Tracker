# 📚 Library Management System

A modern, feature-rich Library Management System built with Java Swing and MySQL, featuring a beautiful gradient UI and comprehensive book/user management capabilities.

## ✨ Features

### 🎨 Modern User Interface
- **Gradient Background Design** - Beautiful blue gradient themes throughout the application
- **Professional Styling** - Hover effects, rounded borders, and modern button designs
- **Responsive Layout** - Clean, organized interface with intuitive navigation
- **Emoji Icons** - Visual indicators for better user experience

### 📖 Book Management
- **Add Books** - Complete book entry with publisher, ISBN, category, and description
- **View Books** - Advanced table view with search and filtering capabilities
- **Book Categories** - Fiction, Technology, Education, and more
- **Book Status Tracking** - Available/Issued status management
- **Advanced Search** - Search by title, author, category, or ISBN

### 👥 User Management
- **Add Users** - Complete user registration with contact details
- **User Roles** - Admin, Librarian, and regular User roles
- **User Profiles** - Store email, phone, and address information
- **Role-based Access** - Different permissions for different user types

### 📋 Issue/Return System
- **Issue Books** - Track book lending with issue dates
- **Return Books** - Process book returns with return dates
- **Issue History** - View all issued books and their status
- **User History** - Track individual user borrowing history

### 🔧 Admin Features
- **Dashboard** - Modern 3x3 grid layout with quick access to all features
- **Database Management** - Built-in database setup and initialization
- **User Management** - Add, view, and manage all system users
- **System Reports** - View comprehensive book and user statistics

## 🚀 Installation & Setup

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- XAMPP (for MySQL server)
- MySQL Connector/J (included in lib folder)

### Database Setup
1. **Start XAMPP**
   - Open XAMPP Control Panel
   - Start Apache and MySQL services

2. **Access phpMyAdmin**
   - Open browser and go to `http://localhost/phpmyadmin`
   - Create a new database named `library`

3. **Initialize Database**
   - Run the application
   - Click "Setup Database" button
   - Confirm database initialization

### Running the Application

#### Method 1: Using Batch File (Recommended)
```bash
# Double-click run.bat file
# OR run in command prompt:
run.bat
```

#### Method 2: Manual Command
```bash
cd "path/to/libmangemt"
java -cp ".;lib/mysql-connector-j-9.3.0.jar;lib/rs2xml.jar" Main
```

## 🔐 Default Login Credentials

```
Username: Aditya
Password: hello
Role: Admin/Librarian Access
```

## 📁 Project Structure

```
libmangemt/
├── lib/                          # Library dependencies
│   ├── mysql-connector-j-9.3.0.jar
│   └── rs2xml.jar
├── Sql/                          # Database scripts
│   └── library_setup.sql
├── *.java                        # Java source files
├── *.class                       # Compiled Java classes
├── run.bat                       # Quick start batch file
└── README.md                     # This file
```

## 🎯 Main Components

### Core Classes
- **Main.java** - Application entry point
- **Login.java** - Authentication system with modern UI
- **Connect.java** - Database connection management
- **Create.java** - Database initialization and setup

### Admin Features
- **AdminMenu.java** - Enhanced admin dashboard
- **AddBook.java** - Professional book addition form
- **ViewBook.java** - Advanced book viewing with search
- **AddUser.java** - Complete user registration
- **ViewUser.java** - User management interface

### Book Operations
- **IssueBook.java** - Book lending system
- **ReturnBook.java** - Book return processing
- **ViewIssuedBooks.java** - Issue tracking and history

### User Interface
- **UserMenu.java** - User dashboard for regular users

## 🗃️ Database Schema

### USERS Table
```sql
- ID (Primary Key, Auto Increment)
- USERNAME (Unique, Not Null)
- PASSWORD (Not Null)
- EMAIL
- PHONE
- ROLE (admin/librarian/user)
- ADDRESS
```

### BOOKS Table
```sql
- ID (Primary Key, Auto Increment)
- NAME (Not Null)
- AUTHOR (Not Null)
- PUBLISHER
- ISBN
- CATEGORY
- DESCRIPTION
- STATUS (available/issued)
```

### ISSUE Table
```sql
- ID (Primary Key, Auto Increment)
- BOOKID (Foreign Key → BOOKS.ID)
- USERID (Foreign Key → USERS.ID)
- ISSUEDATE (Not Null)
- RETURNDATE
```

## 🎨 UI Features

### Visual Enhancements
- **Gradient Backgrounds** - Blue to light blue gradients
- **Hover Effects** - Interactive button highlighting
- **Professional Typography** - Arial font with proper sizing
- **Color Scheme** - Consistent blue and white theme
- **Modern Buttons** - Rounded corners with shadow effects

### User Experience
- **Intuitive Navigation** - Clear button labels with emojis
- **Form Validation** - Input validation with error messages
- **Success Notifications** - Confirmation dialogs for actions
- **Search Functionality** - Real-time search in book listings
- **Category Filtering** - Filter books by category

## 🔧 Configuration

### Database Connection
Default connection settings in `Connect.java`:
```java
URL: jdbc:mysql://localhost:3306/library
Username: root
Password: (empty)
```

### Customization
- **Colors**: Modify gradient colors in each UI class
- **Database**: Update connection string in `Connect.java`
- **Features**: Add new functionality by extending existing classes

## 🚨 Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Ensure XAMPP MySQL is running
   - Check connection credentials
   - Use "Setup Database" button to initialize

2. **ClassNotFoundException**
   - Ensure MySQL connector is in lib folder
   - Use the provided `run.bat` file
   - Check classpath settings

3. **Login Issues**
   - Use default credentials: Aditya/hello
   - Try "Setup Database" to reset user data
   - Check database user table

4. **UI Not Loading**
   - Ensure all .java files are compiled
   - Check for missing dependencies
   - Restart application

## 🔄 Recent Updates

### Version 2.0 Features
- ✅ Modern gradient UI design
- ✅ Enhanced book management with categories
- ✅ Complete user management system
- ✅ Advanced search and filtering
- ✅ Professional form layouts
- ✅ Improved database schema
- ✅ Role-based access control
- ✅ Issue/return tracking system

## 📞 Support

For issues or questions:
1. Check the troubleshooting section
2. Verify XAMPP and MySQL are running
3. Ensure all files are in the correct directory structure
4. Use the "Setup Database" feature for fresh installation

## 📄 License

This project is open source and available for educational purposes.

---

**Made with ❤️ for efficient library management**

🚀 **Quick Start**: Double-click `run.bat` → Click "Setup Database" → Login with Aditya/hello → Enjoy! 🎉
