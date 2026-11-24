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
    
    private static final String[] FIRST_NAMES = {
        "John", "Jane", "Michael", "Sarah", "David", "Emma", "Robert", "Lisa", "James", "Mary",
        "Daniel", "Emily", "William", "Olivia", "Thomas", "Sophie", "George", "Amelia", "Charles", "Grace",
        "Joseph", "Ella", "Edward", "Charlotte", "Henry", "Jessica", "Samuel", "Lucy", "Jack", "Mia",
        "Alexander", "Isla", "Matthew", "Ava", "Daniel", "Lily", "Oscar", "Freya", "Harry", "Isabella",
        "Arthur", "Poppy", "Theo", "Sienna", "Leo", "Daisy", "Muhammad", "Florence", "Archie", "Alice",
        "Joshua", "Evie", "Frederick", "Phoebe", "Ethan", "Sofia", "Isaac", "Ruby", "Edward", "Isabelle",
        "Noah", "Ella", "Oscar", "Chloe", "Finley", "Poppy", "Max", "Rosie", "Sebastian", "Millie",
        "Adam", "Willow", "Lucas", "Evelyn", "Henry", "Elsie", "Benjamin", "Sophie", "Theodore", "Matilda",
        "Harrison", "Harriet", "Archie", "Emily", "Teddy", "Elizabeth", "Dylan", "Layla", "Riley", "Erin",
        "Jacob", "Holly", "Logan", "Georgia", "Toby", "Ellie", "Reuben", "Maisie", "Finley", "Abigail",
        "Liam", "Molly", "Mason", "Jessica", "Hugo", "Amelie", "Reggie", "Esme", "Jenson", "Scarlett",
        "Arlo", "Isabelle", "Louis", "Ivy", "Jude", "Violet", "Tommy", "Lola", "Frankie", "Eliza"
    };

    private static final String[] LAST_NAMES = {
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
        "Wilson", "Taylor", "Anderson", "Thomas", "Moore", "Jackson", "Martin", "Thompson", "White", "Harris",
        "Clark", "Lewis", "Robinson", "Walker", "Young", "Hall", "Allen", "Wright", "King", "Scott",
        "Green", "Baker", "Adams", "Nelson", "Hill", "Ramirez", "Campbell", "Mitchell", "Roberts", "Carter",
        "Phillips", "Evans", "Turner", "Torres", "Parker", "Collins", "Edwards", "Stewart", "Sanchez", "Morris",
        "Rogers", "Reed", "Cook", "Morgan", "Bell", "Murphy", "Bailey", "Rivera", "Cooper", "Richardson",
        "Cox", "Howard", "Ward", "Peterson", "Gray", "Watson", "Brooks", "Bennett", "Wood", "Barnes",
        "Ross", "Henderson", "Coleman", "Jenkins", "Perry", "Powell", "Long", "Patterson", "Hughes", "Flores",
        "Washington", "Butler", "Simmons", "Foster", "Gonzalez", "Bryant", "Alexander", "Russell", "Griffin", "Diaz",
        "Hayes", "Myers", "Ford", "Hamilton", "Graham", "Sullivan", "Wallace", "Woods", "Cole", "West",
        "Jordan", "Owens", "Reynolds", "Fisher", "Ellis", "Harrison", "Gibson", "McDonald", "Cruz", "Marshall",
        "Ortiz", "Gomez", "Murray", "Freeman", "Wells", "Webb", "Simpson", "Stevens", "Tucker", "Porter",
        "Hunter", "Hicks", "Crawford", "Henry", "Boyd", "Mason", "Morales", "Kennedy", "Warren", "Dixon",
        "Ramos", "Reyes", "Burns", "Gordon", "Shaw", "Holmes", "Rice", "Robertson", "Hunt", "Black",
        "Daniels", "Palmer", "Mills", "Nichols", "Grant", "Knight", "Ferguson", "Rose", "Stone", "Hawkins"
    };

    private static final String[] ALIASES_NAMES = {
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
        "Wilson", "Taylor", "Anderson", "Thomas", "Moore", "Jackson", "Martin", "Thompson", "White", "Harris",
        "Clark", "Lewis", "Robinson", "Walker", "Young", "Hall", "Allen", "Wright", "King", "Scott",
        "Green", "Baker", "Adams", "Nelson", "Hill", "Ramirez", "Campbell", "Mitchell", "Roberts", "Carter",
        "Phillips", "Evans", "Turner", "Torres", "Parker", "Collins", "Edwards", "Stewart", "Sanchez", "Morris",
        "Rogers", "Reed", "Cook", "Morgan", "Bell", "Murphy", "Bailey", "Rivera", "Cooper", "Richardson",
        "Cox", "Howard", "Ward", "Peterson", "Gray", "Watson", "Brooks", "Bennett", "Wood", "Barnes",
        "Ross", "Henderson", "Coleman", "Jenkins", "Perry", "Powell", "Long", "Patterson", "Hughes", "Flores",
        "Washington", "Butler", "Simmons", "Foster", "Gonzalez", "Bryant", "Alexander", "Russell", "Griffin", "Diaz",
        "Hayes", "Myers", "Ford", "Hamilton", "Graham", "Sullivan", "Wallace", "Woods", "Cole", "West",
        "Jordan", "Owens", "Reynolds", "Fisher", "Ellis", "Harrison", "Gibson", "McDonald", "Cruz", "Marshall",
        "Ortiz", "Gomez", "Murray", "Freeman", "Wells", "Webb", "Simpson", "Stevens", "Tucker", "Porter",
        "Hunter", "Hicks", "Crawford", "Henry", "Boyd", "Mason", "Morales", "Kennedy", "Warren", "Dixon",
        "Ramos", "Reyes", "Burns", "Gordon", "Shaw", "Holmes", "Rice", "Robertson", "Hunt", "Black",
        "Daniels", "Palmer", "Mills", "Nichols", "Grant", "Knight", "Ferguson", "Rose", "Stone", "Hawkins",         
        "John", "Jane", "Michael", "Sarah", "David", "Emma", "Robert", "Lisa", "James", "Mary",
        "Daniel", "Emily", "William", "Olivia", "Thomas", "Sophie", "George", "Amelia", "Charles", "Grace",
        "Joseph", "Ella", "Edward", "Charlotte", "Henry", "Jessica", "Samuel", "Lucy", "Jack", "Mia",
        "Alexander", "Isla", "Matthew", "Ava", "Daniel", "Lily", "Oscar", "Freya", "Harry", "Isabella",
        "Arthur", "Poppy", "Theo", "Sienna", "Leo", "Daisy", "Muhammad", "Florence", "Archie", "Alice",
        "Joshua", "Evie", "Frederick", "Phoebe", "Ethan", "Sofia", "Isaac", "Ruby", "Edward", "Isabelle",
        "Noah", "Ella", "Oscar", "Chloe", "Finley", "Poppy", "Max", "Rosie", "Sebastian", "Millie",
        "Adam", "Willow", "Lucas", "Evelyn", "Henry", "Elsie", "Benjamin", "Sophie", "Theodore", "Matilda",
        "Harrison", "Harriet", "Archie", "Emily", "Teddy", "Elizabeth", "Dylan", "Layla", "Riley", "Erin",
        "Jacob", "Holly", "Logan", "Georgia", "Toby", "Ellie", "Reuben", "Maisie", "Finley", "Abigail",
        "Liam", "Molly", "Mason", "Jessica", "Hugo", "Amelie", "Reggie", "Esme", "Jenson", "Scarlett",
        "Arlo", "Isabelle", "Louis", "Ivy", "Jude", "Violet", "Tommy", "Lola", "Frankie", "Eliza"
    };

    private static final String[] STREETS = {
        "Main Street", "Oak Avenue", "Elm Road", "Park Lane", "High Street", "Church Street", "School Road", "Castle Drive", "Victoria Road",
        "Station Road", "Church Road", "Mill Lane", "King Street", "Queen Street", "George Street", "Albert Road", "Albert Street", "Victoria Street",
        "London Road", "Manchester Road", "Liverpool Road", "Leeds Road", "Bristol Road", "York Road", "Cambridge Road",  "Oxford Road",  "Richmond Road",
        "Windsor Road", "Park Road", "Garden Street", "Rose Lane", "Church Lane", "Green Lane",  "Mill Road", "Station Lane", "Market Street", "High Road",
        "New Road", "West Street", "East Street", "North Street", "South Street", "Broad Street", "Bridge Street", "Waterloo Road", "Victoria Lane", "Regent Street",
        "Kingston Road", "Harrow Road", "Wellington Road", "Clarence Street", "Grove Road", "Woodland Drive", "Meadow Lane", "Willow Drive", "Hawthorn Road",  "Rosemary Lane",
        "Chestnut Avenue", "Beech Road", "Maple Avenue",  "Ash Grove", "Cedar Road", "Pine Street", "Birch Avenue", "Elm Avenue", "Oak Road", "Lime Street",
        "Hazel Drive", "Sycamore Road", "Poplar Avenue", "Brook Lane", "River Road", "Valley Road", "Hill Street", "Park View", "The Crescent", "The Avenue",
        "The Grove", "The Close", "The Gardens", "The Green"
    };

    private static final String[] CITIES = {
        "London", "Manchester", "Birmingham", "Leeds", "Glasgow", "Sheffield", "Bristol","Edinburgh", "Liverpool", "Newcastle", "Nottingham", "Cardiff",
        "Leicester", "Coventry", "Bradford", "Stoke-on-Trent", "Wolverhampton", "Plymouth", "Derby", "Southampton", "Portsmouth", "Brighton", "Reading",
        "Oxford", "Cambridge", "York", "Bath", "Exeter", "Norwich", "Sunderland", "Hull", "Preston", "Bolton", "Blackpool", "Salford", "Wigan", "Stockport",
        "Oldham", "Rochdale", "Huddersfield", "Wakefield", "Doncaster", "Middlesbrough", "Milton Keynes", "Northampton", "Luton", "Swindon", "Ipswich", 
        "Colchester", "Chelmsford", "Canterbury", "Peterborough", "Lincoln", "Worcester", "Gloucester", "Salisbury", "Carlisle", "Lancaster", "Chester", 
        "Durham", "Inverness", "Dundee", "Aberdeen", "Stirling", "Perth", "Swansea", "Newport","Bangor"
    };

    private static final String[] POSTCODES = {
        "SW1A 1AA", "M1 1AD", "B1 1BD", "LS1 1UR", "G2 1BB", "S1 1WA", "BS1 3AQ", "EH8 8DX", "L1 1AA", "NE1 1AD", "NG1 1AA", "CF10 1AA", "LE1 1AA",
        "CV1 1AA", "BD1 1AA", "ST1 1AA", "WV1 1AA", "PL1 1AA", "DE1 1AA", "SO14 1AA", "PO1 1AA", "BN1 1AA", "RG1 1AA", "OX1 1AA", "CB1 1AA", "YO1 1AA", 
        "BA1 1AA", "EX1 1AA", "NR1 1AA", "SR1 1AA", "HU1 1AA", "PR1 1AA", "BL1 1AA", "FY1 1AA", "M5 1AA", "WN1 1AA", "SK1 1AA", "OL1 1AA", "OL16 1AA",
        "HD1 1AA", "WF1 1AA", "DN1 1AA", "TS1 1AA", "MK9 1AA", "NN1 1AA", "LU1 1AA", "SN1 1AA", "IP1 1AA", "CO1 1AA", "CM1 1AA",  "CT1 1AA", "PE1 1AA", 
        "LN1 1AA", "WR1 1AA", "GL1 1AA", "SP1 1AA", "CA1 1AA", "LA1 1AA",  "CH1 1AA", "DH1 1AA", "IV1 1AA", "DD1 1AA", "AB10 1AA", "FK8 1AA", "PH1 1AA", 
        "SA1 1AA", "NP20 1AA", "LL55 1AA"
    };
    private static final String[] VEHICLE_MAKES = {"Ford", "BMW", "Volkswagen", "Audi", "Toyota", "Honda", "Mercedes-Benz", "Vauxhall"};
    private static final String[] EMPLOYERS = {"Tech Corp", "Finance Ltd", "Health Services", "Retail Group", "Manufacturing Co", "Education Board", "Transport Inc"};
    private static final String[] GENDERS = {"Male", "Female", "Other"};
    private static final String[] ETHNICITIES = {"White", "Black", "Asian", "Mixed", "Other"};
    private static final String[] OCCUPATIONS = {"Engineer", "Doctor", "Teacher", "Accountant", "Manager", "Developer", "Nurse", "Technician"};
    private static final String[] NATIONALITIES = {"British", "Irish", "French", "German", "Spanish", "Italian", "Polish", "Chinese"};
    private static final String[] TITLES = {"Mr", "Mrs", "Miss", "Ms", "Dr", "Prof"};
    private static final String[] OFFENCES = {"AN09009", "AS03503", "BG73015", "AA97006", "TM38008", "TH68019B", "GM31034", "FI68046", "AA06021", "WC81313", "MA55040", "RA93005", "TR10028", "CD20008", "HS21025", "SS86034", "MD68030", "FI68142", "CD98029", "EI09001"};    
    
    
    public static String generateRandomFirstName() {
        return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
    }
    
    public static String generateRandomLastName() {
        return LAST_NAMES[random.nextInt(LAST_NAMES.length)];
    }

    public static String generateRandomAliasesFirstName() {
        return ALIASES_NAMES[random.nextInt(ALIASES_NAMES.length)];
    }

    public static String generateRandomAliasesLastName() {
        return ALIASES_NAMES[random.nextInt(ALIASES_NAMES.length)];
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
    
    public static String generateRandomOFFENCE() {
        return OFFENCES[random.nextInt(OFFENCES.length)];
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

    public static String generateRandomAccountName() {
    return generateRandomFirstName() + " " + generateRandomLastName();
    }

    //     public static String generateRandomFirstNameAccountName() {
    // return generateRandomFirstName();
    // }

    public static String generateRandomAccountNumber() {
        // 6-8 digits
        int length = randomNumber(6, 8);
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    public static String generateRandomAccountReference() {
        // e.g. REF7K29XQ
        return "REF" + generateRandomAlphaNumeric(6);
    }

    public static String generateRandomSortCode() {
        // 6 digits
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    private static String generateRandomAlphaNumeric(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }

    public static String generateRandomAdultDateOfBirth() {
    // Age between 18 and 100
        int age = randomNumber(18, 100);

        LocalDate dob = LocalDate.now()
                .minusYears(age)
                .minusDays(random.nextInt(365));

        return dob.format(dateFormatter);
    }
    public static String generateRandomYouthDateOfBirth() {
    // Age between 5 and 17
        int age = randomNumber(5, 17);

        LocalDate dob = LocalDate.now()
                .minusYears(age)
                .minusDays(random.nextInt(365));

        return dob.format(dateFormatter);
    }
}
