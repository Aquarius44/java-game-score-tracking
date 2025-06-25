-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Anamakine: 127.0.0.1
-- Üretim Zamanı: 13 Haz 2025, 22:35:34
-- Sunucu sürümü: 10.4.32-MariaDB
-- PHP Sürümü: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Veritabanı: `gamescoretracking`
--

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `achievements`
--

CREATE TABLE `achievements` (
  `achievement_id` int(11) NOT NULL,
  `title` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `game_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `blocked_users`
--

CREATE TABLE `blocked_users` (
  `id` int(11) NOT NULL,
  `blocker_username` varchar(255) DEFAULT NULL,
  `blocked_username` varchar(255) DEFAULT NULL,
  `block_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `sender` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `deleted_messages`
--

CREATE TABLE `deleted_messages` (
  `deleter_username` varchar(50) NOT NULL,
  `hidden_with_user` varchar(50) NOT NULL,
  `deleted_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `deleted_messages`
--

INSERT INTO `deleted_messages` (`deleter_username`, `hidden_with_user`, `deleted_at`) VALUES
('ahmed', 'yasemin', '2025-05-27 11:29:41'),
('yasemin', 'ahmed', '2025-05-27 11:38:05'),
('yasemin', 'ahmed', '2025-05-27 11:40:35'),
('yasemin', 'ahmed', '2025-05-27 11:44:55'),
('ahmed', 'yasemin', '2025-05-27 11:45:32'),
('ahmed', 'yasemin', '2025-05-27 11:52:46'),
('ahmed', 'yasemin', '2025-05-27 11:52:55'),
('ahmed', 'yasemin', '2025-05-27 11:52:55'),
('ahmed', 'yasemin', '2025-05-27 11:52:55');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `friendships`
--

CREATE TABLE `friendships` (
  `user1` varchar(255) NOT NULL,
  `user2` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `friend_requests`
--

CREATE TABLE `friend_requests` (
  `id` int(11) NOT NULL,
  `sender_username` varchar(255) DEFAULT NULL,
  `receiver_username` varchar(255) DEFAULT NULL,
  `status` enum('pending','accepted','rejected') DEFAULT 'pending',
  `request_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `friend_requests`
--

INSERT INTO `friend_requests` (`id`, `sender_username`, `receiver_username`, `status`, `request_date`) VALUES
(4, 'kullaniciadi', 'hedefKullanici', 'pending', '2025-06-13 15:33:41'),
(9, 'yasemin', 'ahmed', 'pending', '2025-06-13 16:17:46'),
(11, 'burak', 'yasemin', 'accepted', '2025-06-13 19:22:02'),
(12, 'burak', 'enes', 'pending', '2025-06-13 19:22:08'),
(13, 'yasemin', 'enes', 'pending', '2025-06-13 19:25:04');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `games`
--

CREATE TABLE `games` (
  `game_id` int(11) NOT NULL,
  `game_name` varchar(100) NOT NULL,
  `genre` varchar(50) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `leaderboards`
--

CREATE TABLE `leaderboards` (
  `leaderboard_id` int(11) NOT NULL,
  `game_id` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  `total_score` int(11) NOT NULL,
  `last_updated` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `levels`
--

CREATE TABLE `levels` (
  `level_id` int(11) NOT NULL,
  `game_id` int(11) NOT NULL,
  `level_name` varchar(100) DEFAULT NULL,
  `difficulty` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `messages`
--

CREATE TABLE `messages` (
  `id` int(11) NOT NULL,
  `sender_username` varchar(50) NOT NULL,
  `receiver_username` varchar(50) NOT NULL,
  `message` text NOT NULL,
  `timestamp` datetime DEFAULT current_timestamp(),
  `is_read` tinyint(1) DEFAULT 0,
  `deleted_by_sender` varchar(255) DEFAULT NULL,
  `deleted_by_receiver` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `messages`
--

INSERT INTO `messages` (`id`, `sender_username`, `receiver_username`, `message`, `timestamp`, `is_read`, `deleted_by_sender`, `deleted_by_receiver`) VALUES
(1, 'yasemin', 'ahmed', 'SELAM\n', '2025-05-27 14:15:14', 1, 'yasemin', 'ahmed'),
(2, 'ahmed', 'yasemin', 'selaam\n', '2025-05-27 14:16:24', 1, 'ahmed', 'yasemin'),
(3, 'yasemin', 'ahmed', 'merhaba', '2025-05-27 14:24:47', 1, 'yasemin', 'ahmed'),
(4, 'yasemin', 'ahmed', 'nasılsın', '2025-05-27 14:24:50', 1, 'yasemin', 'ahmed'),
(28, 'yasemin', 'Enes', 'selam', '2025-05-27 15:11:32', 1, 'yasemin', NULL),
(29, 'yasemin', 'Enes', 'merhaba', '2025-05-27 15:11:34', 1, 'yasemin', NULL),
(30, 'Enes', 'yasemin', 'selam', '2025-05-27 15:12:02', 1, NULL, 'yasemin'),
(31, 'Enes', 'yasemin', 'nasılsın', '2025-05-27 15:12:05', 1, NULL, 'yasemin'),
(32, 'buket', 'yasemin', 'selam', '2025-05-29 14:24:01', 1, NULL, NULL),
(33, 'yasemin', 'Buket', 'selam', '2025-05-29 14:24:30', 1, NULL, NULL),
(35, 'ahmed', 'Yasemin', 'selam naber', '2025-06-13 13:19:40', 0, NULL, 'yasemin'),
(37, 'ahmed', 'Yasemin', '😊', '2025-06-13 13:20:23', 0, NULL, 'yasemin'),
(57, 'burak', 'yasemin', 'selam', '2025-06-13 22:22:26', 0, NULL, NULL),
(58, 'burak', 'yasemin', 'nabersin', '2025-06-13 22:22:31', 0, NULL, NULL);

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `playerachievements`
--

CREATE TABLE `playerachievements` (
  `player_achievement_id` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  `achievement_id` int(11) NOT NULL,
  `achieved_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `playerlevels`
--

CREATE TABLE `playerlevels` (
  `player_level_id` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  `level_id` int(11) NOT NULL,
  `completed` tinyint(1) DEFAULT 0,
  `completion_time` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `playerprofiles`
--

CREATE TABLE `playerprofiles` (
  `profile_id` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `bio` text DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `birthday` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `players`
--

CREATE TABLE `players` (
  `player_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `password` varchar(100) NOT NULL,
  `profile_picture` varchar(255) DEFAULT NULL,
  `photo_path` varchar(255) DEFAULT NULL,
  `sender` varchar(255) DEFAULT NULL,
  `blocker` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `players`
--

INSERT INTO `players` (`player_id`, `username`, `email`, `created_at`, `password`, `profile_picture`, `photo_path`, `sender`, `blocker`) VALUES
(27, 'yasemin', 'yasemin@gmail.com', '2025-06-13 08:36:43', '393734yS', NULL, 'profile_photos\\Yasemin_ds.jpg', NULL, NULL),
(28, 'ahmed', 'ahmed@gmail.com', '2025-06-13 10:19:29', '1234ys', NULL, 'profile_photos\\ahmed_download.jpg', NULL, NULL),
(29, 'enes', 'enes@gmail.com', '2025-06-13 11:21:23', '393734ys', NULL, 'profile_photos\\enes_Ekran görüntüsü 2025-06-09 111831.png', NULL, NULL);

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `scores`
--

CREATE TABLE `scores` (
  `score_id` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  `game_id` int(11) DEFAULT NULL,
  `score` int(11) NOT NULL,
  `score_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `username` varchar(255) DEFAULT NULL,
  `game` varchar(255) DEFAULT NULL,
  `game_name` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `scores`
--

INSERT INTO `scores` (`score_id`, `player_id`, `game_id`, `score`, `score_date`, `username`, `game`, `game_name`, `created_at`) VALUES
(38, 27, NULL, 88, '2025-06-13 09:37:24', 'yasemin', 'Minecraft', NULL, '2025-06-13 09:37:24');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `tournamentparticipants`
--

CREATE TABLE `tournamentparticipants` (
  `id` int(11) NOT NULL,
  `tournament_id` int(11) NOT NULL,
  `player_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `tournaments`
--

CREATE TABLE `tournaments` (
  `tournament_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `game_id` int(11) NOT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `achievements`
--
ALTER TABLE `achievements`
  ADD PRIMARY KEY (`achievement_id`),
  ADD KEY `game_id` (`game_id`);

--
-- Tablo için indeksler `blocked_users`
--
ALTER TABLE `blocked_users`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `friendships`
--
ALTER TABLE `friendships`
  ADD PRIMARY KEY (`user1`,`user2`);

--
-- Tablo için indeksler `friend_requests`
--
ALTER TABLE `friend_requests`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `games`
--
ALTER TABLE `games`
  ADD PRIMARY KEY (`game_id`);

--
-- Tablo için indeksler `leaderboards`
--
ALTER TABLE `leaderboards`
  ADD PRIMARY KEY (`leaderboard_id`),
  ADD KEY `game_id` (`game_id`),
  ADD KEY `player_id` (`player_id`);

--
-- Tablo için indeksler `levels`
--
ALTER TABLE `levels`
  ADD PRIMARY KEY (`level_id`),
  ADD KEY `game_id` (`game_id`);

--
-- Tablo için indeksler `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `playerachievements`
--
ALTER TABLE `playerachievements`
  ADD PRIMARY KEY (`player_achievement_id`),
  ADD KEY `player_id` (`player_id`),
  ADD KEY `achievement_id` (`achievement_id`);

--
-- Tablo için indeksler `playerlevels`
--
ALTER TABLE `playerlevels`
  ADD PRIMARY KEY (`player_level_id`),
  ADD KEY `player_id` (`player_id`),
  ADD KEY `level_id` (`level_id`);

--
-- Tablo için indeksler `playerprofiles`
--
ALTER TABLE `playerprofiles`
  ADD PRIMARY KEY (`profile_id`),
  ADD KEY `player_id` (`player_id`);

--
-- Tablo için indeksler `players`
--
ALTER TABLE `players`
  ADD PRIMARY KEY (`player_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Tablo için indeksler `scores`
--
ALTER TABLE `scores`
  ADD PRIMARY KEY (`score_id`),
  ADD KEY `player_id` (`player_id`),
  ADD KEY `game_id` (`game_id`);

--
-- Tablo için indeksler `tournamentparticipants`
--
ALTER TABLE `tournamentparticipants`
  ADD PRIMARY KEY (`id`),
  ADD KEY `tournament_id` (`tournament_id`),
  ADD KEY `player_id` (`player_id`);

--
-- Tablo için indeksler `tournaments`
--
ALTER TABLE `tournaments`
  ADD PRIMARY KEY (`tournament_id`),
  ADD KEY `game_id` (`game_id`);

--
-- Dökümü yapılmış tablolar için AUTO_INCREMENT değeri
--

--
-- Tablo için AUTO_INCREMENT değeri `achievements`
--
ALTER TABLE `achievements`
  MODIFY `achievement_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `blocked_users`
--
ALTER TABLE `blocked_users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Tablo için AUTO_INCREMENT değeri `friend_requests`
--
ALTER TABLE `friend_requests`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- Tablo için AUTO_INCREMENT değeri `games`
--
ALTER TABLE `games`
  MODIFY `game_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `leaderboards`
--
ALTER TABLE `leaderboards`
  MODIFY `leaderboard_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `levels`
--
ALTER TABLE `levels`
  MODIFY `level_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `messages`
--
ALTER TABLE `messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=61;

--
-- Tablo için AUTO_INCREMENT değeri `playerachievements`
--
ALTER TABLE `playerachievements`
  MODIFY `player_achievement_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `playerlevels`
--
ALTER TABLE `playerlevels`
  MODIFY `player_level_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `playerprofiles`
--
ALTER TABLE `playerprofiles`
  MODIFY `profile_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `players`
--
ALTER TABLE `players`
  MODIFY `player_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- Tablo için AUTO_INCREMENT değeri `scores`
--
ALTER TABLE `scores`
  MODIFY `score_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- Tablo için AUTO_INCREMENT değeri `tournamentparticipants`
--
ALTER TABLE `tournamentparticipants`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `tournaments`
--
ALTER TABLE `tournaments`
  MODIFY `tournament_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Dökümü yapılmış tablolar için kısıtlamalar
--

--
-- Tablo kısıtlamaları `achievements`
--
ALTER TABLE `achievements`
  ADD CONSTRAINT `achievements_ibfk_1` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE;

--
-- Tablo kısıtlamaları `leaderboards`
--
ALTER TABLE `leaderboards`
  ADD CONSTRAINT `leaderboards_ibfk_1` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `leaderboards_ibfk_2` FOREIGN KEY (`player_id`) REFERENCES `players` (`player_id`) ON DELETE CASCADE;

--
-- Tablo kısıtlamaları `levels`
--
ALTER TABLE `levels`
  ADD CONSTRAINT `levels_ibfk_1` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE;

--
-- Tablo kısıtlamaları `playerachievements`
--
ALTER TABLE `playerachievements`
  ADD CONSTRAINT `playerachievements_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`player_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `playerachievements_ibfk_2` FOREIGN KEY (`achievement_id`) REFERENCES `achievements` (`achievement_id`) ON DELETE CASCADE;

--
-- Tablo kısıtlamaları `playerlevels`
--
ALTER TABLE `playerlevels`
  ADD CONSTRAINT `playerlevels_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`player_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `playerlevels_ibfk_2` FOREIGN KEY (`level_id`) REFERENCES `levels` (`level_id`) ON DELETE CASCADE;

--
-- Tablo kısıtlamaları `playerprofiles`
--
ALTER TABLE `playerprofiles`
  ADD CONSTRAINT `playerprofiles_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`player_id`) ON DELETE CASCADE;

--
-- Tablo kısıtlamaları `scores`
--
ALTER TABLE `scores`
  ADD CONSTRAINT `scores_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`player_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `scores_ibfk_2` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE;

--
-- Tablo kısıtlamaları `tournamentparticipants`
--
ALTER TABLE `tournamentparticipants`
  ADD CONSTRAINT `tournamentparticipants_ibfk_1` FOREIGN KEY (`tournament_id`) REFERENCES `tournaments` (`tournament_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `tournamentparticipants_ibfk_2` FOREIGN KEY (`player_id`) REFERENCES `players` (`player_id`) ON DELETE CASCADE;

--
-- Tablo kısıtlamaları `tournaments`
--
ALTER TABLE `tournaments`
  ADD CONSTRAINT `tournaments_ibfk_1` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
