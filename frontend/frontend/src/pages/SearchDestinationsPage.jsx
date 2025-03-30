import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { searchFlightDestinations, addOutboundFlightsToItinerary } from '../services/flightService';

const SearchDestinationsPage = () => {
    const { itineraryId } = useParams();
    const navigate = useNavigate();
    const [searchParams, setSearchParams] = useState({
        origin: '',
        departureDate: '',
        duration: 7,
        adults: 1,
        maxPrice: 1000,
        nonStop: false
    });
    const [flightOptions, setFlightOptions] = useState([]);
    const [selectedIndex, setSelectedIndex] = useState(null);

    const handleChange = (e) => {
        setSearchParams({ ...searchParams, [e.target.name]: e.target.value });
    };

    const handleSearch = async () => {
        try {
            const results = await searchFlightDestinations(searchParams);
            setFlightOptions(results);
            setSelectedIndex(null);
        } catch (error) {
            console.error('Destination search error:', error);
            alert('Failed to search destination inspirations');
        }
    };

    const handleSelect = (index) => {
        setSelectedIndex(index);
    };

    const handleAddFlights = async () => {
        if (selectedIndex === null) return;
        try {
            const chosenFlights = flightOptions[selectedIndex]; // outbound flights only
            const updatedItinerary = await addOutboundFlightsToItinerary(itineraryId, chosenFlights);
            alert('Outbound flights from destination inspiration added!');
            // Redirect to Itinerary Detail page so the user can see the updated flights
            navigate(`/itinerary/${itineraryId}`);
        } catch (error) {
            console.error('Error adding flights:', error);
            alert('Failed to add flights');
        }
    };

    return (
        <div className="max-w-4xl mx-auto p-4">
            <h2 className="text-2xl mb-4">Explore Destination Inspirations</h2>
            <div className="flex flex-wrap gap-2 mb-4">
                <div className="flex flex-col">
                    <label className="mb-1 text-sm" htmlFor="origin">Origin</label>
                    <input
                        id="origin"
                        name="origin"
                        type="text"
                        placeholder="e.g. NYC"
                        value={searchParams.origin}
                        onChange={handleChange}
                        className="p-2 border rounded bg-[#333] text-white"
                    />
                </div>
                <div className="flex flex-col">
                    <label className="mb-1 text-sm" htmlFor="departureDate">Departure Date</label>
                    <input
                        id="departureDate"
                        name="departureDate"
                        type="date"
                        value={searchParams.departureDate}
                        onChange={handleChange}
                        className="p-2 border rounded bg-[#333] text-white"
                    />
                </div>
                <div className="flex flex-col">
                    <label className="mb-1 text-sm" htmlFor="duration">Duration (days)</label>
                    <input
                        id="duration"
                        name="duration"
                        type="number"
                        min="1"
                        value={searchParams.duration}
                        onChange={handleChange}
                        className="p-2 border rounded bg-[#333] text-white"
                    />
                </div>
                <div className="flex flex-col">
                    <label className="mb-1 text-sm" htmlFor="adults">Adults</label>
                    <input
                        id="adults"
                        name="adults"
                        type="number"
                        min="1"
                        value={searchParams.adults}
                        onChange={handleChange}
                        className="p-2 border rounded bg-[#333] text-white"
                    />
                </div>
                <div className="flex items-end">
                    <button onClick={handleSearch} className="bg-[var(--color-primary)] text-white p-2 rounded">
                        Find Destinations
                    </button>
                </div>
            </div>
            {flightOptions.length > 0 && (
                <div>
                    {flightOptions.map((flightSet, idx) => (
                        <div
                            key={idx}
                            onClick={() => handleSelect(idx)}
                            className={`p-4 rounded cursor-pointer mb-2 ${selectedIndex === idx ? 'bg-black/30' : 'bg-black/10'}`}
                        >
                            {flightSet.map((flight, i) => (
                                <div key={i} className="border-b border-gray-500 py-1">
                                    <p>
                                        {flight.originCode?.codeId} → {flight.destinationCode?.codeId} on {flight.date}
                                    </p>
                                    {i === flightSet.length - 1 && <p>Price: {flight.price}</p>}
                                </div>
                            ))}
                        </div>
                    ))}
                    {selectedIndex !== null && (
                        <button onClick={handleAddFlights} className="mt-4 bg-[var(--color-primary)] text-white p-2 rounded">
                            Add Selected Outbound Flights
                        </button>
                    )}
                </div>
            )}
        </div>
    );
};

export default SearchDestinationsPage;

