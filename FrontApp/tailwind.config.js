/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,js}"],
  theme: {
    fontFamily:{
      'poppins': ['Poppins'],
      'Unica' : ['Unica One'],
      'Inter' : ['Inter'],
      'Roboto': ['Roboto'],
      'Teco' : ['Teco'],
      'mon':['Montserrat'],
      'rale':['Raleway']
    },
    colors:{
      green: {
        light: '#34997a',
        DEFAULT: '#2f7663',
        dark: '#163f3f',
      },
      yellow:{
        DEFAULT:'#ffcb31',
        light:'#f2d47c',
      },
      white:{
        DEFAULT:'#ffff'
      },
      black:{
        DEFAULT:'#060606'
      },

    extend: {},
  },
  plugins: [],
}
}
