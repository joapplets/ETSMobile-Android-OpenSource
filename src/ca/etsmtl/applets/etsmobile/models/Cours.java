package ca.etsmtl.applets.etsmobile.models;

public class Cours {

    private String coursePlan;
    private int credits;
    private String description;
    private String level;
    private String name;
    private String prerequisites;
    private String professor;
    private String shortName;
    private String website;
    private int workLoad;

    public String getCoursePlan() {
	return coursePlan;
    }

    public int getCredits() {
	return credits;
    }

    public String getDescription() {
	return description;
    }

    public String getLevel() {
	return level;
    }

    public String getName() {
	return name;
    }

    public String getPrerequisites() {
	return prerequisites;
    }

    public String getProfessor() {
	return professor;
    }

    public String getShortName() {
	return shortName;
    }

    public String getWebsite() {
	return website;
    }

    public int getWorkLoad() {
	return workLoad;
    }

    public void setCoursePlan(final String coursePlan) {
	this.coursePlan = coursePlan;
    }

    public void setCredits(final int credits) {
	this.credits = credits;
    }

    public void setDescription(final String description) {
	this.description = description;
    }

    public void setLevel(final String level) {
	this.level = level;
    }

    public void setName(final String name) {
	this.name = name;
    }

    public void setPrerequisites(final String prerequisites) {
	this.prerequisites = prerequisites;
    }

    public void setProfessor(final String professor) {
	this.professor = professor;
    }

    public void setShortName(final String shortName) {
	this.shortName = shortName;
    }

    public void setWebsite(final String website) {
	this.website = website;
    }

    public void setWorkLoad(final int workLoad) {
	this.workLoad = workLoad;
    }

}
