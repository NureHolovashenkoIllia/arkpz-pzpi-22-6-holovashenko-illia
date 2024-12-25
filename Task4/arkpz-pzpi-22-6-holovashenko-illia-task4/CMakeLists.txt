cmake_minimum_required(VERSION 3.30)
project(arkpz_pzpi_22_6_holovashenko_illia_task4)

set(CMAKE_CXX_STANDARD 17)

# Джерела проєкту
set(SOURCES
        main.cpp
        src/config.cpp
        src/utils.cpp
        src/sensors.cpp
        src/mqtt_client.cpp
)

include_directories("C:/mqtt/paho-cpp/include")
include_directories("C:/mqtt/paho-c/include")
include_directories(${PROJECT_SOURCE_DIR}/include)
include_directories("C:\\Users\\f4ily\\vcpkg\\installed\\x64-windows\\include")

set(CMAKE_TOOLCHAIN_FILE "C:/Users/f4ily/vcpkg/scripts/buildsystems/vcpkg.cmake" CACHE STRING "")
set(CMAKE_PREFIX_PATH ${CMAKE_PREFIX_PATH} "C:/Users/f4ily/vcpkg/installed/x64-windows/share")

find_package(nlohmann_json REQUIRED)

find_library(MOSQUITTO_LIB mosquitto HINTS "C:\\Users\\f4ily\\vcpkg\\installed\\x64-windows\\lib")
if (NOT MOSQUITTO_LIB)
    message(FATAL_ERROR "Mosquitto library not found. Please check the path.")
endif()

add_executable(arkpz_pzpi_22_6_holovashenko_illia_task4 ${SOURCES})

# Додайте лінкування до цільового файлу
target_link_libraries(${PROJECT_NAME} PRIVATE
        "C:/mqtt/paho-cpp/lib/paho-mqttpp3-static.lib"
        "C:/mqtt/paho-c/lib/paho-mqtt3a-static.lib"
        "C:/mqtt/paho-c/lib/paho-mqtt3c-static.lib"
        ws2_32
        secur32
        crypt32
        nlohmann_json::nlohmann_json
        ${MOSQUITTO_LIB})
