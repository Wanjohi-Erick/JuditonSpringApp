package com.rickiey_innovates.juditonspringapp.models;

public class PasswordReset {
    String old, newPass, confirm;

    public PasswordReset(String old, String newPass, String confirm) {
        this.old = old;
        this.newPass = newPass;
        this.confirm = confirm;
    }

    public String getOld() {
        return old;
    }

    public void setOld(String old) {
        this.old = old;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
}
