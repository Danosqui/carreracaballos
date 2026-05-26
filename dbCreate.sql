CREATE DATABASE IF NOT EXISTS carreracaballos;
USE carreracaballos;

CREATE TABLE caballos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    velocidad DOUBLE,
    resistencia DOUBLE,
    energia DOUBLE,
    distanciaRecorrida DOUBLE
);

CREATE TABLE jugadores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    mail VARCHAR(100),
    puntaje INT
);
