import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../services/authService';

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const user = await login(email, password);
            localStorage.setItem('user', JSON.stringify(user));
            navigate('/itineraries');
        } catch (error) {
            console.error('Login error:', error);
            alert('Login failed');
        }
    };

    return (
        <div className="max-w-md mx-auto my-10 p-6 bg-black/50 text-white rounded shadow-md">
            <h2 className="text-2xl font-bold mb-6 text-center">Login</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label className="block mb-2" htmlFor="email">Email</label>
                    <input
                        id="email"
                        type="email"
                        className="w-full p-2 border border-gray-500 rounded bg-[#333] text-white"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label className="block mb-2" htmlFor="password">Password</label>
                    <input
                        id="password"
                        type="password"
                        className="w-full p-2 border border-gray-500 rounded bg-[#333] text-white"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit" className="w-full bg-[var(--color-primary)] hover:bg-[var(--color-secondary)] text-white py-2 rounded">
                    Login
                </button>
            </form>
            <div className="mt-4 text-center">
                <p>
                    Don't have an account?{' '}
                    <a href="/register" className="text-[var(--color-primary)] hover:text-[var(--color-secondary)]">
                        Register here
                    </a>
                </p>
            </div>
        </div>
    );
};

export default LoginPage;
