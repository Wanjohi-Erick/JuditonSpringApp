package com.rickiey_innovates.juditonspringapp.livestock.controllers;

import com.rickiey_innovates.juditonspringapp.controllers.LoginController;
import com.rickiey_innovates.juditonspringapp.crop.dtos.CropDTO;
import com.rickiey_innovates.juditonspringapp.crop.dtos.CropVarietyDTO;
import com.rickiey_innovates.juditonspringapp.crop.dtos.PlantedCropSummary;
import com.rickiey_innovates.juditonspringapp.crop.models.Crop;
import com.rickiey_innovates.juditonspringapp.crop.models.CropType;
import com.rickiey_innovates.juditonspringapp.crop.models.CropVariety;
import com.rickiey_innovates.juditonspringapp.crop.models.PlantedCrop;
import com.rickiey_innovates.juditonspringapp.livestock.dtos.BreedDTO;
import com.rickiey_innovates.juditonspringapp.livestock.dtos.LivestockDTO;
import com.rickiey_innovates.juditonspringapp.livestock.models.Breed;
import com.rickiey_innovates.juditonspringapp.livestock.models.FarmedLivestock;
import com.rickiey_innovates.juditonspringapp.livestock.models.Livestock;
import com.rickiey_innovates.juditonspringapp.livestock.repositories.BreedRepository;
import com.rickiey_innovates.juditonspringapp.livestock.repositories.FarmedLivestockRepository;
import com.rickiey_innovates.juditonspringapp.livestock.repositories.LivestockRepository;
import com.rickiey_innovates.juditonspringapp.models.*;
import com.rickiey_innovates.juditonspringapp.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/livestock")
public class LivestockController {
    private final UserRepository userRepository;
    private final LivestockRepository livestockRepository;
    private final BreedRepository breedRepository;
    private final UnitsRepository unitsRepository;
    private final FarmedLivestockRepository farmedLivestockRepository;
    private final FarmActivityRepository farmActivityRepository;
    private final ActivityRepository activityRepository;
    private final BankaccountRepository bankaccountRepository;

    public LivestockController(UserRepository userRepository,
                               LivestockRepository livestockRepository,
                               BreedRepository breedRepository, UnitsRepository unitsRepository,
                               FarmedLivestockRepository farmedLivestockRepository, FarmActivityRepository farmActivityRepository, ActivityRepository activityRepository, BankaccountRepository bankaccountRepository) {
        this.userRepository = userRepository;
        this.livestockRepository = livestockRepository;
        this.breedRepository = breedRepository;
        this.unitsRepository = unitsRepository;
        this.farmedLivestockRepository = farmedLivestockRepository;
        this.farmActivityRepository = farmActivityRepository;
        this.activityRepository = activityRepository;
        this.bankaccountRepository = bankaccountRepository;
    }

    private Long userId() {
        return LoginController.getLoggedInUserId();
    }

    private Farm farm() {
        return userRepository.findById(userId()).orElseThrow(EntityNotFoundException::new).getFarm();
    }



    /** LIVESTOCK **/

    @GetMapping("")
    public String names(Model model, HttpServletRequest request) {

        User user = userRepository.findById(userId()).get();
        List<Livestock> cropNames = livestockRepository.findByFarm(farm());
        List<Units> units = unitsRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("page", "List of livestock");
        model.addAttribute("main", "Livestock");
        model.addAttribute("cropNames", cropNames);
        model.addAttribute("units", units);
        model.addAttribute("requestURI", request.getRequestURI());
        return "livestock/crop_name";
    }

    @GetMapping(value = "/get/all", produces = "application/json")
    @ResponseBody
    public List<Livestock> cropNames() {
        return livestockRepository.findByFarm(farm());
    }

    @PostMapping("/add")
    public String addName(RedirectAttributes redirectAttributes, LivestockDTO cropDTO) {
        System.out.println("cropdto: " + cropDTO);
        String message = "";
        try {
            Livestock cropName = new Livestock();
            cropName.setName(cropDTO.getName());
            cropName.setFarm(farm());
            livestockRepository.save(cropName);
            message = "Livestock added successfully";
            redirectAttributes.addFlashAttribute("message", message);
        } catch (Exception e) {
            e.printStackTrace();
            message = "Livestock failed to add";
            redirectAttributes.addFlashAttribute("error", message);
        }

        // Return the same view to display the response message
        return "redirect:/livestock";
    }

    @GetMapping(value = "/get/{id}", produces = "application/json")
    @ResponseBody
    public Livestock getCropName(@PathVariable Long id) {

        return livestockRepository.findById(id).orElse(null);
    }

    @PostMapping("/update")
    public String updateCropName(LivestockDTO cropDto, RedirectAttributes redirectAttributes) {
        String message, error;
        try {
            Livestock existingCropname = livestockRepository.findById(cropDto.getId()).orElse(null);
            if (existingCropname != null) {
                existingCropname.setName(cropDto.getName());
                livestockRepository.save(existingCropname);

                message = "Livestock updated successfully";
                redirectAttributes.addFlashAttribute("message", message);

            } else {
                error = "Livestock not found. Update failed.";
                redirectAttributes.addFlashAttribute("error", error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = "Livestock update failed";
            redirectAttributes.addFlashAttribute("error", error);
        }

        return "redirect:/livestock";
    }

    @GetMapping("/delete/{id}")
    public String deleteCropName(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            livestockRepository.deleteById(id);

            redirectAttributes.addFlashAttribute("message", "The crop name has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/livestock";
    }

    /** END LIVESTOCK SECTION **/


    /** livestock Breed Section **/

    @GetMapping("/breeds")
    public String breeds(Model model, HttpServletRequest request) {

        User user = userRepository.findById(userId()).get();
        List<Livestock> cropNames = livestockRepository.findByFarm(farm());
        List<Breed> cropVarieties = breedRepository.findByFarm(farm());
        model.addAttribute("user", user);
        model.addAttribute("page", "List of crops");
        model.addAttribute("main", "Crops");
        model.addAttribute("varieties", cropVarieties);
        model.addAttribute("cropNames", cropNames);
        model.addAttribute("requestURI", request.getRequestURI());
        return "livestock/varieties";
    }

    @GetMapping(value = "/breeds/get/all", produces = "application/json")
    @ResponseBody
    public List<Breed> breeds() {
        return breedRepository.findByFarm(farm());
    }

    @PostMapping("/breeds/add")
    public String addBreed(RedirectAttributes redirectAttributes, BreedDTO breedDTO) {
        System.out.println(breedDTO);
        String message = "";
        try {
            Livestock livestock = livestockRepository.findById(breedDTO.getLivestockId()).orElseThrow(EntityNotFoundException::new);
            Breed breed = new Breed();
            breed.setName(breedDTO.getName());
            breed.setLivestock(livestock);
            breed.setFarm(farm());
            breedRepository.save(breed);
            message = "Livestock breed added successfully";
            redirectAttributes.addFlashAttribute("message", message);
        } catch (Exception e) {
            e.printStackTrace();
            message = "Breed failed to add";
            redirectAttributes.addFlashAttribute("error", message);
        }

        // Return the same view to display the response message
        return "redirect:/livestock/breeds";
    }

    @GetMapping(value = "/breeds/get/{id}", produces = "application/json")
    @ResponseBody
    public Breed getBreed(@PathVariable Long id) {

        return breedRepository.findById(id).orElse(null);
    }

    @PostMapping("/breeds/update")
    public String updateCropVariety(Breed updatedBreed, RedirectAttributes redirectAttributes) {
        System.out.println("breeds: " + updatedBreed);
        String message, error;
        try {
            Breed existingBreed = breedRepository.findById(updatedBreed.getId()).orElse(null);
            if (existingBreed != null) {
                existingBreed.setName(updatedBreed.getName());
                existingBreed.setLivestock(updatedBreed.getLivestock());

                breedRepository.save(existingBreed);

                message = "Livestock breed updated successfully";
                redirectAttributes.addFlashAttribute("message", message);

            } else {
                error = "Livestock breed not found. Update failed.";
                redirectAttributes.addFlashAttribute("error", error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = "Crop variety update failed";
            redirectAttributes.addFlashAttribute("error", error);
        }

        return "redirect:/livestock/varieties";
    }

    @GetMapping("/breed/delete/{id}")
    public String deleteCropVariety(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            breedRepository.deleteById(id);

            redirectAttributes.addFlashAttribute("message", "The livestock breed has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/livestock/varieties";
    }

    /** END LIVESTOCK BREED SECTION **/


    /** FARMED LIVESTOCK SECTION **/

    @GetMapping("/list")
    public String livestock(Model model, HttpServletRequest request) {

        User user = userRepository.findById(userId()).get();
        List<FarmedLivestock> plantedCrops = farmedLivestockRepository.findByFarm(farm());
        List<FarmActivity> farmActivities = farmActivityRepository.findByFarm(farm());
        List<Activity> activityList = activityRepository.findByChurch(farm());
        List<Bankaccount> bankaccounts = bankaccountRepository.findByChurch(farm());
        List<PlantedCropSummary> plantedCropSummaries = plantedCrops.stream()
                .map(crop -> {
                    double exp = farmActivities.stream()
                            .filter(activity -> activity.getFarmedLivestock() != null) // Ensure FarmedLivestock is not null
                            .filter(activity -> activity.getFarmedLivestock().getId().equals(crop.getId()))
                            .mapToDouble(activity -> activity.getAccounttransaction() != null
                                    ? activity.getAccounttransaction().getDebit()
                                    : 0.0) // Use 0.0 if Accounttransaction is null
                            .sum();

                    double inc = farmActivities.stream()
                            .filter(activity -> activity.getFarmedLivestock() != null) // Ensure FarmedLivestock is not null
                            .filter(activity -> activity.getFarmedLivestock().getId().equals(crop.getId()))
                            .mapToDouble(activity -> activity.getAccounttransaction() != null
                                    ? activity.getAccounttransaction().getCredit()
                                    : 0.0) // Use 0.0 if Accounttransaction is null
                            .sum();

                    double pro = inc - exp;
                    DecimalFormat df = new DecimalFormat("#,##0.00;(#,##0.00)");
                    Double pPro = (pro / exp) * 100;
                    String formattedPPro = df.format(pPro);

                    PlantedCropSummary summary = new PlantedCropSummary();
                    summary.setCropId(crop.getId());
                    summary.setCropName(crop.getBreed().getName());
                    summary.setExp(exp);
                    summary.setInc(inc);
                    summary.setPro(formattedPPro + "%");
                    return summary;
                })
                .toList();

// Assuming plantedCrops and plantedCropSummaries are fetched
        Map<Long, PlantedCropSummary> summaries = plantedCropSummaries.stream()
                .collect(Collectors.toMap(PlantedCropSummary::getCropId, Function.identity()));


        model.addAttribute("user", user);
        model.addAttribute("page", "List of crops");
        model.addAttribute("main", "Crops");
        model.addAttribute("plantedCrops", plantedCrops);
        model.addAttribute("farmActivities", farmActivities);
        model.addAttribute("summaries", summaries);
        model.addAttribute("activities", activityList);
        model.addAttribute("bankaccounts", bankaccounts);
        model.addAttribute("requestURI", request.getRequestURI());
        return "livestock/crops";
    }

    /** FARMED LIVESTOCK SECTION **/
}
