import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class PasswordSecurityTool {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        System.out.println("====================================");
        System.out.println("   PASSWORD SECURITY TOOL");
        System.out.println("====================================");
        System.out.println("1. Check Password Strength");
        System.out.println("2. Generate Secure Password");
        System.out.print("\nChoose option (1 OR 2): ");
        
        int choice = input.nextInt();
        input.nextLine(); // Consume newline
        
        switch(choice) {
            case 1:
                checkPasswordStrength(input);
                break;
            case 2:
                generateSecurePassword(input);
                break;
            default:
                System.out.println("Invalid choice!");
        }
        
        input.close();
    }
    
    private static void checkPasswordStrength(Scanner input) {
        System.out.print("\nEnter password to analyze: ");
        String password = input.nextLine();
        
        int strength = 0;
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        
        // Check length
        if (password.length() >= 8) strength++;
        
        // Check character diversity
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }
        
        if (hasUpper) strength++;
        if (hasLower) strength++;
        if (hasDigit) strength++;
        if (hasSpecial) strength++;
        
        // Display results
        System.out.println("\n===== PASSWORD ANALYSIS =====");
        System.out.println("Length: " + password.length() + " characters");
        System.out.println("Contains uppercase: " + hasUpper);
        System.out.println("Contains lowercase: " + hasLower);
        System.out.println("Contains digits: " + hasDigit);
        System.out.println("Contains special chars: " + hasSpecial);
        
        System.out.print("\nSTRENGTH: ");
        if (strength <= 2) {
            System.out.println("WEAK (easily cracked)");
        } else if (strength <= 4) {
            System.out.println("MODERATE (needs improvement)");
        } else if (strength <= 6) {
            System.out.println("STRONG (good)");
        } else {
            System.out.println("VERY STRONG (excellent)");
        }
        
        logSecurityCheck(password, strength);
    }
    
    private static void generateSecurePassword(Scanner input) {
        System.out.print("\nEnter desired password length (8-20): ");
        int length = input.nextInt();
        
        if (length < 8 || length > 20) {
            System.out.println("Invalid length! Using 12 characters.");
            length = 12;
        }
        
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*()-_=+";
        
        String allChars = upper + lower + digits + special;
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        
        // Ensure at least one of each character type
        password.append(upper.charAt(random.nextInt(upper.length())));
        password.append(lower.charAt(random.nextInt(lower.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));
        
        // Fill remaining length
        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        
        // Shuffle the characters
        String shuffled = shuffleString(password.toString());
        
        System.out.println("\n===== GENERATED PASSWORD =====");
        System.out.println("Secure password: " + shuffled);
        logPasswordGeneration(shuffled);
    }
    
    private static String shuffleString(String input) {
        char[] chars = input.toCharArray();
        Random random = new Random();
        
        for (int i = 0; i < chars.length; i++) {
            int j = random.nextInt(chars.length);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        
        return new String(chars);
    }
    
    private static void logSecurityCheck(String password, int strength) {
        try (FileWriter writer = new FileWriter("security_log.txt", true)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            
            writer.write("\n\n[Password Check] " + dtf.format(LocalDateTime.now()));
            writer.write("\nPassword: " + maskPassword(password));
            writer.write("\nStrength: " + strength + "/7");
            writer.write("\n----------------------------------");
        } catch (IOException e) {
            System.out.println("Error saving log: " + e.getMessage());
        }
    }
    
    private static void logPasswordGeneration(String password) {
        try (FileWriter writer = new FileWriter("security_log.txt", true)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            
            writer.write("\n\n[Password Generated] " + dtf.format(LocalDateTime.now()));
            writer.write("\nPassword: " + password);
            writer.write("\n----------------------------------");
        } catch (IOException e) {
            System.out.println("Error saving log: " + e.getMessage());
        }
    }
    
    private static String maskPassword(String password) {
        if (password.length() <= 2) return "**";
        return password.charAt(0) + "****" + password.charAt(password.length()-1);
    }
}