import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
    searchOutboundFlights,
    addOutboundFlightsToItinerary,
    replaceOutboundFlights
} from '../services/flightService';
import { getItineraryById } from '../services/itineraryService';

const SearchFlightsPage = () => {
    const { itineraryId } = useParams();
    const navigate = useNavigate();
    const [searchParams, setSearchParams] = useState({
        origin: '',
        destination: '',
        departureDate: '',
        adults: 1,
        maxPrice: 1000,
        nonStop: false
    });
    const [flightOptions, setFlightOptions] = useState([]);
    const [selectedIndex, setSelectedIndex] = useState(null);
    const [updateMode, setUpdateMode] = useState(false);

    // Determine if the itinerary already has outbound flights
    useEffect(() => {
        const checkExistingFlights = async () => {
            const itinerary = await getItineraryById(itineraryId);
            if (itinerary && itinerary.flightsList) {
                // If any flight has type OUTBOUND, enable update mode
                const hasOutbound = itinerary.flightsList.some(flight => flight.flightType === 'OUTBOUND');
                setUpdateMode(hasOutbound);
            }
        };
        checkExistingFlights();
    }, [itineraryId]);

    const handleChange = (e) => {
        setSearchParams({ ...searchParams, [e.target.name]: e.target.value });
    };

    const handleSearch = async () => {
        try {
            const results = await searchOutboundFlights(searchParams);
            setFlightOptions(results);
            setSelectedIndex(null);
        } catch (error) {
            console.error('Flight search error:', error);
            alert('Failed to search outbound flights');
        }
    };

    const handleSelect = (index) => setSelectedIndex(index);

    const handleSubmitFlights = async () => {
        if (selectedIndex === null) return;
        try {
            const flightsToSubmit = flightOptions[selectedIndex];
            if (updateMode) {
                // Replace existing outbound flights
                await replaceOutboundFlights(itineraryId, flightsToSubmit);
                alert('Outbound flights updated successfully!');
            } else {
                // Add new outbound flights
                await addOutboundFlightsToItinerary(itineraryId, flightsToSubmit);
                alert('Outbound flights added successfully!');
            }
            navigate(`/itinerary/${itineraryId}`);
        } catch (error) {
            console.error('Error submitting flights:', error);
            alert('Failed to submit outbound flights');
        }
    };

    return (
        <div className="max-w-4xl mx-auto p-4">
            <h2 className="text-2xl mb-4">Search Outbound Flights</h2>
            <div className="flex flex-wrap gap-4 mb-4">
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
                    <label className="mb-1 text-sm" htmlFor="destination">Destination</label>
                    <input
                        id="destination"
                        name="destination"
                        type="text"
                        placeholder="e.g. LON"
                        value={searchParams.destination}
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
                        Search
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
                        <button onClick={handleSubmitFlights} className="mt-4 bg-[var(--color-primary)] text-white p-2 rounded">
                            {updateMode ? 'Update Outbound Flights' : 'Add Outbound Flights'}
                        </button>
                    )}
                </div>
            )}
        </div>
    );
};

export default SearchFlightsPage;
