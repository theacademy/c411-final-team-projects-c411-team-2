import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './components/common/Header';
import Footer from './components/common/Footer';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import ItineraryListPage from './pages/ItineraryListPage';
import CreateItineraryPage from './pages/CreateItineraryPage';
import ItineraryDetailPage from './pages/ItineraryDetailPage';
import SearchFlightsPage from './pages/SearchFlightsPage';
import SearchReturnFlightsPage from './pages/SearchReturnFlightsPage';
import SearchDestinationsPage from './pages/SearchDestinationsPage';
// The new pages:
import SearchHotelsPage from './pages/SearchHotelsPage';
import SearchActivitiesPage from './pages/SearchActivitiesPage';

import './index.css';

function App() {
    return (
        <Router>
            <div className="flex flex-col min-h-screen">
                <Header />
                <main className="flex-grow">
                    <Routes>
                        <Route path="/" element={<HomePage />} />
                        <Route path="/login" element={<LoginPage />} />
                        <Route path="/register" element={<RegisterPage />} />
                        <Route path="/itineraries" element={<ItineraryListPage />} />
                        <Route path="/create-itinerary" element={<CreateItineraryPage />} />
                        <Route path="/itinerary/:id" element={<ItineraryDetailPage />} />
                        <Route path="/search-flights/:itineraryId" element={<SearchFlightsPage />} />
                        <Route path="/search-return-flights/:itineraryId" element={<SearchReturnFlightsPage />} />
                        <Route path="/search-destinations/:itineraryId" element={<SearchDestinationsPage />} />
                        {/* New routes */}
                        <Route path="/search-hotels/:itineraryId" element={<SearchHotelsPage />} />
                        <Route path="/search-activities/:itineraryId" element={<SearchActivitiesPage />} />
                    </Routes>
                </main>
                <Footer />
            </div>
        </Router>
    );
}

export default App;
