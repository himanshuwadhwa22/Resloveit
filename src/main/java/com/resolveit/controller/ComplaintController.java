package com.resolveit.controller;

import com.resolveit.model.Complaint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Controller
public class ComplaintController {
    private final List<Complaint> complaints = new CopyOnWriteArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1000);

    public ComplaintController() {
        complaints.add(new Complaint(sequence.incrementAndGet(), "Aarav Sharma", "aarav@example.com",
                "IT & Wi-Fi", "Library, Block A", "Wi-Fi not working in library",
                "The campus Wi-Fi keeps disconnecting in the library study area.", "Medium"));
        complaints.add(new Complaint(sequence.incrementAndGet(), "Meera Singh", "meera@example.com",
                "Classroom", "Room 204, Block B", "Projector not working",
                "The projector does not turn on before lectures.", "High"));
        complaints.get(0).setStatus("In Progress");
    }


    @GetMapping("/login")
    public String login(@RequestParam(required = false) String role, Model model) {
        model.addAttribute("selectedRole", "ADMIN".equalsIgnoreCase(role) ? "ADMIN"
                : ("STUDENT".equalsIgnoreCase(role) ? "STUDENT" : ""));
        return "login";
    }

    @GetMapping("/user")
    public String userWorkspace(Model model) {
        addDashboardData(model);
        return "user";
    }

    @GetMapping("/")
    public String home(Model model) {
        addDashboardData(model);
        return "index";
    }

    @GetMapping("/complaints/new")
    public String newComplaint(Model model) {
        model.addAttribute("complaint", new Complaint());
        return "new-complaint";
    }

    @PostMapping("/complaints")
    public String submitComplaint(@RequestParam String name,
                                  @RequestParam String email,
                                  @RequestParam String category,
                                  @RequestParam String location,
                                  @RequestParam String title,
                                  @RequestParam String description,
                                  @RequestParam(defaultValue = "Medium") String priority,
                                  Model model) {
        if (name.isBlank() || email.isBlank() || category.isBlank() || location.isBlank()
                || title.isBlank() || description.isBlank()) {
            model.addAttribute("error", "Please fill in every required field.");
            model.addAttribute("complaint", new Complaint());
            return "new-complaint";
        }
        Complaint c = new Complaint(sequence.incrementAndGet(), name.trim(), email.trim(), category,
                location.trim(), title.trim(), description.trim(), priority);
        complaints.add(0, c);
        model.addAttribute("submittedComplaint", c);
        return "submitted";
    }

    @GetMapping("/track")
    public String trackPage() {
        return "track";
    }

    @GetMapping("/track/search")
    public String track(@RequestParam long id, Model model) {
        Complaint found = complaints.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
        model.addAttribute("complaint", found);
        model.addAttribute("searchedId", id);
        return "track";
    }

    @GetMapping("/admin")
    public String admin(@RequestParam(required = false) String q,
                        @RequestParam(required = false) String status,
                        Model model) {
        List<Complaint> result = complaints.stream()
                .sorted(Comparator.comparing(Complaint::getCreatedAt,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .collect(Collectors.toList());
        if (q != null && !q.isBlank()) {
            String query = q.toLowerCase();
            result = result.stream().filter(c ->
                    String.valueOf(c.getId()).contains(query)
                    || c.getTitle().toLowerCase().contains(query)
                    || c.getName().toLowerCase().contains(query)
                    || c.getCategory().toLowerCase().contains(query)).collect(Collectors.toList());
        }
        if (status != null && !status.isBlank()) {
            result = result.stream().filter(c -> status.equals(c.getStatus())).collect(Collectors.toList());
        }
        model.addAttribute("complaints", result);
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("selectedStatus", status == null ? "" : status);
        addDashboardData(model);
        return "admin";
    }

    @PostMapping("/admin/{id}/update")
    public String updateComplaint(@PathVariable long id,
                                  @RequestParam String status,
                                  @RequestParam(defaultValue = "") String remark) {
        complaints.stream().filter(c -> c.getId() == id).findFirst().ifPresent(c -> {
            c.setStatus(status);
            c.setAdminRemark(remark.trim());
        });
        return "redirect:/admin";
    }

    private void addDashboardData(Model model) {
        model.addAttribute("totalCount", complaints.size());
        model.addAttribute("submittedCount", complaints.stream().filter(c -> "Submitted".equals(c.getStatus())).count());
        model.addAttribute("progressCount", complaints.stream().filter(c -> "In Progress".equals(c.getStatus())).count());
        model.addAttribute("resolvedCount", complaints.stream().filter(c -> "Resolved".equals(c.getStatus())).count());
        model.addAttribute("recentComplaints", complaints.stream().limit(5).collect(Collectors.toList()));
    }
}
