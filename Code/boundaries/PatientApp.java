package boundaries;
import entity.*;
import control.*;
import hms.*;
import java.util.Scanner;

import control.AppointmentService;
import control.AvailabilityService;
import control.OutcomeService;
import control.StaffService;

import java.util.ArrayList;
import java.util.List;

/**
 * The PatientApp class provides a console-based interface for patients to interact with the hospital management system.
 * This class allows patients to view and manage their medical records, appointments, and other services provided by the hospital.
 */
public class PatientApp {
    private static final AppointmentService appointmentService = new AppointmentService();
    private static final OutcomeService outcomeService = new OutcomeService();
    private final AvailabilityService availabilityService = new AvailabilityService();
    private static final StaffService staffService = new StaffService();
    private final Scanner sc = new Scanner(System.in);
    private Patient patient;

    /**
     * Constructs a PatientApp instance with the given patient.
     * 
     * @param patient the patient using the application
     */
    public PatientApp(Patient patient) {
        this.patient = patient;
    }

    /**
     * Runs the main menu for the patient application.
     * Allows the patient to choose from various actions until they choose to log out.
     */
    public void run() {
        boolean exit = false;
        while (!exit) {
            switch (ApplicationMenus.patientMenu()) {
                case 1 -> viewMedicalRecord();
                case 2 -> updateContactInfo();
                case 3 -> viewAvailableSlots();
                case 4 -> scheduleAppointment();
                case 5 -> rescheduleAppointment();
                case 6 -> cancelAppointment();
                case 7 -> viewScheduledAppointments();
                case 8 -> viewPastAppointmentOutcomes();
                case 9 -> {
                    exit = true;
                    System.out.println("Logging out...");
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Displays the patient's medical record.
     */
    private void viewMedicalRecord() {
        System.out.println("******* MEDICAL RECORD ********");
        System.out.println(patient.getDetails());
        System.out.println("*******************************");
    }

    /**
     * Updates the patient's contact information.
     */
    private void updateContactInfo() {
        System.out.println("******* UPDATE CONTACT INFO ********");
        System.out.print("Enter new contact information: ");
        String newContactInfo = sc.nextLine();
        patient.setContactInfo(newContactInfo);
        System.out.println("Contact information updated successfully.");
        System.out.println("************************************");
    }

    /**
     * Views all available appointment slots.
     */
    private void viewAvailableSlots() {
        AvailabilitySlot.viewAvailabilities();
    }

    /**
     * Schedules an appointment for the patient by selecting an available slot.
     */
    private void scheduleAppointment() {
        String[] slot = AvailabilitySlot.pickAvailability();

        if(slot == null){
            return;
        }

        String appointmentId = "A" + System.currentTimeMillis();
        String patientId = patient.getUserId();
        String doctorId = slot[0];
        String appointmentDate = slot[2];
        String appointmentTime = slot[3];
        String status = "PENDING";

        AppointmentRecord appointmentRecord = new AppointmentRecord(
            appointmentId,
            patientId,
            doctorId,
            appointmentDate,
            appointmentTime,
            status
        );

        appointmentService.addToFile(appointmentRecord);
        availabilityService.removeFromFile(slot);

        System.out.println("Appointment request scheduled successfully!");
    }

    /**
     * Reschedules an existing appointment by choosing a new time slot.
     */
    private void rescheduleAppointment() {
        System.out.println("******* RESCHEDULE APPOINTMENT *******");
        List<AvailabilitySlot> slots = availabilityService.getAll();

        if(slots.isEmpty()){
            System.out.println("No alternative time slots available yet.");
            System.out.println("**************************************");
            return;
        }
        List<AppointmentRecord> schdAppointments = viewScheduledAppointments();
        System.out.println("Which appointment would you like to reschedule?");
        int choice=0;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        if(choice > schdAppointments.size() || choice < 0){
            System.out.println("Invalid input");
            return;
        }

        AppointmentRecord oldApp = schdAppointments.get(choice - 1);

        System.out.println("Pick new time slot: ");
        scheduleAppointment();

        AvailabilitySlot freed = new AvailabilitySlot((Doctor) staffService.getStaffById(oldApp.getDoctorId()), oldApp.getAppointmentDate(), oldApp.getAppointmentTime());
        availabilityService.addToFile(freed);

        appointmentService.removeFromFile(oldApp.getAppointmentId(), 0);
        System.out.println("**************************************");
    }

    /**
     * Views the patient's scheduled appointments.
     * 
     * @return a list of the patient's scheduled appointments
     */
    private List<AppointmentRecord> viewScheduledAppointments() {
        System.out.println("******* SCHEDULED APPOINTMENTS *******");
        List<AppointmentRecord> appointments = appointmentService.getAll();
        List<AppointmentRecord> patientAppointments = AppointmentService.filterAppointmentsByPatientId(appointments, patient.getUserId());

        List<AppointmentRecord> pendingPatientAppointments = AppointmentService.filterAppointmentsByStatus(patientAppointments, "PENDING");
        List<AppointmentRecord> confirmedPatientAppointments = AppointmentService.filterAppointmentsByStatus(patientAppointments, "CONFIRMED");

        List<AppointmentRecord> upcomingPatientAppointments = new ArrayList<>(confirmedPatientAppointments);
        upcomingPatientAppointments.addAll(pendingPatientAppointments);

        if(upcomingPatientAppointments.isEmpty()){
            System.out.println("No upcoming appointments.");
            System.out.println("***********************************");
            return upcomingPatientAppointments;
        }

        int i = 1;
        for (AppointmentRecord ar : upcomingPatientAppointments) {
            System.out.print("(" + i++ + ") ");
            System.out.println(ar.getDetails());
        }
        System.out.println("***********************************");

        return upcomingPatientAppointments;
    }

    /**
     * Cancels an existing appointment and makes the time slot available again.
     */
    private void cancelAppointment() {
        System.out.println("******* CANCEL APPOINTMENT *******");
        List<AppointmentRecord> schdAppointments = viewScheduledAppointments();

        if(schdAppointments.isEmpty()){
            return;
        }

        System.out.println("Which appointment would you like to cancel?");
        int choice=0;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        if(choice > schdAppointments.size() || choice < 0){
            System.out.println("Invalid input");
            return;
        }

        AppointmentRecord oldApp = schdAppointments.get(choice - 1);

        AvailabilitySlot freed = new AvailabilitySlot((Doctor) staffService.getStaffById(oldApp.getDoctorId()), oldApp.getAppointmentDate(), oldApp.getAppointmentTime());
        availabilityService.addToFile(freed);
        appointmentService.removeFromFile(oldApp.getAppointmentId(), 0);

        System.out.println("Appointment cancelled successfully!");
        System.out.println("**********************************");
    }

    /**
     * Views the patient's past appointment outcomes.
     * 
     * @return a list of the patient's past appointment outcomes
     */
    private List<AppointmentOutcome> viewPastAppointmentOutcomes() {
        System.out.println("******* PAST APPOINTMENT RECORDS *******");
        List<AppointmentOutcome> outcomes = outcomeService.getAll();
        List<AppointmentOutcome> patientOutcomes = OutcomeService.filterOutcomesByPatientId(outcomes, patient.getUserId());

        if(patientOutcomes.isEmpty()){
            System.out.println("No past appointment outcomes.");
            System.out.println("***********************************");
            return patientOutcomes;
        }

        int i = 1;
        for (AppointmentOutcome appOutcome : patientOutcomes) {
            System.out.print("(" + i++ + ") ");
            System.out.println(appOutcome.getDetails());
        }
        System.out.println("***********************************");

        return patientOutcomes;
    }
}
