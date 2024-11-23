module Motoverse {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
	opens views to javafx.fxml;
    opens controllers to javafx.fxml;
	exports controllers to javafx.fxml;
	
	opens dao to javafx.base;
	opens utils to javafx.base;
	opens models to javafx.base;
}
