package controller;

import java.io.IOException;
import java.net.URL;
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
import model.Part;

public class ModifyPartController implements Initializable {
    private Part selectedPart;
    @FXML
    private Label partIdNameLabel;
    @FXML
    private RadioButton inHouseRadioButton;
    @FXML
    private ToggleGroup tgPartType;
    @FXML
    private RadioButton outsourcedRadioButton;
    @FXML
    private TextField partIdText;
    @FXML
    private TextField partNameText;
    @FXML
    private TextField partInventoryText;
    @FXML
    private TextField partPriceText;
    @FXML
    private TextField partMaxText;
    @FXML
    private TextField partIdNameText;
    @FXML
    private TextField partMinText;

    public ModifyPartController() {
    }

    @FXML
    void cancelButtonAction(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Do you want cancel changes and return to the main screen?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            this.returnToMainScreen(event);
        }

    }

    @FXML
    void inHouseRadioButtonAction(ActionEvent event) {
        this.partIdNameLabel.setText("Machine ID");
    }

    @FXML
    void outsourcedRadioButtonAction(ActionEvent event) {
        this.partIdNameLabel.setText("Company Name");
    }

    @FXML
    void saveButtonAction(ActionEvent event) throws IOException {
        try {
            int id = this.selectedPart.getId();
            String name = this.partNameText.getText();
            Double price = Double.parseDouble(this.partPriceText.getText());
            int stock = Integer.parseInt(this.partInventoryText.getText());
            int min = Integer.parseInt(this.partMinText.getText());
            int max = Integer.parseInt(this.partMaxText.getText());
            boolean partAddSuccessful = false;
            if (this.minValid(min, max) && this.inventoryValid(min, max, stock)) {
                if (this.inHouseRadioButton.isSelected()) {
                    try {
                        int machineId = Integer.parseInt(this.partIdNameText.getText());
                        InHouse newInHousePart = new InHouse(id, name, price, stock, min, max, machineId);
                        Inventory.addPart(newInHousePart);
                        partAddSuccessful = true;
                    } catch (Exception var12) {
                        this.displayAlert(2);
                    }
                }

                if (this.outsourcedRadioButton.isSelected()) {
                    String companyName = this.partIdNameText.getText();
                    Outsourced newOutsourcedPart = new Outsourced(id, name, price, stock, min, max, companyName);
                    Inventory.addPart(newOutsourcedPart);
                    partAddSuccessful = true;
                }

                if (partAddSuccessful) {
                    Inventory.deletePart(this.selectedPart);
                    this.returnToMainScreen(event);
                }
            }
        } catch (Exception var13) {
            this.displayAlert(1);
        }

    }

    private void returnToMainScreen(ActionEvent event) throws IOException {
        Parent parent = (Parent)FXMLLoader.load(this.getClass().getResource("../view/MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private boolean minValid(int min, int max) {
        boolean isValid = true;
        if (min <= 0 || min >= max) {
            isValid = false;
            this.displayAlert(3);
        }

        return isValid;
    }

    private boolean inventoryValid(int min, int max, int stock) {
        boolean isValid = true;
        if (stock < min || stock > max) {
            isValid = false;
            this.displayAlert(4);
        }

        return isValid;
    }

    private void displayAlert(int alertType) {
        Alert alert = new Alert(AlertType.ERROR);
        switch(alertType) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Error Modifying Part");
                alert.setContentText("Form contains blank fields or invalid values.");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid value for Machine ID");
                alert.setContentText("Machine ID may only contain numbers.");
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid value for Min");
                alert.setContentText("Min must be a number greater than 0 and less than Max.");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid value for Inventory");
                alert.setContentText("Inventory must be a number equal to or between Min and Max");
                alert.showAndWait();
        }

    }

    public void initialize(URL location, ResourceBundle resources) {
        this.selectedPart = MainScreenController.getPartToModify();
        if (this.selectedPart instanceof InHouse) {
            this.inHouseRadioButton.setSelected(true);
            this.partIdNameLabel.setText("Machine ID");
            this.partIdNameText.setText(String.valueOf(((InHouse)this.selectedPart).getMachineId()));
        }

        if (this.selectedPart instanceof Outsourced) {
            this.outsourcedRadioButton.setSelected(true);
            this.partIdNameLabel.setText("Company Name");
            this.partIdNameText.setText(((Outsourced)this.selectedPart).getCompanyName());
        }

        this.partIdText.setText(String.valueOf(this.selectedPart.getId()));
        this.partNameText.setText(this.selectedPart.getName());
        this.partInventoryText.setText(String.valueOf(this.selectedPart.getStock()));
        this.partPriceText.setText(String.valueOf(this.selectedPart.getPrice()));
        this.partMaxText.setText(String.valueOf(this.selectedPart.getMax()));
        this.partMinText.setText(String.valueOf(this.selectedPart.getMin()));
    }
}
