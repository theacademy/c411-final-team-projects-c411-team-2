// src/pages/CreateItineraryPage.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

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
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Creating itinerary with:', formData);
        navigate('/itineraries');
    };

    return (
        <div className="max-w-2xl mx-auto my-8 px-4 text-white">
            <h1 className="text-3xl font-bold mb-6">Create New Itinerary</h1>
            <form
                onSubmit={handleSubmit}
                className="bg-[var(--color-form-bg)] rounded-lg shadow-md p-6"
            >
                <div className="mb-4">
                    <label className="block mb-2" htmlFor="numAdults">
                        Number of Travelers
                    </label>
                    <input
                        id="numAdults"
                        name="numAdults"
                        type="number"
                        min="1"
                        className="w-full p-2 border border-gray-500 rounded bg-[#555555] text-white"
                        value={formData.numAdults}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                    <div>
                        <label className="block mb-2" htmlFor="startDate">
                            Start Date
                        </label>
                        <input
                            id="startDate"
                            name="startDate"
                            type="date"
                            className="w-full p-2 border border-gray-500 rounded bg-[#555555] text-white"
                            value={formData.startDate}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div>
                        <label className="block mb-2" htmlFor="endDate">
                            End Date
                        </label>
                        <input
                            id="endDate"
                            name="endDate"
                            type="date"
                            className="w-full p-2 border border-gray-500 rounded bg-[#555555] text-white"
                            value={formData.endDate}
                            onChange={handleChange}
                            required
                        />
                    </div>
                </div>

                <div className="mb-4">
                    <h3 className="text-lg font-semibold mb-3">Budget Ranges (Optional)</h3>
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                        <div>
                            <label className="block mb-2" htmlFor="priceRangeFlight">
                                Flights Budget ($)
                            </label>
                            <input
                                id="priceRangeFlight"
                                name="priceRangeFlight"
                                type="number"
                                min="0"
                                className="w-full p-2 border border-gray-500 rounded bg-[#555555] text-white"
                                value={formData.priceRangeFlight}
                                onChange={handleChange}
                            />
                        </div>

                        <div>
                            <label className="block mb-2" htmlFor="priceRangeHotel">
                                Hotels Budget ($)
                            </label>
                            <input
                                id="priceRangeHotel"
                                name="priceRangeHotel"
                                type="number"
                                min="0"
                                className="w-full p-2 border border-gray-500 rounded bg-[#555555] text-white"
                                value={formData.priceRangeHotel}
                                onChange={handleChange}
                            />
                        </div>

                        <div>
                            <label className="block mb-2" htmlFor="priceRangeActivity">
                                Activities Budget ($)
                            </label>
                            <input
                                id="priceRangeActivity"
                                name="priceRangeActivity"
                                type="number"
                                min="0"
                                className="w-full p-2 border border-gray-500 rounded bg-[#555555] text-white"
                                value={formData.priceRangeActivity}
                                onChange={handleChange}
                            />
                        </div>
                    </div>
                </div>

                <div className="mt-6">
                    <button
                        type="submit"
                        className="w-full bg-[var(--color-primary)] hover:bg-[var(--color-primary-hover)] text-white font-bold py-3 px-4 rounded-lg"
                    >
                        Create Itinerary
                    </button>
                </div>
            </form>
        </div>
    );
};

export default CreateItineraryPage;
