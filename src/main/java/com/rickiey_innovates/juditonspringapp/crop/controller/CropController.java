package com.rickiey_innovates.juditonspringapp.crop.controller;

import com.rickiey_innovates.juditonspringapp.DbConnector;
import com.rickiey_innovates.juditonspringapp.controllers.LoginController;
import com.rickiey_innovates.juditonspringapp.crop.dtos.*;
import com.rickiey_innovates.juditonspringapp.crop.models.*;
import com.rickiey_innovates.juditonspringapp.crop.repositories.*;
import com.rickiey_innovates.juditonspringapp.models.*;
import com.rickiey_innovates.juditonspringapp.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/crop")
public class CropController {
    private final UserRepository userRepository;
    private final CropRepository cropRepository;
    private final CropTypeRepository cropTypeRepository;
    private final CropVarietyRepository cropVarietyRepository;
    private final SeasonRepository seasonRepository;
    private final PlantedCropRepository plantedCropRepository;
    private final FarmActivityRepository farmActivityRepository;
    private final ActivityRepository activityRepository;
    private final BankaccountRepository bankaccountRepository;
    private final UnitsRepository unitsRepository;

    public CropController(UserRepository userRepository,
                          CropRepository cropRepository,
                          CropTypeRepository cropTypeRepository,
                          CropVarietyRepository cropVarietyRepository,
                          SeasonRepository seasonRepository,
                          PlantedCropRepository plantedCropRepository,
                          FarmActivityRepository farmActivityRepository, ActivityRepository activityRepository, BankaccountRepository bankaccountRepository,
                          UnitsRepository unitsRepository) {
        this.userRepository = userRepository;
        this.cropRepository = cropRepository;
        this.cropTypeRepository = cropTypeRepository;
        this.cropVarietyRepository = cropVarietyRepository;
        this.seasonRepository = seasonRepository;
        this.plantedCropRepository = plantedCropRepository;
        this.farmActivityRepository = farmActivityRepository;
        this.activityRepository = activityRepository;
        this.bankaccountRepository = bankaccountRepository;
        this.unitsRepository = unitsRepository;
    }

    private Long userId() {
        return LoginController.getLoggedInUserId();
    }

    private Farm farm() {
        return userRepository.findById(userId()).orElse(null).getFarm();
    }

    /** Start of crop Types **/

    @GetMapping("/types")
    public String types(Model model, HttpServletRequest request) {

        User user = userRepository.findById(userId()).get();
        List<CropType> cropTypes = cropTypeRepository.findByFarm(farm());
        List<Crop> cropNames = cropRepository.findByFarm(farm());
        model.addAttribute("user", user);
        model.addAttribute("page", "List of crops");
        model.addAttribute("main", "Crops");
        model.addAttribute("cropNames", cropNames);
        model.addAttribute("cropTypes", cropTypes);
        model.addAttribute("requestURI", request.getRequestURI());
        return "crop/types";
    }


    @GetMapping(value = "/types/get/all", produces = "application/json")
    @ResponseBody
    public List<CropType> cropTypes() {
        return cropTypeRepository.findByFarm(farm());
    }

    @PostMapping("/types/add")
    public String addType(RedirectAttributes redirectAttributes, CropTypeDto cropTypeDto) {
        String message = "";
        try {
            CropType cropType = new CropType();
            cropType.setCropType(cropTypeDto.getCropType());
            cropType.setFarm(farm());
            cropTypeRepository.save(cropType);
            message = "Crop type added successfully";
            redirectAttributes.addFlashAttribute("message", message);
        } catch (Exception e) {
            e.printStackTrace();
            message = "Crop type failed to add";
            redirectAttributes.addFlashAttribute("error", message);
        }

        // Return the same view to display the response message
        return "redirect:/crop/types";
    }

    @GetMapping(value = "/types/get/{id}", produces = "application/json")
    @ResponseBody
    public CropType getCropType(@PathVariable Long id) {

        return cropTypeRepository.findById(id).orElse(null);
    }

    @PostMapping("/types/update")
    public String updateCropType(CropType updatedCropType, RedirectAttributes redirectAttributes) {
        String message, error;
        try {
            CropType existingCropType = cropTypeRepository.findById(updatedCropType.getId()).orElse(null);
            if (existingCropType != null) {
                existingCropType.setCropType(updatedCropType.getCropType());

                cropTypeRepository.save(existingCropType);

                message = "Crop type updated successfully";
                redirectAttributes.addFlashAttribute("message", message);

            } else {
                error = "Crop type not found. Update failed.";
                redirectAttributes.addFlashAttribute("error", error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = "Crop type update failed";
            redirectAttributes.addFlashAttribute("error", error);
        }

        return "redirect:/crop/types";
    }

    @GetMapping("/type/delete/{id}")
    public String deleteCropType(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            cropTypeRepository.deleteById(id);

            redirectAttributes.addFlashAttribute("message", "The crop type has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/crop/types";
    }

    /** END OF CROP TYPES **/

    /** CROP VARIETY **/

    @GetMapping("/varieties")
    public String varieties(Model model, HttpServletRequest request) {

        User user = userRepository.findById(userId()).get();
        List<Crop> cropNames = cropRepository.findByFarm(farm());
        List<CropVariety> cropVarieties = cropVarietyRepository.findByFarm(farm());
        model.addAttribute("user", user);
        model.addAttribute("page", "List of crops");
        model.addAttribute("main", "Crops");
        model.addAttribute("varieties", cropVarieties);
        model.addAttribute("cropNames", cropNames);
        model.addAttribute("requestURI", request.getRequestURI());
        return "crop/varieties";
    }

    @GetMapping(value = "/varieties/get/all", produces = "application/json")
    @ResponseBody
    public List<CropVariety> cropVarieties() {
        return cropVarietyRepository.findByFarm(farm());
    }

    @PostMapping("/varieties/add")
    public String addVariety(RedirectAttributes redirectAttributes, CropVarietyDTO cropVarietyDTO) {
        System.out.println(cropVarietyDTO);
        String message = "";
        try {
            CropVariety cropVariety = new CropVariety();
            cropVariety.setName(cropVarietyDTO.getName());
            cropVariety.setFarm(farm());
            cropVarietyRepository.save(cropVariety);
            message = "Crop variety added successfully";
            redirectAttributes.addFlashAttribute("message", message);
        } catch (Exception e) {
            e.printStackTrace();
            message = "Crop variety failed to add";
            redirectAttributes.addFlashAttribute("error", message);
        }

        // Return the same view to display the response message
        return "redirect:/crop/varieties";
    }

    @GetMapping(value = "/varieties/get/{id}", produces = "application/json")
    @ResponseBody
    public CropVariety getCropVariety(@PathVariable Long id) {

        return cropVarietyRepository.findById(id).orElse(null);
    }

    @PostMapping("/varieties/update")
    public String updateCropVariety(CropVariety updatedCropVariety, RedirectAttributes redirectAttributes) {
        String message, error;
        try {
            CropVariety existingCropVariety = cropVarietyRepository.findById(updatedCropVariety.getId()).orElse(null);
            if (existingCropVariety != null) {
                existingCropVariety.setName(updatedCropVariety.getName());

                cropVarietyRepository.save(existingCropVariety);

                message = "Crop variety updated successfully";
                redirectAttributes.addFlashAttribute("message", message);

            } else {
                error = "Crop variety not found. Update failed.";
                redirectAttributes.addFlashAttribute("error", error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = "Crop variety update failed";
            redirectAttributes.addFlashAttribute("error", error);
        }

        return "redirect:/crop/varieties";
    }

    @GetMapping("/variety/delete/{id}")
    public String deleteCropVariety(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            cropVarietyRepository.deleteById(id);

            redirectAttributes.addFlashAttribute("message", "The crop variety has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/crop/varieties";
    }

    /** CROP VARIETY SECTION **/


    /** CROP NAMES **/

    @GetMapping("/names")
    public String names(Model model, HttpServletRequest request) {

        User user = userRepository.findById(userId()).get();
        List<Crop> cropNames = cropRepository.findByFarm(farm());
        List<Units> units = unitsRepository.findAll();
        List<CropType> cropTypes = cropTypeRepository.findByFarm(farm());
        model.addAttribute("user", user);
        model.addAttribute("page", "List of crops");
        model.addAttribute("main", "Crops");
        model.addAttribute("cropNames", cropNames);
        model.addAttribute("units", units);
        model.addAttribute("cropTypes", cropTypes);
        model.addAttribute("requestURI", request.getRequestURI());
        return "crop/crop_name";
    }

    @GetMapping(value = "/names/get/all", produces = "application/json")
    @ResponseBody
    public List<Crop> cropNames() {
        return cropRepository.findByFarm(farm());
    }

    @PostMapping("/names/add")
    public String addName(RedirectAttributes redirectAttributes, CropDTO cropDTO) {
        String message = "";
        try {
            Crop cropName = new Crop();
            cropName.setName(cropDTO.getName());
            cropName.setPlantingUnits(unitsRepository.findById(cropDTO.getPlantingUnits()).orElse(null));
            cropName.setHarvestingUnits(unitsRepository.findById(cropDTO.getHarvestingUnits()).orElse(null));
            cropName.setType(cropTypeRepository.findById(cropDTO.getType()).orElse(null));
            cropName.setFarm(farm());
            cropRepository.save(cropName);
            message = "Crop name added successfully";
            redirectAttributes.addFlashAttribute("message", message);
        } catch (Exception e) {
            e.printStackTrace();
            message = "Crop name failed to add";
            redirectAttributes.addFlashAttribute("error", message);
        }

        // Return the same view to display the response message
        return "redirect:/crop/names";
    }

    @GetMapping(value = "/names/get/{id}", produces = "application/json")
    @ResponseBody
    public Crop getCropName(@PathVariable Long id) {

        return cropRepository.findById(id).orElse(null);
    }

    @PostMapping("/names/update")
    public String updateCropName(CropDTO cropDto, RedirectAttributes redirectAttributes) {
        String message, error;
        try {
            Crop existingCropname = cropRepository.findById(cropDto.getId()).orElse(null);
            if (existingCropname != null) {
                existingCropname.setName(cropDto.getName());
                existingCropname.setPlantingUnits(unitsRepository.findById(cropDto.getPlantingUnits()).orElse(null));
                existingCropname.setHarvestingUnits(unitsRepository.findById(cropDto.getHarvestingUnits()).orElse(null));
                existingCropname.setType(cropTypeRepository.findById(cropDto.getType()).orElse(null));
                cropRepository.save(existingCropname);

                message = "Crop updated successfully";
                redirectAttributes.addFlashAttribute("message", message);

            } else {
                error = "Crop not found. Update failed.";
                redirectAttributes.addFlashAttribute("error", error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = "Crop update failed";
            redirectAttributes.addFlashAttribute("error", error);
        }

        return "redirect:/crop/names";
    }

    @GetMapping("/name/delete/{id}")
    public String deleteCropName(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            cropRepository.deleteById(id);

            redirectAttributes.addFlashAttribute("message", "The crop name has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/crop/names";
    }

    /** CROP NAMES SECTION **/

    /** PLANTED CROPS SECTION **/

    @GetMapping("/list")
    public String crops(Model model, HttpServletRequest request) {

        User user = userRepository.findById(userId()).get();
        List<PlantedCrop> plantedCrops = plantedCropRepository.findByFarm(farm());
        List<FarmActivity> farmActivities = farmActivityRepository.findByFarm(farm());
        List<Activity> activityList = activityRepository.findByChurch(farm());
        List<Bankaccount> bankaccounts = bankaccountRepository.findByChurch(farm());
        List<PlantedCropSummary> plantedCropSummaries = plantedCrops.stream()
                .map(crop -> {
                    double exp = farmActivities.stream()
                            .filter(activity -> activity.getPlantedCrop() != null) // Ensure PlantedCrop is not null
                            .filter(activity -> activity.getPlantedCrop().getId().equals(crop.getId()))
                            .mapToDouble(activity -> activity.getAccounttransaction().getDebit())
                            .sum();

                    double inc = farmActivities.stream()
                            .filter(activity -> activity.getPlantedCrop() != null) // Ensure PlantedCrop is not null
                            .filter(activity -> activity.getPlantedCrop().getId().equals(crop.getId()))
                            .mapToDouble(activity -> activity.getAccounttransaction().getCredit())
                            .sum();

                    double pro = inc - exp;
                    DecimalFormat df = new DecimalFormat("#,##0.00;(#,##0.00)");
                    Double pPro = (pro / exp) * 100;
                    String formattedPPro = df.format(pPro);

                    PlantedCropSummary summary = new PlantedCropSummary();
                    summary.setCropId(crop.getId());
                    summary.setCropName(crop.getVariety().getName());
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
        return "crop/crops";
    }

    /** PLANTED CROPS SECTION **/


    /** SEASONS SECTION **/

    @GetMapping("/seasons")
    public String seasons(Model model, HttpServletRequest request) {

        User user = userRepository.findById(userId()).get();
        List<Season> seasons = seasonRepository.findByFarm(farm());
        model.addAttribute("user", user);
        model.addAttribute("page", "List of crops");
        model.addAttribute("main", "Crops");
        model.addAttribute("seasons", seasons);
        model.addAttribute("requestURI", request.getRequestURI());
        return "crop/seasons";
    }

    @GetMapping(value = "/seasons/get/all", produces = "application/json")
    @ResponseBody
    public List<Season> seasons() {
        return seasonRepository.findByFarm(farm());
    }

    @PostMapping("/seasons/add")
    public String addSeason(RedirectAttributes redirectAttributes, String name) {
        String message = "";
        try {
            Season season = new Season();
            season.setName(name);
            season.setFarm(farm());
            seasonRepository.save(season);
            message = "Season added successfully";
            redirectAttributes.addFlashAttribute("message", message);
        } catch (Exception e) {
            e.printStackTrace();
            message = "Season failed to add";
            redirectAttributes.addFlashAttribute("error", message);
        }

        // Return the same view to display the response message
        return "redirect:/crop/seasons";
    }

    @GetMapping(value = "/seasons/get/{id}", produces = "application/json")
    @ResponseBody
    public Season getSeason(@PathVariable Long id) {

        return seasonRepository.findById(id).orElse(null);
    }

    @PostMapping("/seasons/update")
    public String updateSeason(Season season, RedirectAttributes redirectAttributes) {
        String message, error;
        try {
            Season existingSeason = seasonRepository.findById(season.getId()).orElse(null);
            if (existingSeason != null) {
                existingSeason.setName(season.getName());

                seasonRepository.save(existingSeason);

                message = "Season updated successfully";
                redirectAttributes.addFlashAttribute("message", message);

            } else {
                error = "Season not found. Update failed.";
                redirectAttributes.addFlashAttribute("error", error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = "Season update failed";
            redirectAttributes.addFlashAttribute("error", error);
        }

        return "redirect:/crop/seasons";
    }

    @GetMapping("/season/delete/{id}")
    public String deleteSeason(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            seasonRepository.deleteById(id);

            redirectAttributes.addFlashAttribute("message", "The season has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/crop/seasons";
    }

    /** END SEASONS SECTION **/


    /** PLANTING SECTION**/

    @PostMapping("/planting")
    public ResponseEntity<String> submitPlantingForm(@RequestBody PlantingForm plantingForm) {
        try {
            System.out.println("Plant reg: " + plantingForm);

            CropVariety variety = cropVarietyRepository.findById(Long.valueOf(plantingForm.getCropVariety())).orElseThrow(EntityNotFoundException::new);
            Season season = seasonRepository.findById(plantingForm.getSeason()).orElseThrow(EntityNotFoundException::new);
            PlantedCrop plantedCrop = new PlantedCrop();
            plantedCrop.setFarm(farm());
            plantedCrop.setVariety(variety);
            plantedCrop.setPlantedDate(plantingForm.getPlantingDate());
            plantedCrop.setSeason(season);
            plantedCrop.setPlantedQuantity(1L);
            plantedCropRepository.save(plantedCrop);

            return ResponseEntity.ok("Planting data submitted successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /** PLANTING SECTION**/



    @GetMapping(value = "/api/getMonthlyExpenses/{plantedCropId}", produces = "application/json")
    @ResponseBody
    public List<MonthlyTransactions> MonthlyExpenses (@PathVariable Long plantedCropId) {
        List<MonthlyTransactions> monthlyExpensesList = new ArrayList<>();
        try {
            Connection connection = DbConnector.getConnection();
            String sql = "SELECT year,\n" +
                    "       month,\n" +
                    "       credit\n" +
                    "FROM (SELECT YEAR(`Date`)                                                       AS year,\n" +
                    "             DATE_FORMAT(`Date`, '%b')                                          AS month,\n" +
                    "             SUM(debit)                                                         AS credit,\n" +
                    "             ROW_NUMBER() OVER (ORDER BY YEAR(`Date`) DESC, MONTH(`Date`) DESC) AS row_num\n" +
                    "      FROM accounttransactions a\n" +
                    "               inner join farm_activity f on a.`transaction id` = f.`accounttransaction_transaction id`\n" +
                    "      where a.farm = "+farm().getId()+"\n" +
                    "        and f.planted_crop = "+plantedCropId+"\n" +
                    "      GROUP BY YEAR(`Date`), MONTH(`Date`)\n" +
                    "      ORDER BY YEAR(`Date`) DESC, MONTH(`Date`) DESC) AS subquery\n" +
                    "WHERE row_num <= 6\n" +
                    "ORDER BY year, month;";

            System.out.println(sql);

            ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
            while(resultSet.next()) {
                MonthlyTransactions monthlyExpenses = new MonthlyTransactions(resultSet.getString("year"), resultSet.getString("month"), resultSet.getString("credit"));
                monthlyExpensesList.add(monthlyExpenses);
            }

            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monthlyExpensesList;
    }

    @GetMapping(value = "/api/getMonthlyIncome/{plantedCropId}", produces = "application/json")
    @ResponseBody
    public List<MonthlyTransactions> MonthlyIncome (@PathVariable Long plantedCropId) {
        List<MonthlyTransactions> monthlyTransactionsList = new ArrayList<>();
        try {
            Connection connection = DbConnector.getConnection();
            String sql = "SELECT year,\n" +
                    "       month,\n" +
                    "       credit\n" +
                    "FROM (SELECT YEAR(`Date`)                                                       AS year,\n" +
                    "             DATE_FORMAT(`Date`, '%b')                                          AS month,\n" +
                    "             SUM(credit)                                                        AS credit,\n" +
                    "             ROW_NUMBER() OVER (ORDER BY YEAR(`Date`) DESC, MONTH(`Date`) DESC) AS row_num\n" +
                    "      FROM accounttransactions a\n" +
                    "               inner join farm_activity f\n" +
                    "      where a.farm = "+farm().getId()+"\n" +
                    "        and f.planted_crop = "+plantedCropId+"\n" +
                    "      GROUP BY YEAR(`Date`), MONTH(`Date`)\n" +
                    "      ORDER BY YEAR(`Date`) DESC, MONTH(`Date`) DESC) AS subquery\n" +
                    "WHERE row_num <= 6\n" +
                    "ORDER BY year, month;";

            System.out.println(sql);

            ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
            while(resultSet.next()) {
                MonthlyTransactions monthlyExpenses = new MonthlyTransactions(resultSet.getString("year"), resultSet.getString("month"), resultSet.getString("credit"));
                monthlyTransactionsList.add(monthlyExpenses);
            }

            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monthlyTransactionsList;
    }

}
