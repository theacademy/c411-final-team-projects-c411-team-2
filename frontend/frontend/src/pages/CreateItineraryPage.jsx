import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createItinerary } from '../services/itineraryService';

const CreateItineraryPage = () => {
    const [formData, setFormData] = useState({
        numAdults: 2,
        startDate: '',
        endDate: '',
        priceRangeFlight: '',
        priceRangeHotel: '',
        priceRangeActivity: ''
    });
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // Get user info from localStorage
            const user = JSON.parse(localStorage.getItem('user'));
            if (!user) {
                alert('Please log in first');
                navigate('/login');
                return;
            }
            // Prepare parameters for /itinerary/create endpoint
            const itineraryParams = {
                userId: user.id,
                numAdults: formData.numAdults,
                priceRangeFlight: formData.priceRangeFlight || null,
                priceRangeHotel: formData.priceRangeHotel || null,
                priceRangeActivity: formData.priceRangeActivity || null,
                startDate: formData.startDate,
                endDate: formData.endDate
            };
            const createdItinerary = await createItinerary(itineraryParams);
            navigate(`/itinerary/${createdItinerary.id}`);
        } catch (error) {
            console.error('Error creating itinerary:', error);
            alert('Failed to create itinerary');
        }
    };

    return (
        <div className="max-w-md mx-auto my-10 p-6 bg-black/50 text-white rounded shadow-md">
            <h2 className="text-2xl font-bold mb-6 text-center">Create New Itinerary</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
                {/* Number of Adults */}
                <div>
                    <label className="block mb-2" htmlFor="numAdults">Number of Adults</label>
                    <input
                        id="numAdults"
                        name="numAdults"
                        type="number"
                        min="1"
                        value={formData.numAdults}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-500 rounded bg-[#333] text-white"
                        required
                    />
                </div>
                {/* Start Date */}
                <div>
                    <label className="block mb-2" htmlFor="startDate">Start Date</label>
                    <input
                        id="startDate"
                        name="startDate"
                        type="date"
                        value={formData.startDate}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-500 rounded bg-[#333] text-white"
                        required
                    />
                </div>
                {/* End Date */}
                <div>
                    <label className="block mb-2" htmlFor="endDate">End Date</label>
                    <input
                        id="endDate"
                        name="endDate"
                        type="date"
                        value={formData.endDate}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-500 rounded bg-[#333] text-white"
                        required
                    />
                </div>
                {/* Flight Budget (Optional) */}
                <div>
                    <label className="block mb-2" htmlFor="priceRangeFlight">Flight Budget (Optional)</label>
                    <input
                        id="priceRangeFlight"
                        name="priceRangeFlight"
                        type="number"
                        min="0"
                        value={formData.priceRangeFlight}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-500 rounded bg-[#333] text-white"
                    />
                </div>
                {/* Hotel Budget (Optional) */}
                <div>
                    <label className="block mb-2" htmlFor="priceRangeHotel">Hotel Budget (Optional)</label>
                    <input
                        id="priceRangeHotel"
                        name="priceRangeHotel"
                        type="number"
                        min="0"
                        value={formData.priceRangeHotel}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-500 rounded bg-[#333] text-white"
                    />
                </div>
                {/* Activity Budget (Optional) */}
                <div>
                    <label className="block mb-2" htmlFor="priceRangeActivity">Activity Budget (Optional)</label>
                    <input
                        id="priceRangeActivity"
                        name="priceRangeActivity"
                        type="number"
                        min="0"
                        value={formData.priceRangeActivity}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-500 rounded bg-[#333] text-white"
                    />
                </div>
                {/* Submit Button */}
                <button type="submit" className="w-full bg-[var(--color-primary)] hover:bg-[var(--color-secondary)] text-white font-bold py-2 rounded">
                    Create Itinerary
                </button>
            </form>
        </div>
    );
};

export default CreateItineraryPage;
