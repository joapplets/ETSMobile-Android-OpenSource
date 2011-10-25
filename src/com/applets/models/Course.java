package com.applets.models;

public class Course {
    private String name;
    private String shortName;
    private String description;
    private String professor;
    private String coursePlan;
    private String website;
    private int credits;
    private String prerequisites;
    private String level;
    private int workLoad;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getShortName() {
	return shortName;
    }

    public void setShortName(String shortName) {
	this.shortName = shortName;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getProfessor() {
	return professor;
    }

    public void setProfessor(String professor) {
	this.professor = professor;
    }

    public String getCoursePlan() {
	return coursePlan;
    }

    public void setCoursePlan(String coursePlan) {
	this.coursePlan = coursePlan;
    }

    public String getWebsite() {
	return website;
    }

    public void setWebsite(String website) {
	this.website = website;
    }

    public int getCredits() {
	return credits;
    }

    public void setCredits(int credits) {
	this.credits = credits;
    }

    public String getPrerequisites() {
	return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
	this.prerequisites = prerequisites;
    }

    public String getLevel() {
	return level;
    }

    public void setLevel(String level) {
	this.level = level;
    }

    public int getWorkLoad() {
	return workLoad;
    }

    public void setWorkLoad(int workLoad) {
	this.workLoad = workLoad;
    }

}

