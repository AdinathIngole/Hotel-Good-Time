package com.HotelGoodTime.Utils;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

import com.HotelGoodTime.DTO.BookingDTO;
import com.HotelGoodTime.DTO.RoomDTO;
import com.HotelGoodTime.DTO.UserDTO;
import com.HotelGoodTime.Entity.Booking;
import com.HotelGoodTime.Entity.Room;
import com.HotelGoodTime.Entity.User;

public class Utils {
	
	private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	private static final SecureRandom secureRandom = new SecureRandom();
	
	
	//Generate Alpha-Numeric Random Number
	public static String generateRandomConfirmationCode(int length) {
		StringBuilder stringBuilder = new StringBuilder();
		
		for(int i=0; i<length; i++) {
			int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
			char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
			stringBuilder.append(randomChar);
		}
		
		return stringBuilder.toString();
	}

	//User Entity to User DTO mapper function
	public static UserDTO mapUserEntityToUserDTO(User user) {
		
		UserDTO userDTO = new UserDTO();
		
		userDTO.setId(user.getId());
		userDTO.setEmail(user.getEmail());
		userDTO.setName(user.getName());
		userDTO.setMobileNo(user.getMobileNo());
		userDTO.setRole(user.getRole());
		
		return userDTO;
		
	}
	
	
	//User Entity to User DTO + Bookings and Room mapper function
	public static UserDTO mapUserEntityToUserDTOPlusUserBookingsAndRoom(User user) {
		
		UserDTO userDTO = new UserDTO();
		
		userDTO.setId(user.getId());
		userDTO.setEmail(user.getEmail());
		userDTO.setName(user.getName());
		userDTO.setMobileNo(user.getMobileNo());
		userDTO.setRole(user.getRole());
		
		if(!user.getBookings().isEmpty()) {
			
			userDTO.setBookings(user.getBookings().stream().map(booking -> mapBookingEntityToBookingDTOPlusBookedRoom(booking, false)).collect(Collectors.toList()));
			
		}
		return userDTO;
	}
	
	
	//Room Entity to Room DTO mapper function
	public static RoomDTO mapRoomEntityToRoomDTO(Room room) {
		
		RoomDTO roomDTO = new RoomDTO();
		
		roomDTO.setId(room.getId());
		roomDTO.setRoomType(room.getRoomType());
		roomDTO.setRoomPrice(room.getRoomPrice());
		roomDTO.setRoomPhotoUrl(room.getRoomPhotoUrl());
		roomDTO.setRoomDescription(room.getRoomDescription());
		
		
		return roomDTO;
		
	}
	
	
	//Room Entity to Room DTO + Bookings mapper function
	public static RoomDTO mapRoomEntityToRoomDTOPlusBookings(Room room) {
		
		RoomDTO roomDTO = new RoomDTO();
		
		roomDTO.setId(room.getId());
		roomDTO.setRoomType(room.getRoomType());
		roomDTO.setRoomPrice(room.getRoomPrice());
		roomDTO.setRoomPhotoUrl(room.getRoomPhotoUrl());
		roomDTO.setRoomDescription(room.getRoomDescription());
		
		
		if(room.getBookings() !=null) {
			roomDTO.setBookings(room.getBookings().stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList()));
			
		}
		
		return roomDTO;
		
	}
	
	
	//Booking Entity to Booking DTO mapper function
	public static BookingDTO mapBookingEntityToBookingDTO(Booking booking) {
		
		BookingDTO bookingDTO = new BookingDTO();
		
		bookingDTO.setId(booking.getId());
		bookingDTO.setCheckInDate(booking.getCheckInDate());
		bookingDTO.setCheckOutDate(booking.getCheckOutDate());
		bookingDTO.setNumberOfAdults(booking.getNumberOfAdults());
		bookingDTO.setNumberOfChildren(booking.getNumberOfChildren());
		bookingDTO.setTotalNumOfGuest(booking.getTotalNumOfGuest());
		bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());
		
		
		return bookingDTO;
		
	}
	
	//Booking Entity to Booking DTO + Booked Room mapper function
	public static BookingDTO mapBookingEntityToBookingDTOPlusBookedRoom(Booking booking, boolean mapUser) {
		
		BookingDTO bookingDTO = new BookingDTO();
		
		bookingDTO.setId(booking.getId());
		bookingDTO.setCheckInDate(booking.getCheckInDate());
		bookingDTO.setCheckOutDate(booking.getCheckOutDate());
		bookingDTO.setNumberOfAdults(booking.getNumberOfAdults());
		bookingDTO.setNumberOfChildren(booking.getNumberOfChildren());
		bookingDTO.setTotalNumOfGuest(booking.getTotalNumOfGuest());
		bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());
		
		if(mapUser) {
			bookingDTO.setUser(Utils.mapUserEntityToUserDTO(booking.getUser()));
		}
		
		if(booking.getRoom() != null) {
			RoomDTO roomDTO = new RoomDTO();
			
			roomDTO.setId(booking.getRoom().getId());
			roomDTO.setRoomType(booking.getRoom().getRoomType());
			roomDTO.setRoomPrice(booking.getRoom().getRoomPrice());
			roomDTO.setRoomPhotoUrl(booking.getRoom().getRoomPhotoUrl());
			roomDTO.setRoomDescription(booking.getRoom().getRoomDescription());
			bookingDTO.setRoom(roomDTO);
		}
		return bookingDTO;
	}


	//User List Entity to User List DTO mapper function 
	public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList){
		
		return userList.stream().map(Utils::mapUserEntityToUserDTO).collect(Collectors.toList());
	}
	
	//Room List Entity to Room List DTO mapper function 
	public static List<RoomDTO> mapRoomListEntityToRoomListDTO(List<Room> roomList){
		
		return roomList.stream().map(Utils::mapRoomEntityToRoomDTO).collect(Collectors.toList());
	}
	
	//Booking List Entity to Booking List DTO mapper function 
	public static List<BookingDTO> mapBookingListEntityToBookingListDTO(List<Booking> bookingList){
		
		return bookingList.stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList());
	}
}
