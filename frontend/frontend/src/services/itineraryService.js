import api from './api';

export const createItinerary = async (itineraryData) => {
    const response = await api.post('/itinerary/create', null, { params: itineraryData });
    return response.data;
};

export const getItineraryById = async (id) => {
    const response = await api.get(`/itinerary/${id}`);
    return response.data;
};

export const getUserItineraries = async (userId) => {
    const response = await api.get(`/itinerary/user/${userId}`);
    return response.data;
};

export const updateItinerary = async (id, itineraryData) => {
    const response = await api.put(`/itinerary/${id}`, itineraryData);
    return response.data;
};

export const confirmItinerary = async (id) => {
    const response = await api.put(`/itinerary/${id}/confirm`);
    return response.data;
};

// New: Remove all flights of a given type ("OUTBOUND" or "RETURN")
export const removeFlightsByType = async (itineraryId, flightType) => {
    await api.delete(`/itinerary/${itineraryId}/flights/${flightType}`);
};
