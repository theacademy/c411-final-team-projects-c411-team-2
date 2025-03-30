import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

const ItineraryListPage = () => {
  const [itineraries, setItineraries] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // This would be replaced with real API call
    setItineraries([
      { id: 1, startDate: '2025-06-01', endDate: '2025-06-07', destination: 'London', confirmed: true },
      { id: 2, startDate: '2025-07-15', endDate: '2025-07-22', destination: 'Paris', confirmed: false }
    ]);
    setLoading(false);
  }, []);

  if (loading) {
    return <div className="text-center p-12">Loading itineraries...</div>;
  }

  return (
      <div className="max-w-4xl mx-auto my-8 px-4 bg-black/60 text-white p-6 rounded shadow">
        {/* Page Title + Create Button */}
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold">My Itineraries</h1>
          <Link
              to="/create-itinerary"
              className="bg-[var(--color-primary)] hover:bg-[var(--color-secondary)] text-white px-4 py-2 rounded"
          >
            Create New Itinerary
          </Link>
        </div>

        {itineraries.length === 0 ? (
            <div className="text-center p-8 bg-black/30 rounded">
              <p className="text-lg">You don't have any itineraries yet.</p>
              <Link to="/create-itinerary" className="text-[var(--color-primary)] hover:underline mt-2 inline-block">
                Create your first itinerary
              </Link>
            </div>
        ) : (
            <div className="grid gap-4 md:grid-cols-2">
              {itineraries.map((itinerary) => (
                  <div
                      key={itinerary.id}
                      className="bg-black/30 rounded p-4 hover:shadow-md transition-shadow"
                  >
                    <div className="flex justify-between">
                      <h3 className="text-xl font-semibold">
                        {itinerary.destination || 'Trip'}
                      </h3>
                      <span
                          className={`px-2 py-1 text-sm rounded-full ${
                              itinerary.confirmed
                                  ? 'bg-green-100 text-green-800'
                                  : 'bg-yellow-100 text-yellow-800'
                          }`}
                      >
                  {itinerary.confirmed ? 'Confirmed' : 'Draft'}
                </span>
                    </div>
                    <p className="mt-2">
                      {new Date(itinerary.startDate).toLocaleDateString()} -{' '}
                      {new Date(itinerary.endDate).toLocaleDateString()}
                    </p>
                    <div className="mt-4">
                      <Link
                          to={`/itinerary/${itinerary.id}`}
                          className="text-[var(--color-primary)] hover:underline"
                      >
                        View Details
                      </Link>
                    </div>
                  </div>
              ))}
            </div>
        )}
      </div>
  );
};

export default ItineraryListPage;
