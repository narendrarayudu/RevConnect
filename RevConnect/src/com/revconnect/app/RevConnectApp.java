package com.revconnect.app;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;

import java.sql.*;

import java.util.*;

public class RevConnectApp {

    static Scanner sc = new Scanner(System.in);

    static User currentUser = null;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== REVCONNECT ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> register();
                case 2 -> login();
                case 3 -> { System.out.println("👋 Goodbye!"); System.exit(0); }
                default -> System.out.println("❌ Invalid choice");
            }
        }
    }

    static int getIntInput() {
        while (!sc.hasNextInt()) {
            System.out.println("❌ Please enter a valid number");
            sc.next();
        }
        return sc.nextInt();
    }

    // ================= REGISTER =================
    static void register() {
        sc.nextLine();
        System.out.print("Email: "); String email = sc.nextLine();
        System.out.print("Username: "); String username = sc.nextLine();
        System.out.print("Password: "); String password = sc.nextLine();
        System.out.print("User Type (PERSONAL/CREATOR/BUSINESS): "); String userType = sc.nextLine();

        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO users(email, username, password, user_type) VALUES(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, userType);
            int res = ps.executeUpdate();
            System.out.println(res > 0 ? "✅ Registration successful!" : "❌ Registration failed!");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // ================= LOGIN =================
    static void login() {
        sc.nextLine();
        System.out.print("Username: "); String username = sc.nextLine();
        System.out.print("Password: "); String password = sc.nextLine();

        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                currentUser = new User();
                currentUser.setUserId(rs.getInt("user_id"));
                currentUser.setUsername(rs.getString("username"));
                currentUser.setEmail(rs.getString("email"));
                currentUser.setUserType(rs.getString("user_type"));
                currentUser.setFullName(rs.getString("full_name"));
                currentUser.setBio(rs.getString("bio"));
                currentUser.setLocation(rs.getString("location"));
                currentUser.setWebsite(rs.getString("website"));
                System.out.println("✅ Login successful!");
                userMenu();
            } else {
                System.out.println("❌ Invalid credentials");
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // ================= USER MENU =================
    static void userMenu() {
        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Create Post");
            System.out.println("2. View Feed");
            System.out.println("3. Edit Post");
            System.out.println("4. Delete Post");
            System.out.println("5. Like a Post");
            System.out.println("6. Comment on a Post");
            System.out.println("7. Send Connection Request");
            System.out.println("8. View Notifications");
            System.out.println("9. Edit Profile");
            System.out.println("10. View Profile");
            System.out.println("11. Search Users");
            System.out.println("12. Logout");
            System.out.print("Enter choice: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> createPost();
                case 2 -> viewFeed();
                case 3 -> editPost();
                case 4 -> deletePost();
                case 5 -> likePost();
                case 6 -> commentOnPost();
                case 7 -> sendConnectionRequest();
                case 8 -> viewNotifications();
                case 9 -> editProfile();
                case 10 -> viewProfile();
                case 11 -> searchUsers();
                case 12 -> { currentUser = null; System.out.println("👋 Logged out"); return; }
                default -> System.out.println("❌ Invalid option");
            }
        }
    }

    // ================= POSTS =================
    static void createPost() {
        sc.nextLine();
        System.out.print("Enter post content: ");
        String content = sc.nextLine();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO posts(user_id, content) VALUES(?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, currentUser.getUserId());
            ps.setString(2, content);
            if (ps.executeUpdate() > 0) System.out.println("✅ Post created");
            else System.out.println("❌ Failed to create post");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    static void viewFeed() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM posts WHERE user_id=? OR user_id IN (SELECT friend_id FROM connections WHERE user_id=?) ORDER BY created_at DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, currentUser.getUserId());
            ps.setInt(2, currentUser.getUserId());
            ResultSet rs = ps.executeQuery();
            System.out.println("\n--- FEED ---");
            while (rs.next()) {
                System.out.println(rs.getInt("post_id") + " | User " + rs.getInt("user_id") + " | " + rs.getString("content") + " | " + rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    static void editPost() {
        System.out.print("Enter Post ID to edit: ");
        int postId = getIntInput();
        sc.nextLine();
        System.out.print("New Content: ");
        String content = sc.nextLine();

        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE posts SET content=? WHERE post_id=? AND user_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, content);
            ps.setInt(2, postId);
            ps.setInt(3, currentUser.getUserId());
            if (ps.executeUpdate() > 0) System.out.println("✅ Post updated");
            else System.out.println("❌ Failed to update post");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    static void deletePost() {
        System.out.print("Enter Post ID to delete: ");
        int postId = getIntInput();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "DELETE FROM posts WHERE post_id=? AND user_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, postId);
            ps.setInt(2, currentUser.getUserId());
            if (ps.executeUpdate() > 0) System.out.println("✅ Post deleted");
            else System.out.println("❌ Failed to delete post");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    static int getPostOwner(int postId) {
        int ownerId = -1;
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT user_id FROM posts WHERE post_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ownerId = rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ownerId;
    }

     // ================= LIKES =================
    static void likePost() {
    	 System.out.print("Enter Post ID to like: ");
    	    int postId = sc.nextInt();
    	    sc.nextLine();
        int postOwnerId = getPostOwner(postId);

        if(postOwnerId == -1) {
            System.out.println("❌ Post not found");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO likes(post_id, user_id) VALUES(?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, postId);
            ps.setInt(2, currentUser.getUserId());
            ps.executeUpdate();

            // Send notification
            sql = "INSERT INTO notifications(user_id, message) VALUES(?, ?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, postOwnerId);
            ps.setString(2, currentUser.getUsername() + " liked your post");
            ps.executeUpdate();

            System.out.println("✅ Post liked and notification sent");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= COMMENTS =================
    static void commentOnPost() {
    	System.out.print("Enter Post ID to Comment: ");
        int postId = sc.nextInt();
        sc.nextLine();
        System.out.print("Comment: ");
        String content = sc.nextLine();

        int postOwnerId = getPostOwner(postId); // fetch owner from posts table

        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO comments(post_id, user_id, content) VALUES(?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, postId);
            ps.setInt(2, currentUser.getUserId());
            ps.setString(3, content);
            ps.executeUpdate();

            // Send notification
            sql = "INSERT INTO notifications(user_id, message) VALUES(?, ?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, postOwnerId);
            ps.setString(2, currentUser.getUsername() + " commented on your post");
            ps.executeUpdate();

            System.out.println("✅ Comment added and notification sent");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    // ================= CONNECTIONS =================
    static void sendConnectionRequest() {
        System.out.print("Enter User ID to connect: ");
        int friendId = getIntInput();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO connections(user_id, friend_id) VALUES(?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, currentUser.getUserId());
            ps.setInt(2, friendId);
            if(ps.executeUpdate() > 0) System.out.println("✅ Connection request sent!");
            else System.out.println("❌ Could not send request");
        } catch(SQLException e){ e.printStackTrace(); }
    }
    
    
    // ================= NOTIFICATIONS =================
    static void viewNotifications() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT notification_id, message, is_read, created_at FROM notifications WHERE user_id=? ORDER BY created_at DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, currentUser.getUserId());
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- NOTIFICATIONS ---");
            boolean hasNotifications = false;
            while (rs.next()) {
                hasNotifications = true;
                int id = rs.getInt("notification_id");
                String message = rs.getString("message");
                boolean read = rs.getBoolean("is_read");
                Timestamp time = rs.getTimestamp("created_at");
                System.out.println(id + " | " + message + " | Read: " + read + " | " + time);
            }
            if (!hasNotifications) {
                System.out.println("No notifications yet.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    static void sendNotification(int userId, String message) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO notifications(user_id, message) VALUES(?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, message);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= PROFILE =================
    static void editProfile() {
        sc.nextLine();
        try (Connection con = DBConnection.getConnection()) {
            System.out.print("Full Name: "); String fullName = sc.nextLine();
            System.out.print("Bio: "); String bio = sc.nextLine();
            System.out.print("Location: "); String location = sc.nextLine();
            System.out.print("Website: "); String website = sc.nextLine();

            String sql = "UPDATE users SET full_name=?, bio=?, location=?, website=? WHERE user_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, fullName);
            ps.setString(2, bio);
            ps.setString(3, location);
            ps.setString(4, website);
            ps.setInt(5, currentUser.getUserId());
            if (ps.executeUpdate() > 0) System.out.println("✅ Profile updated");
            else System.out.println("❌ Failed to update profile");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    static void viewProfile() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE user_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, currentUser.getUserId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("\n--- PROFILE ---");
                System.out.println("Full Name: " + rs.getString("full_name"));
                System.out.println("Username: " + rs.getString("username"));
                System.out.println("Bio: " + rs.getString("bio"));
                System.out.println("Location: " + rs.getString("location"));
                System.out.println("Website: " + rs.getString("website"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    static void searchUsers() {
        sc.nextLine();
        System.out.print("Enter username to search: ");
        String query = sc.nextLine();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + query + "%");
            ResultSet rs = ps.executeQuery();
            System.out.println("\n--- USERS ---");
            while (rs.next()) {
                System.out.println(rs.getInt("user_id") + " | " + rs.getString("username") + " | " + rs.getString("full_name"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // ================= USER MODEL =================
    static class User {
        private int userId;
        private String username, email, userType;
        private String fullName, bio, location, website;

        public int getUserId() { return userId; }
        public void setUserId(int userId) { this.userId = userId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getUserType() { return userType; }
        public void setUserType(String userType) { this.userType = userType; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getBio() { return bio; }
        public void setBio(String bio) { this.bio = bio; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public String getWebsite() { return website; }
        public void setWebsite(String website) { this.website = website; }
    }

    // ================= DB CONNECTION =================
    static class DBConnection {
        static final String URL = "jdbc:mysql://localhost:3306/revconnect";
        static final String USER = "root";
        static final String PASS = "123456789";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASS);
        }
    }
}