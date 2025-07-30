package com.HotelGoodTime.ServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.HotelGoodTime.DTO.Response;
import com.HotelGoodTime.DTO.RoomDTO;
import com.HotelGoodTime.Entity.Room;
import com.HotelGoodTime.Exception.UserException;
import com.HotelGoodTime.Repository.RoomRepository;
import com.HotelGoodTime.Service.RoomService;
import com.HotelGoodTime.Utils.Utils;

@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomRepository roomRepository;


	@Override
	public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {

		Response response = new Response();

		try {

			String imageUrl = null;

			Room room = new Room();

			room.setRoomPhotoUrl(imageUrl);// Please Save proper Image URL

			room.setRoomType(roomType);
			room.setRoomPrice(roomPrice);
			room.setRoomDescription(description);

			Room savedRoom = roomRepository.save(room);

			RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(savedRoom);

			response.setStatusCode(200);
			response.setMessage("successful");
			response.setRoom(roomDTO);

		} catch (Exception e) {

			response.setStatusCode(500);
			response.setMessage("Error saving a room" + e.getMessage());
		}
		return response;
	}

	@Override
	public List<String> getAllRoomTypes() {

		return roomRepository.findDistinctRoomTypes();
	}

	@Override
	public Response getAllRooms() {
		Response response = new Response();

		try {
			List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

			List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);
			response.setStatusCode(200);
			response.setMessage("successful");
			response.setRoomList(roomDTOList);

		} catch (Exception e) {

			response.setStatusCode(500);
			response.setMessage("Error getting All room" + e.getMessage());
		}
		return response;
	}

	@Override
	public Response deleteRoom(Long roomId) {

		Response response = new Response();

		try {
			roomRepository.findById(roomId).orElseThrow(() -> new UserException("Room Not Found"));
			roomRepository.deleteById(roomId);
			response.setStatusCode(200);
			response.setMessage("successful");

		} catch (UserException e) {

			response.setStatusCode(404);
			response.setMessage(e.getMessage());

		} catch (Exception e) {

			response.setStatusCode(500);
			response.setMessage("Error saving a room " + e.getMessage());

		}
		return response;
	}

	@Override
	public Response updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice,
			MultipartFile photo) {
		Response response = new Response();

		try {
			String imageUrl = null;
			/*
			 * if (photo != null && !photo.isEmpty()) { imageUrl =
			 * awsS3Service.saveImageToS3(photo); }
			 */
			Room room = roomRepository.findById(roomId).orElseThrow(() -> new UserException("Room Not Found"));
			if (roomType != null)
				room.setRoomType(roomType);
			if (roomPrice != null)
				room.setRoomPrice(roomPrice);
			if (description != null)
				room.setRoomDescription(description);
			if (imageUrl != null)
				room.setRoomPhotoUrl(imageUrl);

			Room updatedRoom = roomRepository.save(room);
			RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(updatedRoom);

			response.setStatusCode(200);
			response.setMessage("successful");
			response.setRoom(roomDTO);

		} catch (UserException e) {

			response.setStatusCode(404);
			response.setMessage(e.getMessage());

		} catch (Exception e) {

			response.setStatusCode(500);
			response.setMessage("Error saving a room " + e.getMessage());
		}
		return response;
	}

	@Override
	public Response getRoomById(Long roomId) {
		Response response = new Response();

		try {
			Room room = roomRepository.findById(roomId).orElseThrow(() -> new UserException("Room Not Found"));
			RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTOPlusBookings(room);
			response.setStatusCode(200);
			response.setMessage("successful");
			response.setRoom(roomDTO);

		} catch (UserException e) {

			response.setStatusCode(404);
			response.setMessage(e.getMessage());

		} catch (Exception e) {

			response.setStatusCode(500);
			response.setMessage("Error getting a room by Id " + e.getMessage());
		}
		return response;
	}

	@Override
	public Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {

		Response response = new Response();

		try {

			List<Room> availableRooms = roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate, checkOutDate,
					roomType);
			List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(availableRooms);
			response.setStatusCode(200);
			response.setMessage("successful");
			response.setRoomList(roomDTOList);

		} catch (Exception e) {

			response.setStatusCode(500);
			response.setMessage("Error Getting Available Rooms By Date And Type.  " + e.getMessage());

		}
		return response;
	}

	@Override
	public Response getAllAvailableRooms() {

		Response response = new Response();

		try {
			List<Room> roomList = roomRepository.getAllAvailableRoom();
			List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);

			response.setStatusCode(200);
			response.setMessage("successful");
			response.setRoomList(roomDTOList);

		} catch (UserException e) {

			response.setStatusCode(404);
			response.setMessage(e.getMessage());

		} catch (Exception e) {

			response.setStatusCode(500);
			response.setMessage("Error Getting All Available Rooms. " + e.getMessage());
		}
		return response;
	}

}
