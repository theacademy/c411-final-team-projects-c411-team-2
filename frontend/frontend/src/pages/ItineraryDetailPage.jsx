import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { getItineraryById, confirmItinerary } from '../services/itineraryService';
import { removeHotelFromItinerary } from '../services/hotelService';
import { removeActivityFromItinerary } from '../services/activityService';
import { removeFlightsByType } from '../services/itineraryService'; // Ensure this function is defined in your itineraryService

const ItineraryDetailPage = () => {
    const { id } = useParams();
    const [itinerary, setItinerary] = useState(null);

    const fetchItinerary = async () => {
        const data = await getItineraryById(id);
        setItinerary(data);
    };

    useEffect(() => {
        fetchItinerary();
        // eslint-disable-next-line
    }, [id]);

    const handleConfirm = async () => {
        try {
            const updated = await confirmItinerary(itinerary.id);
            setItinerary(updated);
            alert('Itinerary confirmed! Total Price: ' + updated.totalPrice);
        } catch (error) {
            console.error('Confirmation error:', error);
            alert('Failed to confirm itinerary');
        }
    };

    // Remove a hotel
    const handleRemoveHotel = async (hotelId) => {
        try {
            await removeHotelFromItinerary(itinerary.id, hotelId);
            alert('Hotel removed!');
            fetchItinerary();
        } catch (error) {
            console.error('Remove hotel error:', error);
            alert('Failed to remove hotel');
        }
    };

    // Remove an activity
    const handleRemoveActivity = async (activityId) => {
        try {
            await removeActivityFromItinerary(itinerary.id, activityId);
            alert('Activity removed!');
            fetchItinerary();
        } catch (error) {
            console.error('Remove activity error:', error);
            alert('Failed to remove activity');
        }
    };

    // Remove all flights of a given type ("OUTBOUND" or "RETURN")
    const handleRemoveAllFlights = async (flightType) => {
        try {
            await removeFlightsByType(itinerary.id, flightType);
            alert(`All ${flightType} flights removed!`);
            fetchItinerary();
        } catch (error) {
            console.error(`Error removing ${flightType} flights:`, error);
            alert(`Failed to remove ${flightType} flights`);
        }
    };

    if (!itinerary) return <div className="text-center p-8">Loading...</div>;

    return (
        <div className="max-w-4xl mx-auto p-4 bg-black/50 text-white rounded shadow-md">
            <h1 className="text-3xl font-bold mb-4">Itinerary Details</h1>
            <div className="mb-4">
                <p>
                    <strong>Trip Dates:</strong> {new Date(itinerary.startDate).toLocaleDateString()} - {new Date(itinerary.endDate).toLocaleDateString()}
                </p>
                <p>
                    <strong>Number of Adults:</strong> {itinerary.numAdults}
                </p>
                {itinerary.totalPrice && <p><strong>Total Price:</strong> ${itinerary.totalPrice}</p>}
            </div>

            {/* Flights Section */}
            <div className="mb-6">
                <h2 className="text-xl font-semibold mb-2">Flights</h2>
                {itinerary.flightsList && itinerary.flightsList.length > 0 ? (
                    <div className="space-y-2">
                        {itinerary.flightsList.map((flight) => (
                            <div key={flight.flightId} className="bg-black/30 p-2 rounded">
                                <p>
                                    <strong>{flight.flightType} Flight:</strong> {flight.originCode?.codeId} → {flight.destinationCode?.codeId}
                                </p>
                                <p>Date: {flight.date}</p>
                                {flight.price > 0 && <p>Price: {flight.price}</p>}
                            </div>
                        ))}
                    </div>
                ) : (
                    <p className="italic">No flights added yet.</p>
                )}

                {/* Remove All Flights by Type */}
                <div className="mt-4 flex gap-4">
                    <button
                        onClick={() => handleRemoveAllFlights('OUTBOUND')}
                        className="bg-red-600 hover:bg-red-500 text-white py-1 px-3 rounded"
                    >
                        Remove All Outbound Flights
                    </button>
                    <button
                        onClick={() => handleRemoveAllFlights('RETURN')}
                        className="bg-red-600 hover:bg-red-500 text-white py-1 px-3 rounded"
                    >
                        Remove All Return Flights
                    </button>
                </div>
            </div>

            {/* Hotels */}
            <div className="mb-6">
                <h2 className="text-xl font-semibold mb-2">Hotels</h2>
                {itinerary.hotelsList && itinerary.hotelsList.length > 0 ? (
                    <div className="space-y-2">
                        {itinerary.hotelsList.map((hotel) => (
                            <div key={hotel.hotel_id} className="bg-black/30 p-2 rounded flex justify-between items-center">
                                <div>
                                    <p><strong>{hotel.name}</strong></p>
                                    <p>Price: {hotel.price}</p>
                                </div>
                                <button
                                    onClick={() => handleRemoveHotel(hotel.hotel_id)}
                                    className="bg-red-600 hover:bg-red-500 text-white py-1 px-3 rounded"
                                >
                                    Remove
                                </button>
                            </div>
                        ))}
                    </div>
                ) : (
                    <p className="italic">No hotels added yet.</p>
                )}
            </div>

            {/* Activities */}
            <div className="mb-6">
                <h2 className="text-xl font-semibold mb-2">Activities</h2>
                {itinerary.activitiesList && itinerary.activitiesList.length > 0 ? (
                    <div className="space-y-2">
                        {itinerary.activitiesList.map((activity) => (
                            <div key={activity.id} className="bg-black/30 p-2 rounded flex justify-between items-center">
                                <div>
                                    <p><strong>{activity.name}</strong></p>
                                    <p>Price: {activity.price}</p>
                                </div>
                                <button
                                    onClick={() => handleRemoveActivity(activity.id)}
                                    className="bg-red-600 hover:bg-red-500 text-white py-1 px-3 rounded"
                                >
                                    Remove
                                </button>
                            </div>
                        ))}
                    </div>
                ) : (
                    <p className="italic">No activities added yet.</p>
                )}
            </div>

            {/* Action Buttons */}
            <div className="flex flex-col sm:flex-row gap-4">
                <button
                    onClick={handleConfirm}
                    className="bg-[var(--color-primary)] hover:bg-[var(--color-secondary)] text-white py-2 px-4 rounded"
                >
                    Confirm Booking
                </button>
                <Link
                    to={`/search-flights/${itinerary.id}`}
                    className="bg-[var(--color-primary)] hover:bg-[var(--color-secondary)] text-white py-2 px-4 rounded"
                >
                    Add/Update Outbound Flights
                </Link>
                <Link
                    to={`/search-return-flights/${itinerary.id}`}
                    className="bg-[var(--color-primary)] hover:bg-[var(--color-secondary)] text-white py-2 px-4 rounded"
                >
                    Add/Update Return Flights
                </Link>
                <Link
                    to={`/search-destinations/${itinerary.id}`}
                    className="bg-[var(--color-primary)] hover:bg-[var(--color-secondary)] text-white py-2 px-4 rounded"
                >
                    Explore Destinations
                </Link>
                <Link
                    to={`/search-hotels/${itinerary.id}`}
                    className="bg-[var(--color-primary)] hover:bg-[var(--color-secondary)] text-white py-2 px-4 rounded"
                >
                    Search Hotels
                </Link>
                <Link
                    to={`/search-activities/${itinerary.id}`}
                    className="bg-[var(--color-primary)] hover:bg-[var(--color-secondary)] text-white py-2 px-4 rounded"
                >
                    Search Activities
                </Link>
            </div>
        </div>
    );
};

export default ItineraryDetailPage;

