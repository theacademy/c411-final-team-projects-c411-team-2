// src/pages/ItineraryDetailPage.jsx
import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';

const ItineraryDetailPage = () => {
    const { id } = useParams();
    const [itinerary, setItinerary] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Mock loading
        setTimeout(() => {
            setItinerary({
                id: parseInt(id),
                startDate: '2025-06-01',
                endDate: '2025-06-07',
                numAdults: 2,
                confirmed: false,
                totalPrice: null,
                flights: [
                    { id: 1, departure: 'New York (JFK)', arrival: 'London (LHR)', date: '2025-06-01', price: 450 },
                    { id: 2, departure: 'London (LHR)', arrival: 'New York (JFK)', date: '2025-06-07', price: 480 }
                ],
                hotels: [
                    { id: 'LON123', name: 'London Plaza Hotel', price: 195, nights: 6 }
                ],
                activities: [
                    { id: 1, name: 'London Eye Tour', price: 35, participants: 2 }
                ]
            });
            setLoading(false);
        }, 1000);
    }, [id]);

    if (loading) {
        return <div className="text-center p-12">Loading itinerary details...</div>;
    }

    if (!itinerary) {
        return (
            <div className="text-center p-12">
                <p className="text-lg text-red-600">Itinerary not found</p>
                <Link to="/itineraries" className="text-[var(--color-primary)] hover:underline mt-4 inline-block">
                    Back to My Itineraries
                </Link>
            </div>
        );
    }

    const calculateTotalPrice = () => {
        let flightTotal = itinerary.flights.reduce((sum, f) => sum + (f.price || 0), 0);
        let hotelTotal = itinerary.hotels.reduce((sum, h) => sum + (h.price * h.nights || 0), 0);
        let activityTotal = itinerary.activities.reduce((sum, a) => sum + (a.price * a.participants || 0), 0);
        return flightTotal + hotelTotal + activityTotal;
    };

    return (
        <div className="max-w-4xl mx-auto my-8 px-4 bg-black/50 text-white p-6 rounded">
            <div className="flex justify-between items-start mb-6">
                <div>
                    <h1 className="text-3xl font-bold">Trip to London</h1>
                    <p className="mt-1">
                        {new Date(itinerary.startDate).toLocaleDateString()} -{' '}
                        {new Date(itinerary.endDate).toLocaleDateString()}
                    </p>
                </div>
                <div className="flex items-center space-x-3">
          <span
              className={`px-3 py-1 text-sm rounded-full ${
                  itinerary.confirmed
                      ? 'bg-green-100 text-green-800'
                      : 'bg-yellow-100 text-yellow-800'
              }`}
          >
            {itinerary.confirmed ? 'Confirmed' : 'Draft'}
          </span>

                    {!itinerary.confirmed && (
                        <button className="bg-[var(--color-primary)] hover:bg-[var(--color-secondary)] text-white px-4 py-2 rounded">
                            Confirm Booking
                        </button>
                    )}
                </div>
            </div>

            {/* Main Card */}
            <div className="bg-[#333]/40 rounded-lg overflow-hidden">
                {/* Flights */}
                <div className="p-6 border-b border-gray-700">
                    <div className="flex justify-between items-center mb-4">
                        <h2 className="text-xl font-semibold">Flights</h2>
                        <button className="bg-[var(--color-primary)] hover:bg-[var(--color-secondary)] text-white px-3 py-1 text-sm rounded">
                            {itinerary.flights.length > 0 ? 'Edit Flights' : 'Add Flights'}
                        </button>
                    </div>
                    {/* flights list */}
                    {itinerary.flights.length === 0 ? (
                        <p className="italic">No flights added yet</p>
                    ) : (
                        <div className="space-y-4">
                            {itinerary.flights.map((flight) => (
                                <div
                                    key={flight.id}
                                    className="flex justify-between items-center p-3 bg-[#444]/50 rounded"
                                >
                                    <div>
                                        <p className="font-medium">
                                            {flight.departure} → {flight.arrival}
                                        </p>
                                        <p className="text-sm">
                                            {new Date(flight.date).toLocaleDateString()}
                                        </p>
                                    </div>
                                    <div className="text-right">
                                        <p className="font-bold">${flight.price}</p>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </div>

                {/* Hotels */}
                <div className="p-6 border-b border-gray-700">
                    <div className="flex justify-between items-center mb-4">
                        <h2 className="text-xl font-semibold">Hotels</h2>
                        <button className="bg-[var(--color-primary)] hover:bg-[var(--color-secondary)] text-white px-3 py-1 text-sm rounded">
                            {itinerary.hotels.length > 0 ? 'Edit Hotels' : 'Add Hotels'}
                        </button>
                    </div>
                    {itinerary.hotels.length === 0 ? (
                        <p className="italic">No hotels added yet</p>
                    ) : (
                        <div className="space-y-4">
                            {itinerary.hotels.map((hotel) => (
                                <div
                                    key={hotel.id}
                                    className="flex justify-between items-center p-3 bg-[#444]/50 rounded"
                                >
                                    <div>
                                        <p className="font-medium">{hotel.name}</p>
                                        <p className="text-sm">
                                            {hotel.nights} night{hotel.nights !== 1 ? 's' : ''}
                                        </p>
                                    </div>
                                    <div className="text-right">
                                        <p className="font-bold">${hotel.price * hotel.nights}</p>
                                        <p className="text-sm">${hotel.price}/night</p>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </div>

                {/* Activities */}
                <div className="p-6 border-b border-gray-700">
                    <div className="flex justify-between items-center mb-4">
                        <h2 className="text-xl font-semibold">Activities</h2>
                        <button className="bg-[var(--color-primary)] hover:bg-[var(--color-secondary)] text-white px-3 py-1 text-sm rounded">
                            {itinerary.activities.length > 0 ? 'Edit Activities' : 'Add Activities'}
                        </button>
                    </div>
                    {itinerary.activities.length === 0 ? (
                        <p className="italic">No activities added yet</p>
                    ) : (
                        <div className="space-y-4">
                            {itinerary.activities.map((activity) => (
                                <div
                                    key={activity.id}
                                    className="flex justify-between items-center p-3 bg-[#444]/50 rounded"
                                >
                                    <div>
                                        <p className="font-medium">{activity.name}</p>
                                        <p className="text-sm">
                                            For {activity.participants}{' '}
                                            {activity.participants === 1 ? 'person' : 'people'}
                                        </p>
                                    </div>
                                    <div className="text-right">
                                        <p className="font-bold">
                                            ${activity.price * activity.participants}
                                        </p>
                                        <p className="text-sm">${activity.price}/person</p>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </div>

                {/* Total */}
                <div className="p-6 bg-[#444]/50">
                    <div className="flex justify-between items-center">
                        <h2 className="text-xl font-semibold">Total Estimated Cost</h2>
                        <p className="text-2xl font-bold">${calculateTotalPrice()}</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ItineraryDetailPage;
