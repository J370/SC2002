public abstract class User {
    private String userID;
    private String password = "password";
    private int Age;
    private String MaritalStatus;

    public User(String userID, String password, int Age, String MaritalStatus) {}
    public void login(String loginUser, String loginPassword){
        if(loginUser.equals(this.userID) && loginPassword.equals(this.password)){}
    }
    public void changePassword(String newPassword){
        this.password = newPassword;
    }
}
