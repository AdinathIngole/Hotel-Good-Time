package com.HotelGoodTime.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.HotelGoodTime.DTO.LoginRequest;
import com.HotelGoodTime.DTO.Response;
import com.HotelGoodTime.DTO.UserDTO;
import com.HotelGoodTime.Entity.User;
import com.HotelGoodTime.Exception.UserException;
import com.HotelGoodTime.Repository.UserRepository;
import com.HotelGoodTime.Service.UserService;
import com.HotelGoodTime.Utils.JWTUtils;
import com.HotelGoodTime.Utils.Utils;


@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JWTUtils jwtUtils;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	

	@Override
	public Response register(User user) {
		
		Response response = new Response();
		
		try {
			
			if(user.getRole() == null || user.getRole().isBlank()) {
				user.setRole("USER");
			}
			
			if(userRepository.existsByEmail(user.getEmail())) {
				throw new UserException(user.getEmail() + "Email is Already Exists");
			}
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			User saveUser = userRepository.save(user);
			
			UserDTO userDTO = Utils.mapUserEntityToUserDTO(saveUser);
			
			response.setStatusCode(200);
			response.setUser(userDTO);
			
		} catch(UserException e) {
			
			response.setStatusCode(400);
			response.setMessage(e.getMessage());
			
		} catch (Exception e) {
			
			response.setStatusCode(500);
			response.setMessage("Error Occurred During User Registration" + e.getMessage());
			
		}
		return response;
	}

	@Override
	public Response login(LoginRequest loginRequest) {
		
		Response response = new Response();
		
		try {
			
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
			
			var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new UserException("User Not Found"));
			var token = jwtUtils.genrateToken(user);
			
			response.setStatusCode(200);
			response.setToken(token);
			response.setRole(user.getRole());
			response.setExpirationTime("7 Days");
			response.setMessage("Successful");
			
		} catch(UserException e) {
			
			response.setStatusCode(404);
			response.setMessage(e.getMessage());
			
		} catch (Exception e) {
			
			response.setStatusCode(500);
			response.setMessage("Error Occurred During User Login" + e.getMessage());
			
		}
		return response;
	}

	@Override
	public Response getAllUsers() {
		
		Response response = new Response();
		
		try {
			
			List<User> userList = userRepository.findAll();
			List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userList);
			
			response.setStatusCode(200);
			response.setMessage("successful");
			response.setUserList(userDTOList);
			
			
		} catch (Exception e) {
			
			response.setStatusCode(500);
			response.setMessage("Error getting all users " + e.getMessage());
			
		}
		return response;
	}

	@Override
	public Response getUserBookingHistory(String userId) {

		Response response = new Response();
		
		try {
			User user =  userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new UserException("User Not Found"));
			
			UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);
			
			response.setStatusCode(200);
			response.setMessage("successful");
			response.setUser(userDTO);
			
		} catch(UserException e) {
			
			response.setStatusCode(404);
			response.setMessage(e.getMessage());
			
		} catch (Exception e) {
			
			response.setStatusCode(500);
			response.setMessage("Error getting users Booking history " + e.getMessage());
			
		}
		return response;
	}

	@Override
	public Response deleteUser(String userId) {

		Response response = new Response();
		
		try {
			 userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new UserException("User Not Found"));
			
			 userRepository.deleteById(Long.valueOf(userId));
			 
			response.setStatusCode(200);
			response.setMessage("successful");
			
		} catch(UserException e) {
			
			response.setStatusCode(404);
			response.setMessage(e.getMessage());
			
		} catch (Exception e) {
			
			response.setStatusCode(500);
			response.setMessage("Error getting delete User " + e.getMessage());
			
		}
		return response;
	}

	@Override
	public Response getUserById(String userId) {

		Response response = new Response();
		
		try {
			 User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new UserException("User Not Found"));
			
			 UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
			 
			response.setStatusCode(200);
			response.setMessage("successful");
			response.setUser(userDTO);
			
		} catch(UserException e) {
			
			response.setStatusCode(404);
			response.setMessage(e.getMessage());
			
		} catch (Exception e) {
			
			response.setStatusCode(500);
			response.setMessage("Error getting User by ID " + e.getMessage());
			
		}
		return response;
	}

	@Override
	public Response getMyInfo(String email) {

		Response response = new Response();
		
		try {
			 User user = userRepository.findByEmail(email).orElseThrow(() -> new UserException("User Not Found"));
			
			 UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
			 
			response.setStatusCode(200);
			response.setMessage("successful");
			response.setUser(userDTO);
			
		} catch(UserException e) {
			
			response.setStatusCode(404);
			response.setMessage(e.getMessage());
			
		} catch (Exception e) {
			
			response.setStatusCode(500);
			response.setMessage("Error getting User by Email " + e.getMessage());
			
		}
		return response;
	}

}
