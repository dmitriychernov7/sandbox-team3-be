SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE country RESTART IDENTITY;
TRUNCATE TABLE location RESTART IDENTITY;
TRUNCATE TABLE vendor RESTART IDENTITY;
TRUNCATE TABLE category RESTART IDENTITY;
TRUNCATE TABLE discount RESTART IDENTITY;
TRUNCATE TABLE role RESTART IDENTITY;
TRUNCATE TABLE tag RESTART IDENTITY;
TRUNCATE TABLE "user" RESTART IDENTITY;
TRUNCATE TABLE tag_discount RESTART IDENTITY;
TRUNCATE TABLE location_vendor RESTART IDENTITY;
SET FOREIGN_KEY_CHECKS = 1;
