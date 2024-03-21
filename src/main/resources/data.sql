INSERT INTO Roles (role_name, description)
VALUES ('ROLE_ADMIN', 'Администратор'),
('ROLE_MANAGER_USER', 'куратор пользователей'),
('ROLE_MANAGER_STATION', 'диспетчер станций');

INSERT INTO Users (username, password, chat_id, first_name, last_name, uid)
VALUES ('admin', '$2a$12$S34BUzdZz1JscJ2SdronNO9aVDyAQkiU6rHKn7a7EbmTt.78Q9XNC', NULL, 'admin', 'admin', NULL),
('testmu', '$2a$12$P5kjyFapVbW9ZqVQ/Dv6Gepq5BIFahHaPz27.Z3ZTj6t5bFqzTlyS', NULL, 'Ирина', 'Говорова', NULL),
('testms', '$2a$12$eM7ZsLXGMWTVV.vdUyAI4OWroetaAyjQIJSrcPLPQzibHD3EfiPtm', NULL, 'Павел', 'Быков', NULL),
('testuser', '$2a$12$nvQsVUAYbnzDgU/ykc3It.Wi5aIN9l6bLDT7gpKcAwNws6WSAcDTG', NULL, 'Иван', 'Соломин', '00000001');

INSERT INTO Users_roles (user_id, role_id)
VALUES (1, 1),
(2, 2),
(3, 3);

INSERT INTO Addresses (city, street, house_number)
VALUES ('Рязань', 'Каширина', 1),
('Рязань', 'Ленина', 5);

INSERT INTO Vendors(company_name, phone_support_service)
VALUES ('ООО Парус электро', '+79001112233'),
('Yablochkovtech', '+79100001122');

INSERT INTO Stations (station_name, login, password, manager_id, vendor_id, address_id, station_state)
VALUES ('ЭЗС7КВ1', 'teststation1', '$2a$12$0ZMKp1pbK9XsIi1uvGC6puhWoFpAg0u/4eJhtoZ81Nzdl9eac1Xu.', 3, 1, 1, NULL),
('ЭЗС12КВ2', 'teststation2', '$2a$12$plp/Ftjj7FSuyxn9xGM4A.sFP2ilWQSePze8yZlszV4gL47aHyNc2', 3, 1, 1, NULL),
('ЭЗС22КВ3', 'teststation3', '$2a$12$QFk69vcIm379zn.5cgzbb.w5Pc.WcN3PEaFoaLDE4Mi5RW/y8eiq.', NULL, 2, 2, NULL);