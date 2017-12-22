package com.skj.passwordview;

/**
 * Created by 孙科技 on 2017/11/9.
 */

public interface PasswordView {
    String getPassWord();

    void clearPassword();

    void setPassword(String password);

    void setPasswordVisibility(boolean visible);

    void togglePasswordVisibility();

    void setOnPasswordChangedListener(GridPasswordView.OnPasswordChangedListener listener);

    void setPasswordType(PasswordType passwordType);

    public enum PasswordType {

        NUMBER, TEXT, TEXTVISIBLE, TEXTWEB;

    }
}
