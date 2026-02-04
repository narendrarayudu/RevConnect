
package com.revconnect.service;

import java.sql.SQLException;

import com.revconnect.dao.UserDAO;
import com.revconnect.model.User;

public class AuthService {

    private UserDAO userDAO = new UserDAO();

    public User login(String username, String password) {

        try {
            return userDAO.login(username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean register(User user) {

        try {
            return userDAO.register(user);
        } catch (SQLException e) {
        	e.printStackTrace();
            return false;
        }
    }
}
