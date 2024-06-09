package source.restaurant_web_project.models.dto.configuration;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import source.restaurant_web_project.configurations.enums.DayOfWeeks;

import java.time.LocalTime;

public class RestaurantConfigurationViewDTO {
    @Size(min=3, max = 20,message = "Restaurant name must be between 3 and 20 chars!")
    private String name;
    @Size(max=50,message = "Restaurant description must be max 50 chars!")
    private String subname;

    private String phone;
    private String firm;
    private String location;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime openTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime closeTime;

    private DayOfWeeks openDay;
    private DayOfWeeks closeDay;

    private String facebookLink;
    private String instagramLink;
    private String twitterLink;
    private String email;
    private String street;
    private String city;

    public RestaurantConfigurationViewDTO(){}

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getInstagramLink() {
        return instagramLink;
    }

    public void setInstagramLink(String instagramLink) {
        this.instagramLink = instagramLink;
    }

    public String getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalTime closeTime) {
        this.closeTime = closeTime;
    }

    public DayOfWeeks getOpenDay() {
        return openDay;
    }

    public void setOpenDay(DayOfWeeks openDay) {
        this.openDay = openDay;
    }

    public DayOfWeeks getCloseDay() {
        return closeDay;
    }

    public void setCloseDay(DayOfWeeks closeDay) {
        this.closeDay = closeDay;
    }

    public String getFirm() {
        return firm;
    }

    public void setFirm(String firm) {
        this.firm = firm;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
