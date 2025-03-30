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
import './index.css'; // or App.css with Tailwind imports

function App() {
    return (
        <Router>
            {/* Full-height flex column so footer can stick to bottom if you want */}
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
                    </Routes>
                </main>
                <Footer />
            </div>
        </Router>
    );
}

export default App;
