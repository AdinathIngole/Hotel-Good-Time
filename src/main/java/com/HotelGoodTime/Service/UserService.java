package com.HotelGoodTime.Service;

import com.HotelGoodTime.DTO.LoginRequest;
import com.HotelGoodTime.DTO.Response;
import com.HotelGoodTime.Entity.User;

public interface UserService {

	Response register(User user);
	Response login(LoginRequest loginRequest);
	Response getAllUsers();
	Response getUserBookingHistory(String userId);
	Response deleteUser(String userId);
	Response getUserById(String userId);
	Response getMyInfo(String email);
}
