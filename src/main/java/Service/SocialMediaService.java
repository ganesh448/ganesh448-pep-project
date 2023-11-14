package Service;

import DAO.SocialMediaDAO;
import Model.Account;
public class SocialMediaService {
    SocialMediaDAO socialMediaDAO;
    public SocialMediaService()
    {
        socialMediaDAO = new SocialMediaDAO();
    }
    public SocialMediaService(SocialMediaDAO socialMediaDAO)
    {
        this.socialMediaDAO=socialMediaDAO;
    }
    private boolean isValidAccount(Account account)
    {
        return account.getUsername() !=null && !account.getUsername().isEmpty() && account.getPassword() != null && account.getPassword().length() >=4;
    }
    public Account registerAccount(Account account)
    {
        if(isValidAccount(account))
        {
            if(socialMediaDAO.getAccountByUsername(account.getUsername()) == null)
            {
                Account createdAccount = socialMediaDAO.createAccount(account);
                return createdAccount;
            }
        }
        return null;
    }
    public Account login(String username, String password)
    {
        Account account = socialMediaDAO.getAccountByUsername(username);
        if(account != null && account.getPassword().equals(password))
        {
            return account;
        }
        return null;
    }
}
