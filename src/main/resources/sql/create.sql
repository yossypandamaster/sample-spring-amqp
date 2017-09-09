CREATE TABLE `authorities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `authority` varchar(255) NOT NULL,
  `expire_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
INSERT INTO authorities values(0, 'USER', '2100-12-31 12:59:59');

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `locked` tinyint(1) NOT NULL DEFAULT '0',
  `expire_date` datetime DEFAULT NULL,
  `authorities_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_UNIQUE` (`username`),
  KEY `fk_users_authorities1_idx` (`authorities_id`),
  CONSTRAINT `fk_users_authorities1` FOREIGN KEY (`authorities_id`) REFERENCES `authorities` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
INSERT INTO users values(0, 'foo', MD5('foo'), true, false, '2100-12-31 12:59:59', 1);
INSERT INTO users values(0, 'bar', MD5('bar'), true, false, '2100-12-31 12:59:59', 1);
