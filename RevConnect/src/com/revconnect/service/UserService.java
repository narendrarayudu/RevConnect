package com.revconnect.service;

import com.revconnect.dao.UserDAO;
import com.revconnect.model.User;

import java.util.List;
import java.util.Scanner;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    // Edit logged-in user's profile
    public void editProfile(User loggedInUser, Scanner sc) {
        System.out.print("Full Name: ");
        loggedInUser.setFullName(sc.nextLine());

        System.out.print("Bio: ");
        loggedInUser.setBio(sc.nextLine());

        System.out.print("Profile Pic Path: ");
        loggedInUser.setProfilePicPath(sc.nextLine());

        System.out.print("Location: ");
        loggedInUser.setLocation(sc.nextLine());

        System.out.print("Website (optional): ");
        loggedInUser.setWebsite(sc.nextLine());

        if (userDAO.updateProfile(loggedInUser)) {
            System.out.println("✅ Profile updated successfully!");
        } else {
            System.out.println("❌ Profile update failed!");
        }
    }

    // View profile by ID
    public void viewProfile(int userId) {
        User user = userDAO.getUserById(userId);
        if (user != null) {
            System.out.println("---- PROFILE ----");
            System.out.println("Username: " + user.getUsername());
            System.out.println("Full Name: " + user.getFullName());
            System.out.println("Bio: " + user.getBio());
            System.out.println("Location: " + user.getLocation());
            System.out.println("Website: " + user.getWebsite());
        } else {
            System.out.println("User not found!");
        }
    }

    // Search users by name or username
    public void searchUsers(Scanner sc) {
        System.out.print("Enter name or username: ");
        String keyword = sc.nextLine();

        List<User> users = userDAO.searchUsers(keyword);
        if (users.isEmpty()) {
            System.out.println("No users found!");
        } else {
            System.out.println("---- SEARCH RESULTS ----");
            for (User u : users) {
                System.out.println(u.getUserId() + " | " + u.getUsername() + " | " + u.getFullName());
            }
        }
    }
}