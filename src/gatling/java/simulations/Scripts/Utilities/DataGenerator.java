package simulations.Scripts.Utilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Utility class for generating random test data for various fields.
 */
public class DataGenerator {
    private static final Random random = new Random();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private static final String[] FIRST_NAMES = {"John", "Jane", "Michael", "Sarah", "David", "Emma", "Robert", "Lisa", "James", "Mary"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};
    private static final String[] STREETS = {"Main Street", "Oak Avenue", "Elm Road", "Park Lane", "High Street", "Church Street", "School Road", "Castle Drive"};
    private static final String[] CITIES = {"London", "Manchester", "Birmingham", "Leeds", "Glasgow", "Sheffield", "Bristol", "Edinburgh"};
    private static final String[] POSTCODES = {"SW1A 1AA", "M1 1AD", "B1 1BD", "LS1 1UR", "G2 1BB", "S1 1WA", "BS1 3AQ", "EH8 8DX"};
    private static final String[] VEHICLE_MAKES = {"Ford", "BMW", "Volkswagen", "Audi", "Toyota", "Honda", "Mercedes-Benz", "Vauxhall"};
    private static final String[] EMPLOYERS = {"Tech Corp", "Finance Ltd", "Health Services", "Retail Group", "Manufacturing Co", "Education Board", "Transport Inc"};
    private static final String[] GENDERS = {"Male", "Female", "Other"};
    private static final String[] ETHNICITIES = {"White", "Black", "Asian", "Mixed", "Other"};
    private static final String[] OCCUPATIONS = {"Engineer", "Doctor", "Teacher", "Accountant", "Manager", "Developer", "Nurse", "Technician"};
    private static final String[] NATIONALITIES = {"British", "Irish", "French", "German", "Spanish", "Italian", "Polish", "Chinese"};
    private static final String[] TITLES = {"Mr", "Mrs", "Miss", "Ms", "Dr", "Prof"};
    
    public static String generateRandomFirstName() {
        return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
    }
    
    public static String generateRandomLastName() {
        return LAST_NAMES[random.nextInt(LAST_NAMES.length)];
    }
    
    public static String generateRandomAddress() {
        return randomNumber(1, 999) + " " + STREETS[random.nextInt(STREETS.length)];
    }
    
    public static String generateRandomCity() {
        return CITIES[random.nextInt(CITIES.length)];
    }
    
    public static String generateRandomPostcode() {
        return POSTCODES[random.nextInt(POSTCODES.length)];
    }
    
    public static String generateRandomEmailAddress() {
        return generateRandomFirstName().toLowerCase() + "." + generateRandomLastName().toLowerCase() 
            + randomNumber(1000, 9999) + "@example.com";
    }
    
    public static String generateRandomPhoneNumber() {
        return "0" + randomNumber(1, 9) + randomNumber(1000000000, 9999999999L);
    }
    
    public static String generateRandomDrivingLicenceNumber() {
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            result.append(alpha.charAt(random.nextInt(alpha.length())));
        }
        result.append(randomNumber(100000, 999999));
        result.append(alpha.charAt(random.nextInt(alpha.length())));
        result.append(randomNumber(1, 9));
        result.append(alpha.charAt(random.nextInt(alpha.length())));
        result.append(randomNumber(1, 9));
        return result.toString();
    }
    
    public static String generateRandomVehicleRegistration() {
        return randomNumber(10, 99) + randomLetter() + randomLetter() + " " 
            + randomNumber(10, 99) + randomLetter() + randomLetter();
    }
    
    public static String generateRandomVehicleMake() {
        return VEHICLE_MAKES[random.nextInt(VEHICLE_MAKES.length)];
    }
    
    public static String generateRandomNationalInsuranceNumber() {
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            result.append(alpha.charAt(random.nextInt(alpha.length())));
        }
        result.append(randomNumber(100000, 999999));
        result.append(randomLetter());
        return result.toString();
    }
    
    public static String generateRandomPNCId() {
        return "PNC" + randomNumber(1000000, 9999999);
    }
    
    public static String generateRandomCRONumber() {
        return randomNumber(1000, 9999) + "/" + randomNumber(1000000, 9999999);
    }
    
    public static String generateRandomPrisonNumber() {
        return "A" + randomNumber(1000000, 9999999);
    }
    
    public static String generateRandomEmployeeReference() {
        return "EMP" + randomNumber(100000, 999999);
    }
    
    public static String generateRandomEmployerPostcode() {
        return POSTCODES[random.nextInt(POSTCODES.length)];
    }
    
    public static String generateRandomEmployerName() {
        return EMPLOYERS[random.nextInt(EMPLOYERS.length)];
    }
    
    public static String generateRandomGender() {
        return GENDERS[random.nextInt(GENDERS.length)];
    }
    
    public static String generateRandomEthnicity() {
        return ETHNICITIES[random.nextInt(ETHNICITIES.length)];
    }
    
    public static String generateRandomOccupation() {
        return OCCUPATIONS[random.nextInt(OCCUPATIONS.length)];
    }
    
    public static String generateRandomNationality() {
        return NATIONALITIES[random.nextInt(NATIONALITIES.length)];
    }
    
    public static String generateRandomTitle() {
        return TITLES[random.nextInt(TITLES.length)];
    }
    
    public static String generateRandomLanguage() {
        String[] languages = {"English", "Welsh", "Urdu", "Punjabi", "Polish", "Romanian", "French"};
        return languages[random.nextInt(languages.length)];
    }
    
    public static String generateRandomDate(int daysBack) {
        LocalDate date = LocalDate.now().minusDays(random.nextInt(daysBack));
        return date.format(dateFormatter);
    }
    
    public static String generateRandomDateOfBirth() {
        // Generate age between 18 and 80
        LocalDate dob = LocalDate.now().minusYears(random.nextInt(62) + 18);
        return dob.format(dateFormatter);
    }
    
    public static long generateRandomNumber(long min, long max) {
        return min + (long) (random.nextDouble() * (max - min));
    }
    
    public static String generateRandomNoteText() {
        String[] notes = {
            "Account under review",
            "Payment arrangement proposed",
            "Enforcement action initiated",
            "Court summons issued",
            "Penalty notice served",
            "Appeal submitted",
            "Hearing scheduled"
        };
        return notes[random.nextInt(notes.length)];
    }
    
    public static String generateRandomOffenceDescription() {
        String[] offences = {
            "Speeding",
            "Parking violation",
            "Traffic signal violation",
            "Uninsured driving",
            "No MOT certificate",
            "Driving without license",
            "Vehicle tax evasion"
        };
        return offences[random.nextInt(offences.length)];
    }
    
    public static String generateRandomNoticeNumber() {
        return randomNumber(10000000000L, 99999999999L) + "";
    }
    
    public static String generateRandomFPRegistrationNumber() {
        return randomNumber(1000000, 9999999) + "";
    }
    
    // Helper methods
    private static long randomNumber(long min, long max) {
        return min + random.nextLong() % (max - min + 1);
    }
    
    private static int randomNumber(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }
    
    private static String randomLetter() {
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return String.valueOf(alpha.charAt(random.nextInt(alpha.length())));
    }

    private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final Random RANDOM = new Random();

    public String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
    return sb.toString();
    }
    
}
