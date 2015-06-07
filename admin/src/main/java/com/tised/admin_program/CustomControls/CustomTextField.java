package com.tised.admin_program.CustomControls;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by tised_000 on 07.06.2015.
 */
public class CustomTextField extends javafx.scene.control.TextField{

    final static Logger logger = LogManager.getLogger(CustomTextField.class);

    public CustomTextField(){

        this.setOnMouseClicked(event -> {

            logger.debug("clicked on text field");
        });
    }
}

