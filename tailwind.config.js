/** @type {import('tailwindcss').Config} */
export default {
  content: ['./src/**/*.{js,jsx}'],
  theme: {
    extend: {
      colors: {
        prime: '#1DC078',
        title: '#333B3D',
        subTitle: '#B6BBC1',
        subBackground: '#FAFAFA',
        errprime: '#DB4455',
      },
      keyframes: {
        'accordion-down': {
          from: { height: '0' },
          to: { height: 'var(--radix-accordion-content-height)' },
        },
        'accordion-up': {
          from: { height: 'var(--radix-accordion-content-height)' },
          to: { height: '0' },
        },
        'fade-in': {
          from: { opacity: 0 },
          to: { opacity: 1 },
        },
        fadeIn: {
          '0%': { opacity: 0 },
          '100%': { opacity: 1 },
        },
        fadeOut: {
          '0%': { opacity: 1 },
          '100%': { opacity: 0 },
        },
      },
      animation: {
        'accordion-down': 'accordion-down 0.5s ease-out',
        'accordion-up': 'accordion-up s ease-out',
        'fade-in': 'fade-in 0.5s ease-in-out',
        fadeIn: 'fadeIn 500ms ease-out',
        fadeOut: 'fadeOut 500ms ease-out',
      },
    },
  },
  plugins: [],
};
