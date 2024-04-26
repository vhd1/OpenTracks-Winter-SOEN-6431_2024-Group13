package de.dennisguse.opentracks.settings;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import de.dennisguse.opentracks.data.models.ActivityType;
import de.dennisguse.opentracks.data.models.HeartRate;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.data.models.TrackPoint;
import de.dennisguse.opentracks.stats.TrackStatistics;

public class CalorieUserDetail {
    private double height;
    private double weight;
    private int age;
    private String sport;
    private int heartRate;

    Track track;

    public String getActivityType(){
        return track.getActivityType();
    }

    TrackStatistics trackStatistics;

    public Duration getMovingTime(){
        return trackStatistics.getMovingTime();
    }

    public float getBPM(){
        return trackStatistics.getAverageHeartRate().getBPM();
    }

    // Getters and setters for height

    public double getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = PreferencesUtils.getHeightInNumber();
    }

    // Getters and setters for weight
    public double getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = PreferencesUtils.getWeightInNumber();
    }

    // Getters and setters for age
    public int getAge() {
        String dob = PreferencesUtils.getDateOfBirth();
        return calculateYears(dob);
    }

    public void setAge(int age) {
        this.age = age;
    }

    // Getters and setters for sport
    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = getActivityType();
    }

    // Getters and setters for heart rate
    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public double getCalorieComputation() {

        // Calculate Basal Metabolic Rate (BMR)
        double bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        double activityLevelMultiplier;
        switch (sport) {
            case "Running":
                activityLevelMultiplier = 1.55; 
                break;
	        case "Biking" :activityLevelMultiplier = 1.75;
		        break;
	        case "RoadBiking " :activityLevelMultiplier = 2.00;
		        break;
	        case "MountainBiking" :activityLevelMultiplier = 2.55;
		        break;
            default:
                activityLevelMultiplier = 1.55; 
                break;
        }
        
        double totalCalorieExpenditure = bmr * activityLevelMultiplier;
        return totalCalorieExpenditure;
    }

    public double getCalorieComputationTest(double height, double weight,String dob,String sport) {
        int age = calculateYears(dob);
        // Calculate Basal Metabolic Rate (BMR)
        double bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        double activityLevelMultiplier;
        switch (sport) {
            case "Running":
                activityLevelMultiplier = 1.55; 
                break;
	        case "Biking" :activityLevelMultiplier = 1.75;
		        break;
	        case "RoadBiking " :activityLevelMultiplier = 2.00;
		        break;
	        case "MountainBiking" :activityLevelMultiplier = 2.55;
		        break;
            default:
                activityLevelMultiplier = 1.55; 
                break;
        }
        
        double totalCalorieExpenditure = bmr * activityLevelMultiplier;
        return totalCalorieExpenditure;
    }

    // Utility method to convert weight from various units to kilograms
    private double convertHeight(double value, String unit) {
        switch (unit.toLowerCase()) {
            case "feet":
                return value * 0.3048; // Convert feet to meters
            case "inches":
                return value * 0.0254; // Convert inches to meters
            default:
                return value; // Assume input is already in meters
        }
    }

    // Utility method to convert weight from various units to kilograms
    private double convertWeight(double value, String unit) {
        switch (unit.toLowerCase()) {
            case "pounds":
                return value * 0.453592; // Convert pounds to kilograms
            default:
                return value; // Assume input is already in kilograms
        }
    }

    private int calculateYears(String dobString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dob = LocalDate.parse(dobString, formatter);
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(dob, currentDate);
        int years = period.getYears();
        return years;
    }
}
