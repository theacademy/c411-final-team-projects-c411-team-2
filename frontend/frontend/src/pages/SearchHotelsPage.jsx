import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { searchHotel, addHotelToItinerary } from '../services/hotelService';
import {addActivityToItinerary} from "../services/activityService.js";

const SearchHotelsPage = () => {
    const { itineraryId } = useParams();
    const navigate = useNavigate();
    const [searchParams, setSearchParams] = useState({
        destination: '',
        hotelBudget: '300',
        numberAdults: 2,
        checkIn: '',
        checkOut: '',
        boardType: '' // e.g. FULL_BOARD, HALF_BOARD, etc.
    });
    const [hotelOptions, setHotelOptions] = useState([]);
    const [selectedIndex, setSelectedIndex] = useState(null);

    const handleChange = (e) => {
        setSearchParams({ ...searchParams, [e.target.name]: e.target.value });
    };

    const handleSearch = async () => {
        try {
            const results = await searchHotel(searchParams);
            setHotelOptions(results);
            setSelectedIndex(null);
        } catch (error) {
            console.error('Hotel search error:', error);
            alert('Failed to search hotels');
        }
    };

    const handleSelect = (index) => {
        setSelectedIndex(index);
    };

    const handleAddHotel = async () => {
        if (selectedIndex === null) return;
        try {
            const chosenHotel = hotelOptions[selectedIndex];
            // Now pass the full activity object to the backend
            await addHotelToItinerary(itineraryId, chosenHotel);
            alert('Hotel added successfully!');
            navigate(`/itinerary/${itineraryId}`);
        } catch (error) {
            console.error('Error adding hotel:', error);
            alert('Failed to add hotel');
        }
    };

    return (
        <div className="max-w-4xl mx-auto p-4">
            <h2 className="text-2xl mb-4">Search Hotels</h2>
            <div className="flex flex-wrap gap-4 mb-4">
                <div className="flex flex-col">
                    <label className="mb-1 text-sm" htmlFor="destination">Destination Code</label>
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
                    <label className="mb-1 text-sm" htmlFor="hotelBudget">Hotel Budget</label>
                    <input
                        id="hotelBudget"
                        name="hotelBudget"
                        type="text"
                        placeholder="e.g. 300"
                        value={searchParams.hotelBudget}
                        onChange={handleChange}
                        className="p-2 border rounded bg-[#333] text-white"
                    />
                </div>
                <div className="flex flex-col">
                    <label className="mb-1 text-sm" htmlFor="checkIn">Check-In Date</label>
                    <input
                        id="checkIn"
                        name="checkIn"
                        type="date"
                        value={searchParams.checkIn}
                        onChange={handleChange}
                        className="p-2 border rounded bg-[#333] text-white"
                    />
                </div>
                <div className="flex flex-col">
                    <label className="mb-1 text-sm" htmlFor="checkOut">Check-Out Date</label>
                    <input
                        id="checkOut"
                        name="checkOut"
                        type="date"
                        value={searchParams.checkOut}
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
            {hotelOptions.length > 0 && (
                <div>
                    {hotelOptions.map((hotel, idx) => (
                        <div
                            key={hotel.hotel_id}
                            onClick={() => handleSelect(idx)}
                            className={`p-4 rounded cursor-pointer mb-2 ${selectedIndex === idx ? 'bg-black/30' : 'bg-black/10'}`}
                        >
                            <p><strong>{hotel.name}</strong></p>
                            <p>Price: {hotel.price}</p>
                            <p>Check-in: {hotel.checkinDate} / Check-out: {hotel.checkoutDate}</p>
                        </div>
                    ))}
                    {selectedIndex !== null && (
                        <button onClick={handleAddHotel} className="mt-4 bg-[var(--color-primary)] text-white p-2 rounded">
                            Add Selected Hotel
                        </button>
                    )}
                </div>
            )}
        </div>
    );
};

export default SearchHotelsPage;
