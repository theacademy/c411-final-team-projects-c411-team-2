import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { searchReturnFlights, addReturnFlightsToItinerary, replaceReturnFlights } from '../services/flightService';
import { getItineraryById } from '../services/itineraryService';

const SearchReturnFlightsPage = () => {
    const { itineraryId } = useParams();
    const navigate = useNavigate();
    const [searchParams, setSearchParams] = useState({
        origin: '',
        destination: '',
        returnDate: '',
        adults: 1,
        maxPrice: 1000,
        nonStop: false
    });
    const [flightOptions, setFlightOptions] = useState([]);
    const [selectedIndex, setSelectedIndex] = useState(null);
    const [updateMode, setUpdateMode] = useState(false);

    useEffect(() => {
        const checkExistingReturn = async () => {
            const itinerary = await getItineraryById(itineraryId);
            if (itinerary && itinerary.flightsList) {
                const hasReturn = itinerary.flightsList.some(flight => flight.flightType === 'RETURN');
                setUpdateMode(hasReturn);
            }
        };
        checkExistingReturn();
    }, [itineraryId]);

    const handleChange = (e) => {
        setSearchParams({ ...searchParams, [e.target.name]: e.target.value });
    };

    const handleSearch = async () => {
        try {
            const results = await searchReturnFlights(searchParams);
            setFlightOptions(results);
            setSelectedIndex(null);
        } catch (error) {
            console.error('Return flight search error:', error);
            alert('Failed to search return flights');
        }
    };

    const handleSelect = (index) => setSelectedIndex(index);

    const handleSubmitFlights = async () => {
        if (selectedIndex === null) return;
        try {
            const flightsToSubmit = flightOptions[selectedIndex];
            if (updateMode) {
                await replaceReturnFlights(itineraryId, flightsToSubmit);
                alert('Return flights updated successfully!');
            } else {
                await addReturnFlightsToItinerary(itineraryId, flightsToSubmit);
                alert('Return flights added successfully!');
            }
            navigate(`/itinerary/${itineraryId}`);
        } catch (error) {
            console.error('Error submitting return flights:', error);
            alert('Failed to submit return flights');
        }
    };

    return (
        <div className="max-w-4xl mx-auto p-4">
            <h2 className="text-2xl mb-4">Search Return Flights</h2>
            <div className="flex flex-wrap gap-4 mb-4">
                <div className="flex flex-col">
                    <label className="mb-1 text-sm" htmlFor="origin">Origin</label>
                    <input
                        id="origin"
                        name="origin"
                        type="text"
                        placeholder="e.g. LON"
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
                        placeholder="e.g. NYC"
                        value={searchParams.destination}
                        onChange={handleChange}
                        className="p-2 border rounded bg-[#333] text-white"
                    />
                </div>
                <div className="flex flex-col">
                    <label className="mb-1 text-sm" htmlFor="returnDate">Return Date</label>
                    <input
                        id="returnDate"
                        name="returnDate"
                        type="date"
                        value={searchParams.returnDate}
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
                            {updateMode ? 'Update Return Flights' : 'Add Return Flights'}
                        </button>
                    )}
                </div>
            )}
        </div>
    );
};

export default SearchReturnFlightsPage;
