CREATE DATABASE  IF NOT EXISTS `portabilidad` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `portabilidad`;
-- MySQL dump 10.13  Distrib 5.7.18, for Linux (x86_64)
--
-- Host: localhost    Database: myapp
-- ------------------------------------------------------
-- Server version	5.7.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cfg_definicion_parametros`
--

DROP TABLE IF EXISTS `cfg_definicion_parametros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cfg_definicion_parametros` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `parent_id` int(10) unsigned DEFAULT NULL,
  `tipo_parametro_id` int(10) unsigned NOT NULL,
  `clave_parametro` varchar(20) NOT NULL,
  `descripcion` varchar(128) NOT NULL,
  `caracteristica_1` varchar(25) DEFAULT NULL,
  `caracteristica_2` varchar(25) DEFAULT NULL,
  `caracteristica_3` varchar(25) DEFAULT NULL,
  `caracteristica_4` varchar(25) DEFAULT NULL,
  `caracteristica_5` varchar(25) DEFAULT NULL,
  `configuracion` longtext,
  `usuario_alta` varchar(20) DEFAULT NULL,
  `fecha_alta` datetime DEFAULT NULL,
  `usuario_ult_modificacion` varchar(20) DEFAULT NULL,
  `fecha_ult_modificacion` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_definicion_tipo_parametro` (`tipo_parametro_id`),
  KEY `idx_definicion_clave_parametro` (`tipo_parametro_id`,`clave_parametro`),
  KEY `fk_parametro_id_parametro_idx` (`parent_id`),
  CONSTRAINT `FKhoiqay0qi2g521vv5nfpo3qke` FOREIGN KEY (`parent_id`) REFERENCES `cfg_definicion_parametros` (`id`),
  CONSTRAINT `FKop4bamdqsgccqffiq8478isrd` FOREIGN KEY (`tipo_parametro_id`) REFERENCES `cfg_tipo_parametro` (`id`),
  CONSTRAINT `fk_parametro_id_parametro` FOREIGN KEY (`parent_id`) REFERENCES `cfg_definicion_parametros` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_parametro_tipo_parametro` FOREIGN KEY (`tipo_parametro_id`) REFERENCES `cfg_tipo_parametro` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cfg_definicion_parametros`
--

LOCK TABLES `cfg_definicion_parametros` WRITE;
/*!40000 ALTER TABLE `cfg_definicion_parametros` DISABLE KEYS */;
INSERT INTO `cfg_definicion_parametros` VALUES (1,NULL,1,'QUERY_PARAMS','S/D','','','','','','\r\n','admin','2017-09-04 17:35:10','admin','2017-09-04 18:41:56'),(2,1,1,'QUERY_1','QUERY_1','','','','','','SELECT\r\n    *\r\nFROM cfg_definicion_parametros','admin','2017-09-04 18:19:56','admin','2017-09-04 18:42:03'),(3,NULL,2,'DEMO','PROCESOS DEMO','','','','','','','admin','2017-09-05 10:42:02',NULL,NULL),(4,3,2,'SCRIPT_1','DEMO SCRIPT 1','','','','','','print(\"Hola mundo\");','admin','2017-09-05 10:42:45',NULL,NULL);
/*!40000 ALTER TABLE `cfg_definicion_parametros` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cfg_opcion`
--

DROP TABLE IF EXISTS `cfg_opcion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cfg_opcion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(128) NOT NULL,
  `imagen` varchar(128) DEFAULT NULL,
  `nombre` varchar(128) NOT NULL,
  `opcion_id` bigint(20) DEFAULT NULL,
  `orden` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_opcion_parent_idx` (`opcion_id`),
  CONSTRAINT `FKnkq03u9sjbqf6bsmm41cgn6fr` FOREIGN KEY (`opcion_id`) REFERENCES `cfg_opcion` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cfg_opcion`
--

LOCK TABLES `cfg_opcion` WRITE;
/*!40000 ALTER TABLE `cfg_opcion` DISABLE KEYS */;
INSERT INTO `cfg_opcion` VALUES (1,'aplicacion/administracion','fa fa-th','Admón. General',NULL,1),(2,'aplicacion/administracion/usuarios.xhtml','fa fa-users','Usuarios',1,1),(3,'aplicacion/administracion/perfiles.xhtml','fa fa-users','Perfiles',1,2),(4,'aplicacion/administracion/opciones.xhtml','fa fa-list','Opciones',1,3),(5,'aplicacion/administracion/cambioPassword.xhtml','fa fa-key','Reset Password',1,4),(6,'aplicacion/administracion/configuracionParametros.xhtml','fa fa-th','Consultas dinámicas',1,5),(7,'aplicacion/operacion','fa fa-gears','Operación',NULL,2),(8,'aplicacion/operacion/speech.xhtml','fa fa-file-audio-o','Voz a Texto',7,1);
/*!40000 ALTER TABLE `cfg_opcion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cfg_permiso`
--

DROP TABLE IF EXISTS `cfg_permiso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cfg_permiso` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `consultar` varchar(255) NOT NULL,
  `crear` varchar(255) NOT NULL,
  `eliminar` varchar(255) NOT NULL,
  `modificar` varchar(255) NOT NULL,
  `opcion_id` bigint(20) NOT NULL,
  `rol_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_7bxisjeqr8scbo5genji133wr` (`opcion_id`),
  KEY `FK_d0ogy6mv1a2b4ajkl89wruldo` (`rol_id`),
  CONSTRAINT `FK13bewjsswdhu83eyf0vxp71y5` FOREIGN KEY (`opcion_id`) REFERENCES `cfg_opcion` (`id`),
  CONSTRAINT `FK_7bxisjeqr8scbo5genji133wr` FOREIGN KEY (`opcion_id`) REFERENCES `cfg_opcion` (`id`),
  CONSTRAINT `FK_d0ogy6mv1a2b4ajkl89wruldo` FOREIGN KEY (`rol_id`) REFERENCES `cfg_rol` (`id`),
  CONSTRAINT `FKjp842id6hsk2qqnfibtwwl4fv` FOREIGN KEY (`rol_id`) REFERENCES `cfg_rol` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cfg_permiso`
--

LOCK TABLES `cfg_permiso` WRITE;
/*!40000 ALTER TABLE `cfg_permiso` DISABLE KEYS */;
INSERT INTO `cfg_permiso` VALUES (1,'V','V','V','V',1,1),(2,'V','V','V','V',2,1),(3,'V','V','V','V',3,1),(4,'V','V','V','V',4,1),(5,'V','V','V','V',5,1),(6,'V','V','V','V',6,1),(7,'V','V','V','V',7,1),(8,'V','V','V','V',8,1),(28,'V','V','V','V',1,2),(29,'V','V','V','V',2,2),(30,'V','V','V','V',3,2),(31,'V','V','V','V',5,2);
/*!40000 ALTER TABLE `cfg_permiso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cfg_rol`
--

DROP TABLE IF EXISTS `cfg_rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cfg_rol` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `clave_rol` varchar(128) NOT NULL,
  `descripcion` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `clave_rol_UNIQUE` (`clave_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cfg_rol`
--

LOCK TABLES `cfg_rol` WRITE;
/*!40000 ALTER TABLE `cfg_rol` DISABLE KEYS */;
INSERT INTO `cfg_rol` VALUES (1,'ROLE_ADMIN','ADMINISTRADOR'),(2,'ROLE_USER','OPERADOR');
/*!40000 ALTER TABLE `cfg_rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cfg_tipo_parametro`
--

DROP TABLE IF EXISTS `cfg_tipo_parametro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cfg_tipo_parametro` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(25) NOT NULL,
  `descripcion` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cfg_tipo_parametro`
--

LOCK TABLES `cfg_tipo_parametro` WRITE;
/*!40000 ALTER TABLE `cfg_tipo_parametro` DISABLE KEYS */;
INSERT INTO `cfg_tipo_parametro` VALUES (1,'CONSULTAS_SQL','CONSULTAS SQL'),(2,'PROCESOS','PROCESOS'),(3,'REPORTES','REPORTES');
/*!40000 ALTER TABLE `cfg_tipo_parametro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cfg_transaccion`
--

DROP TABLE IF EXISTS `cfg_transaccion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cfg_transaccion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `aplicacion_id` int(11) DEFAULT NULL,
  `clave_transaccion` varchar(50) NOT NULL,
  `detalle` varchar(2056) DEFAULT NULL,
  `fecha_transaccion` datetime DEFAULT NULL,
  `user_name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_cfg_transaccion_username_idx` (`user_name`),
  KEY `idx_trans_clave_transaccion` (`clave_transaccion`),
  CONSTRAINT `fk_cfg_transaccion_username` FOREIGN KEY (`user_name`) REFERENCES `cfg_usuario` (`user_name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cfg_transaccion`
--

LOCK TABLES `cfg_transaccion` WRITE;
/*!40000 ALTER TABLE `cfg_transaccion` DISABLE KEYS */;
INSERT INTO `cfg_transaccion` VALUES (1,0,'INICIO_SESION','[Date: Thu Aug 17 13:03:39 CDT 2017]','2017-08-17 13:03:40','asalgado'),(2,0,'INICIO_SESION','[Date: Thu Aug 17 13:10:10 CDT 2017]','2017-08-17 13:10:11','asalgado'),(3,0,'MODIFICACION_USUARIO','[Id: 1, Username: admin, Roles: [1]]','2017-08-17 13:10:24','asalgado'),(4,0,'INICIO_SESION','[Date: Thu Aug 17 13:18:01 CDT 2017]','2017-08-17 13:18:01','admin'),(5,0,'MODIFICACION_USUARIO','[Id: 2, Username: asalgado, Roles: [2]]','2017-08-17 13:18:18','admin'),(6,0,'INICIO_SESION','[Date: Thu Aug 17 13:18:31 CDT 2017]','2017-08-17 13:18:32','asalgado'),(7,0,'INICIO_SESION','[Date: Thu Aug 17 13:23:10 CDT 2017]','2017-08-17 13:23:11','admin'),(8,0,'INICIO_SESION','[Date: Thu Aug 17 13:32:07 CDT 2017]','2017-08-17 13:32:08','asalgado'),(9,0,'INICIO_SESION','[Date: Thu Aug 17 13:33:18 CDT 2017]','2017-08-17 13:33:19','admin'),(10,0,'INICIO_SESION','[Date: Thu Aug 17 13:35:10 CDT 2017]','2017-08-17 13:35:10','admin'),(11,0,'INICIO_SESION','[Date: Thu Aug 17 13:36:34 CDT 2017]','2017-08-17 13:36:34','asalgado'),(12,0,'INICIO_SESION','[Date: Thu Aug 17 13:41:53 CDT 2017]','2017-08-17 13:41:54','asalgado'),(13,0,'INICIO_SESION','[Date: Thu Aug 17 13:42:37 CDT 2017]','2017-08-17 13:42:37','admin'),(14,0,'INICIO_SESION','[Date: Thu Aug 17 13:47:19 CDT 2017]','2017-08-17 13:47:20','admin'),(15,0,'INICIO_SESION','[Date: Thu Aug 17 13:54:51 CDT 2017]','2017-08-17 13:54:51','admin'),(16,0,'INICIO_SESION','[Date: Thu Aug 17 13:55:22 CDT 2017]','2017-08-17 13:55:22','asalgado'),(17,0,'MODIFICACION_USUARIO','[Id: 2, Username: asalgado, Roles: [2, 1]]','2017-08-17 13:55:39','asalgado'),(18,0,'INICIO_SESION','[Date: Thu Aug 17 13:55:52 CDT 2017]','2017-08-17 13:55:52','asalgado'),(19,0,'INICIO_SESION','[Date: Thu Aug 17 13:56:36 CDT 2017]','2017-08-17 13:56:36','asalgado'),(20,0,'INICIO_SESION','[Date: Thu Aug 17 15:43:15 CDT 2017]','2017-08-17 15:43:15','asalgado'),(21,0,'INICIO_SESION','[Date: Thu Aug 17 16:00:21 CDT 2017]','2017-08-17 16:00:22','asalgado'),(22,0,'INICIO_SESION','[Date: Thu Aug 17 16:02:55 CDT 2017]','2017-08-17 16:02:56','admin'),(23,0,'INICIO_SESION','[Date: Thu Aug 17 16:15:11 CDT 2017]','2017-08-17 16:15:11','asalgado'),(24,0,'INICIO_SESION','[Date: Fri Aug 18 10:05:45 CDT 2017]','2017-08-18 10:05:45','asalgado'),(25,0,'INICIO_SESION','[Date: Fri Aug 18 10:12:10 CDT 2017]','2017-08-18 10:12:10','asalgado'),(26,0,'INICIO_SESION','[Date: Fri Aug 25 16:42:09 CDT 2017]','2017-08-25 16:42:09','asalgado'),(27,0,'INICIO_SESION','[Date: Fri Aug 25 17:03:40 CDT 2017]','2017-08-25 17:03:40','asalgado'),(28,0,'INICIO_SESION','[Date: Fri Aug 25 17:03:57 CDT 2017]','2017-08-25 17:03:58','asalgado'),(29,0,'INICIO_SESION','[Date: Mon Aug 28 16:52:33 CDT 2017]','2017-08-28 16:52:34','asalgado'),(30,0,'INICIO_SESION','[Date: Tue Aug 29 10:02:08 CDT 2017]','2017-08-29 10:02:09','asalgado'),(31,0,'INICIO_SESION','[Date: Tue Aug 29 10:06:38 CDT 2017]','2017-08-29 10:06:39','asalgado'),(32,0,'INICIO_SESION','[Date: Tue Aug 29 10:13:20 CDT 2017]','2017-08-29 10:13:21','asalgado'),(33,0,'INICIO_SESION','[Date: Tue Aug 29 10:29:48 CDT 2017]','2017-08-29 10:29:49','asalgado'),(34,0,'INICIO_SESION','[Date: Tue Aug 29 10:43:55 CDT 2017]','2017-08-29 10:43:55','admin'),(35,0,'INICIO_SESION','[Date: Tue Aug 29 10:44:13 CDT 2017]','2017-08-29 10:44:14','asalgado'),(36,0,'INICIO_SESION','[Date: Tue Aug 29 10:50:17 CDT 2017]','2017-08-29 10:50:18','admin'),(37,0,'INICIO_SESION','[Date: Tue Aug 29 10:55:24 CDT 2017]','2017-08-29 10:55:24','admin'),(38,0,'INICIO_SESION','[Date: Tue Aug 29 10:56:26 CDT 2017]','2017-08-29 10:56:27','admin'),(39,0,'INICIO_SESION','[Date: Tue Aug 29 10:57:23 CDT 2017]','2017-08-29 10:57:24','admin'),(40,0,'INICIO_SESION','[Date: Tue Aug 29 10:57:28 CDT 2017]','2017-08-29 10:57:28','admin'),(41,0,'INICIO_SESION','[Date: Tue Aug 29 10:57:50 CDT 2017]','2017-08-29 10:57:50','admin'),(42,0,'INICIO_SESION','[Date: Tue Aug 29 10:58:18 CDT 2017]','2017-08-29 10:58:19','admin'),(43,0,'INICIO_SESION','[Date: Tue Aug 29 10:58:29 CDT 2017]','2017-08-29 10:58:30','admin'),(44,0,'INICIO_SESION','[Date: Tue Aug 29 10:59:42 CDT 2017]','2017-08-29 10:59:42','admin'),(45,0,'INICIO_SESION','[Date: Tue Aug 29 10:59:53 CDT 2017]','2017-08-29 10:59:54','admin'),(46,0,'INICIO_SESION','[Date: Tue Aug 29 11:00:03 CDT 2017]','2017-08-29 11:00:03','asalgado'),(47,0,'INICIO_SESION','[Date: Tue Aug 29 11:11:46 CDT 2017]','2017-08-29 11:11:46','admin'),(48,0,'INICIO_SESION','[Date: Tue Aug 29 11:12:44 CDT 2017]','2017-08-29 11:12:44','admin'),(49,0,'INICIO_SESION','[Date: Wed Aug 30 09:43:12 CDT 2017]','2017-08-30 09:43:13','admin'),(50,0,'INICIO_SESION','[Date: Wed Aug 30 09:44:04 CDT 2017]','2017-08-30 09:44:04','asalgado'),(51,0,'INICIO_SESION','[Date: Wed Aug 30 09:44:24 CDT 2017]','2017-08-30 09:44:24','admin'),(52,0,'INICIO_SESION','[Date: Wed Aug 30 12:19:17 CDT 2017]','2017-08-30 12:19:17','admin'),(53,0,'INICIO_SESION','[Date: Wed Aug 30 14:58:19 CDT 2017]','2017-08-30 14:58:20','admin'),(54,0,'MODIFICACION_USUARIO','[Id: 1, Username: admin, Roles: [1, 2]]','2017-08-30 14:59:14','admin'),(55,0,'INICIO_SESION','[Date: Wed Aug 30 15:07:02 CDT 2017]','2017-08-30 15:07:03','admin'),(56,0,'INICIO_SESION','[Date: Wed Aug 30 15:08:19 CDT 2017]','2017-08-30 15:08:19','admin'),(57,0,'INICIO_SESION','[Date: Wed Aug 30 15:11:39 CDT 2017]','2017-08-30 15:11:39','admin'),(58,0,'INICIO_SESION','[Date: Mon Sep 04 16:16:11 CDT 2017]','2017-09-04 16:16:12','admin'),(59,0,'INICIO_SESION','[Date: Mon Sep 04 17:30:25 CDT 2017]','2017-09-04 17:30:26','admin'),(60,0,'INICIO_SESION','[Date: Mon Sep 04 18:19:04 CDT 2017]','2017-09-04 18:19:05','admin'),(61,0,'INICIO_SESION','[Date: Mon Sep 04 18:41:37 CDT 2017]','2017-09-04 18:41:37','admin'),(62,0,'INICIO_SESION','[Date: Tue Sep 05 10:21:57 CDT 2017]','2017-09-05 10:21:57','admin'),(63,0,'INICIO_SESION','[Date: Tue Sep 05 10:32:49 CDT 2017]','2017-09-05 10:32:50','admin'),(64,0,'INICIO_SESION','[Date: Thu Sep 07 09:40:18 CDT 2017]','2017-09-07 09:40:18','admin'),(65,0,'INICIO_SESION','[Date: Thu Sep 07 10:43:47 CDT 2017]','2017-09-07 10:43:48','admin'),(66,0,'INICIO_SESION','[Date: Thu Sep 07 10:45:30 CDT 2017]','2017-09-07 10:45:30','admin'),(67,0,'INICIO_SESION','[Date: Thu Sep 07 12:33:55 CDT 2017]','2017-09-07 12:33:56','admin'),(68,0,'INICIO_SESION','[Date: Thu Sep 07 12:50:02 CDT 2017]','2017-09-07 12:50:02','admin'),(69,0,'INICIO_SESION','[Date: Thu Sep 07 12:56:58 CDT 2017]','2017-09-07 12:56:59','asalgado'),(70,0,'INICIO_SESION','[Date: Thu Sep 07 12:57:10 CDT 2017]','2017-09-07 12:57:11','admin'),(71,0,'INICIO_SESION','[Date: Thu Sep 07 17:57:41 CDT 2017]','2017-09-07 17:57:41','admin'),(72,0,'INICIO_SESION','[Date: Thu Sep 07 18:07:36 CDT 2017]','2017-09-07 18:07:36','asalgado'),(73,0,'INICIO_SESION','[Date: Tue Sep 12 13:33:30 CDT 2017]','2017-09-12 13:33:30','asalgado'),(74,0,'INICIO_SESION','[Date: Tue Sep 12 13:45:27 CDT 2017]','2017-09-12 13:45:27','asalgado'),(75,0,'INICIO_SESION','[Date: Wed Sep 20 10:34:11 CDT 2017]','2017-09-20 10:34:11','asalgado'),(76,0,'INICIO_SESION','[Date: Wed Sep 20 10:34:48 CDT 2017]','2017-09-20 10:34:48','asalgado'),(77,0,'INICIO_SESION','[Date: Fri Nov 24 08:18:01 CST 2017]','2017-11-24 08:18:01','asalgado'),(78,0,'INICIO_SESION','[Date: Tue Dec 05 18:20:14 CST 2017]','2017-12-05 18:20:14','admin');
/*!40000 ALTER TABLE `cfg_transaccion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cfg_usuario`
--

DROP TABLE IF EXISTS `cfg_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cfg_usuario` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30) NOT NULL,
  `paterno` varchar(30) NOT NULL,
  `materno` varchar(30) DEFAULT NULL,
  `email` varchar(40) DEFAULT NULL,
  `activo` int(11) NOT NULL,
  `user_name` varchar(30) NOT NULL,
  `password` varchar(256) NOT NULL,
  `fecha_alta` datetime DEFAULT NULL,
  `fecha_ult_modificacion` datetime DEFAULT NULL,
  `no_accesos` int(11) DEFAULT '0',
  `no_intentos` int(4) DEFAULT '0',
  `cuenta_bloqueada` varchar(1) DEFAULT 'F',
  `fecha_expiracion` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_pimirdqbf828ejsgnxmc3kfix` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cfg_usuario`
--

LOCK TABLES `cfg_usuario` WRITE;
/*!40000 ALTER TABLE `cfg_usuario` DISABLE KEYS */;
INSERT INTO `cfg_usuario` VALUES (1,'ADMINISTRADOR','GENERAL','','admin@gmail.com',1,'admin','0DPiKuNIrrVmD8IUCuw1hQxNqZc=',NULL,'2017-08-30 14:59:13',43,0,'F',NULL),(2,'ADRIAN','SALGADO','DELGADO','adsalgado@gmail.com',1,'asalgado','mlodmAu/g/ZaMML9aWCzkQbtCHk=',NULL,'2017-08-17 13:55:38',41,0,'F',NULL);
/*!40000 ALTER TABLE `cfg_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cfg_usuario_rol`
--

DROP TABLE IF EXISTS `cfg_usuario_rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cfg_usuario_rol` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` bigint(20) NOT NULL,
  `rol_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_ur_usuario_rol` (`usuario_id`,`rol_id`),
  KEY `fk_ur_usuario_idx` (`usuario_id`),
  KEY `fk_ur_rol_idx` (`rol_id`),
  CONSTRAINT `FKchbapc52b4bokwvg6mjtgwm3g` FOREIGN KEY (`rol_id`) REFERENCES `cfg_rol` (`id`),
  CONSTRAINT `FKdpenqec0ij0qy6m8bxadlft9r` FOREIGN KEY (`usuario_id`) REFERENCES `cfg_usuario` (`id`),
  CONSTRAINT `fk_ur_rol` FOREIGN KEY (`rol_id`) REFERENCES `cfg_rol` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ur_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `cfg_usuario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cfg_usuario_rol`
--

LOCK TABLES `cfg_usuario_rol` WRITE;
/*!40000 ALTER TABLE `cfg_usuario_rol` DISABLE KEYS */;
INSERT INTO `cfg_usuario_rol` VALUES (32,1,1),(33,1,2),(31,2,1),(30,2,2);
/*!40000 ALTER TABLE `cfg_usuario_rol` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-12-12 10:12:43
