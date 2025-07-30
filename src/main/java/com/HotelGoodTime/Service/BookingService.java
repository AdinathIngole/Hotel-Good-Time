package com.HotelGoodTime.Service;

import com.HotelGoodTime.DTO.Response;
import com.HotelGoodTime.Entity.Booking;

public interface BookingService {

	Response saveBooking(Long roomId , Long userId , Booking bookingRequest);
	
	Response findBookingByConfirmationCode(String confirmationCode);
	
	Response getAllBooking();
	
	Response cancelBooking(Long bookingId);
}
