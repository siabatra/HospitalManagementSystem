package entity;
import control.*;
import boundaries.*;
import hms.*;


 /**
  * Represents a user in the Hospital Management System.
  * This is a base class from which different types of users can inherit.
  */
 public class User {
     /**
      * Enum representing possible roles of a user.
      */
     public enum UserRoles {
        PATIENT, DOCTOR, PHARMACIST, ADMIN;
     }
 
     protected String userId;
     protected String password;
     protected UserRoles role;
     protected String name;
     protected String gender;
 
     /**
      * Constructs a User with specified details.
      *
      * @param userId   the user ID of the user
      * @param password the password of the user
      * @param role     the role of the user (e.g., PATIENT, Doctor)
      * @param name     the name of the user
      * @param gender   the gender of the user
      */
     public User(String userId, String password, UserRoles role, String name, String gender) {
         this.userId = userId;
         this.password = password;
         this.role = role;
         this.name = name;
         this.gender = gender;
     }
 
     /**
      * Gets the user ID.
      *
      * @return the user ID
      */
     public String getUserId() {
         return userId;
     }
 
     /**
      * Gets the role of the user.
      *
      * @return the role of the user
      */
     public UserRoles getRole() {
         return role;
     }
 
     /**
      * Gets the user's password.
      *
      * @return the user's password
      */
     public String getPassword() {
         return password;
     }
 
     /**
      * Sets a new password for the user.
      *
      * @param password the new password
      */
     public void setPassword(String password) {
         this.password = password;
     }
 
     /**
      * Gets the name of the user.
      *
      * @return the name of the user
      */
     public String getName() {
         return name;
     }
 
     /**
      * Sets a new name for the user.
      *
      * @param name the new name
      */
     public void setName(String name) {
         this.name = name;
     }
 
     /**
      * Gets the gender of the user.
      *
      * @return the gender of the user
      */
     public String getGender() {
         return gender;
     }
 
     /**
      * Sets a new gender for the user.
      *
      * @param gender the new gender
      */
     public void setGender(String gender) {
         this.gender = gender;
     }
 
     /**
      * Returns details of the user.
      * This method is overridden in subclasses to provide role-specific details.
      *
      * @return details of the user
      */
     public String getDetails() {
         return "";
     }
 }
 