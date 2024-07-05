package dataProvider;

import java.io.File;

public class TakingFiles {
    public TakingFiles() {
        File customerFile = new File("src\\resources\\Customer-Main_Data.csv");
        File supplementaryFile = new File("src\\resources\\supplementary_date.csv");

        if (customerFile.exists() && customerFile.isFile()) {
            new ProvidingData(customerFile, "Customer");
        } else {
            System.out.println("Customer file not found. Please use this method to insert the address of the file:\"src\\\\resources\\\\customertestdata\"");
        }

        if (supplementaryFile.exists() && supplementaryFile.isFile()) {
            new ProvidingData(supplementaryFile, "Supplementary");
        } else {
            System.out.println("Supplementary file not found. Please use this method to insert the address of the file: \"src\\\\resources\\\\supplytestdata\"");
        }
    }
}
