package movieManagement.data;

import movieManagement.src.users.Admin;
import movieManagement.src.authentication.CredentialsCheck;

public class PredefinedAdmins {
    
    private static final String[][] ADMIN_DATA = {
        {"admin", "Admin123!"},
        {"superadmin", "SuperAdmin456!"},
        {"manager", "Manager789!"}
    };
    
    public static Admin[] loadAdmins() {
        Admin[] admins = new Admin[ADMIN_DATA.length];
        
        for (int i = 0; i < ADMIN_DATA.length; i++) {
            String username = ADMIN_DATA[i][0];
            String password = ADMIN_DATA[i][1];
            CredentialsCheck credential = new CredentialsCheck(password);
            admins[i] = new Admin(username, credential);
        }
        
        return admins;
    }
    
    public static Admin loadAdmin() {
        Admin[] admins = loadAdmins();
        return admins[0];
    }
}
