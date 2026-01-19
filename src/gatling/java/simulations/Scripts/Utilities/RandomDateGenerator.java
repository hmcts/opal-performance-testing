package simulations.Scripts.Utilities;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDateGenerator {

    public static void main(String[] args) {
        // Get current date
        LocalDate currentDate = LocalDate.now();

        // Define the date range constraints
        LocalDate startDate = LocalDate.of(2024, 3, 1);
        LocalDate endDate = LocalDate.of(2024, 3, 15);

        // Ensure the endDate is at least 2 years from the startDate
        LocalDate minEndDate = startDate.plusYears(2);

        // Adjust the endDate if necessary
        if (endDate.isBefore(minEndDate)) {
            endDate = minEndDate;
        }

        // Generate random dates within the range
        LocalDate randomDateFrom = getRandomDate(startDate, endDate.minusYears(2));
        LocalDate randomDateTo = getRandomDate(randomDateFrom.plusYears(2), endDate);

        // Ensure dates don't go past the current date
        randomDateFrom = randomDateFrom.isAfter(currentDate) ? currentDate : randomDateFrom;
        randomDateTo = randomDateTo.isAfter(currentDate) ? currentDate : randomDateTo;

        // Print the random dates
        System.out.println("Random Date From: " + randomDateFrom);
        System.out.println("Random Date To: " + randomDateTo);
    }

    public static LocalDate getRandomDate(LocalDate startDate, LocalDate endDate) {
        long startEpochDay = startDate.toEpochDay();
        long endEpochDay = endDate.toEpochDay();
        long randomEpochDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay + 1);
        return LocalDate.ofEpochDay(randomEpochDay);
    }
    public class GeoUtils {

        public static int[] getTileXY(double lat, double lon, int zoom) {
            // Convert latitude to match TMS tiling convention
            lat = -lat;
            int xtile = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
            int ytile = (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1 << zoom));

            if (xtile < 0) xtile = 0;
            if (ytile < 0) ytile = 0;
            if (xtile >= (1 << zoom)) xtile = (1 << zoom) - 1;
            if (ytile >= (1 << zoom)) ytile = (1 << zoom) - 1;

            return new int[]{xtile, ytile};
        }
    }

    public static String generateRandomBBox() {
        double xminLimit = -975000;
        double xmaxLimit = 200000;
        double yminLimit = 6300000;
        double ymaxLimit = 8600000;

        double width = 3000 + Math.random() * 7000; // between 3km–10km wide
        double height = 3000 + Math.random() * 7000;

        double xmin = xminLimit + Math.random() * (xmaxLimit - xminLimit - width);
        double ymin = yminLimit + Math.random() * (ymaxLimit - yminLimit - height);

        double xmax = xmin + width;
        double ymax = ymin + height;

        return String.format("%.6f,%.6f,%.6f,%.6f", xmin, ymin, xmax, ymax);
    }

}
