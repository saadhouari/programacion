
-- Volcando estructura de base de datos para empresa_ventas
CREATE DATABASE IF NOT EXISTS `empresa_ventas` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `empresa_ventas`;

-- Volcando estructura para tabla empresa_ventas.cliente
CREATE TABLE IF NOT EXISTS `cliente` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) DEFAULT NULL,
  `nif` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando estructura para tabla empresa_ventas.producto
CREATE TABLE IF NOT EXISTS `producto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(45) DEFAULT NULL,
  `precio` float DEFAULT NULL,
  `stock` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- Volcando estructura para tabla empresa_ventas.venta
CREATE TABLE IF NOT EXISTS `venta` (
  `id` int NOT NULL AUTO_INCREMENT,
  `idcliente` int NOT NULL,
  `fecha` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idcliente_idx` (`idcliente`),
  CONSTRAINT `idcliente` FOREIGN KEY (`idcliente`) REFERENCES `cliente` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando estructura para tabla empresa_ventas.detalleventa
CREATE TABLE IF NOT EXISTS `detalleventa` (
  `id` int NOT NULL AUTO_INCREMENT,
  `idventa` int DEFAULT NULL,
  `idproducto` int DEFAULT NULL,
  `cantidad` int DEFAULT NULL,
  `precioventa` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idventa_idx` (`idventa`),
  KEY `idproducto_idx` (`idproducto`),
  CONSTRAINT `idproducto` FOREIGN KEY (`idproducto`) REFERENCES `producto` (`id`),
  CONSTRAINT `idventa` FOREIGN KEY (`idventa`) REFERENCES `venta` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
