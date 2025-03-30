// src/components/common/Header.jsx
import React from 'react';
import { Link } from 'react-router-dom';
import bobLogo from '../../assets/bob.png';

const Header = () => {
    return (
        <header className="py-4 px-8 flex justify-between items-center">
            <div className="flex items-center space-x-3">
                <img src={bobLogo} alt="Logo" className="h-10" />
                <h1 className="text-2xl font-bold">Build-A-Trip</h1>
            </div>
            <nav>
                <ul className="flex space-x-6">
                    <li><Link to="/">Home</Link></li>
                    <li><Link to="/itineraries">My Itineraries</Link></li>
                    <li><Link to="/create-itinerary">Create Itinerary</Link></li>
                    <li><Link to="/login">Login</Link></li>
                </ul>
            </nav>
        </header>
    );
};

export default Header;

