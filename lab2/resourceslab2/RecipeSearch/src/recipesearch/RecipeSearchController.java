
package recipesearch;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.sun.xml.internal.ws.api.FeatureConstructor;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.collections.*;

import java.util.*;

import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;



public class RecipeSearchController implements Initializable,PropertyChangeListener {
    
    @FXML private MenuBar menuBar;


    //The different "Tabs" that contain information.
    @FXML private AnchorPane pane1;
    @FXML private AnchorPane pane2;
    @FXML private AnchorPane pane3;
    
    @FXML private ChoiceBox<String> cuisineChoiceBox;
    @FXML private ChoiceBox ingredientChoiceBox;
    @FXML private RadioButton ezRadio;
    @FXML private RadioButton lessEzRadio;
    @FXML private RadioButton notEzRadio;
    @FXML private TextField priceTextField;
    @FXML private Slider timeSlider;
    @FXML private Button searchButton;
    @FXML private ToggleGroup difficulty;
    @FXML private VBox resultVBox;

    private

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    List <Recipe> recipes;

    //Pane numero dos
    @FXML private AnchorPane recipe1;

    //Pane numero dres
    @FXML private Label nameText;
    @FXML private Label ingredientText;
    @FXML private Label recipeText;
    @FXML private ImageView recipePic;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setToggleGroupRadioButtons();
        setCuisineChoiceBox();
        setIngredientChoiceBox();
    }

    private void setCuisineChoiceBox(){

        cuisineChoiceBox.setItems(FXCollections.observableArrayList("Sverige",
                "Grekland",
                "Indien",
                "Asien",
                "Afrika",
                "Frankrike"));

    }
    private void setIngredientChoiceBox(){
        ingredientChoiceBox.setItems(FXCollections.observableArrayList("Kött",
                "Fisk",
                "Kyckling",
                "Vegetarisk"));

    }
    private void setToggleGroupRadioButtons(){
        ezRadio.setToggleGroup(difficulty);
        lessEzRadio.setToggleGroup(difficulty);
        notEzRadio.setToggleGroup(difficulty);
    }

    @FXML
    protected void openAboutActionPerformed(ActionEvent event) throws IOException{
    
        ResourceBundle bundle = java.util.ResourceBundle.getBundle("recipesearch/resources/RecipeSearch");
        Parent root = FXMLLoader.load(getClass().getResource("recipe_search_about.fxml"), bundle);
        Stage aboutStage = new Stage();
        aboutStage.setScene(new Scene(root));
        aboutStage.setTitle(bundle.getString("about.title.text"));
        aboutStage.initModality(Modality.APPLICATION_MODAL);
        aboutStage.setResizable(false);
        aboutStage.showAndWait();
    }
    
    @FXML 
    protected void closeApplicationActionPerformed(ActionEvent event) throws IOException{
        
        Stage addressBookStage = (Stage) menuBar.getScene().getWindow();
        addressBookStage.hide();
    }

    @FXML
    protected void backButton1ActionPreformed(ActionEvent event){
        pane1.toFront();
    }

    @FXML
    protected void backButton2ActionPreformed(ActionEvent event) {
        pane2.toFront();
    }

    @FXML
    protected void searchButtonActionPerformed(ActionEvent event){ 3

        String country = (String) cuisineChoiceBox.getValue();
        String mainIngredient = (String) ingredientChoiceBox.getValue();
        int cookingTime = (int)timeSlider.getValue();
        int price = getPrice();
        String diff = getDifficulty();

        recipes = db.search(new SearchFilter(diff,cookingTime,country,price,mainIngredient));
        setResults();


        pane2.toFront();

    }

    private void setResults(){
        int i = 0;

        resultVBox.getChildren().removeAll(resultVBox.getChildren());
        for(Recipe r:recipes){
            if(r!=null) {
                RecipeSearchResultPane temp = new RecipeSearchResultPane(r);
                temp.addPropertyChangeListener(this);
                resultVBox.getChildren().add(temp);
            }else{
                break;
            }
        }
    }

    private int getPrice(){
        if(priceTextField.getText()!=null) {
            String price = priceTextField.getText();

            try {
                return Integer.parseInt(price);
            } catch (NumberFormatException e1) {

            }
        }
        return 0;
    }

    private void hideRest (int i){

    }


    private String getDifficulty() {
        if (notEzRadio.isSelected()) {
            return "Svår";
        }
        if (lessEzRadio.isSelected()) {
            return "Mellan";
        }
        if (ezRadio.isSelected()) {
            return "Lätt";
        }
        return null;
    }

    protected void backButtonActionPreformed(ActionEvent event){
        System.out.println(event.getSource().toString());
    }
    @FXML
    protected void recipeChoosen(Recipe r){
        nameText.setText(r.getName());
        ingredientText.setText(r.getIngredients().toString());
        recipeText.setText(r.getInstruction());
        recipePic.setImage(r.getFXImage());
        pane3.toFront();
    }
    @FXML
    protected void recipeHoover(MouseEvent event){
        //TODO
    }

    public void propertyChange(PropertyChangeEvent evt){

        if(evt.getPropertyName().equals("recipe selected")){
            recipeChoosen((Recipe)evt.getNewValue());
        }
    }

}
