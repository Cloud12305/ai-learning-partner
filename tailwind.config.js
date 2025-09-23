/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        "./public/index.html",
        "./src/**/*.{vue,js,ts,jsx,tsx}"
    ],
    theme: {
        extend: {
            colors: {
                primary: '#165DFF',
                secondary: '#36CFC9',
                accent: '#722ED1',
                success: '#52C41A',
                warning: '#FAAD14',
                danger: '#FF4D4F',
                neutral: {
                    100: '#F5F5F5',
                    200: '#E5E5E5',
                    300: '#D4D4D4',
                    400: '#A3A3A3',
                    500: '#737373',
                    600: '#525252',
                    700: '#404040',
                    800: '#262626',
                    900: '#171717',
                }
            },
            fontFamily: {
                inter: ['Inter', 'system-ui', 'sans-serif'],
            },
            boxShadow: {
                'card': '0 4px 20px rgba(0, 0, 0, 0.08)',
                'hover': '0 8px 30px rgba(0, 0, 0, 0.12)',
            }
        },
    },
    plugins: []
}
