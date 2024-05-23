const { defineConfig } = require("cypress");

module.exports = defineConfig({
  video: false,
  e2e: {
    baseUrl: "http://localhost:8080/templates/index.html",
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
  },
});
