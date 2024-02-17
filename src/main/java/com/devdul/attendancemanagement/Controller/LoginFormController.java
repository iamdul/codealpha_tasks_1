package com.devdul.attendancemanagement.Controller;

import com.devdul.attendancemanagement.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Objects;
import java.sql.*;

public class LoginFormController {

    @FXML
    public TextField txtUsername;

    @FXML
    public AnchorPane context;

    @FXML
    public PasswordField txtPassword;

    public void studentOnAction(ActionEvent actionEvent) throws IOException,ClassNotFoundException {
        String username=txtUsername.getText().toLowerCase();
        String password=txtPassword.getText().trim();
        try{
            User selectedUser=login(username);
            if (selectedUser != null) {
                if(password.equals(selectedUser.getPassword())){
                    new Alert(Alert.AlertType.INFORMATION, "Login Successful..!").show();
                    setUi("MarkAttendanceForm");
                }else{
                    new Alert(Alert.AlertType.ERROR, "Wrong Password.!").show();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "User Not Found").show();
            }
        }catch(ClassNotFoundException|SQLException e){
            new Alert(Alert.AlertType.ERROR,e.toString()).show();
        }
    }

    public void adminOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        String username=txtUsername.getText().toLowerCase();
        String password=txtPassword.getText().trim();
        try{
            User selectedUser=login(username);
            if (selectedUser != null) {
                if(password.equals(selectedUser.getPassword())){
                    if(selectedUser.getAdmin()){
                        new Alert(Alert.AlertType.INFORMATION, "Login Successful..!").show();
                        setUi("DashboardForm");
                    }else{
                        new Alert(Alert.AlertType.WARNING, "You are not allowed to login.!").show();
                    }
                }else{
                    new Alert(Alert.AlertType.ERROR, "Wrong Password.!").show();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "User Not Found").show();
            }
        }catch(ClassNotFoundException|SQLException e){
            new Alert(Alert.AlertType.ERROR,e.toString()).show();
        }
    }


    public void setUi(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/devdul/attendancemanagement/View/" + location + ".fxml"));
        if (loader.getLocation() == null) {
            System.out.println("Resource is null!");
        }
        stage.setScene(new Scene(loader.load()));
        stage.centerOnScreen();
    }

    //============================================================
    private User login(String username) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection=
        DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance","root","1234");
        String sql="SELECT * FROM user WHERE username=? ";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,username);
        ResultSet resultSet= statement.executeQuery();
        if(resultSet.next()){
            User user= new User(
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("email"),
                    resultSet.getString("name"),
                    resultSet.getBoolean("isAdmin")
            );
            return user;
        }
        return null;
    }
}