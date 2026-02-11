INSERT INTO users (id, name, email, password, role, is_enabled) VALUES
(1, 'Mercury', 'mercury@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'CLIENT', true),
(2, 'Venus', 'venus@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'CLIENT', true),
(3, 'Earth', 'earth@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'CLIENT', true),
(4, 'Mars', 'mars@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'CLIENT', true),
(5, 'Jupiter', 'jupiter@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'CLIENT', true),
(6, 'Saturn', 'saturn@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'CLIENT', true),
(7, 'Uranus', 'uranus@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'CLIENT', true),
(8, 'Neptune', 'neptune@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'CLIENT', true),
(9, 'Pluto', 'pluto@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'CLIENT', true),
(10, 'Ceres', 'ceres@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'CLIENT', true),
(11, 'Sirius', 'sirius@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'CLIENT', true),
(12, 'Andromeda', 'andromeda@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'CLIENT', true),

(13, 'Phobos', 'phobos@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'EMPLOYEE', true),
(14, 'Moon', 'moon@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'EMPLOYEE', true),
(15, 'Deimos', 'deimos@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'EMPLOYEE', true),
(16, 'Europa', 'europa@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'EMPLOYEE', true),
(17, 'Io', 'io@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'EMPLOYEE', true),
(18, 'Ganymede', 'ganymede@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'EMPLOYEE', true),
(19, 'Titan', 'titan@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'EMPLOYEE', true),
(20, 'Dima', 'employee@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'EMPLOYEE', true),
(21, 'Admin', 'admin@gmail.com', '$2a$12$cQH5rafWulAK/.R/eQp2Vei6ZBgdOL0jXaSdkT/R5wJo.CbqtXYne', 'ADMIN', true);

ALTER TABLE users ALTER COLUMN id RESTART WITH 100;