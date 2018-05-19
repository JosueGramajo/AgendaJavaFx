/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agenda;

import handlers.ActivityHandler;
import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import objects.Activity;
import objects.ActivityList;
import utils.JsonUtils;
/**
 *
 * @author Josue Gramajo
 */
public class Agenda extends Application{    
    private GridPane grid;
    private Scene scene;
    private Stage stage;
    
    private Label lbNombre, lbLugar, lbFecha, lbObservaciones, lbTipo; 
    private TextField tfNombre, tfLugar, tfFecha, tfObservaciones;
    private ComboBox cbTipo;
    private Button btnAgregar, btnEditar, btnCancelarEdicion;
    private TableView tblActivity;
    
    private final ObservableList<Activity> data = FXCollections.observableArrayList();
    
    private int editingId = 0;
    
    public void getActivities(){
        data.clear();
        try{
            ActivityList list = JsonUtils.INSTANCIA.readJSON(JsonUtils.FILE_TYPE.AGENDA, ActivityList.class);
            for(Activity act : list.getList()){
                data.add(act);
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    @Override
    public void start(Stage primaryStage) {
        getActivities();
                
        lbNombre = new Label("Nombre");
        tfNombre = new TextField();
        tfNombre.setMaxWidth(150);
        
        lbLugar = new Label("Lugar");
        tfLugar = new TextField();
        tfLugar.setMaxWidth(150);
        
        lbFecha = new Label("Fecha");
        tfFecha = new TextField();
        tfFecha.setMaxWidth(150);
        
        lbObservaciones = new Label("Observasiones");
        tfObservaciones = new TextField();
        tfObservaciones.setMaxWidth(150);
        
        lbTipo = new Label("Tipo de actividad");
        cbTipo = new ComboBox();
        cbTipo.setMaxWidth(150);
        
        btnAgregar = new Button("Agregar");
        btnEditar = new Button("Editar");
        btnEditar.setVisible(false);
        
        btnCancelarEdicion = new Button("Cancelar");
        btnCancelarEdicion.setVisible(false);
        
        tblActivity = new TableView();
        tblActivity.setPrefWidth(800);
        tblActivity.setEditable(true);

        
        TableColumn colNombre = new TableColumn("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<Activity,String>("nombre"));
        
        TableColumn colLugar = new TableColumn("Lugar");
        colLugar.setCellValueFactory(new PropertyValueFactory<Activity,String>("lugar"));
        
        TableColumn colFecha = new TableColumn("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<Activity,String>("fecha"));
        
        TableColumn colObservaciones = new TableColumn("Observaciones");
        colObservaciones.setCellValueFactory(new PropertyValueFactory<Activity,String>("observaciones"));
        
        TableColumn colTipo = new TableColumn("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<Activity,String>("tipo"));
        
        TableColumn colAction = new TableColumn("");
        colAction.setCellFactory(new PropertyValueFactory<>(""));
        
        TableColumn colActionEdit = new TableColumn("");
        colActionEdit.setCellFactory(new PropertyValueFactory<>(""));
        
        Callback<TableColumn<Activity,String>, TableCell<Activity, String>> cellFactory;
        cellFactory = new Callback<TableColumn<Activity,String>, TableCell<Activity, String>>(){
            @Override
            public TableCell<Activity, String> call(TableColumn<Activity, String> param) {
                final TableCell<Activity, String> cell = new TableCell<Activity, String>(){
                    final Button btn = new Button("Eliminar");
                    
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                Activity activity = getTableView().getItems().get(getIndex());
                                ActivityHandler handler = new ActivityHandler();
                                if(handler.deleteActivity(activity)){
                                    showAlert("Actividad eliminada exitosamente");
                                    getActivities();
                                    tblActivity.refresh();
                                }else{
                                    showAlert("Ocurrio un error al intentar eliminar la actividad");
                                }
                                
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }                    
                };
                return cell;
            }   
        };
        colAction.setCellFactory(cellFactory);
        
        
        Callback<TableColumn<Activity,String>, TableCell<Activity, String>> cellFactoryEdicion;
        cellFactoryEdicion = new Callback<TableColumn<Activity,String>, TableCell<Activity, String>>(){
            @Override
            public TableCell<Activity, String> call(TableColumn<Activity, String> param) {
                final TableCell<Activity, String> cell = new TableCell<Activity, String>(){
                    final Button btn = new Button("Editar");
                    
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                Activity activity = getTableView().getItems().get(getIndex());
                                btnAgregar.setVisible(false);
                                btnEditar.setVisible(true);
                                btnCancelarEdicion.setVisible(true);
                                
                                tfNombre.setText(activity.getNombre());
                                tfLugar.setText(activity.getLugar());
                                tfFecha.setText(activity.getFecha());
                                tfObservaciones.setText(activity.getObservaciones());
                                editingId = activity.getId();
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }                    
                };
                return cell;
            }   
        };
        colActionEdit.setCellFactory(cellFactoryEdicion);
        
        tblActivity.setItems(data);
        tblActivity.getColumns().addAll(colNombre, colLugar, colFecha, colObservaciones, colTipo, colAction, colActionEdit);

        ObservableList<String> options = 
        FXCollections.observableArrayList(
            "Trabajo",
            "Estudio",
            "Ocio",
            "Iglesia"
        );
        lbTipo = new Label("Tipo de actividad");
        cbTipo = new ComboBox(options);  
        
        grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.add(lbNombre, 0,1);
        grid.add(tfNombre, 1,1);
        grid.add(lbLugar, 0,2);
        grid.add(tfLugar, 1,2);
        grid.add(lbFecha, 0,3);
        grid.add(tfFecha, 1,3);
        grid.add(lbObservaciones,0,4);
        grid.add(tfObservaciones, 1,4);
        grid.add(lbTipo, 0,6);
        grid.add(cbTipo, 1,6);
        grid.add(btnAgregar,1,8);
        grid.add(btnEditar,0,8);
        grid.add(btnCancelarEdicion, 1, 8);
        grid.add(tblActivity, 0, 9, 2,1);
             
                
        scene = new Scene(grid, 660, 800);
        stage = new Stage();
   
        stage.setTitle("Agenda");
        stage.setScene(scene);

        eventos();
        stage.show();
    }

    private boolean checkFields(){
        if(tfNombre.getText().toString().equals("")){
            showAlert("Ingrese nombre");
            return false;
        }
        if(tfLugar.getText().toString().equals("")){
            showAlert("Ingrese lugar");
            return false;
        }
        if(tfFecha.getText().toString().equals("")){
            showAlert("Ingrese fecha");
            return false;
        }
        if(cbTipo.getValue() == null){
            showAlert("Ingrese el tipo de actividad");
            return false;
        }

        return true;
    }
    
    public void eventos(){
        btnAgregar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) { 
                if(checkFields()){
                    Activity activity = new Activity();
                    activity.setId(1);
                    activity.setNombre(tfNombre.getText());
                    activity.setLugar(tfLugar.getText());
                    activity.setFecha(tfFecha.getText());
                    activity.setObservaciones(tfObservaciones.getText());
                    activity.setTipo(cbTipo.getValue().toString());

                    if(ActivityHandler.INSTANCE.addActivity(activity)){
                        showAlert("Actividad Agregada Exitosamente");
                        getActivities();
                        tblActivity.refresh();
                        
                        tfNombre.setText("");
                        tfLugar.setText("");
                        tfFecha.setText("");
                        tfObservaciones.setText("");
                    }else{
                        showAlert("Ocurrio un error al agregar la actividad");
                    }                
                }
            }
        });
        btnEditar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                
                if(checkFields()){
                    Activity activity = new Activity();
                    activity.setId(editingId);
                    activity.setNombre(tfNombre.getText());
                    activity.setFecha(tfFecha.getText());
                    activity.setLugar(tfLugar.getText());
                    activity.setObservaciones(tfObservaciones.getText());
                    activity.setTipo(cbTipo.getValue().toString());

                    if(ActivityHandler.INSTANCE.editActivity(activity)){
                        showAlert("Actividad Editada Exitosamente");
                        getActivities();
                        tblActivity.refresh();
                        
                        btnEditar.setVisible(false);
                        btnCancelarEdicion.setVisible(false);
                        btnAgregar.setVisible(true);

                        tfNombre.setText("");
                        tfLugar.setText("");
                        tfFecha.setText("");
                        tfObservaciones.setText("");
                        
                        editingId = 0;
                    }else{
                        showAlert("Ocurrio un error al editar la actividad");
                    }               
                }
            }
        });
        btnCancelarEdicion.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btnEditar.setVisible(false);
                btnCancelarEdicion.setVisible(false);
                btnAgregar.setVisible(true);
                
                tfNombre.setText("");
                tfLugar.setText("");
                tfFecha.setText("");
                tfObservaciones.setText("");
            }
        });
    }
    
    private void showAlert(String msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacion");
        alert.setContentText(msg);
        alert.showAndWait();
    }
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
