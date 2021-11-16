package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Objects;
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
 * Controller class that provides control logic for the Main screen.
 *
 * @author Samuel Taylor
 */
public class MainScreenController implements Initializable
{
    private static Part partToModify; // to reference part selected by user in table view
    private static Product productToModify; // to reference product selected by user in table view
    @FXML private TextField partSearchField; // search field for part table view
    @FXML private TableView<Part> partTableView; // table view for parts list
    @FXML private TableColumn<Part, Integer> partIdColumn; // column for part Id
    @FXML private TableColumn<Part, String> partNameColumn; // column for part name
    @FXML private TableColumn<Part, Integer> partInventoryColumn; // column for part inventory level
    @FXML private TableColumn<Part, Double> partPriceColumn; // column for part price
    @FXML private TextField productSearchField; // search field for product table view
    @FXML private TableView<Product> productTableView; // table view for products list
    @FXML private TableColumn<Product, Integer> productIdColumn; // column for product id
    @FXML private TableColumn<Product, String> productNameColumn; // column for product name
    @FXML private TableColumn<Product, Integer> productInventoryColumn; // column for product inventory level
    @FXML private TableColumn<Product, Double> productPriceColumn; // column for product price

    /**
     * Accessor for part selected by user in table view.
     *
     * @return part object or null.
     *
     */
    public static Part getPartToModify()
    {
        return partToModify;
    }

    /**
     * Accessor for product selected by user in table view.
     *
     * @return product object or null.
     *
     */
    public static Product getProductToModify()
    {
        return productToModify;
    }

    /**
     * Loads the AddPartController.
     *
     * @param event on add button mouse click.
     * @throws IOException From FXMLLoader.
     */
    @FXML
    void onAddPartClicked(ActionEvent event) throws IOException
    {
        Parent parent = (Parent)FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource(
                "../view/AddPartScreen.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Removes part selected by user from allParts list.
     *
     * Requires a confirmation and displays an alert if no part is selected.
     *
     * @param event on delete button mouse click.
     */
    @FXML
    void onDeletePartClicked(ActionEvent event)
    {
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null)
        {
            displayAlert(3);
        }

        else
        {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setContentText("Delete selected part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK)
            {
                Inventory.deletePart(selectedPart);
            }
        }
    }

    /**
     * Loads the ModifyPartController.
     *
     * Displays alert message if no part is selected.
     *
     * @param event on modify button mouse click.
     * @throws IOException From FXMLLoader.
     */
    @FXML
    void onModifyPartClicked(ActionEvent event) throws IOException
    {
        partToModify = partTableView.getSelectionModel().getSelectedItem();

        if (partToModify == null)
        {
            displayAlert(3);
        }

        else
        {
            Parent parent = (Parent)FXMLLoader.load(this.getClass().getResource(
                    "../view/ModifyPartScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Searches allParts list for either Id or Name.
     *
     * Takes contents of text field and compares it to each entry in allParts list.
     * Works using both Id number or part name using only built in functions instead
     * of passing variables to another method.  Then populates parts table with results.
     * If no results found, refreshes parts table with allParts.  Uses built in iterator
     * function instead of a for loop to step through the entire list.
     *
     * @param event on Search button mouse click
     */
    @FXML
    void onSearchPartTableClicked(ActionEvent event)
    {
        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        String searchString = partSearchField.getText();
        Iterator iterator = allParts.iterator();

        while(true)
        {
            Part part;

            do
            {
                if (!iterator.hasNext())
                {
                    this.partTableView.setItems(partsFound);

                    if (partsFound.size() == 0)
                    {
                        displayAlert(1);
                    }
                    return;
                }

                part = (Part)iterator.next();
            }
            while(!String.valueOf(part.getId()).contains(searchString) &&
                    !part.getName().contains(searchString));

            partsFound.add(part);
        }
    }

    /**
     * Refreshes part table view to show all parts when key pressed while search field empty.
     *
     * @param event on Key press in search field.
     */
    @FXML
    void onPartSearchFieldKeyPressed(KeyEvent event)
    {
        if (partSearchField.getText().isEmpty())
        {
            partTableView.setItems(Inventory.getAllParts());
        }
    }

    /**
     * Loads the AddProductController.
     *
     * @param event on add button mouse click.
     * @throws IOException From FXMLLoader.
     */
    @FXML
    void onAddProductClicked(ActionEvent event) throws IOException
    {
        Parent parent = (Parent)FXMLLoader.load(this.getClass().getResource(
                "../view/AddProductScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Removes product selected by user from allProducts list.
     *
     * Checks that product has no associated parts and displays alert
     * message if associated parts exist.
     *
     * @param event on delete button mouse click.
     */
    @FXML
    void onDeleteProductClicked(ActionEvent event)
    {
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

        if (selectedProduct == null)
        {
            displayAlert(4);
        }

        else
        {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setContentText("Delete the selected product?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK)
            {
                ObservableList<Part> assocParts = selectedProduct.getAllAssociatedParts();

                if (assocParts.size() >= 1)
                {
                    this.displayAlert(5);
                }

                else
                {
                    Inventory.deleteProduct(selectedProduct);
                }
            }
        }
    }

    /**
     * Loads the ModifyProductController.
     *
     * Displays alert message if no product is selected.
     *
     * @param event on modify button mouse click.
     * @throws IOException From FXMLLoader.
     */
    @FXML
    void onModifyProductClicked(ActionEvent event) throws IOException
    {
        productToModify = productTableView.getSelectionModel().getSelectedItem();

        if (productToModify == null)
        {
            displayAlert(4);
        }

        else
        {
            Parent parent = (Parent)FXMLLoader.load(this.getClass().getResource(
                    "../view/ModifyProductScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Searches allProducts list for either Id or Name.
     *
     * Takes contents of text field and compares it to each entry in allProducts list.
     * Works using both Id number or product name using only built in functions instead
     * of passing variables to another method.  Then populates products table with results.
     * If no results found, refreshes products table with allProducts.  Uses built in iterator
     * function instead of a for loop to step through the entire list.
     *
     * @param event on Search button mouse click
     */
    @FXML
    void onSearchProductTableClicked(ActionEvent event)
    {
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        ObservableList<Product> productsFound = FXCollections.observableArrayList();
        String searchString = productSearchField.getText();
        Iterator iterator = allProducts.iterator();

        while(true)
        {
            Product product;
            do
            {
                if (!iterator.hasNext())
                {
                    this.productTableView.setItems(productsFound);

                    if (productsFound.size() == 0)
                    {
                        displayAlert(2);
                    }
                    return;
                }
                product = (Product)iterator.next();
            }
            while(!String.valueOf(product.getId()).contains(searchString)
                    && !product.getName().contains(searchString));

            productsFound.add(product);
        }
    }

    /**
     * Refreshes product table view to show all products when
     * key pressed while search field empty.
     *
     * @param event on Key press in search field.
     */
    @FXML
    void onProductSearchFieldKeyPressed(KeyEvent event)
    {
        if (productSearchField.getText().isEmpty())
        {
            productTableView.setItems(Inventory.getAllProducts());
        }
    }

    /**
     * Method that exits the program.
     *
     * @param event on exit button mouse click.
     */
    @FXML
    void onExitClicked(ActionEvent event)
    {
        System.exit(0);
    }

    /**
     * Displays alert messages using a switch statement.
     *
     * @param alertType variable indicating chosen switch statement.
     */
    private void displayAlert(int alertType)
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        Alert alertError = new Alert(AlertType.ERROR);

        switch(alertType)
        {
            case 1:
                alert.setTitle("Alert");
                alert.setHeaderText("Part not found");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Alert");
                alert.setHeaderText("Product not found");
                alert.showAndWait();
                break;
            case 3:
                alertError.setTitle("Error");
                alertError.setHeaderText("Part not selected");
                alertError.setContentText("Must select a part from table.");
                alertError.showAndWait();
                break;
            case 4:
                alertError.setTitle("Error");
                alertError.setHeaderText("Product not selected");
                alertError.setContentText("Must select a product from table.");
                alertError.showAndWait();
                break;
            case 5:
                alertError.setTitle("Error");
                alertError.setHeaderText("Parts Associated With Product");
                alertError.setContentText("Must remove associated parts before product can be removed.");
                alertError.showAndWait();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + alertType);
        }
    }

    /**
     * Initializes MainScreen controller and populates both table views.
     *
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    public void initialize(URL location, ResourceBundle resources)
    {
        partTableView.setItems(Inventory.getAllParts());
        partIdColumn.setCellValueFactory(new PropertyValueFactory("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory("price"));
        productTableView.setItems(Inventory.getAllProducts());
        productIdColumn.setCellValueFactory(new PropertyValueFactory("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        productInventoryColumn.setCellValueFactory(new PropertyValueFactory("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory("price"));
    }
}
