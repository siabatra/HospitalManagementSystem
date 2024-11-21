package boundaries;
import entity.*;
import control.*;
import hms.*;
import java.util.List;
import java.util.Scanner;

import control.AppointmentService;
import control.AvailabilityService;
import control.OutcomeService;
import hms.Utility;

/**
 * Represents the Doctor Application, which allows doctors to manage their appointments,
 * patient medical records, availability slots, and other functionalities within the Hospital Management System.
 */
public class DoctorApp {
    private final Scanner sc = new Scanner(System.in);
    private final AppointmentService appointmentService = new AppointmentService();
    private final AvailabilityService availabilityService = new AvailabilityService();
    private static final OutcomeService outcomeService = new OutcomeService();
    private Doctor doctor;

    /**
     * Constructs a DoctorApp instance for the specified doctor.
     *
     * @param doctor the doctor using the application
     */
    public DoctorApp(Doctor doctor) {
        this.doctor = doctor;
    }

    /**
     * Starts the Doctor application, allowing the doctor to interact with various features.
     */
    public void run() {
        boolean exit = false;
        while (!exit) {
            switch (ApplicationMenus.doctorMenu()) {
                case 1 -> viewPatientMedicalRecords();
                case 2 -> updatePatientMedicalRecords();
                case 3 -> viewPersonalSchedule();
                case 4 -> setAvailabilitySlots();
                case 5 -> acceptOrDeclineAppointment();
                case 6 -> viewUpcomingAppointments();
                case 7 -> recordAppointmentOutcome();
                case 8 -> {
                    exit = true;
                    System.out.println("Logging out...");
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Prompts the doctor to reset their password during the first login.
     */
    private void resetPassword() {
        String newPassword;
        System.out.println("****** First Login - Password Reset ******");
        while (true) {
            System.out.print("Enter a new password (min 8 characters, 1 uppercase, 1 lowercase, 1 special char, 1 number): ");
            newPassword = sc.nextLine();

            if (Utility.validatePassword(newPassword)) {
                doctor.setPassword(newPassword);
                System.out.println("Password changed successfully!");
                break;
            } else {
                System.out.println("Invalid password. Please ensure it meets all the criteria.");
            }
        }
    }

    /**
     * Displays the medical records of patients under the doctor's care.
     */
    private void viewPatientMedicalRecords() {
        System.out.println("******* PATIENT MEDICAL RECORDS *******");
        List<Patient> patientsUnderDoc = doctor.getPatientsUnderDoctorCare();

        if(patientsUnderDoc.isEmpty()){
            System.out.println("No patients currently under your care.");
            System.out.println("**********************************************");
            return;
        }
        
        for (Patient patient : patientsUnderDoc) {
            System.out.println(patient.getDetails());
        }
        System.out.println("***************************************");
    }

    /**
     * Updates the medical records of a selected patient under the doctor's care.
     */
    private void updatePatientMedicalRecords() {
        System.out.println("******* UPDATE PATIENT MEDICAL RECORDS *******");
    
        List<Patient> patientsUnderDoc = doctor.getPatientsUnderDoctorCare();

        if(patientsUnderDoc.isEmpty()){
            System.out.println("No patients currently under your care.");
            System.out.println("**********************************************");
            return;
        }

        int i = 1;
        for (Patient patient : patientsUnderDoc) {
            System.out.println("(" + i++ + ") " + patient.getUserId() + " - " + patient.getName());
        }

        System.out.print("\nChoose patient: ");
        int choice=0;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }
        
        if(choice > patientsUnderDoc.size() || choice < 0){
            System.out.println("Invalid input");
            return;
        }

        Patient chosenPatient = patientsUnderDoc.get(choice - 1);

        System.out.println("\nChosen patient: ");
        System.out.println(chosenPatient.getDetails());

        System.out.print("Enter diagnosis (Press s to skip): ");
        String entry = sc.nextLine();
        if (!entry.equalsIgnoreCase("s"))
            chosenPatient.addDiagnosis(entry);

        System.out.print("Enter treatment (Press s to skip): ");
        entry = sc.nextLine();
        if (!entry.equalsIgnoreCase("s"))
            chosenPatient.addTreatment(entry);

        System.out.println("**********************************************");
    }

    /**
     * Views the doctor's personal schedule, including upcoming appointments and availability slots.
     */
    private void viewPersonalSchedule() {
        viewUpcomingAppointments();

        List<AvailabilitySlot> availabilitySlots = availabilityService.getAll();
        List<AvailabilitySlot> docSlots = AvailabilitySlot.filterslots(availabilitySlots, "doctorId", doctor.getUserId());

        System.out.println("******* AVAILABILITY SLOTS *******");
        if(availabilitySlots.isEmpty())
        {
            System.out.println("No open availability slots.");
            System.out.println("**********************************");
            return;
        }
        for (AvailabilitySlot slot : docSlots) {
            System.out.print(slot.getDetails());
        }
        System.out.println("**********************************");
    }

    /**
     * Views the upcoming confirmed appointments for the doctor.
     *
     * @return a list of upcoming appointments for the doctor
     */
    private List<AppointmentRecord> viewUpcomingAppointments() {
        System.out.println("******* SCHEDULED APPOINTMENTS *******");
        List<AppointmentRecord> appointments = appointmentService.getAll();
        List<AppointmentRecord> doctorAppointments = AppointmentService.filterAppointmentsByDoctorId(appointments, doctor.getUserId());
        List<AppointmentRecord> upcomingDoctorAppointments = AppointmentService.filterAppointmentsByStatus(doctorAppointments, "CONFIRMED");

        if(upcomingDoctorAppointments.isEmpty()){
            System.out.println("No upcoming appointments.");
            System.out.println("***********************************");
            return upcomingDoctorAppointments;
        }
        int i = 1;
        for (AppointmentRecord ar : upcomingDoctorAppointments) {
            System.out.print("(" + i++ + ") ");
            System.out.println(ar.getDetails());
        }
        System.out.println("***********************************");

        return upcomingDoctorAppointments;
    }

    /**
     * Sets availability slots for the doctor.
     * Allows the doctor to pick 30-minute time slots for specific days.
     */
    private void setAvailabilitySlots() {
        try{
            System.out.println("******* AVAILABILITY CALENDAR *******");
            System.out.println("(1) Monday (2) Tuesday (3) Wednesday (4) Thursday (5) Friday (6) Saturday");
            int day=0;
            try {
                day = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                return;
            }

            if (day < 1 || day > 6) {
                System.out.println("Invalid day. Please select a valid day.");
                return;
            }

            AvailabilitySlot.displayTimeSlotMenu();
            int startTimeOption=0;
            try {
                startTimeOption = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                return;
            }

            if (startTimeOption < 1 || startTimeOption > 16) {
                System.out.println("Invalid time slot. Please select a valid 30-minute time.");
                return;
            }

            double startTime = AvailabilitySlot.getStartTimeForSlot(startTimeOption);

            if (startTime == -1) {
                System.out.println("Invalid time slot. Please try again.");
                return;
            }

            AvailabilitySlot avSlot = new AvailabilitySlot(doctor);
            avSlot.addAvailability(day, startTime);

            System.out.println("Availability slot added successfully.");
        }
        catch(IllegalArgumentException e){
            System.out.println("Invalid input");
            return;
        }
    }

    /**
     * Allows the doctor to accept or decline pending appointment requests.
     */
    public void acceptOrDeclineAppointment() {
        List<AppointmentRecord> appointments = appointmentService.getAll();
        List<AppointmentRecord> doctorAppointments = AppointmentService.filterAppointmentsByDoctorId(appointments, doctor.getUserId());
        List<AppointmentRecord> pendingDoctorAppointments = AppointmentService.filterAppointmentsByStatus(doctorAppointments, "PENDING");

        System.out.println("******* ACCEPT/DECLINE APPOINTMENTS *******");

        if(pendingDoctorAppointments.isEmpty()){
            System.out.println("No pending appointment requests.");
            System.out.println("*******************************************");
            return;
        }

        boolean continueProcessing = true;
        while (continueProcessing && !pendingDoctorAppointments.isEmpty()) {
            System.out.println("Pending Appointments:");
            int i = 1;
            for (AppointmentRecord appointment : pendingDoctorAppointments) {
                System.out.print("(" + i++ + ") " + appointment.getDetails());
            }
            System.out.println("(0) quit");

            System.out.println("Choose appointment to accept/decline: ");
            int choice=0;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                return;
            }
    
            if(choice > pendingDoctorAppointments.size() || choice < 0){
                System.out.println("Invalid input");
                return;
            }

            if (choice == 0) {
                continueProcessing = false;
                continue;
            }

            AppointmentRecord selectedAppointment = pendingDoctorAppointments.get(choice - 1);

            System.out.print("Chosen appointment: " + selectedAppointment.getDetails());

            System.out.println("(1) Accept\n(2) Decline");
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                return;
            }
            switch (choice) {
                case 1:
                    selectedAppointment.setStatus("CONFIRMED");
                    System.out.println("Appointment accepted!");
                    break;
                case 2:
                    selectedAppointment.setStatus("CANCELLED");
                    System.out.println("Appointment cancelled!");
                    break;
                default:
                    System.out.println("Invalid input");
                    return;
            }

            pendingDoctorAppointments.remove(selectedAppointment);
        }
        System.out.println("*******************************************");
        
    }

    /**
     * Records the outcome of a completed appointment.
     */
    public void recordAppointmentOutcome() {
        System.out.println("******* RECORD APPOINTMENT OUTCOME *******");
        List<AppointmentRecord> docAppointments = viewUpcomingAppointments();

        if(docAppointments.isEmpty()){
            return;
        }

        try{
            System.out.println("Choose appointment: ");
            int choice = Integer.parseInt(sc.nextLine());
    
            if(choice > docAppointments.size() || choice < 0){
                System.out.println("Invalid input");
                return;
            }

            AppointmentRecord chosenAppointment = docAppointments.get(choice - 1);

            chosenAppointment.setStatus("COMPLETED");

            System.out.print("Type of service: ");
            String service = sc.nextLine();
            System.out.print("Consultation Notes: ");
            String consultationNotes = sc.nextLine();

            AppointmentOutcome appOutcome = new AppointmentOutcome(chosenAppointment.getAppointmentId(), chosenAppointment.getPatientId(), chosenAppointment.getDoctorId(), chosenAppointment.getAppointmentDate(), chosenAppointment.getAppointmentTime(), chosenAppointment.getStatus(), service, consultationNotes);

            try{
                System.out.println("Prescription: ");
                boolean continueProcessing = true;
                while (continueProcessing) {
                    System.out.println("(1) Add Medication");
                    System.out.println("(2) STOP");
                    choice = Integer.parseInt(sc.nextLine());
                    if (choice == 1) {
                        System.out.print("Medicine Name: ");
                        String medicineName = sc.nextLine();
                        System.out.print("Quantity: ");
                        int quantity = Integer.parseInt(sc.nextLine());
                        String status = "PENDING";

                        PrescriptionEntry pEntry = new PrescriptionEntry(medicineName, quantity, status);
                        appOutcome.addPrescriptionEntry(pEntry);
                    } else if (choice == 2) {
                        continueProcessing = false;
                    }
                    else{
                        System.out.println("Invalid input");
                        continue;
                    }
                }
            }
            catch(IllegalArgumentException e){
                System.out.println("Invalid input");
                return;
            }
            outcomeService.addToFile(appOutcome);
            appointmentService.removeFromFile(chosenAppointment.getAppointmentId(), 0);

            System.out.println("Outcome Record added successfully!");
            System.out.println("***********************************");
        }
        catch(IllegalArgumentException e){
            System.out.println("Invalid input");
            return;
        }
    }
}
