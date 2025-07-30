package com.HotelGoodTime.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.HotelGoodTime.DTO.BookingDTO;
import com.HotelGoodTime.DTO.Response;
import com.HotelGoodTime.Entity.Booking;
import com.HotelGoodTime.Entity.Room;
import com.HotelGoodTime.Entity.User;
import com.HotelGoodTime.Exception.UserException;
import com.HotelGoodTime.Repository.BookingRepository;
import com.HotelGoodTime.Repository.RoomRepository;
import com.HotelGoodTime.Repository.UserRepository;
import com.HotelGoodTime.Service.BookingService;
import com.HotelGoodTime.Utils.Utils;

@Service
public class BookingServiceImpl implements BookingService{
	
	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
		
		Response response = new Response();
		
		try {
			
			if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
				throw new IllegalArgumentException("Check out date must be come after check in date.");
			}
			
			Room room = roomRepository.findById(roomId).orElseThrow(() -> new UserException("Room Not Found"));
			
			User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User Not Found"));

			List<Booking> existingBookings = room.getBookings();
			
			if(!roomIsAvailable(bookingRequest , existingBookings)) {
				throw new UserException("Room Not Available for Selected date range");
			}
			
			bookingRequest.setRoom(room);
			bookingRequest.setUser(user);
			String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
			
			bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
			
			bookingRepository.save(bookingRequest);
			
			response.setStatusCode(200);
			response.setMessage("successful");
			response.setBookingConfirmationCode(bookingConfirmationCode);
			
		} catch (UserException e) {
			response.setStatusCode(404);
			response.setMessage(e.getMessage());
		} catch (Exception e) {
			
			response.setStatusCode(500);
			response.setMessage("Error Saving A Booking " +e.getMessage());

		}
		return response;
	}

	

	@Override
	public Response findBookingByConfirmationCode(String confirmationCode) {
		
			Response response = new Response();

	        try {
	        	Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new UserException("Booking Not Found"));
	        	BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedRoom(booking, true);
	            response.setStatusCode(200);
	            response.setMessage("successful");
	            response.setBooking(bookingDTO);

	        } catch (UserException e) {
	        	
	            response.setStatusCode(404);
	            response.setMessage(e.getMessage());

	        } catch (Exception e) {
	            response.setStatusCode(500);
	            response.setMessage("Error Finding a booking: " + e.getMessage());

	        }
	        return response;
	}

	@Override
	public Response getAllBooking() {
		 
		Response response = new Response();

	        try {
	            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
	            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);
	            response.setStatusCode(200);
	            response.setMessage("successful");
	            response.setBookingList(bookingDTOList);

	        } catch (UserException e) {
	        	
	            response.setStatusCode(404);
	            response.setMessage(e.getMessage());

	        } catch (Exception e) {
	        	
	            response.setStatusCode(500);
	            response.setMessage("Error Getting all bookings: " + e.getMessage());

	        }
	        return response;
	}

	@Override
	public Response cancelBooking(Long bookingId) {
		 
		Response response = new Response();

	        try {
	            bookingRepository.findById(bookingId).orElseThrow(() -> new UserException("Booking Does Not Exist"));
	            bookingRepository.deleteById(bookingId);
	            response.setStatusCode(200);
	            response.setMessage("successful");

	        } catch (UserException e) {
	        	
	            response.setStatusCode(404);
	            response.setMessage(e.getMessage());

	        } catch (Exception e) {
	        	
	            response.setStatusCode(500);
	            response.setMessage("Error Cancelling a booking: " + e.getMessage());

	        }
	        return response;
	}

	
	private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
		
		 return existingBookings.stream()
	                .noneMatch(existingBooking ->
	                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
	                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
	                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
	                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
	                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

	                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
	                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

	                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

	                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
	                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

	                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
	                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
	                );
	}
	
}
