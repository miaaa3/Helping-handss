/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,js}"],
  theme: {
    fontFamily:{
      'poppins': ['Poppins'],
      'Unica' : ['Unica One'],
      'Inter' : ['Inter']
    },
    colors:{
      green: {
        light: '#6c9ca4',
        DEFAULT: '#2f7663',
        dark: '#163f3f',
    },
    extend: {},
  },
  plugins: [],
}
}
