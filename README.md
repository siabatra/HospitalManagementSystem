# HospitalManagementSystem
Hospital Management System (HMS)

NTU AY2024/25 Semester 1 SC2002 Group Project - Hospital Management System (HMS).

The Hospital Management System (HMS) is a Java console application designed to streamline hospital operations, using object-oriented programming principles to enhance usability and functionality. HMS focuses on maintainability, reusability, and scalability, providing an adaptable platform for hospital management.

The initial password for every user is password.

Links

GitHub Repository

Documentation

Report

Highlights

Generic Repository Pattern: Service classes such as PatientService and StaffService use generic repository-style functions for adding, removing, and updating data, reducing code duplication and enhancing maintainability.

CSV Integration: Data storage is managed using CSV files through the CSVDataAccess utility. This component provides methods for reading, writing, and updating records in CSV files, keeping data handling simple and avoiding the need for a dedicated database.

Role-Based Access Control: The system is designed to accommodate different user roles such as Patient, Doctor, Pharmacist, and Admin. Each role has specific functionalities implemented via the App classes (PatientApp, DoctorApp, etc.), promoting modularity.

Javadoc Documentation: To ensure maintainability, Javadoc was used to generate detailed documentation for the codebase, including explanations for classes, methods, and parameters. This helps new developers quickly get familiar with the system's structure and logic.

Features

Patient

View Medical Records

Update Personal Information

Schedule or Reschedule Appointments

Cancel Appointments

Doctor

View Patient Records

Set Availability for Appointments

Accept/Decline Appointments

Record Appointment Outcomes

Pharmacist

View and Update Prescription Status

Manage Medication Inventory

Submit Replenishment Requests

Admin

Manage Hospital Staff

View Appointment Details

Approve Replenishment Requests

Design Principles

Encapsulation & Cohesion: To ensure good encapsulation, high cohesion, and low coupling, we centralized CSV-related operations by creating a CSVService class. This class acts as an interface for accessing CSV files, and other classes use its methods rather than directly implementing file access themselves.

Dependency Inversion Principle (DIP): The application has four main types of classes—main classes, service classes, app classes, and utility classes—which are organized to promote loose coupling. The high-level modules (App classes) depend on the abstract main classes, while lower-level services provide functionality, ensuring adaptability.

Open/Closed Principle (OCP): Extensibility is emphasized in the design. Data updates follow a consistent pattern: CSV records are read into objects, changes are made to the objects, and the updated object is then written back to the CSV file. This ensures changes are handled consistently without modifying core data-handling logic.

Single Responsibility Principle (SRP): Each class is designed to handle a specific function. For example, PatientService manages patient-related data, while PatientApp handles interactions for patient users, ensuring the separation of concerns.

Interface Segregation Principle (ISP): By creating specific interfaces for different entity types, each implementing class only needs to handle relevant methods. This prevents bloated classes and allows greater flexibility when extending functionality.

Polymorphism: Polymorphism is implemented primarily when retrieving data from CSV files. Different service classes (e.g., PatientService or DoctorService) share the same base (CSVService<T>) and override the CSV transformation methods to create their specific entities.

Build

Download the project from GitHub.

git clone https://github.com/SiaBatra/HMS-FINAL.git

Open the project in an IDE such as VSCode or IntelliJ.

The project is built with Java 17.

Run

To run the application, you can use the following command in the terminal:

java -cp out/production/HMS-FINAL hms.App

Alternatively, use your IDE to run the hms.App main class.

Javadoc Generation

To generate Javadoc documentation for this project:

javadoc -d ../docs hms/.java control/.java boundaries/.java entity/.java

License

MIT © Sia Batra, Chavi Goyal, Samriddhi Srivastava, Wahaj
