package Service;

import DAO.AccountDAO;
import Model.Account;

import java.util.List;
import java.util.Optional;

public class AccountService {
   /**
    * The below comment is from the flight traker excercise, to help me understand
    * what a services class should do.
    * The purpose of a Service class is to contain "business logic" that sits
    * between the web layer (controller) and
    * persistence layer (DAO). That means that the Service class performs tasks
    * that aren't done through the web or
    * SQL: programming tasks like checking that the input is valid, conducting
    * additional security checks, or saving the
    * actions undertaken by the API to a logging file.
    *
    * It's perfectly normal to have Service methods that only contain a single line
    * that calls a DAO method. An
    * application that follows best practices will often have unnecessary code, but
    * this makes the code more
    * readable and maintainable in the long run!
    */

   AccountDAO accountDAO;

   // No argument constructor
   public AccountService() {
      accountDAO = new AccountDAO();
   }

   /**
    * Constructor for when accountDAO is provided
    * 
    * @param accountDAO
    */
   public AccountService(AccountDAO accountDAO) {
      this.accountDAO = accountDAO;
   }

   /**
    * Should have more logic for if an account already exists
    * 
    * @param account
    * @return the new account
    */
   public Account addAccount(Account account) {
      //test for valid account information for a new account.
      if(accountDAO.sameUsername(account.getUsername()) || account.getUsername() == "" || account.getPassword().length() < 4){
         return null;
      }

      Account tempAccount = accountDAO.insertAccount(account);
      return tempAccount;
   }


   public Account validLogin(Account account) {
      if(account.getUsername() == "" || account.getPassword().length() < 4){
         return null;
      }

      Account tempAccount = accountDAO.validLogin(account.getUsername(), account.getPassword());
      if(tempAccount != null){
         return tempAccount;
      }
      return null;
   }

   public Account getAccountId(int id){
      Account account = accountDAO.getAccountId(id);
      return account;
   }





}
