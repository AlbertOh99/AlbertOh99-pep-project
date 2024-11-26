package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AccountDAO {
    public Account newAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();
            try(ResultSet generateKey = preparedStatement.getGeneratedKeys()){
                if(generateKey.next()){
                    int newId = generateKey.getInt(1);
                    return new Account(newId, account.getUsername(), account.getPassword());
                }
            } catch (SQLException e){
                System.out.println(e.getMessage()); //This is prob wrong I need to change this.
            }


            return account;
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }
}
