import React from 'react';
import { Link } from 'react-router-dom';

export default function HomePage() {
    return (
        <div className="min-h-screen">
            {/* Hero Section */}
            <div className="pt-20 pb-20 px-4 flex flex-col items-center text-center">
                <div className="bg-black/60 text-white p-8 rounded max-w-2xl">
                    <h1 className="text-5xl font-bold mb-6">Your Travel Itinerary Planner</h1>
                    <p className="text-xl mb-8">
                        Build your perfect trip with flights, hotels, and activities tailored to your preferences.
                    </p>
                    <div className="flex flex-col sm:flex-row sm:space-x-4 space-y-4 sm:space-y-0 justify-center">
                        <Link to="/create-itinerary" className="bg-[var(--color-primary)] hover:bg-[var(--color-secondary)] text-white font-bold py-3 px-6 rounded">
                            Start Building
                        </Link>
                        <Link to="/login" className="bg-white hover:bg-gray-200 text-[var(--color-primary)] font-bold py-3 px-6 rounded">
                            Login / Register
                        </Link>
                    </div>
                </div>
            </div>
            {/* How It Works Section */}
            <div className="max-w-5xl mx-auto pb-20 px-4">
                <h2 className="text-3xl font-bold text-center mb-8">How It Works</h2>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                    <div className="bg-black/50 text-white p-4 rounded">
                        <h3 className="text-xl font-semibold mb-2">1. Search Flights</h3>
                        <p>Find the best flight options for your travel dates and destination.</p>
                    </div>
                    <div className="bg-black/50 text-white p-4 rounded">
                        <h3 className="text-xl font-semibold mb-2">2. Add Accommodations</h3>
                        <p>Select from a variety of hotels that fit your budget and preferences.</p>
                    </div>
                    <div className="bg-black/50 text-white p-4 rounded">
                        <h3 className="text-xl font-semibold mb-2">3. Plan Activities</h3>
                        <p>Add exciting activities and attractions to make your trip unforgettable.</p>
                    </div>
                </div>
            </div>
        </div>
    );
}
