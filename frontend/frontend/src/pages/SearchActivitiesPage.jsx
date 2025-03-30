import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { searchActivities, addActivityToItinerary } from '../services/activityService';

const SearchActivitiesPage = () => {
    const { itineraryId } = useParams();
    const navigate = useNavigate();
    const [latitude, setLatitude] = useState('');
    const [longitude, setLongitude] = useState('');
    const [budgetActivity, setBudgetActivity] = useState('100');
    const [activityOptions, setActivityOptions] = useState([]);
    const [selectedIndex, setSelectedIndex] = useState(null);

    const handleSearch = async () => {
        try {
            const results = await searchActivities(latitude, longitude, budgetActivity);
            setActivityOptions(results);
            setSelectedIndex(null);
        } catch (error) {
            console.error('Activity search error:', error);
            alert('Failed to search activities');
        }
    };

    const handleSelect = (index) => {
        setSelectedIndex(index);
    };

    const handleAddActivity = async () => {
        if (selectedIndex === null) return;
        try {
            const chosenActivity = activityOptions[selectedIndex];
            console.log('Adding activity:', { itineraryId, chosenActivity });

            // Now pass the full activity object to the backend
            await addActivityToItinerary(itineraryId, chosenActivity);
            alert('Activity added successfully!');
            navigate(`/itinerary/${itineraryId}`);
        } catch (error) {
            console.error('Error adding activity:', error);
            alert('Failed to add activity');
        }
    };

    return (
        <div className="max-w-4xl mx-auto p-4">
            <h2 className="text-2xl mb-4">Search Activities</h2>
            <div className="flex flex-wrap gap-4 mb-4">
                <div className="flex flex-col">
                    <label className="mb-1 text-sm" htmlFor="latitude">Latitude</label>
                    <input
                        id="latitude"
                        type="text"
                        value={latitude}
                        onChange={(e) => setLatitude(e.target.value)}
                        className="p-2 border rounded bg-[#333] text-white"
                    />
                </div>
                <div className="flex flex-col">
                    <label className="mb-1 text-sm" htmlFor="longitude">Longitude</label>
                    <input
                        id="longitude"
                        type="text"
                        value={longitude}
                        onChange={(e) => setLongitude(e.target.value)}
                        className="p-2 border rounded bg-[#333] text-white"
                    />
                </div>
                <div className="flex flex-col">
                    <label className="mb-1 text-sm" htmlFor="budgetActivity">Activity Budget</label>
                    <input
                        id="budgetActivity"
                        type="text"
                        value={budgetActivity}
                        onChange={(e) => setBudgetActivity(e.target.value)}
                        className="p-2 border rounded bg-[#333] text-white"
                    />
                </div>
                <div className="flex items-end">
                    <button onClick={handleSearch} className="bg-[var(--color-primary)] text-white p-2 rounded">
                        Search
                    </button>
                </div>
            </div>
            {activityOptions.length > 0 && (
                <div>
                    {activityOptions.map((activity, idx) => (
                        <div
                            key={activity.id}
                            onClick={() => handleSelect(idx)}
                            className={`p-4 rounded cursor-pointer mb-2 ${selectedIndex === idx ? 'bg-black/30' : 'bg-black/10'}`}
                        >
                            <p><strong>{activity.name}</strong></p>
                            <p>Price: {activity.price}</p>
                            <p>{activity.description}</p>
                        </div>
                    ))}
                    {selectedIndex !== null && (
                        <button onClick={handleAddActivity} className="mt-4 bg-[var(--color-primary)] text-white p-2 rounded">
                            Add Selected Activity
                        </button>
                    )}
                </div>
            )}
        </div>
    );
};

export default SearchActivitiesPage;
