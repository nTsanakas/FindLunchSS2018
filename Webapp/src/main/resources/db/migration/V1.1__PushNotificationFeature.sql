-- Spalte `push_notification_enabled` in Tabelle "User" ergänzen.
ALTER TABLE `findlunch`.`user`
ADD COLUMN IF NOT EXISTS `push_notification_enabled` TINYINT(1) DEFAULT 0
AFTER `password`;

-- Spalte `sns_token` aus Tabelle `push_notification` löschen, da nur Firebase genutzt wird.
ALTER TABLE `findlunch`.`push_notification`
DROP COLUMN IF EXISTS `sns_token`;