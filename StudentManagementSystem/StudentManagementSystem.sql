CREATE DATABASE IF NOT EXISTS student_management_db;
USE student_management_db;

-- 1. Students table
CREATE TABLE IF NOT EXISTS students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    student_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2. Courses table
CREATE TABLE IF NOT EXISTS courses (
    course_id INT PRIMARY KEY AUTO_INCREMENT,
    course_name VARCHAR(100) NOT NULL,
    duration VARCHAR(50),
    fees DECIMAL(10,2) DEFAULT 0.00
) ENGINE=InnoDB;

-- 3. Enrollments table
CREATE TABLE IF NOT EXISTS enrollments (
    enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    enrollment_date DATE NOT NULL,
    UNIQUE (student_id, course_id),
    FOREIGN KEY (student_id)
        REFERENCES students(student_id)
        ON DELETE CASCADE,
    FOREIGN KEY (course_id)
        REFERENCES courses(course_id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- 4. Attendance table
CREATE TABLE IF NOT EXISTS attendance (
    attendance_id INT PRIMARY KEY AUTO_INCREMENT,
    enrollment_id INT NOT NULL,
    attendance_date DATE NOT NULL,
    attendance_status ENUM('Present', 'Absent', 'Late') NOT NULL,
    UNIQUE (enrollment_id, attendance_date),
    FOREIGN KEY (enrollment_id)
        REFERENCES enrollments(enrollment_id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- 5. Marks table
CREATE TABLE IF NOT EXISTS marks (
    mark_id INT PRIMARY KEY AUTO_INCREMENT,
    enrollment_id INT NOT NULL UNIQUE,
    marks_obtained DECIMAL(5,2) NOT NULL,
    max_marks DECIMAL(5,2) NOT NULL DEFAULT 100.00,
    exam_date DATE,
    FOREIGN KEY (enrollment_id)
        REFERENCES enrollments(enrollment_id)
        ON DELETE CASCADE,
    CHECK (marks_obtained >= 0),
    CHECK (max_marks > 0),
    CHECK (marks_obtained <= max_marks)
) ENGINE=InnoDB;

-- 6. Insert main student names
INSERT IGNORE INTO students
    (student_name, email, phone)
VALUES
    ('Abhinay', 'abhinay@example.com', '9876500001'),
    ('Vijay', 'vijay@example.com', '9876500002'),
    ('Vikram', 'vikram@example.com', '9876500003'),
    ('Krish', 'krish@example.com', '9876500004'),
    ('Prakash', 'prakash@example.com', '9876500005'),
    ('Kamitha', 'kamitha@example.com', '9876500006');

-- 7. Verify all 5 tables
SHOW TABLES;

-- 8. Verify students and other records
SELECT * FROM students;
SELECT * FROM courses;
SELECT * FROM enrollments;
SELECT * FROM attendance;
SELECT * FROM marks;