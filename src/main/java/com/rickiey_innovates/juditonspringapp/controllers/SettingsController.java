package com.rickiey_innovates.juditonspringapp.controllers;

import com.rickiey_innovates.juditonspringapp.models.*;
import com.rickiey_innovates.juditonspringapp.repositories.CompanyofficialRepository;
import com.rickiey_innovates.juditonspringapp.repositories.FarmRepository;
import com.rickiey_innovates.juditonspringapp.repositories.RoleRepository;
import com.rickiey_innovates.juditonspringapp.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private FarmRepository farmRepository;
    @Autowired
    private CompanyofficialRepository companyofficialRepository;

    @Autowired
    RoleRepository roleRepository;

    private Long userId() {
        return LoginController.getLoggedInUserId();
    }

    @GetMapping("/farm")
    @ResponseBody
    private Farm farm() {
        return userRepository.findById(userId()).orElse(null).getFarm();
    }

    @GetMapping(value = "/users")
    public String getUsers(Model model) {
        List<User> userList = userRepository.findByChurch(farm());
        User user = userRepository.findById(userId()).get();
        model.addAttribute("user", user);
        model.addAttribute("users", userList);
        model.addAttribute("page", "groups");
        model.addAttribute("main", "members");

        return "users";
    }

    @GetMapping(value = "/api/users", produces = "application/json")
    @ResponseBody
    public List<User> users() {
        return userRepository.findByChurch(farm());
    }

    @PostMapping("/user/add")
    public String addGroup(User user, RedirectAttributes redirectAttributes) {
        try {
            user.setFarm(farm());
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("message", "User group added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "An error occurred while adding user");
        }
        return "redirect:/settings/users";
    }

    @GetMapping("/user/get/{id}")
    @ResponseBody
    public User membergroup(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping("/user/update")
    public String updateGroup(User membergroup, RedirectAttributes redirectAttributes) {
        try {
            User old = userRepository.findById(membergroup.getId()).get();
            old.setUsername(membergroup.getUsername());
            old.setEmail(membergroup.getEmail());
            userRepository.save(old);
            redirectAttributes.addFlashAttribute("message", "User updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "An error occurred while updating user");
        }
        return "redirect:/settings/users";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteTimetable(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userRepository.deleteById(id);

            redirectAttributes.addFlashAttribute("message", "The user has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/settings/users";
    }

    @PostMapping("/farm/update")
    public String updateChurch(Farm farm, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String referer = request.getHeader("referer");

        String redirectUrl = referer != null ? referer : "/settings/users";
        try {
            Farm old = farmRepository.findById(farm.getId()).get();
            old.setName(farm.getName());
            old.setEmail(farm.getEmail());
            old.setRegion(farm.getRegion());
            old.setAddress(farm.getAddress());
            old.setCountry(farm.getCountry());
            old.setZip(farm.getZip());
            farmRepository.save(old);
            redirectAttributes.addFlashAttribute("message", "Farm details updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "An error occurred while updating farm details");
        }
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/official")
    @ResponseBody
    public Companyofficial companyofficial() {
        return companyofficialRepository.findByChurch(farm());
    }

    @PostMapping("/official/update")
    public String updateOfficials(Companyofficial companyofficial, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String referer = request.getHeader("referer");

        String redirectUrl = referer != null ? referer : "/settings/users";
        try {
            Companyofficial old = companyofficialRepository.findById(companyofficial.getId()).get();
            old.setAccountant(companyofficial.getAccountant());
            old.setSeniorPastor(companyofficial.getSeniorPastor());
            old.setSecondSignatory(companyofficial.getSecondSignatory());
            old.setTreasurer(companyofficial.getTreasurer());
            Companyofficial savedOfficials =  companyofficialRepository.save(old);

            User accountant = savedOfficials.getAccountant();
            User senior = savedOfficials.getSeniorPastor();
            User second = savedOfficials.getSecondSignatory();
            User treasurer = savedOfficials.getTreasurer();

            if (accountant != null) {
                Set<Role> roles = new HashSet<>();

                Role userRole = roleRepository.findByName(ERole.SHAREHOLDER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);

                accountant.setRoles(roles);
                userRepository.save(accountant);
            }

            if (senior != null) {
                Set<Role> roles = new HashSet<>();

                Role userRole = roleRepository.findByName(ERole.SHAREHOLDER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);

                senior.setRoles(roles);
                userRepository.save(senior);
            }

            if (second != null) {
                Set<Role> roles = new HashSet<>();

                Role userRole = roleRepository.findByName(ERole.SHAREHOLDER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);

                second.setRoles(roles);
                userRepository.save(second);
            }

            if (treasurer != null) {
                Set<Role> roles = new HashSet<>();

                Role userRole = roleRepository.findByName(ERole.SHAREHOLDER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);

                treasurer.setRoles(roles);
                userRepository.save(treasurer);
            }

            redirectAttributes.addFlashAttribute("message", "Farm officials updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "An error occurred while updating farm officials");
        }
        return "redirect:" + redirectUrl;
    }
}
