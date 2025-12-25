import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * MAIN PROGRAM: VehicleRentalSystem
 * Moved to the top of the file to ensure the environment identifies 
 * the entry point correctly.
 */
public class VehicleRentalSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Vehicle> fleet = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   CAR RENTAL AGENCY INFORMATION SYSTEM   ");
        System.out.println("==========================================");

        boolean exit = false;
        while (!exit) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addVehicle("Car");
                case "2" -> addVehicle("Motorcycle");
                case "3" -> addVehicle("Truck");
                case "4" -> displayAllVehicles();
                case "5" -> {
                    System.out.println("Shutting down system...");
                    exit = true;
                }
                default -> System.out.println("Invalid selection. Please enter 1-5.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Add a Car");
        System.out.println("2. Add a Motorcycle");
        System.out.println("3. Add a Truck");
        System.out.println("4. Display Inventory");
        System.out.println("5. Exit");
        System.out.print("Selection: ");
    }

    private static void addVehicle(String type) {
        try {
            System.out.print("Enter Make: ");
            String make = scanner.nextLine();
            System.out.print("Enter Model: ");
            String model = scanner.nextLine();
            
            int year = getValidInteger("Enter Year of Manufacture: ");

            if (type.equals("Car")) {
                Car car = new Car(make, model, year);
                car.setNumDoors(getValidInteger("Number of Doors: "));
                System.out.print("Fuel Type (Petrol/Diesel/Electric): ");
                car.setFuelType(scanner.nextLine());
                fleet.add(car);
            } 
            else if (type.equals("Motorcycle")) {
                Motorcycle bike = new Motorcycle(make, model, year);
                bike.setNumWheels(getValidInteger("Number of Wheels: "));
                System.out.print("Motorcycle Type (Sport/Cruiser/Off-road): ");
                bike.setMotorcycleType(scanner.nextLine());
                fleet.add(bike);
            } 
            else if (type.equals("Truck")) {
                Truck truck = new Truck(make, model, year);
                System.out.print("Cargo Capacity (tons): ");
                String capInput = scanner.nextLine();
                truck.setCargoCapacity(Double.parseDouble(capInput));
                System.out.print("Transmission Type (Manual/Automatic): ");
                truck.setTransmissionType(scanner.nextLine());
                fleet.add(truck);
            }
            System.out.println("\nSUCCESS: " + type + " added to system.");
        } catch (Exception e) {
            System.out.println("\nERROR: Invalid input provided. Vehicle creation aborted.");
        }
    }

    private static void displayAllVehicles() {
        System.out.println("\n--- CURRENT FLEET INVENTORY ---");
        if (fleet.isEmpty()) {
            System.out.println("The inventory is currently empty.");
        } else {
            for (int i = 0; i < fleet.size(); i++) {
                System.out.print((i + 1) + ". ");
                fleet.get(i).displayDetails();
            }
        }
    }

    private static int getValidInteger(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Input Error: Please enter a whole number.");
            }
        }
    }
}

/**
 * VEHICLE INTERFACE HIERARCHY
 */

interface Vehicle {
    String getMake();
    String getModel();
    int getYear();
    void displayDetails();
}

interface CarVehicle {
    void setNumDoors(int doors);
    int getNumDoors();
    void setFuelType(String fuelType);
    String getFuelType();
}

interface MotorVehicle {
    void setNumWheels(int wheels);
    int getNumWheels();
    void setMotorcycleType(String type);
    String getMotorcycleType();
}

interface TruckVehicle {
    void setCargoCapacity(double capacity);
    double getCargoCapacity();
    void setTransmissionType(String transmission);
    String getTransmissionType();
}

/**
 * CONCRETE CLASS IMPLEMENTATIONS
 */

class Car implements Vehicle, CarVehicle {
    private String make, model, fuelType;
    private int year, numDoors;

    public Car(String make, String model, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
    }

    @Override public String getMake() { return make; }
    @Override public String getModel() { return model; }
    @Override public int getYear() { return year; }
    @Override public void setNumDoors(int doors) { this.numDoors = doors; }
    @Override public int getNumDoors() { return numDoors; }
    @Override public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    @Override public String getFuelType() { return fuelType; }

    @Override
    public void displayDetails() {
        System.out.println("Type: Car | " + year + " " + make + " " + model + 
                           " | Doors: " + numDoors + " | Fuel: " + fuelType);
    }
}

class Motorcycle implements Vehicle, MotorVehicle {
    private String make, model, type;
    private int year, wheels;

    public Motorcycle(String make, String model, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
    }

    @Override public String getMake() { return make; }
    @Override public String getModel() { return model; }
    @Override public int getYear() { return year; }
    @Override public void setNumWheels(int wheels) { this.wheels = wheels; }
    @Override public int getNumWheels() { return wheels; }
    @Override public void setMotorcycleType(String type) { this.type = type; }
    @Override public String getMotorcycleType() { return type; }

    @Override
    public void displayDetails() {
        System.out.println("Type: Motorcycle | " + year + " " + make + " " + model + 
                           " | Wheels: " + wheels + " | Style: " + type);
    }
}

class Truck implements Vehicle, TruckVehicle {
    private String make, model, transmission;
    private int year;
    private double capacity;

    public Truck(String make, String model, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
    }

    @Override public String getMake() { return make; }
    @Override public String getModel() { return model; }
    @Override public int getYear() { return year; }
    @Override public void setCargoCapacity(double capacity) { this.capacity = capacity; }
    @Override public double getCargoCapacity() { return capacity; }
    @Override public void setTransmissionType(String transmission) { this.transmission = transmission; }
    @Override public String getTransmissionType() { return transmission; }

    @Override
    public void displayDetails() {
        System.out.println("Type: Truck | " + year + " " + make + " " + model + 
                           " | Capacity: " + capacity + " Tons | Gearbox: " + transmission);
    }
}
