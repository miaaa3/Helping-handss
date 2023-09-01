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
        dollar:'#ffc107',
        lighter:'#ffda6a'
      },
      white:{
        DEFAULT:'#ffff',
        chalk:'#FBFFFF'
      },
      black:{
        DEFAULT:'#060606'
      },
      gray:{
        100:'#f3f4f6',
        200:'#e5e7eb',
        300:'#cbd5e1',
        400:'#94a3b8',
        500:'#64748b',
        800:'#1f2937',
        900:'#0f172a',
      },
      whitesmoke:{
        default:'#F5F5F5',
      },
      blue:{
        600:'#2563eb',
        500:'#3b82f6',
        100:'#dbeafe',
    },

    extend: {},
  },
  plugins: [],
}
}
