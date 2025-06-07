CREATE DATABASE TO_DO_LIST_DB;
USE TO_DO_LIST_DB;

-- Tạo bảng USER
CREATE TABLE USERS (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    UserName VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(100) NOT NULL,
    FullName NVARCHAR(100) NOT NULL,
    Email VARCHAR(100) UNIQUE,
    SDT VARCHAR(20)
);

-- Tạo bảng TYPES_OF_EVENTS
CREATE TABLE TYPES_OF_EVENTS (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    TypeName NVARCHAR(100) NOT NULL,
    Note NVARCHAR(255),
    Color VARCHAR(20)
);

-- Tạo bảng EVENTS
CREATE TABLE EVENTS (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    User_ID INT NOT NULL,
    Title NVARCHAR(255) NOT NULL,
    Description NVARCHAR(255),
    
    IsAllDay BIT DEFAULT 0,
    IsImportant BIT DEFAULT 0,

    StartTime DATETIME NOT NULL,
    EndTime DATETIME NOT NULL,
    RemindTime DATETIME,

    Type_ID INT NOT NULL,
    Note NVARCHAR(255),

    -- Khóa ngoại
    FOREIGN KEY (User_ID) REFERENCES USERS(ID),
    FOREIGN KEY (Type_ID) REFERENCES TYPES_OF_EVENTS(ID)
);



-- Dữ liệu mẫu cho bảng USERS
INSERT INTO USERS (UserName, Password, FullName, Email, SDT) VALUES
('truong', '123', N'Phạm Xuân Trường', 'truongpham@gmail.com', '0909123456'),
('tranthib', 'pass456', N'Trần Thị B', 'thib@example.com', '0912345678'),
('lehongc', 'pass789', N'Lê Hồng C', 'hongc@example.com', '0987654321');

-- Dữ liệu mẫu cho bảng TYPES_OF_EVENTS
INSERT INTO TYPES_OF_EVENTS (TypeName, Note, Color) VALUES
(N'Cuộc họp', N'Cuộc họp công việc', 'Blue'),
(N'Cá nhân', N'Việc cá nhân cần làm', 'Green'),
(N'Kỷ niệm', N'Ngày đặc biệt, kỷ niệm', 'Red');

-- Dữ liệu mẫu cho bảng EVENTS
INSERT INTO EVENTS (User_ID, Title, Description, IsAllDay, IsImportant, StartTime, EndTime, RemindTime, Type_ID, Note) VALUES
(4, N'Họp dự án', N'Cuộc họp dự án phát triển phần mềm', 0, 1, '2025-06-10 09:00:00', '2025-06-10 10:00:00', '2025-06-10 08:45:00', 1, N'Chuẩn bị tài liệu'),
(4, N'Mua sắm', N'Mua đồ dùng cá nhân', 1, 0, '2025-06-12 00:00:00', '2025-06-12 23:59:59', NULL, 2, NULL),
(4, N'Sinh nhật bạn', N'Đi chơi mừng sinh nhật bạn thân', 1, 1, '2025-06-15 00:00:00', '2025-06-15 23:59:59', '2025-06-14 20:00:00', 3, N'Mua quà'),
(4, N'Giao bài tập', N'Hoàn thành bài tập lập trình', 0, 1, '2025-06-09 14:00:00', '2025-06-09 16:00:00', '2025-06-09 13:30:00', 2, NULL);

