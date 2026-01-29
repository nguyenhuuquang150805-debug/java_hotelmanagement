package com.nguyenhuuquang.hotelmanagement.service;

import com.nguyenhuuquang.hotelmanagement.dto.request.AuthResponse;
import com.nguyenhuuquang.hotelmanagement.dto.request.ChangePasswordRequest;
import com.nguyenhuuquang.hotelmanagement.dto.request.ForgotPasswordRequest;
import com.nguyenhuuquang.hotelmanagement.dto.request.LoginRequest;
import com.nguyenhuuquang.hotelmanagement.dto.request.RegisterRequest;
import com.nguyenhuuquang.hotelmanagement.dto.request.ResetPasswordRequest;
import com.nguyenhuuquang.hotelmanagement.entity.User;

public interface AuthService {
    User register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    void changePassword(ChangePasswordRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);
}