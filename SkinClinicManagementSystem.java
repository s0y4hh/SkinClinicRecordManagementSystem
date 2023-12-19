import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


enum SkinTreatmentDisease {
    ACNE(2500),
    PSORIASIS(1500),
    ECZEMA(1950),
    ROSACEA(1500),
    MELASMA(2550),
    VITILIGO(1500),
    DERMATITIS(1200),
    HYPERPIGMENTATIO(2500),
    URTICARIA(1100),
    WARTS(3500),
    FUNGAL_INFECTION(3450),
    ALLERGIC_REACTIONS(2000),
    SKIN_CANCER(20000);

    private double price;

    SkinTreatmentDisease(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}


class Patient implements Serializable {
    private String name;
    private int age;
    private String address;
    private char gender;
    private SkinTreatmentDisease treatmentDisease;
    private ArrayList<Appointment> appointments;
    private String contactNumber;
    private String emailAddress;
    private double treatmentPrice;

    public Patient(String name, int age, String address, char gender, SkinTreatmentDisease treatmentDisease) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.gender = gender;
        this.treatmentDisease = treatmentDisease;
        this.appointments = new ArrayList<>();
        this.contactNumber = "";
        this.emailAddress = "";
        this.treatmentPrice = calculateTreatmentPrice(treatmentDisease);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public char getGender() {
        return gender;
    }

    public SkinTreatmentDisease getTreatmentDisease() {
        return treatmentDisease;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }
    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    public double getTreatmentPrice() {
        return treatmentPrice;
    }
    private double calculateTreatmentPrice(SkinTreatmentDisease treatmentDisease) {
        return treatmentDisease.getPrice();
    }
    @Override
    public String toString() {
        return "Name: " + name + ", Age: " + age + ", Address: " + address + ", Gender: " + gender +
                ", Treatment Disease: " + treatmentDisease + ", Appointments: " + appointments;
    }
}

class Appointment implements Serializable {
    private String date;
    private String time;

    public Appointment(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Date: " + date + ", Time: " + time;
    }
}

class SkinClinic implements Serializable {
    private ArrayList<Patient> patients;

    public SkinClinic() {
        this.patients = new ArrayList<>();
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public ArrayList<Patient> getAllPatients() {
        return patients;
    }

    public void scheduleAppointment(Patient patient, String date, String time) {
        Appointment appointment = new Appointment(date, time);
        patient.addAppointment(appointment);
    }

    public ArrayList<Appointment> getAllAppointments() {
        ArrayList<Appointment> allAppointments = new ArrayList<>();
        for (Patient patient : patients) {
            allAppointments.addAll(patient.getAppointments());
        }
        return allAppointments;
    }
}
public class SkinClinicManagementSystem {
    private static final String DATA_FILE = "skinClinicRecords.csv";
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean isAdminLoggedIn = false;

    public static void main(String[] args) {
        SkinClinic skinClinic = loadSkinClinicData();

        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    adminLogin();
                    break;
                case 2:
                    addPatient(skinClinic);
                    break;
                case 3:
                    if (isAdminLoggedIn) {
                        scheduleAppointment(skinClinic);
                    } else {
                        System.out.println("\nAdmin login required to schedule appointments.\n");
                    }
                    break;
                case 4:
                    viewAllPatients(skinClinic);
                    break;
                case 5:
                    saveSkinClinicData(skinClinic);
                    System.out.println("\nData saved. Exiting Skin Clinic Records Management. Goodbye!\n");
                    System.exit(0);
                    break;
                case 6:
                    if (isAdminLoggedIn) {
                        displayAllAppointments(skinClinic);
                    } else {
                        System.out.println("\nAdmin login required to view all appointments.\n");
                    }
                    break;
                default:
                    System.out.println("\nInvalid choice. Please enter a valid option.\n");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("=====================================");
        System.out.println("    DERMATIQUE LOUNGE SKIN CLINIC");
        System.out.println("=====================================");
        System.out.println("1. Admin Login");
        System.out.println("2. Add Patient");
        System.out.println("3. Schedule Appointment (Admin Only)");
        System.out.println("4. View All Patients (Admin only)");
        System.out.println("5. Save and Exit");
        System.out.println("6. View All Appointments (Admin Only)");
        System.out.print("Enter your choice: ");
    }

    private static void displayAllAppointments(SkinClinic skinClinic) {
        ArrayList<Appointment> allAppointments = skinClinic.getAllAppointments();
    
        if (allAppointments.isEmpty()) {
            System.out.println("\nNo appointments scheduled.\n");
        } else {
            System.out.println("\nAll Scheduled Appointments:");
            for (Appointment appointment : allAppointments) {
                Patient patient = findPatientByAppointment(skinClinic, appointment);
                System.out.println("Patient: " + patient.getName() +
                        ", Age: " + patient.getAge() +
                        ", Treatment: " + patient.getTreatmentDisease() +
                        ", " + appointment);
            }
        }
    }
    
    private static Patient findPatientByAppointment(SkinClinic skinClinic, Appointment appointment) {
        for (Patient patient : skinClinic.getAllPatients()) {
            if (patient.getAppointments().contains(appointment)) {
                return patient;
            }
        }
        return null;
    }
    
    private static void adminLogin() {
        Console console = System.console();
    
        if (console != null) {
            System.out.print("\nEnter admin username: ");
            String username = scanner.nextLine();
    
            char[] passwordChars = console.readPassword("Enter admin password: ");
            String password = new String(passwordChars);
    
            if ("admin".equals(username) && "admin123".equals(password)) {
                isAdminLoggedIn = true;
                System.out.println("\nAdmin login successful!\n");
            } else {
                System.out.println("\nInvalid credentials. Admin login failed.\n");
            }
    
            // Clear the password from memory
            java.util.Arrays.fill(passwordChars, ' ');
        } else {
            System.out.println("\nConsole not available. Unable to hide password.");
        }
    }

    private static void addPatient(SkinClinic skinClinic) {
        System.out.print("Enter patient name: ");
        String name = scanner.nextLine();
        System.out.print("Enter patient age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.print("Enter patient address: ");
        String address = scanner.nextLine();

        System.out.print("Enter patient contact number: ");
        String contactNumber = scanner.nextLine();

        System.out.print("Enter patient email address: ");
        String emailAddress = scanner.nextLine();

        System.out.print("Select gender (M/F): ");
        char gender = scanner.nextLine().toUpperCase().charAt(0);

        System.out.println("\nSelect treatment disease:");
        for (SkinTreatmentDisease disease : SkinTreatmentDisease.values()) {
            System.out.println("[" + (disease.ordinal() + 1) + "] " + disease.name());
        }
        System.out.print("Enter your choice: ");
        int diseaseChoice = scanner.nextInt();
        SkinTreatmentDisease selectedDisease = SkinTreatmentDisease.values()[diseaseChoice - 1];

        Patient newPatient = new Patient(name, age, address, gender, selectedDisease);
        newPatient.setContactNumber(contactNumber);
        newPatient.setEmailAddress(emailAddress);

        skinClinic.addPatient(newPatient);

        System.out.println("\nPatient added successfully!\n");
    }
    
    private static void scheduleAppointment(SkinClinic skinClinic) {
        System.out.print("\nEnter patient name for appointment: ");
        String patientName = scanner.nextLine();
        Patient selectedPatient = findPatientByName(skinClinic, patientName);

        if (selectedPatient != null) {
            System.out.print("Enter appointment date (YYYY-MM-DD): ");
            String date = scanner.nextLine();
            System.out.print("Enter appointment time (HH:MM): ");
            String time = scanner.nextLine();

            skinClinic.scheduleAppointment(selectedPatient, date, time);
            System.out.println("\nAppointment scheduled successfully!\n");
        } else {
            System.out.println("\nPatient not found.\n");
        }
    }

    private static void viewAllPatients(SkinClinic skinClinic) {
        if (isAdminLoggedIn) {
            ArrayList<Patient> allPatients = skinClinic.getAllPatients();
            System.out.println("\nAll Patients:");
            for (Patient patient : allPatients) {
                System.out.println(patient);
            }
        } else {
            System.out.println("\nAdmin login required to view all patients.\n");
        }
    }
    

    private static Patient findPatientByName(SkinClinic skinClinic, String name) {
        for (Patient patient : skinClinic.getAllPatients()) {
            if (patient.getName().equalsIgnoreCase(name)) {
                return patient;
            }
        }
        return null;
    }



private static void saveSkinClinicData(SkinClinic skinClinic) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
        // Write header
        writer.println("Name,Age,Address,Gender,ContactNumber,EmailAddress,TreatmentDisease,TreatmentPrice,Date,Time");

        // Write patient data with appointments
        for (Patient patient : skinClinic.getAllPatients()) {
            for (Appointment appointment : patient.getAppointments()) {
                writer.println(
                        patient.getName() + "," +
                                patient.getAge() + "," +
                                patient.getAddress() + "," +
                                patient.getGender() + "," +
                                patient.getContactNumber() + "," +
                                patient.getEmailAddress() + "," +
                                patient.getTreatmentDisease() + "," +
                                patient.getTreatmentPrice() + "," +
                                appointment.getDate() + "," +
                                appointment.getTime()
                );
            }
        }

        System.out.println("\nData saved successfully.");
    } catch (IOException e) {
        System.err.println("Error saving data: " + e.getMessage());
    }
}

    
    private static SkinClinic loadSkinClinicData() {
        SkinClinic skinClinic = new SkinClinic();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            // Skip header line
            reader.readLine();
    
            String line;
            Patient currentPatient = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                int age = Integer.parseInt(parts[1]);
                String address = parts[2];
                char gender = parts[3].charAt(0);
                String contactNumber = parts[4];
                String emailAddress = parts[5];
                SkinTreatmentDisease treatmentDisease = SkinTreatmentDisease.valueOf(parts[6]);
                String date = parts[7];
                String time = parts[8];
    
                if (currentPatient == null || !currentPatient.getName().equals(name)) {
                    // New patient
                    currentPatient = new Patient(name, age, address, gender, treatmentDisease);
                    currentPatient.setContactNumber(contactNumber);
                    currentPatient.setEmailAddress(emailAddress);
                    skinClinic.addPatient(currentPatient);
                }
    
                // Add appointment to the current patient
                currentPatient.addAppointment(new Appointment(date, time));
            }
    
            System.out.println("Data loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    
        return skinClinic;
    }
    
}
