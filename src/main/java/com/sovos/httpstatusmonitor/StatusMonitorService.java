package com.sovos.httpstatusmonitor;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;

@Path("/monitor")
@ApplicationPath("/api")
public class StatusMonitorService extends Application {
    private static final String driverName = "com.mysql.cj.jdbc.Driver";
    private static final String serverName = "localhost";
    private static final String database = "monitor";
    private static final String url = "jdbc:mysql://" + serverName + "/" + database;
    private static final String username = "root";
    private static final String password = "root";
    Connection connection = null;
    PreparedStatement preparedStatement;

    @GET
    @Path("/say-hello")
    public String hello() {
        return "hello";
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public String newURL(MonitoredURLRequest monitoredURLRequest) {
        String insert = "insert into urls(url) values(?)";
        try {
            Class.forName(driverName);

            // create a database connection
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(insert);
            preparedStatement.setString(1, monitoredURLRequest.getUrl());
            preparedStatement.setQueryTimeout(30);  // set timeout to 30 sec.
            preparedStatement.executeUpdate();

        }catch(SQLException e){
            System.err.println(e.getMessage());
            return "SQL Exception";
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally{
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                System.err.println(e.getMessage());
            }
        }
        return "URL created";
    }

    @POST
    @Path("/insert")
    @Consumes(MediaType.APPLICATION_JSON)
    public String insertLogs(MonitoredLogRequest monitoredLogRequest) throws URISyntaxException, IOException, InterruptedException {
        long startTime = System.nanoTime();
        monitoredLogRequest.setResponse_code(MonitoredURL.monitorRequestStatusCode(monitoredLogRequest.getUrl()));
        long elapsedTime = (System.nanoTime() - startTime);
        monitoredLogRequest.setResponse_time((int)(elapsedTime/1000000));
        String findId = "select id from urls where url = ?";
        String insertLogs = "insert into responses(url_id, response_code, response_time) values(?, ?, ?)";
        try {
            Class.forName(driverName);

            // create a database connection
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(findId);
            preparedStatement.setString(1, monitoredLogRequest.getUrl());
            preparedStatement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet resultSet = preparedStatement.executeQuery();
            int urlId = 0;
            while (resultSet.next()){
                urlId = resultSet.getInt(1);
            }
            preparedStatement = connection.prepareStatement(insertLogs);
            preparedStatement.setInt(1, urlId);
            preparedStatement.setInt(2, monitoredLogRequest.getResponse_code());
            preparedStatement.setInt(3, monitoredLogRequest.getResponse_time());
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            System.err.println(e.getMessage());
            return "SQL Exception";
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally{
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                System.err.println(e.getMessage());
            }
        }
        return "Logs have been inserted";
    }

    @POST
    @Path("/select/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response selectLogs(@PathParam("id") int urlId){
        String selectLogs = "select response_code, response_time, created_at from responses where url_id = ? order by created_at desc limit 10";
        MonitoredLogsResponse monitoredLogsResponse = new MonitoredLogsResponse();
        try {
            Class.forName(driverName);
            // create a database connection
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(selectLogs);
            preparedStatement.setInt(1, urlId);
            preparedStatement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                MonitoredLog monitoredLog = new MonitoredLog();
                monitoredLog.setResponse_code(resultSet.getInt(1));
                monitoredLog.setResponse_time(resultSet.getInt(2));
                monitoredLog.setCreated_at(resultSet.getTimestamp(3));
                monitoredLogsResponse.addMonitoredLogs(monitoredLog);
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally{
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                System.err.println(e.getMessage());
            }
        }
        return Response.ok(monitoredLogsResponse.getMonitoredLogs()).build();
    }
}