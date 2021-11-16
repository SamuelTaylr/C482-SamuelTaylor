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

public class MainScreenController implements Initializable {
    private static Part partToModify;
    private static Product productToModify;
    @FXML
    private TextField partSearchText;
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
    private TextField productSearchText;
    @FXML
    private TableView<Product> productTableView;
    @FXML
    private TableColumn<Product, Integer> productIdColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, Integer> productInventoryColumn;
    @FXML
    private TableColumn<Product, Double> productPriceColumn;

    public MainScreenController() {
    }

    public static Part getPartToModify() {
        return partToModify;
    }

    public static Product getProductToModify() {
        return productToModify;
    }

    @FXML
    void exitButtonAction(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void partAddAction(ActionEvent event) throws IOException {
        Parent parent = (Parent)FXMLLoader.load(this.getClass().getResource("../view/AddPartScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void partDeleteAction(ActionEvent event) {
        Part selectedPart = (Part)this.partTableView.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            this.displayAlert(3);
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Do you want to delete the selected part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(selectedPart);
            }
        }

    }

    @FXML
    void partModifyAction(ActionEvent event) throws IOException {
        partToModify = (Part)this.partTableView.getSelectionModel().getSelectedItem();
        if (partToModify == null) {
            this.displayAlert(3);
        } else {
            Parent parent = (Parent)FXMLLoader.load(this.getClass().getResource("../view/ModifyPartScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
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
    void partSearchTextKeyPressed(KeyEvent event) {
        if (this.partSearchText.getText().isEmpty()) {
            this.partTableView.setItems(Inventory.getAllParts());
        }

    }

    @FXML
    void productAddAction(ActionEvent event) throws IOException {
        Parent parent = (Parent)FXMLLoader.load(this.getClass().getResource("../view/AddProductScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void productDeleteAction(ActionEvent event) {
        Product selectedProduct = (Product)this.productTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            this.displayAlert(4);
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Do you want to delete the selected product?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                ObservableList<Part> assocParts = selectedProduct.getAllAssociatedParts();
                if (assocParts.size() >= 1) {
                    this.displayAlert(5);
                } else {
                    Inventory.deleteProduct(selectedProduct);
                }
            }
        }

    }

    @FXML
    void productModifyAction(ActionEvent event) throws IOException {
        productToModify = (Product)this.productTableView.getSelectionModel().getSelectedItem();
        if (productToModify == null) {
            this.displayAlert(4);
        } else {
            Parent parent = (Parent)FXMLLoader.load(this.getClass().getResource("../view/ModifyProductScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }

    }

    @FXML
    void productSearchBtnAction(ActionEvent event) {
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        ObservableList<Product> productsFound = FXCollections.observableArrayList();
        String searchString = this.productSearchText.getText();
        Iterator var5 = allProducts.iterator();

        while(true) {
            Product product;
            do {
                if (!var5.hasNext()) {
                    this.productTableView.setItems(productsFound);
                    if (productsFound.size() == 0) {
                        this.displayAlert(2);
                    }

                    return;
                }

                product = (Product)var5.next();
            } while(!String.valueOf(product.getId()).contains(searchString) && !product.getName().contains(searchString));

            productsFound.add(product);
        }
    }

    @FXML
    void productSearchTextKeyPressed(KeyEvent event) {
        if (this.productSearchText.getText().isEmpty()) {
            this.productTableView.setItems(Inventory.getAllProducts());
        }

    }

    private void displayAlert(int alertType) {
        Alert alert = new Alert(AlertType.INFORMATION);
        Alert alertError = new Alert(AlertType.ERROR);
        switch(alertType) {
            case 1:
                alert.setTitle("Information");
                alert.setHeaderText("Part not found");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Information");
                alert.setHeaderText("Product not found");
                alert.showAndWait();
                break;
            case 3:
                alertError.setTitle("Error");
                alertError.setHeaderText("Part not selected");
                alertError.showAndWait();
                break;
            case 4:
                alertError.setTitle("Error");
                alertError.setHeaderText("Product not selected");
                alertError.showAndWait();
                break;
            case 5:
                alertError.setTitle("Error");
                alertError.setHeaderText("Parts Associated");
                alertError.setContentText("All parts must be removed from product before deletion.");
                alertError.showAndWait();
        }

    }

    public void initialize(URL location, ResourceBundle resources) {
        this.partTableView.setItems(Inventory.getAllParts());
        this.partIdColumn.setCellValueFactory(new PropertyValueFactory("id"));
        this.partNameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        this.partInventoryColumn.setCellValueFactory(new PropertyValueFactory("stock"));
        this.partPriceColumn.setCellValueFactory(new PropertyValueFactory("price"));
        this.productTableView.setItems(Inventory.getAllProducts());
        this.productIdColumn.setCellValueFactory(new PropertyValueFactory("id"));
        this.productNameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        this.productInventoryColumn.setCellValueFactory(new PropertyValueFactory("stock"));
        this.productPriceColumn.setCellValueFactory(new PropertyValueFactory("price"));
    }
}
