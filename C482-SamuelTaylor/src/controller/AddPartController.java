package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

/**
 * Controller class that provides control logic for the Add Part screen.
 *
 * @author Samuel Taylor
 */
public class AddPartController implements Initializable
{
    @FXML private Label partIdOrNameLabel; // Label for Id/Name that changes depending on Radio Button
    @FXML private RadioButton inHouseRadioButton; // Radio button for in house parts
    @FXML private ToggleGroup togglePartForm; // Toggle group containing both radio buttons
    @FXML private RadioButton outsourcedRadioButton; // Radio button for outsourced parts
    @FXML private TextField partIdField; // Text field for auto generated part Id
    @FXML private TextField partNameField; // Text field for part name
    @FXML private TextField partInventoryField; // Text field for inventory level of part
    @FXML private TextField partPriceField; // Text field for price of part
    @FXML private TextField partMaxField; // Text field for maximum inventory level
    @FXML private TextField partIdOrNameField; // Text field for either Id or Comp. Name
    @FXML private TextField partMinField; // Text field for minimum inventory level

    /**
     * Method to cancel add part and return to main screen.
     *
     * @param event occurs on mouse click on cancel button
     * @throws IOException From FXMLLoader.
     */
    @FXML
    void onCancelAddPartButtonClicked(ActionEvent event) throws IOException
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText("Cancel changes and return to main screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK)
        {
            returnToMainScreen(event);
        }
    }

    /**
     * Sets Id/Name label to "Machine Id".
     *
     * @param event on selection of Inhouse radio button
     */
    @FXML
    void inHouseRadioButtonAction(ActionEvent event)
    {
        partIdOrNameLabel.setText("Machine Id");
    }

    /**
     * Sets Id/Name label to "Company name".
     *
     * @param event on selection of Outsourced radio button
     */
    @FXML
    void outsourcedRadioButtonAction(ActionEvent event)
    {
        partIdOrNameLabel.setText("Comp. Name");
    }

    /**
     * Method to add a new part to inventory then return to main screen.
     * Input validation performed on all fields and appropriate error
     * messages displayed to user. Passes min/max/inv values to separate
     * methods to ensure values are valid.
     *
     * @param event on mouse click of save button
     * @throws IOException from FXMLLoader
     */
    @FXML
    void onPartSaveButtonClicked(ActionEvent event) throws IOException
    {
        try
        {
            int id = 0;
            String name = partNameField.getText();
            Double price = Double.parseDouble(partPriceField.getText());
            int stock = Integer.parseInt(partInventoryField.getText());
            int min = Integer.parseInt(partMinField.getText());
            int max = Integer.parseInt(partMaxField.getText());
            boolean partAddSuccessful = false;

            if(name.isEmpty())
            {
                displayAlert(5);
            }
            else if(price <= 0.0)
            {
                displayAlert(6);
            }
            else if(minValid(min, max) && inventoryValid(min, max, stock))
            {
                if(inHouseRadioButton.isSelected())
                {
                    try
                    {
                        int machineId = Integer.parseInt(partIdOrNameField.getText());
                        InHouse newInHousePart = new InHouse(id, name, price, stock, min, max, machineId);
                        newInHousePart.setId(Inventory.getNewPartId());
                        Inventory.addPart(newInHousePart);
                        partAddSuccessful = true;
                    }
                    catch(Exception exception)
                    {
                        displayAlert(2);
                    }
                }

                if(outsourcedRadioButton.isSelected())
                {
                    String companyName = partIdOrNameField.getText();
                    Outsourced newOutsourcedPart = new Outsourced(id, name, price, stock, min, max, companyName);
                    newOutsourcedPart.setId(Inventory.getNewPartId());
                    Inventory.addPart(newOutsourcedPart);
                    partAddSuccessful = true;
                }

                if(partAddSuccessful)
                {
                    returnToMainScreen(event);
                }
            }
        }
        catch(IOException exception)
        {
            displayAlert(1);
        }
    }

    /**
     * Validates that min entered is  > 0 and < max
     *
     * @param min The minimum inventory level for the part.
     * @param max The maximum inventory level for the part.
     * @return Boolean indicating if min is valid.
     */
    private boolean minValid(int min, int max)
    {
        boolean isValid = true;

        if(min <= 0 || min > max)
        {
            isValid = false;
            displayAlert(3);
        }
        return isValid;
    }

    /**
     * Validates that min < stock < max
     *
     * @param min The minimum inventory level for the part.
     * @param max The maximum inventory level for the part.
     * @param stock The actual inventory level for the part.
     * @return Boolean indicating if stock is within accepted range.
     */
    private boolean inventoryValid(int min, int max, int stock)
    {
        boolean isValid = true;

        if(stock < min || stock > max)
        {
            isValid = false;
            displayAlert(4);
        }

        return isValid;
    }

    /**
     * Loads MainScreenController.
     *
     * @param event Passed from parent method.
     * @throws IOException From FXMLLoader.
     */
    private void returnToMainScreen(ActionEvent event) throws IOException
    {
        Parent parent = (Parent)FXMLLoader.load(getClass().getResource(
                "../view/MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Displays alert messages using a switch statement.
     *
     * @param alertType variable indicating chosen switch statement.
     */
    private void displayAlert(int alertType)
    {
        Alert alert = new Alert(AlertType.ERROR);
        switch(alertType)
        {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Cannot Add Part");
                alert.setContentText("Form contains blank fields or invalid values.");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Entry for Machine Id");
                alert.setContentText("Machine Id must be an integer.");
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Entry for Minimum");
                alert.setContentText("Min must be greater than 0 and less than Max");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Entry for Inventory");
                alert.setContentText("Inv must be greater than Min and less than or equal to Max");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error");
                alert.setHeaderText("Name Field Empty");
                alert.setContentText("Name cannot be empty.");
                alert.showAndWait();
                break;
            case 6:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Price Entered");
                alert.setContentText("Price must be greater than 0");
                alert.showAndWait();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + alertType);
        }
    }

    /**
     * Initializes controller and sets in-house radio button to true.
     *
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    public void initialize(URL location, ResourceBundle resources)
    {
        this.inHouseRadioButton.setSelected(true);
    }
}
