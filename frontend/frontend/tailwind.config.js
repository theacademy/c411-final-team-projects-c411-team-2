/** @type {import('tailwindcss').Config} */
export default {
    content: [
        './index.html',
        './src/**/*.{js,ts,jsx,tsx}',
    ],
    theme: {
        extend: {
            colors: {
                darkEarth: '#2F2D2E',   // A dark earthy background
                overlayBlack: 'rgba(0,0,0,0.6)',
                accentGreen: '#4CAF50', // A less "earthy" green for buttons
            },
        },
    },
    plugins: [],
};
