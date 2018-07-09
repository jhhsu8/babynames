/**
 * This Java program finds numbers and ranks of baby names from the United States
 * 
 * @author Joanne Hsu 
 * @version 1.0
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
public class BabyBirths {
    
    // print individual names, genders, and number of births
    public void printNames() {
        FileResource fr = new FileResource();
        for (CSVRecord rec: fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            if (numBorn <= 100) {
                System.out.println("Name: " + rec.get(0) +
                    " Gender: " + rec.get(1) +
                    " Num Born: " + rec.get(2));
            }
        }
    }
    
    // print total number of boy/girl births and names
    public void totalBirths(FileResource fr) {
        int totalBirths = 0;
        int totalGirls = 0;
        int totalBoys = 0;
        int totalNames = 0;
        int totalGirlNames = 0;
        int totalBoyNames = 0;
        for (CSVRecord rec: fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            totalNames++;
            if (rec.get(1).equals("M")) {
                totalBoys += numBorn;
                totalBoyNames++;
            } else {
                totalGirls += numBorn;
                totalGirlNames++;
            }

        }
        System.out.println("total births " + totalBirths);
        System.out.println("total girls " + totalGirls);
        System.out.println("total boys " + totalBoys);
        System.out.println("total names " + totalNames);
        System.out.println("total girl names " + totalGirlNames);
        System.out.println("total boy names " + totalBoyNames);
    }

    public void testTotalBirths() {
        FileResource fr = new FileResource("us_babynames/us_babynames_by_year/yob2012.csv");
        totalBirths(fr);
    }
    
    // get rank under the given name, gender and year
    public int getRank(int year, String name, String gender) {
        int rank = 0;
        int tempRank = 0;
        FileResource fr = new FileResource("us_babynames/us_babynames_by_year/yob" + year + ".csv");
        CSVParser parser = fr.getCSVParser();
        for (CSVRecord record: parser) {
            if (record.get(1).equals(gender)) {
                tempRank++;
                if (record.get(0).equals(name)) {
                    rank = tempRank;
                    break;
                }
            }
        }
        return rank;
    }
    
    // get name under the given rank, year, and gender
    public String getName(int year, int rank, String gender) {
        FileResource fr = new FileResource("us_babynames/us_babynames_by_year/yob" + year + ".csv");
        CSVParser parser = fr.getCSVParser();
        for (CSVRecord rec: parser) {
            if (rec.get(1).equals(gender) && rank == getRank(year, rec.get(0), gender)) {
                return rec.get(0);
            }
        }
        return "no name";
    }
    // print name with the same rank in a different year
    public void whatIsNameInYear(String name, int year, int newYear, String gender) {
        int rank = getRank(year, name, gender);
        String new_name = getName(newYear, rank, gender);
        System.out.println(name + " born in " + year + " would be " + new_name + " if born in " + newYear);
    }

    public void testWhatIsNameInYear() {
        whatIsNameInYear("Isabella", 2012, 2014, "F");
    }
    
    public void testGetRank() {
        int result = getRank(2014, "Emma", "F");
        System.out.println(result);
    }

    public void testGetName() {
        String result = getName(2014, 3, "F");
        System.out.println(result);

    }
    
    // get year with the highest rank of given name
    public int yearOfHighestRank(String name, String gender) {
        int yearTemp = 0;
        int year = 0;
        int currentRank = 0;
        int highestRank = Integer.MAX_VALUE;
        DirectoryResource dr = new DirectoryResource();
        for (File file: dr.selectedFiles()) {
            String filename = file.getName();
            String yearSubstring = filename.substring(3, 7);
            yearTemp = Integer.parseInt(yearSubstring);
            currentRank = getRank(yearTemp, name, gender);
            if (currentRank < highestRank) {
                highestRank = currentRank;
                year = yearTemp;
            }
        }
        return year;
    }

    public void testYearOfHighestRank() {
        int result = yearOfHighestRank("Joanne", "F");
        System.out.println(result);
    }
    
    // get average rank of the given name in different files
    public double getAverageRank(String name, String gender) {
        int yearTemp = 0;
        int count = 0;
        int tempRank = 0;
        double rank = 0;
        DirectoryResource dr = new DirectoryResource();
        for (File file: dr.selectedFiles()) {
            count++;
            String filename = file.getName();
            String yearSubstring = filename.substring(3, 7);
            yearTemp = Integer.parseInt(yearSubstring);
            tempRank = getRank(yearTemp, name, gender);
            rank += tempRank;

        }
        double average = rank / count;
        double averageTwoDec = Math.floor(average * 100) / 100;
        return averageTwoDec;
    }

    public void testGetAverageRank() {
        double result = getAverageRank("Jacob", "M");
        System.out.println(result);
    }
    
    //get the total number of births of those names with the same gender and same year who are ranked higher than the given name
    public int getTotalBirthsRankedHigher(int year, String name, String gender) {
        int totalBirths = 0;
        FileResource fr = new FileResource("us_babynames/us_babynames_test/yob" + year + "short.csv");
        CSVParser parser = fr.getCSVParser();
        for (CSVRecord record: parser) {
            int numBorn = Integer.parseInt(record.get(2));
            if (record.get(1).equals(gender)) {
                totalBirths += numBorn;
                if (record.get(0).equals(name)) {
                    totalBirths = totalBirths - Integer.parseInt(record.get(2));
                    break;
                }
            }
        }
        return totalBirths;
    }

    public void testGetTotalBirthsRankedHigher() {
        int result = getTotalBirthsRankedHigher(2012, "Ethan", "M");
        System.out.println(result);
    }

}