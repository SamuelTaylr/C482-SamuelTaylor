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

/**
 * Controller class that provides control logic for the Modify Product screen.
 *
 * @author Samuel Taylor
 */
public class ModifyProductController implements Initializable
{
    Product selectedProduct; // Product selected by user on main screen
    private ObservableList<Part> assocParts = FXCollections.observableArrayList(); // List of associated parts for a product
    @FXML private TableView<Part> associatedPartTableView; // Table view for the associated parts
    @FXML private TableColumn<Part, Integer> associatedPartIdColumn; // Part id column for associated parts
    @FXML private TableColumn<Part, String> associatedPartNameColumn; // Part name column for associated parts
    @FXML private TableColumn<Part, Integer> associatedPartInvColumn; // Part inv column for associated parts
    @FXML private TableColumn<Part, Double> associatedPartPriceColumn; // Part price column for associated parts
    @FXML private TableView<Part> partTableView; // Table view for all parts or searched part
    @FXML private TableColumn<Part, Integer> partIdColumn; // Part id column for parts table
    @FXML private TableColumn<Part, String> partNameColumn; // Part name column for parts table
    @FXML private TableColumn<Part, Integer> partInvColumn; // Part inv column for parts table
    @FXML private TableColumn<Part, Double> partPriceColumn; // Part price column for parts table
    @FXML private TextField partSearchField; // Search field for parts table
    @FXML private TextField productIdField; // Field for autogenerated product id
    @FXML private TextField productNameField; // Field for product name
    @FXML private TextField productInvField; // Field for product inventory level
    @FXML private TextField productPriceField; // Field for product price
    @FXML private TextField productMaxField; // Field for maximum product inventory
    @FXML private TextField productMinField; // Field for minimum product inventory

    /**
     * Adds part object selected in the all parts table to the associated parts table.
     *
     * Displays error message if no part is selected.
     *
     * @param event mouse click on Add associated part button.
     */
    @FXML
    void onAddAssociatedPartClicked(ActionEvent event)
    {
        Part selectedPart = (Part)partTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null)
        {
            displayAlert(5);
        }

        else
        {
            assocParts.add(selectedPart);
            associatedPartTableView.setItems(assocParts);
        }
    }

    /**
     * Displays confirmation box and loads main screen controller.
     *
     * @param event mouse click on cancel button.
     * @throws IOException from FXMLLoader
     */
    @FXML
    void onCancelModifyProductClicked(ActionEvent event) throws IOException
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText("Cancel changes and return to the main screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK)
        {
            returnToMainScreen(event);
        }
    }

    /**
     * Removes associated part from product.
     *
     * Takes part selected by user and removes it from assocParts list.
     * Then repopulates table view to show remaining associated parts.
     * Displays alert message if no part selected.
     *
     * @param event on Remove associated part button mouse click
     */
    @FXML
    void onRemoveAssociatedPartClicked(ActionEvent event)
    {
        Part selectedPart = (Part)this.associatedPartTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null)
        {
            displayAlert(5);
        }
        else
        {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setContentText("Remove selected part from associated parts?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK)
            {
                assocParts.remove(selectedPart);
                associatedPartTableView.setItems(assocParts);
            }
        }
    }

    /**
     * Adds modified product to inventory and loads MainScreenController.
     *
     * All fields are validated with appropriate error messages displayed to user
     * to indicate invalid values.  Uses iterator to add each individual
     * associated part.
     *
     * @param event on save button mouse click.
     * @throws IOException From FXMLLoader.
     */
    @FXML
    void onSaveModifiedProductClicked(ActionEvent event) throws IOException
    {
        try
        {
            int id = selectedProduct.getId();
            String name = productNameField.getText();
            Double price = Double.parseDouble(productPriceField.getText());
            int stock = Integer.parseInt(productInvField.getText());
            int min = Integer.parseInt(productMinField.getText());
            int max = Integer.parseInt(productMaxField.getText());

            if (name.isEmpty())
            {
                displayAlert(7);
            }

            else if(price <= 0.0)
            {
                displayAlert(6);
            }

            else if (this.minValid(min, max) && this.inventoryValid(min, max, stock))
            {
                Product newProduct = new Product(id, name, price, stock, min, max);

                for(Iterator<Part> iterator = this.assocParts.iterator(); iterator.hasNext();)
                {
                    Part part = iterator.next();
                    newProduct.addAssociatedPart(part);
                }
                Inventory.addProduct(newProduct);
                Inventory.deleteProduct(this.selectedProduct);
                returnToMainScreen(event);
            }
        }
        catch (IOException exception)
        {
            displayAlert(1);
        }
    }

    /**
     * Searches allParts list for either Id or Name.
     *
     * Takes contents of text field and compares it to each entry in allParts list.
     * Works using both Id number or part name using only built in functions instead
     * of passing variables to another method.  Then populates parts table with results.
     * If no results found, refreshes parts table with allParts.
     *
     * @param event on Search button mouse click.
     */
    @FXML
    void onSearchPartClicked(ActionEvent event)
    {
        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        String searchString = partSearchField.getText();

        for(Part part : allParts)
        {
            if (String.valueOf(part.getId()).contains(searchString) ||
                    part.getName().contains(searchString))
            {
                partsFound.add(part);
            }
        }
        partTableView.setItems(partsFound);

        if(partsFound.size() == 0)
        {
            displayAlert(2);
            partTableView.setItems(allParts);
        }
    }

    /**
     * Refreshes part table view to show all parts when key pressed while search field empty.
     *
     * @param event on Key press in search field.
     */
    @FXML
    void onPartSearchKeyPressed(KeyEvent event)
    {
        if (partSearchField.getText().isEmpty())
        {
            partTableView.setItems(Inventory.getAllParts());
        }
    }

    /**
     * Loads MainScreenController.
     *
     * @param event Passed from parent method.
     * @throws IOException From FXMLLoader.
     */
    private void returnToMainScreen(ActionEvent event) throws IOException
    {
        Parent parent = (Parent)FXMLLoader.load(this.getClass().getResource(
                "../view/MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
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

        if (min <= 0 || min >= max)
        {
            isValid = false;
            this.displayAlert(3);
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

        if (stock < min || stock > max)
        {
            isValid = false;
            displayAlert(4);
        }
        return isValid;
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
                alert.setHeaderText("Cannot Modify Product");
                alert.setContentText("Form contains blank fields or invalid values.");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");
                alert.setHeaderText("No Search Results Found");
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
                alert.setHeaderText("Part not selected");
                alert.setContentText("Must select a part from the table");
                alert.showAndWait();
            case 6:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Price Entered");
                alert.setContentText("Price must be greater than 0");
                alert.showAndWait();
            case 7:
                alert.setTitle("Error");
                alert.setHeaderText("Name Field Empty");
                alert.setContentText("Name cannot be empty.");
                alert.showAndWait();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + alertType);
        }
    }

    /**
     * Initializes controller and populates both tables with values.
     *
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    public void initialize(URL location, ResourceBundle resources)
    {
        selectedProduct = MainScreenController.getProductToModify();

        assocParts = selectedProduct.getAllAssociatedParts();
        partIdColumn.setCellValueFactory(new PropertyValueFactory("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory("price"));
        partTableView.setItems(Inventory.getAllParts());

        associatedPartIdColumn.setCellValueFactory(new PropertyValueFactory("id"));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        associatedPartInvColumn.setCellValueFactory(new PropertyValueFactory("stock"));
        associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory("price"));
        associatedPartTableView.setItems(this.assocParts);

        productIdField.setText(String.valueOf(this.selectedProduct.getId()));
        productNameField.setText(this.selectedProduct.getName());
        productInvField.setText(String.valueOf(this.selectedProduct.getStock()));
        productPriceField.setText(String.valueOf(this.selectedProduct.getPrice()));
        productMaxField.setText(String.valueOf(this.selectedProduct.getMax()));
        productMinField.setText(String.valueOf(this.selectedProduct.getMin()));
    }
}