import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents an Employee in the organization.
 */
class Employee {
    private String name;
    private int age;
    private String department;
    private double salary;

    public Employee(String name, int age, String department, double salary) {
        this.name = name;
        this.age = age;
        this.department = department;
        this.salary = salary;
    }

    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getDepartment() { return department; }
    public double getSalary() { return salary; }

    @Override
    public String toString() {
        return String.format("Employee[Name=%s, Age=%d, Dept=%s, Salary=%.2f]", 
                             name, age, department, salary);
    }
}

public class EmployeeProcessor {

    public static void main(String[] args) {
        // 1. Develop program and store dataset in a collection
        List<Employee> employees = Arrays.asList(
            new Employee("Alice Smith", 28, "Engineering", 75000),
            new Employee("Bob Jones", 35, "Marketing", 62000),
            new Employee("Charlie Brown", 42, "Engineering", 89000),
            new Employee("Diana Prince", 31, "HR", 58000),
            new Employee("Edward Norton", 25, "Sales", 45000),
            new Employee("Fiona Gallagher", 38, "Engineering", 95000)
        );

        System.out.println("--- Original Employee Dataset ---");
        employees.forEach(System.out::println);

        // 2. Function Interface: Concatenate name and department
        // Purpose: Takes Employee (T) and returns String (R)
        Function<Employee, String> nameDeptFormatter = emp -> 
            "Name: " + emp.getName() + " | Dept: " + emp.getDepartment();

        // 3. Using Streams to generate a new collection of strings
        // 5. Generalize with a filter function (Age > 30)
        int ageThreshold = 30;
        List<String> transformedData = employees.stream()
            .filter(emp -> emp.getAge() > ageThreshold) // Filter by age
            .map(nameDeptFormatter)                     // Apply the Function interface
            .collect(Collectors.toList());              // Terminal operation

        System.out.println("\n--- Concatenated Info (Age > " + ageThreshold + ") ---");
        transformedData.forEach(System.out::println);

        // 4. Find the average salary using stream's built-in functions
        OptionalDouble averageSalary = employees.stream()
            .mapToDouble(Employee::getSalary)
            .average();

        if (averageSalary.isPresent()) {
            System.out.printf("\nAverage Salary of all Employees: $%.2f%n", averageSalary.getAsDouble());
        }

        // ADDITIONAL FEATURE: Grouping Employees by Department
        // This demonstrates the versatility of Streams beyond simple filtering.
        Map<String, List<Employee>> employeesByDept = employees.stream()
            .collect(Collectors.groupingBy(Employee::getDepartment));

        System.out.println("\n--- Bonus: Employee Count by Department ---");
        employeesByDept.forEach((dept, list) -> 
            System.out.println(dept + ": " + list.size() + " employee(s)"));
    }
}


