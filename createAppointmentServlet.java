package com.counselling.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.counselling.model.Appointment;
import com.counselling.model.Counselor;
import com.counselling.model.Student;

public class createAppointmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection details
    private static final String DB_URL = "jdbc:derby://localhost:1527/eCounsellingDB";
    private static final String DB_USER = "app";
    private static final String DB_PASSWORD = "app";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve form data
        String appReason = request.getParameter("app_reason");
        LocalDate appDate = LocalDate.parse(request.getParameter("app_date"));
        LocalTime appTime = LocalTime.parse(request.getParameter("app_time"));
        
        // Mock counselor and student data
        Counselor counselor = new Counselor("John Doe", "1234567890", "john@example.com");
        Student student = new Student("Jane Smith", "S123456", "123456789012", "Computer Science", "Engineering", "A", "Y", "password");

        // Create appointment object
        Appointment appointment = new Appointment();
        appointment.setAppReason(appReason);
        appointment.setAppDate(appDate);
        appointment.setAppTime(appTime);
        appointment.setCounselor(counselor);
        appointment.setStudent(student);

        // Insert appointment into database
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO appointment (app_reason, app_date, app_time, counselor_name, student_name, student_id, course, faculty, class) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, appReason);
                preparedStatement.setDate(2, java.sql.Date.valueOf(appDate));
                preparedStatement.setTime(3, java.sql.Time.valueOf(appTime));
                preparedStatement.setString(4, counselor.getCouns_name());
                preparedStatement.setString(5, student.getStud_name());
                preparedStatement.setString(6, student.getStud_ID());
                preparedStatement.setString(7, student.getStud_course());
                preparedStatement.setString(8, student.getStud_fac());
                preparedStatement.setString(9, student.getStud_class());

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    // Redirect to view appointments page if insertion is successful
                    response.sendRedirect(request.getContextPath() + "/View_appointment.jsp");
                } else {
                    // Handle insertion failure
                    response.getWriter().println("Failed to create appointment. Please try again.");
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Database access error", e);
        }
    }
}
