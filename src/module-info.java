module Motoverse {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml;
	opens views to javafx.fxml;
    opens controllers to javafx.fxml;
	exports controllers to javafx.fxml;
}
