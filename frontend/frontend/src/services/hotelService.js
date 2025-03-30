import api from './api';

// Search hotels by calling GET /hotel/{destination}/{hotelBudget}
// with optional query params: numberAdults, checkIn, checkOut, boardType
export const searchHotel = async (params) => {
    // params: { destination, hotelBudget, numberAdults, checkIn, checkOut, boardType }
    const { destination, hotelBudget, ...rest } = params;
    console.log("rest params is: ", rest);
    const response = await api.get(`/hotel/${destination}/${hotelBudget}`, { params: rest });
    return response.data;
};

// Add a specific hotel to the itinerary
// POST /itinerary/{itineraryId}/hotel/{hotelId}
export const addHotelToItinerary = async (itineraryId, hotelObj) => {
    // First POST request to save the hotel and get its ID
    const hotelResponse = await api.post(`/hotel/`, hotelObj);
    const savedHotelId = hotelResponse.data.id;

    // Second POST request to add the hotel to the itinerary using the saved hotel ID
    const response = await api.post(`/itinerary/${itineraryId}/hotel/${savedHotelId}`);
    return response.data;
};

// Remove a specific hotel from the itinerary
// DELETE /itinerary/{itineraryId}/hotel/{hotelId}
export const removeHotelFromItinerary = async (itineraryId, hotelId) => {
    await api.delete(`/itinerary/${itineraryId}/hotel/${hotelId}`);
};

// Replace an existing hotel in the itinerary with a new one
// PUT /itinerary/{itineraryId}/hotel/{oldHotelId} (and pass newHotel in body)
export const replaceHotelInItinerary = async (itineraryId, oldHotelId, newHotel) => {
    // newHotel is an object with hotel details
    const response = await api.put(`/itinerary/${itineraryId}/hotel/${oldHotelId}`, newHotel);
    return response.data;
};
