import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { register } from '../services/authService';

const RegisterPage = () => {
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        originCity: '',
        dateOfBirth: ''
    });
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // Call registration service
            await register(formData);
            alert('Registration successful! Please log in.');
            navigate('/login');
        } catch (error) {
            console.error('Registration error:', error);
            alert('Registration failed');
        }
    };

    return (
        <div className="max-w-md mx-auto my-10 p-6 bg-black/50 text-white rounded shadow-md">
            <h2 className="text-2xl font-bold mb-6 text-center">Register</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                    <div>
                        <label className="block mb-2" htmlFor="firstName">First Name</label>
                        <input
                            id="firstName"
                            name="firstName"
                            type="text"
                            className="w-full p-2 border border-gray-500 rounded bg-[#333] text-white"
                            value={formData.firstName}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div>
                        <label className="block mb-2" htmlFor="lastName">Last Name</label>
                        <input
                            id="lastName"
                            name="lastName"
                            type="text"
                            className="w-full p-2 border border-gray-500 rounded bg-[#333] text-white"
                            value={formData.lastName}
                            onChange={handleChange}
                            required
                        />
                    </div>
                </div>
                <div>
                    <label className="block mb-2" htmlFor="email">Email</label>
                    <input
                        id="email"
                        name="email"
                        type="email"
                        className="w-full p-2 border border-gray-500 rounded bg-[#333] text-white"
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label className="block mb-2" htmlFor="password">Password</label>
                    <input
                        id="password"
                        name="password"
                        type="password"
                        className="w-full p-2 border border-gray-500 rounded bg-[#333] text-white"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label className="block mb-2" htmlFor="originCity">Origin City</label>
                    <input
                        id="originCity"
                        name="originCity"
                        type="text"
                        className="w-full p-2 border border-gray-500 rounded bg-[#333] text-white"
                        value={formData.originCity}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label className="block mb-2" htmlFor="dateOfBirth">Date of Birth</label>
                    <input
                        id="dateOfBirth"
                        name="dateOfBirth"
                        type="date"
                        className="w-full p-2 border border-gray-500 rounded bg-[#333] text-white"
                        value={formData.dateOfBirth}
                        onChange={handleChange}
                        required
                    />
                </div>
                <button
                    type="submit"
                    className="w-full bg-[var(--color-primary)] hover:bg-[var(--color-secondary)] text-white font-bold py-2 rounded"
                >
                    Register
                </button>
            </form>
            <div className="mt-4 text-center">
                <p>
                    Already have an account?{' '}
                    <a href="/login" className="text-[var(--color-primary)] hover:text-[var(--color-secondary)]">
                        Login here
                    </a>
                </p>
            </div>
        </div>
    );
};

export default RegisterPage;
