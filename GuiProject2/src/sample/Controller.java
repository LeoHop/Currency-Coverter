package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private ChoiceBox<String> country1DropDown;
    @FXML private ChoiceBox<String> country2DropDown;
    @FXML private TextField printArea;
    @FXML private TextField inputAmount;
    @FXML private Button convertButton;
    @FXML private Button switchButton;
    @FXML private Button clearButton;
    @FXML private Text country1Text;
    @FXML private Text country2Text;
    @FXML private AnchorPane anchorPane;
    @FXML private ImageView imageView;
    private double usaAmount = 0;

    /**
     * This is the code that runs when your Application starts, it sets up all the interactivity
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)  {

        String[] countries1 = {"Select one..." , "USD", "Pound", "Russian Ruble", "Chinese Renminbi", "Argentine peso" , "Brazilian Real", "South African Rand"};
        String[] paths = {"/src/Images/America.jpg", "/src/Images/Britain.png", "/src/Images/Russia.jpg", "/src/Images/China.png", "/src/Images/Argentina.png", "/src/Images/Brazil.png", "/src/Images/SouthAfrica.png"};
        String[] symbols = {"$", "£", "₽", "¥", "$", "R$", "R"};
        Double[] convertUSD = {1.0, 1.289410, 0.015627, 0.142115, 0.016746, 0.237290, 0.067766};
        Double[] convertFromUSD = {1.0, 0.7751, 63.70, 7.039, 59.7089, 4.196, 14.757265};
        //all conversion amounts are accurate as of 11/21/19

        country1DropDown.setItems(FXCollections.observableArrayList(countries1));
        country2DropDown.setItems(FXCollections.observableArrayList(countries1));
        country1DropDown.getSelectionModel().selectFirst();
        country2DropDown.getSelectionModel().selectFirst();


        convertButton.setOnAction(e -> {
            if (inputAmount.getText() != null){
                if (!inputAmount.getText().matches("\\d*")) {
                    inputAmount.setText(inputAmount.getText().replaceAll("[^\\d]", ""));
                }
                //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
                if (!inputAmount.getText().equals("")){
                    double inputAmount2 = Double.parseDouble(inputAmount.getText());

                    if (country1DropDown.getValue() != null && country2DropDown.getValue() != null){
                        String country2Value = country2DropDown.getValue();
                        String country1Value = country1DropDown.getValue();
                        int positionOf1 = find(countries1, country1Value) - 1;
                        int positionOf2 = find(countries1, country2Value) - 1;
                        if (positionOf1 >= 0 && positionOf2 >= 0){
                            //convert all to USD
                            usaAmount = inputAmount2 * convertUSD[positionOf1];

                            inputAmount.setText(symbols[positionOf1] + inputAmount.getText());
                            //Convert USD to their currency
                            double finalConvert = usaAmount * convertFromUSD[positionOf2];
                            String print = Double.toString(round(finalConvert, 2));
                            printArea.setText("");
                            printArea.setText(symbols[positionOf2] + print + "\n"); // add it to the output area


                            //Code for displaying the image :)

                            //Getting the path of this file in the user's cpu
                            String path = Paths.get(".").toAbsolutePath().normalize().toString();

                            Image img = null;
                            try {
                                img = new Image(new FileInputStream(path + paths[positionOf2]));
                            } catch (FileNotFoundException ex) {
                                ex.printStackTrace();
                            }
                            imageView.setImage(img);
                        }
                    }
                }
            }


        });


        clearButton.setOnAction(e -> {
            //Clear Everything
            imageView.setImage(null);
            inputAmount.setText(null);
            printArea.setText(null);
            country1DropDown.getSelectionModel().selectFirst();
            country2DropDown.getSelectionModel().selectFirst();

        });


        switchButton.setOnAction(e -> {
            //switching all aspects of the gui from one side to another
            String country1Drop = country1DropDown.getValue();
            country1DropDown.setValue(country2DropDown.getValue());
            country2DropDown.setValue(country1Drop);
            String input = inputAmount.getText();
            inputAmount.setText(printArea.getText());
            printArea.setText(input);
            String path = Paths.get(".").toAbsolutePath().normalize().toString();
            for (int i = 0; i < countries1.length - 1; i++){
                if (country2DropDown.getValue().equals(countries1[i + 1])) {
                    Image img = null;
                    try {
                        img = new Image(new FileInputStream(path + paths[i]));
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    imageView.setImage(img);
                }
            }
        });

    }

    public static double round(double value, int places) {
        double divide = Math.pow(10, places);
        return Math.round(value * divide) / divide;
    }

    public static int find(String[] a, String target) {
        for (int i = 0; i < a.length; i++){
            if (a[i] == target){
                return i;
            }
        }
        return -1;
    }
}
