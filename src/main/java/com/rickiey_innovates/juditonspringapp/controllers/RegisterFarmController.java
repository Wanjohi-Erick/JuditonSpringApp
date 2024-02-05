package com.rickiey_innovates.juditonspringapp.controllers;

import com.rickiey_innovates.juditonspringapp.models.Companyofficial;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import com.rickiey_innovates.juditonspringapp.models.Reference;
import com.rickiey_innovates.juditonspringapp.models.User;
import com.rickiey_innovates.juditonspringapp.repositories.CompanyofficialRepository;
import com.rickiey_innovates.juditonspringapp.repositories.FarmRepository;
import com.rickiey_innovates.juditonspringapp.repositories.ReferenceRepository;
import com.rickiey_innovates.juditonspringapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("/hidden/hidden/stuff/farms")
@Controller
public class RegisterFarmController {

    @Autowired
    FarmRepository farmRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private ReferenceRepository referenceRepository;
    @Autowired
    private CompanyofficialRepository companyofficialRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Long userId() {
        return LoginController.getLoggedInUserId();
    }

    private Farm farm() {
        return userRepository.findById(userId()).orElse(null).getFarm();
    }

    @GetMapping("/show")
    public String farms(Model model) {
        User user = userRepository.findById(userId()).get();
        List<Farm> farms = farmRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("farms", farms);
        model.addAttribute("page", "farms");
        model.addAttribute("main", "system");
        return "farms";
    }
    @PostMapping(value = "/register", produces = "application/json")
    private String register(Farm farm, RedirectAttributes redirectAttributes) {
        String message = "";
        try {
            Farm createdFarm = farmRepository.save(farm);
            Reference reference = new Reference();
            reference.setRef(0);
            reference.setRct(0);
            reference.setPv(0);
            reference.setFarm(createdFarm);

            referenceRepository.save(reference);

            Companyofficial companyofficial = new Companyofficial();
            companyofficial.setTreasurer(null);
            companyofficial.setAccountant(null);
            companyofficial.setSecondSignatory(null);
            companyofficial.setAccountant(null);
            companyofficial.setFarm(createdFarm);
            companyofficialRepository.save(companyofficial);

            User user = new User();
            user.setUsername("admin@" + createdFarm.getId());
            user.setEmail("admin" + createdFarm.getId() + "@farm.com");
            user.setPassword(passwordEncoder.encode("1234@#"));
            user.setLogins(0);
            user.setTheme(0);
            user.setFarm(createdFarm);

            userRepository.save(user);

            message = "farm added Successfully";
            redirectAttributes.addFlashAttribute("message", message);
        } catch (Exception e) {
            e.printStackTrace();
            message = "farm failed to add";
            redirectAttributes.addFlashAttribute("error", message);
        }

        return "redirect:/hidden/hidden/stuff/farms/show";
    }

    @GetMapping(value = "/get/{id}", produces = "application/json")
    @ResponseBody
    public Farm farm(@PathVariable Integer id) {
        return farmRepository.findById(id).orElse(null);
    }


    @GetMapping("/delete/{id}")
    public String deleteFarm(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            farmRepository.deleteById(id);

            redirectAttributes.addFlashAttribute("message", "The farm has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/hidden/hidden/stuff/farms/show";
    }
}
