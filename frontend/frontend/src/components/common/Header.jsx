import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import bobLogo from '../../assets/bob.png';

const Header = () => {
    const navigate = useNavigate();
    const user = JSON.parse(localStorage.getItem('user'));

    const handleLogout = () => {
        localStorage.removeItem('user');
        navigate('/login');
    };

    return (
        <header className="py-4 px-8 flex justify-between items-center bg-[var(--color-header-footer)] text-white">
            <div className="flex items-center space-x-3">
                <img src={bobLogo} alt="Logo" className="h-10" />
                <h1 className="text-2xl font-bold">Build-A-Trip</h1>
            </div>
            <nav>
                <ul className="flex items-center space-x-6">
                    <li>
                        <Link to="/">Home</Link>
                    </li>
                    <li>
                        <Link to="/itineraries">My Itineraries</Link>
                    </li>
                    <li>
                        <Link to="/create-itinerary">Create Itinerary</Link>
                    </li>

                    {user ? (
                        // Treat Logout as a text link
                        <li>
                            <button
                                onClick={handleLogout}
                                className="bg-transparent border-none cursor-pointer hover:underline"
                            >
                                Logout
                            </button>
                        </li>
                    ) : (
                        <li>
                            <Link to="/login">Login</Link>
                        </li>
                    )}
                </ul>
            </nav>
        </header>
    );
};

export default Header;
