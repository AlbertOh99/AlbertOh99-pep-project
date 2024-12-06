package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    /**
     * Add a new user
     * 
     * @param account
     * @return a new user if the sql statement works
     */
    public Account insertAccount(Account account) { // Add tests to prevent repeate users, users with blank names, < 4
                                                    // character passwords.
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.executeUpdate();
            ResultSet pkeyRS = ps.getGeneratedKeys();
            if (pkeyRS.next()) {
                int newId = (int) pkeyRS.getLong(1);
                return new Account(newId, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Return the account with the given id.
     * @param id
     * @return
     */
    public Account getAccountId(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE account_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    
    /**
     * Return all accounts in the database
     * @return
     */
    public List<Account> getAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            String sql = "SELECT * FROM account";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                accounts.add(account);
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public Account validLogin(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account where username = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                if(password.equals(account.getPassword())){
                    return account;
                }
            }
          } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    // Tests if the new user has a valid username that is not blank or already in
    // the database
    /**
     * Valids the username by checking if it already used.
     * 
     * @param username
     * @return
     */
    public boolean sameUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                if(username.equals(account.getUsername())){
                    return true;
                }
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return false;
    }


}
