package com.mkalugin;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.event.EventHandler;

public class Ui {
    
    static ObservableList<TableEntry> data = FXCollections.observableArrayList();
    static ObservableList<Integer> blocknumbers = FXCollections.observableArrayList();
    static ObservableList<TableHexEntry> editordata = FXCollections.observableArrayList();

    static int currentEditorOffset = 0;
    static int selectedOffsetInt = 0;

    @FXML
    private Tab propertiesTab;

    @FXML
    private Tab editorTab;
 
    @FXML
    private TableView<TableEntry> table;
    @FXML
    private HBox bottomMenu;
    @FXML
    private TableColumn<TableEntry, String> paramColumn;
    @FXML
    private TableColumn<TableEntry, String> valueColumn;
    @FXML
    private TableColumn<TableEntry, String> offsetColumn;
    @FXML
    private ChoiceBox<Integer> blockselector;
    @FXML
    private Button savebin;
    @FXML
    private Button calcFullChecksum;
    @FXML
    private Button calcBlockChecksum;
    @FXML
    private Text fullChecksum;
    @FXML
    private Text blockChecksum;
    @FXML
    private TextField customOffset;
    @FXML
    private TextField customData;

    @FXML
    private TextField editorOffset;
    @FXML
    private Text offset1;
    @FXML
    private Text offset2;
    @FXML
    private Text offset3;
    @FXML
    private Text offset4;
    @FXML
    private Text offset5;
    @FXML
    private Text offset6;
    @FXML
    private Text offset7;

    @FXML
    private Text ascii0;

    @FXML
    private TableView<TableHexEntry> editor;
    @FXML
    private TableColumn<TableHexEntry, String> c0;
    @FXML
    private TableColumn<TableHexEntry, String> c1;
    @FXML
    private TableColumn<TableHexEntry, String> c2;
    @FXML
    private TableColumn<TableHexEntry, String> c3;
    @FXML
    private TableColumn<TableHexEntry, String> c4;
    @FXML
    private TableColumn<TableHexEntry, String> c5;
    @FXML
    private TableColumn<TableHexEntry, String> c6;
    @FXML
    private TableColumn<TableHexEntry, String> c7;
    @FXML
    private TableColumn<TableHexEntry, String> c8;
    @FXML
    private TableColumn<TableHexEntry, String> c9;
    @FXML
    private TableColumn<TableHexEntry, String> ca;
    @FXML
    private TableColumn<TableHexEntry, String> cb;
    @FXML
    private TableColumn<TableHexEntry, String> cc;
    @FXML
    private TableColumn<TableHexEntry, String> cd;
    @FXML
    private TableColumn<TableHexEntry, String> ce;
    @FXML
    private TableColumn<TableHexEntry, String> cf;

    @FXML
    private HBox editorTools;
    @FXML
    private Text selectedOffset;
    @FXML
    private TextField actualData;
    @FXML
    private ScrollBar editorBar;



    @FXML
    private void openFileDialog() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Открыть SGO");
        fileChooser.getExtensionFilters().addAll(
                                                    new FileChooser.ExtensionFilter("SGO Files", "*.sgo")
                                                );
        if (App.setFile(fileChooser.showOpenDialog(App.getWindow()))) {
            table.setItems(data);
            editor.setItems(editordata);
            blockselector.getSelectionModel().clearSelection();
            blockselector.setItems(blocknumbers);
            blockselector.setValue(0);
            editorOffset.setText("00000000");
            editorBar.setMax(Sgotool.getBufferSize() - Sgotool.getBufferSize() % 16);
            showAll();
            updateEditor();
            updateActualData();
        }
    }

    @FXML
    private void saveFileDialog() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить SGO");
        fileChooser.getExtensionFilters().addAll(
                                                    new FileChooser.ExtensionFilter("SGO Files", "*.sgo")
                                                );
        Sgotool.saveFile(fileChooser.showSaveDialog(App.getWindow()));        
    }

    @FXML
    private void initialize() {

        //customOffset.setOnAction(eventHandler);
        editorOffset.setOnAction(eventHandler2);
        editor.setOnScroll(event -> {
                                                double deltaY = event.getDeltaY();
                                                if (deltaY < 0) 
                                                {
                                                    if (currentEditorOffset > Sgotool.getBufferSize() - 16)
                                                    {
                                                        currentEditorOffset = (Sgotool.getBufferSize() - 16) - (Sgotool.getBufferSize() - 16) % 0x10;
                                                    } else
                                                    {
                                                        currentEditorOffset += 0x10;
                                                    }                                                    
                                                    editorOffset.setText(String.format("%08X", currentEditorOffset));
                                                    updateEditor();
                                                } else if (deltaY > 0) 
                                                {
                                                    if (currentEditorOffset <= 0x10)
                                                    {
                                                        currentEditorOffset = 0;
                                                    } else
                                                    {
                                                        currentEditorOffset -= 0x10;
                                                    }   
                                                    editorOffset.setText(String.format("%08X", currentEditorOffset));
                                                    updateEditor();
                                                }
                                             });
        actualData.setOnKeyPressed(pressEnter);
        table.setPlaceholder(new Label("SGO-файл не загружен"));
        paramColumn.setCellValueFactory(cellData -> cellData.getValue().parameterProperty());
        valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        offsetColumn.setCellValueFactory(cellData -> cellData.getValue().offsetProperty());

        editorBar.setMin(0);
        editorBar.valueProperty().addListener(event -> {
            currentEditorOffset = (int)editorBar.getValue() - (int)editorBar.getValue() % 16;
            editorOffset.setText(String.format("%08X", currentEditorOffset));
            updateEditor();
        });

        hideUnused();
        initEditorTable();
    }

    @FXML
    private void saveToBin() throws IOException {

        int blockNumber = blockselector.getValue();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить bin");
        fileChooser.getExtensionFilters().addAll(
                                                    new FileChooser.ExtensionFilter("BIN Files", "*.bin")
                                                );        
        Sgotool.saveToBin(fileChooser.showSaveDialog(App.getWindow()), blockNumber);        
    }

    @FXML
    private void loadBin() throws IOException {
        int blockNumber = blockselector.getValue();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузить bin");
        fileChooser.getExtensionFilters().addAll(
                                                    new FileChooser.ExtensionFilter("BIN Files", "*.bin")
                                                );
        Sgotool.loadBin(fileChooser.showOpenDialog(App.getWindow()), blockNumber);                   
    }

    @FXML
    private void calcFullChecksum() throws IOException {
        fullChecksum.setText(String.format("0x%08X", Sgotool.calcFullChecksum()));
    }

    @FXML
    private void calcBlockChecksum() throws IOException {
        blockChecksum.setText(String.format("0x%04X", Sgotool.calcBlockChecksum(blockselector.getValue())));
    }

    EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int address = Integer.decode("0x" + customOffset.getText());
                customData.setText(String.format("0x%02X", Sgotool.getByteFromBuffer(address)));
            }
    };

    EventHandler<ActionEvent> eventHandler2 = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            currentEditorOffset = Integer.decode("0x" + editorOffset.getText());
            updateEditor();
        }
    };

    EventHandler<MouseEvent> clickEvent = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2){}

                int row = editor.getSelectionModel().getSelectedIndex();
                int column = editor.getSelectionModel().getSelectedCells().get(0).getColumn();
                
                selectedOffsetInt = currentEditorOffset + row * 16 + column;

                if (Sgotool.addressExist(selectedOffsetInt)) 
                {
                    updateActualData();
                }                
            }
        }
    };

    EventHandler<KeyEvent> pressEnter = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent ke) {
            if (ke.getCode().equals(KeyCode.ENTER)) 
            {
                writeActualData();               
            }
        }
    };

    @FXML
    private void writeActualData() {
        try {
            int i = Integer.decode("0x" + actualData.getText());
            Sgotool.setByteToBuffer(selectedOffsetInt, i);
            if (selectedOffsetInt < Sgotool.getBufferSize() - 1)
            {
                selectedOffsetInt++;
            }
            updateEditor();  
            updateActualData();
        } 
        catch (Exception e) {}        
    }
 

    private void hideUnused() {
        bottomMenu.setVisible(false);
        propertiesTab.setDisable(true);   
        editorTab.setDisable(true); 
    }

    private void showAll() {
        bottomMenu.setVisible(true);
        propertiesTab.setDisable(false);   
        editorTab.setDisable(false);    
    }

    private void initEditorTable()
    {
        c0.setCellValueFactory(cellData -> cellData.getValue().getc0());
        c1.setCellValueFactory(cellData -> cellData.getValue().getc1());
        c2.setCellValueFactory(cellData -> cellData.getValue().getc2());
        c3.setCellValueFactory(cellData -> cellData.getValue().getc3());
        c4.setCellValueFactory(cellData -> cellData.getValue().getc4());
        c5.setCellValueFactory(cellData -> cellData.getValue().getc5());
        c6.setCellValueFactory(cellData -> cellData.getValue().getc6());
        c7.setCellValueFactory(cellData -> cellData.getValue().getc7());
        c8.setCellValueFactory(cellData -> cellData.getValue().getc8());
        c9.setCellValueFactory(cellData -> cellData.getValue().getc9());
        ca.setCellValueFactory(cellData -> cellData.getValue().getca());
        cb.setCellValueFactory(cellData -> cellData.getValue().getcb());
        cc.setCellValueFactory(cellData -> cellData.getValue().getcc());
        cd.setCellValueFactory(cellData -> cellData.getValue().getcd());
        ce.setCellValueFactory(cellData -> cellData.getValue().getce());
        cf.setCellValueFactory(cellData -> cellData.getValue().getcf());

        editor.setOnMouseClicked(clickEvent);
    }

    private void updateEditor()
    {
        editordata.clear();
        
        editordata.add(0, Sgotool.getHexString(currentEditorOffset));
        editordata.add(1, Sgotool.getHexString(currentEditorOffset + 0x10));
        editordata.add(2, Sgotool.getHexString(currentEditorOffset + 0x20));
        editordata.add(3, Sgotool.getHexString(currentEditorOffset + 0x30));
        editordata.add(4, Sgotool.getHexString(currentEditorOffset + 0x40));
        editordata.add(5, Sgotool.getHexString(currentEditorOffset + 0x50));
        editordata.add(6, Sgotool.getHexString(currentEditorOffset + 0x60));
        editordata.add(7, Sgotool.getHexString(currentEditorOffset + 0x70));

        //ascii0.setText(Sgotool.getStringFromHex(editor.getItems().get(0)));

        offset1.setText(String.format("0x%08X", currentEditorOffset + 0x10));
        offset2.setText(String.format("0x%08X", currentEditorOffset + 0x20));
        offset3.setText(String.format("0x%08X", currentEditorOffset + 0x30));
        offset4.setText(String.format("0x%08X", currentEditorOffset + 0x40));
        offset5.setText(String.format("0x%08X", currentEditorOffset + 0x50));
        offset6.setText(String.format("0x%08X", currentEditorOffset + 0x60));
        offset7.setText(String.format("0x%08X", currentEditorOffset + 0x70));
    }

    private void updateActualData()
    {
        selectedOffset.setText(String.format("0x%08X", selectedOffsetInt));
        actualData.setText(String.format("%02X", Sgotool.getByteFromBuffer(selectedOffsetInt)));
    }
}
