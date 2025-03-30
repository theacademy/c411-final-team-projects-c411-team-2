import api from './api';

// Search for activities
// GET /activity/search?latitude=...&longitude=...&budgetActivity=...
export const searchActivities = async (latitude, longitude, budgetActivity) => {
    const response = await api.get('/activity/search', {
        params: { latitude, longitude, budgetActivity }
    });
    return response.data;
};

// Add an activity to the itinerary
// POST /itinerary/{itineraryId}/activity/{activityId}
export const addActivityToItinerary = async (itineraryId, activityObj) => {
    // First POST request to save the activity and get its ID
    const activityResponse = await api.post(`/activity/`, activityObj);
    const savedActivityId = activityResponse.data.id;

    // Second POST request to add the activity to the itinerary using the saved activity ID
    const response = await api.post(`/itinerary/${itineraryId}/activity/${savedActivityId}`);
    return response.data;
};

// Remove an activity from the itinerary
// DELETE /itinerary/{itineraryId}/activity/{activityId}
export const removeActivityFromItinerary = async (itineraryId, activityId) => {
    await api.delete(`/itinerary/${itineraryId}/activity/${activityId}`);
};

// Replace an existing activity with a new one
// PUT /itinerary/{itineraryId}/activity/{oldActivityId} (body is newActivity)
export const replaceActivityInItinerary = async (itineraryId, oldActivityId, newActivity) => {
    const response = await api.put(`/itinerary/${itineraryId}/activity/${oldActivityId}`, newActivity);
    return response.data;
};
