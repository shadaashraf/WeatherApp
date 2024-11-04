# Weather App

## Project Brief Description
The Weather App is an Android mobile application that displays the weather status and temperature based on your location. Users can select a specific location on the map or search for it using an auto-complete edit text, allowing them to add locations to a list of favorites. The app provides comprehensive weather information for each location, including alerts for various weather conditions such as rain, wind, and extreme temperatures.

## Features
### 1. Settings Screen
- Choose the location:
  - Get location via GPS or select a location from the map.
- Select temperature units:
  - Kelvin, Celsius, or Fahrenheit.
- Select wind speed units:
  - Meter/second or miles/hour.
- Choose language:
  - Arabic or English.

### 2. Home Screen
- Displays:
  - Current temperature
  - Current date and time
  - Humidity, wind speed, pressure, and cloud coverage
  - Weather icon representing the current status
  - Weather description (e.g., clear sky, light rain)
  - Hourly past weather data for the current date
  - Weather forecast for the next 5 days.

### 3. Weather Alerts Screen
- Add weather alerts with the following settings:
  - Duration for which the alarm is active.
  - Type of alarm (notification or default alarm sound).
  - Option to stop notifications or turn off the alarm.

### 4. Favorite Screen
- Lists favorite locations with the option to:
  - View detailed forecast information for each location.
  - Add new favorite places using a map and auto-complete edit text.
  - Remove saved locations.

## API Usage
The app utilizes the OpenWeatherMap API to fetch weather data. For implementation, refer to the [API documentation](https://api.openweathermap.org/data/2.5/forecast) to understand the endpoints and data structures.

## Installation Instructions
1. **Clone the Repository:**
   ```bash
   git clone https://github.com/shadaashraf/WeatherApp.git
   cd WeatherApp
