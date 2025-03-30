import api from './api';

export const createItinerary = async (itineraryData) => {
    // eslint-disable-next-line no-useless-catch
    try {
        const response = await api.post('/itinerary/create', null, {
            params: itineraryData
        });
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const getItineraryById = async (id) => {
    // eslint-disable-next-line no-useless-catch
    try {
        const response = await api.get(`/itinerary/${id}`);
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const getUserItineraries = async (userId) => {
    // eslint-disable-next-line no-useless-catch
    try {
        const response = await api.get(`/itinerary/user/${userId}`);
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const confirmItinerary = async (id) => {
    // eslint-disable-next-line no-useless-catch
    try {
        const response = await api.put(`/itinerary/${id}/confirm`);
        return response.data;
    } catch (error) {
        throw error;
    }
};