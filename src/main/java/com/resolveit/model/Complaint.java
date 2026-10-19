package com.resolveit.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Complaint {
    private long id;
    private String name;
    private String email;
    private String category;
    private String location;
    private String title;
    private String description;
    private String priority;
    private String status;
    private String adminRemark;
    private LocalDateTime createdAt;

    public Complaint() {}

    public Complaint(long id, String name, String email, String category, String location,
                     String title, String description, String priority) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.category = category;
        this.location = location;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = "Submitted";
        this.adminRemark = "";
        this.createdAt = LocalDateTime.now();
    }

    public String getCreatedAtDisplay() {
        return createdAt == null ? "" : createdAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAdminRemark() { return adminRemark; }
    public void setAdminRemark(String adminRemark) { this.adminRemark = adminRemark; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
