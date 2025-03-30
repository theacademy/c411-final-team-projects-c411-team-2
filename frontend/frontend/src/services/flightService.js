import api from './api';

export const searchOutboundFlights = async (params) => {
    const response = await api.get('/flight/search', { params });
    return response.data;
};

export const searchReturnFlights = async (params) => {
    const response = await api.get('/flight/search/return', { params });
    return response.data;
};

export const searchFlightDestinations = async (params) => {
    const response = await api.get('/flight/destinations', { params });
    return response.data;
};

export const addOutboundFlightsToItinerary = async (itineraryId, flights) => {
    const response = await api.post(`/itinerary/${itineraryId}/flights/outbound`, flights);
    return response.data;
};

export const addReturnFlightsToItinerary = async (itineraryId, flights) => {
    const response = await api.post(`/itinerary/${itineraryId}/flights/return`, flights);
    return response.data;
};

// New: Replace outbound flights (update)
export const replaceOutboundFlights = async (itineraryId, newFlights) => {
    const response = await api.put(`/itinerary/${itineraryId}/flights/outbound`, newFlights);
    return response.data;
};

// New: Replace return flights (update)
export const replaceReturnFlights = async (itineraryId, newFlights) => {
    const response = await api.put(`/itinerary/${itineraryId}/flights/return`, newFlights);
    return response.data;
};
