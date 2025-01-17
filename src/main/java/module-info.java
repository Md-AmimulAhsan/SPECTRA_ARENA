module com.example.spectra_arena {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    opens com.example.spectra_arena to javafx.fxml;
    exports com.example.spectra_arena;
}