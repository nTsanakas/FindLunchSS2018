-- Spalte `push_notification_enabled` in Tabelle "User" ergänzen.
ALTER TABLE `findlunch`.`user`
ADD COLUMN IF NOT EXISTS `push_notification_enabled` TINYINT(1) DEFAULT 0
AFTER `password`;

