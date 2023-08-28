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
        lighter:'#5fbfb4',
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
      gray:{
        400:'#94a3b8',
        500:'#64748b',
        300:'#cbd5e1',
        900:'#0f172a'
      },

    extend: {},
  },
  plugins: [],
}
}
