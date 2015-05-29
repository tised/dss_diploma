package com.tised.admin_program.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RootLayoutController implements Initializable{

    final static Logger logger = LogManager.getLogger(RootLayoutController.class);
	@FXML
	Button startAddingInfo, addAlternative;
	
	@FXML
	Button addCriteria;
	
	@FXML
	Pane addPanelCriterias;
	
	
	public void initialize(java.net.URL location,
            java.util.ResourceBundle resources){

		addPanelCriterias.setDisable(true);
	}
	
	@FXML
	public void addClicked(){
		
		logger.info("clicked");
		//addPanelCriterias.setDisable(true);
	}
	
	@FXML
	public void addCriteriaClicked(){

        logger.info("clicked add criteria");
	}

}
