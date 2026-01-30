package com.revconnect.app;

import java.util.Scanner;

import com.revconnect.model.User;
import com.revconnect.service.AuthService;
import com.revconnect.service.PostService;
import com.revconnect.service.LikeService;
import com.revconnect.service.CommentService;
import com.revconnect.service.ConnectionService;
import com.revconnect.service.NotificationService;

public class RevConnectApp {

    static Scanner sc = new Scanner(System.in);

    static AuthService authService = new AuthService();
    static PostService postService = new PostService();
    static LikeService likeService = new LikeService();
    static CommentService commentService = new CommentService();
    static ConnectionService connectionService = new ConnectionService();
    static NotificationService notificationService = new NotificationService();

    static User currentUser = null;

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n=== REVCONNECT ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> register();
                case 2 -> login();
                case 3 -> {
                    System.out.println("👋 Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("❌ Invalid choice");
            }
        }
    }

    // ================= REGISTER =================
    static void register() {
        sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        System.out.print("User Type (PERSONAL / CREATOR / BUSINESS): ");
        String userType = sc.nextLine();

        User user = new User(email, username, password, userType);

        if (authService.register(user)) {
            System.out.println("✅ Registration successful!");
        } else {
            System.out.println("❌ Registration failed!");
        }
    }

    // ================= LOGIN =================
    static void login() {
        sc.nextLine();

        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        currentUser = authService.login(username, password);

        if (currentUser != null) {
            System.out.println("✅ Login success!");
            userMenu();
        } else {
            System.out.println("❌ Invalid credentials");
        }
    }

    // ================= USER MENU =================
    static void userMenu() {
        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Create Post");
            System.out.println("2. View Feed");
            System.out.println("3. Like a Post");
            System.out.println("4. Comment on a Post");
            System.out.println("5. Send Connection Request");
            System.out.println("6. View Notifications");
            System.out.println("7. Logout");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> createPost();
                case 2 -> viewFeed();
                case 3 -> likePost();
                case 4 -> commentOnPost();
                case 5 -> sendConnectionRequest();
                case 6 -> viewNotifications();
                case 7 -> {
                    currentUser = null;
                    System.out.println("👋 Logged out");
                    return;
                }
                default -> System.out.println("❌ Invalid option");
            }
        }
    }

    // ================= POSTS =================
    static void createPost() {
        sc.nextLine();
        System.out.print("Enter post content: ");
        String content = sc.nextLine();

        if (postService.createPost(currentUser.getUserId(), content)) {
            System.out.println("✅ Post created");
        } else {
            System.out.println("❌ Failed to create post");
        }
    }

    static void viewFeed() {
        postService.getFeed().forEach(p ->
            System.out.println(p.getPostId() + " | User " + p.getUserId() + " : " + p.getContent())
        );
    }

    // ================= LIKES =================
    static void likePost() {
        System.out.print("Enter Post ID to like: ");
        int postId = sc.nextInt();

        if (likeService.likePost(currentUser.getUserId(), postId)) {
            int ownerId = postService.getPostOwner(postId);
            notificationService.sendNotification(
                ownerId,
                currentUser.getUsername() + " liked your post"
            );
            System.out.println("✅ Post liked");
        } else {
            System.out.println("❌ Like failed");
        }
    }

    // ================= COMMENTS =================
    static void commentOnPost() {
        System.out.print("Enter Post ID: ");
        int postId = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter comment: ");
        String content = sc.nextLine();

        if (commentService.addComment(currentUser.getUserId(), postId, content)) {
            int ownerId = postService.getPostOwner(postId);
            notificationService.sendNotification(
                ownerId,
                currentUser.getUsername() + " commented on your post"
            );
            System.out.println("✅ Comment added");
        } else {
            System.out.println("❌ Comment failed");
        }
    }

    // ================= CONNECTIONS =================
    static void sendConnectionRequest() {
        System.out.print("Enter User ID to connect: ");
        int friendId = sc.nextInt();

        if (connectionService.sendRequest(currentUser.getUserId(), friendId)) {
            notificationService.sendNotification(
                friendId,
                currentUser.getUsername() + " sent you a connection request"
            );
            System.out.println("✅ Connection request sent!");
        } else {
            System.out.println("❌ Failed to send request");
        }
    }

    // ================= NOTIFICATIONS =================
    static void viewNotifications() {
        System.out.println("\n--- NOTIFICATIONS ---");
        notificationService
            .getUserNotifications(currentUser.getUserId())
            .forEach(n ->
                System.out.println("- " + n.getMessage() + " | Read: " + n.isRead())
            );
    }
}