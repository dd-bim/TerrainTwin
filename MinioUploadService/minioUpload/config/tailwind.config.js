module.exports = {
  purge: [
    '**/*.html',
    '**/*.js',
  ],
  darkMode: false,
  theme: {
    extend: {
      colors: {
        'head-green': '#F0FDD7',
        'body-green': '#FAFFE9',
        'button-green-2': '#50A526'

      }
    },
    fontFamily: {
      'sans': ['Roboto', 'Helvetica', 'Arial', 'sans-serif']
    },

  },
  variants: {},
  plugins: [
    require('@tailwindcss/forms'),
  ],
}