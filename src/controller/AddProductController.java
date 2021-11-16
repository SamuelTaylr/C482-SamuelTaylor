package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

public class AddProductController implements Initializable {
    private ObservableList<Part> assocParts = FXCollections.observableArrayList();
    @FXML
    private TableView<Part> assocPartTableView;
    @FXML
    private TableColumn<Part, Integer> assocPartIdColumn;
    @FXML
    private TableColumn<Part, String> assocPartNameColumn;
    @FXML
    private TableColumn<Part, Integer> assocPartInventoryColumn;
    @FXML
    private TableColumn<Part, Double> assocPartPriceColumn;
    @FXML
    private TableView<Part> partTableView;
    @FXML
    private TableColumn<Part, Integer> partIdColumn;
    @FXML
    private TableColumn<Part, String> partNameColumn;
    @FXML
    private TableColumn<Part, Integer> partInventoryColumn;
    @FXML
    private TableColumn<Part, Double> partPriceColumn;
    @FXML
    private TextField partSearchText;
    @FXML
    private TextField productIdText;
    @FXML
    private TextField productNameText;
    @FXML
    private TextField productInventoryText;
    @FXML
    private TextField productPriceText;
    @FXML
    private TextField productMaxText;
    @FXML
    private TextField productMinText;

    public AddProductController() {
    }

    @FXML
    void addButtonAction(ActionEvent event) {
        Part selectedPart = (Part)this.partTableView.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            this.displayAlert(5);
        } else {
            this.assocParts.add(selectedPart);
            this.assocPartTableView.setItems(this.assocParts);
        }

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
    void partSearchBtnAction(ActionEvent event) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        String searchString = this.partSearchText.getText();
        Iterator var5 = allParts.iterator();

        while(true) {
            Part part;
            do {
                if (!var5.hasNext()) {
                    this.partTableView.setItems(partsFound);
                    if (partsFound.size() == 0) {
                        this.displayAlert(1);
                    }

                    return;
                }

                part = (Part)var5.next();
            } while(!String.valueOf(part.getId()).contains(searchString) && !part.getName().contains(searchString));

            partsFound.add(part);
        }
    }

    @FXML
    void partSearchKeyPressed(KeyEvent event) {
        if (this.partSearchText.getText().isEmpty()) {
            this.partTableView.setItems(Inventory.getAllParts());
        }

    }

    @FXML
    void removeButtonAction(ActionEvent event) {
        Part selectedPart = (Part)this.assocPartTableView.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            this.displayAlert(5);
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Do you want to remove the selected part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                this.assocParts.remove(selectedPart);
                this.assocPartTableView.setItems(this.assocParts);
            }
        }

    }

    @FXML
    void saveButtonAction(ActionEvent event) throws IOException {
        try {
            int id = 0;
            String name = this.productNameText.getText();
            Double price = Double.parseDouble(this.productPriceText.getText());
            int stock = Integer.parseInt(this.productInventoryText.getText());
            int min = Integer.parseInt(this.productMinText.getText());
            int max = Integer.parseInt(this.productMaxText.getText());
            if (name.isEmpty()) {
                this.displayAlert(7);
            } else if (this.minValid(min, max) && this.inventoryValid(min, max, stock)) {
                Product newProduct = new Product(id, name, price, stock, min, max);
                Iterator var9 = this.assocParts.iterator();

                while(var9.hasNext()) {
                    Part part = (Part)var9.next();
                    newProduct.addAssociatedPart(part);
                }

                newProduct.setId(Inventory.getNewProductId());
                Inventory.addProduct(newProduct);
                this.returnToMainScreen(event);
            }
        } catch (Exception var11) {
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
        Alert alertInfo = new Alert(AlertType.INFORMATION);
        switch(alertType) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding Product");
                alert.setContentText("Form contains blank fields or invalid values.");
                alert.showAndWait();
                break;
            case 2:
                alertInfo.setTitle("Information");
                alertInfo.setHeaderText("Part not found");
                alertInfo.showAndWait();
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
                break;
            case 5:
                alert.setTitle("Error");
                alert.setHeaderText("Part not selected");
                alert.showAndWait();
            case 6:
            default:
                break;
            case 7:
                alert.setTitle("Error");
                alert.setHeaderText("Name Empty");
                alert.setContentText("Name cannot be empty.");
                alert.showAndWait();
        }

    }

    public void initialize(URL location, ResourceBundle resources) {
        this.partIdColumn.setCellValueFactory(new PropertyValueFactory("id"));
        this.partNameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        this.partInventoryColumn.setCellValueFactory(new PropertyValueFactory("stock"));
        this.partPriceColumn.setCellValueFactory(new PropertyValueFactory("price"));
        this.partTableView.setItems(Inventory.getAllParts());
        this.assocPartIdColumn.setCellValueFactory(new PropertyValueFactory("id"));
        this.assocPartNameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        this.assocPartInventoryColumn.setCellValueFactory(new PropertyValueFactory("stock"));
        this.assocPartPriceColumn.setCellValueFactory(new PropertyValueFactory("price"));
    }
}
