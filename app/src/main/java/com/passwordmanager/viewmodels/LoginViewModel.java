//Menyimpan status user yang login, dan menyediakan:
//login(): validasi login
//register(): validasi dan pendaftaran user baru
//getLoggedInUser(): mengambil data user setelah login berhasil
package com.passwordmanager.viewmodels;

import com.passwordmanager.models.User;
import com.passwordmanager.daos.UserDAO;

public class LoginViewModel {
    private User loggedInUser;

    public boolean login(String username, String password) {
        User user = UserDAO.login(username, password);
        if (user != null) {
            this.loggedInUser = user;
            return true;
        }
        return false;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public User register(String username, String password, String confirmPassword, String fullname) {
        if (username.length() < 4 || password.length() < 8 || fullname.isBlank()) return null;
        if (!password.equals(confirmPassword)) return null;

        User user = UserDAO.register(username, password, fullname);
        if (user != null) {
            this.loggedInUser = user;
        }
        return user;
    }
}
