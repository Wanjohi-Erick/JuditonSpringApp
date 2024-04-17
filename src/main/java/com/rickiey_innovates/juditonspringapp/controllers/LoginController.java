package com.rickiey_innovates.juditonspringapp.controllers;

import com.rickiey_innovates.juditonspringapp.repositories.UserRepository;
import com.rickiey_innovates.juditonspringapp.DbConnector;
import com.rickiey_innovates.juditonspringapp.models.PasswordReset;
import com.rickiey_innovates.juditonspringapp.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Controller
public class LoginController {

    private final UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    PasswordEncoder encoder;

    @GetMapping({"/"})
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout, Model model,
                        RedirectAttributes flash, Principal principal) {
        if (principal != null) {
            flash.addFlashAttribute("info", "You have previously logged in");
            return "redirect:";
        }
        if (error != null)
            model.addAttribute("error", "Incorrect username or password");
        if (logout != null)
            model.addAttribute("success", "You have successfully logged out");
        return "sign-in";
    }

    @RequestMapping(value = {"/logout"}, method = {RequestMethod.GET})
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null)
            (new SecurityContextLogoutHandler()).logout(request, response, auth);
        return "redirect:/";
    }

    @ResponseBody
    public static Long getLoggedInUserId() {
        long user = 0;
        Connection conn = null;
        try {
            conn = DbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT u.id FROM users u  where sessionid=?  ");
            pstmt.setString(1, RequestContextHolder.currentRequestAttributes().getSessionId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
                user = rs.getLong("id");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException sQLException) {
                    sQLException.printStackTrace();
                }
        }
        return 1L;
    }

    @ResponseBody
    public static int getFarm() {
        int farm = 0;
        Connection conn = null;
        try {
            conn = DbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT u.farm FROM users u  where sessionid=?  ");
            pstmt.setString(1, RequestContextHolder.currentRequestAttributes().getSessionId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
                farm = rs.getInt("farm");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException sQLException) {
                    sQLException.printStackTrace();
                }
        }
        return farm;
    }

    @PostMapping("/api/resetPass")
    public String resetPass(PasswordReset passwordReset, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String referer = request.getHeader("referer");

        String redirectUrl = referer != null ? referer : "/"; // If no referer is available, redirect to the home page ("/")
        try {
            String old, newPass, confirm;
            old = passwordReset.getOld();
            newPass = passwordReset.getNewPass();
            confirm = passwordReset.getConfirm();

            User user = userRepository.findById(getLoggedInUserId()).orElse(null);
            if (!encoder.matches(old, user.getPassword())) {
                redirectAttributes.addFlashAttribute("error", "Old password is incorrect");
            } else {
                if (!newPass.equals(confirm)) {
                    redirectAttributes.addFlashAttribute("error", "Passwords do not match");
                } else {
                    user.setPassword(encoder.encode(newPass));
                    userRepository.save(user);
                    redirectAttributes.addFlashAttribute("message", "Password reset successfully");
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getLocalizedMessage());
            e.printStackTrace();
        }
        return "redirect:" + redirectUrl;
    }
}
