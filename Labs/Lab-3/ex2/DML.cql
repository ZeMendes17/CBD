-- User Inserts

INSERT INTO user (username, name, email, registTime) VALUES ('john_doe', 'John Doe', 'john@example.com', '2023-11-15 08:00:00+0000');
INSERT INTO user (username, name, email, registTime) VALUES ('jane_smith', 'Jane Smith', 'jane@example.com', '2023-11-14 10:30:00+0000');
INSERT INTO user (username, name, email, registTime) VALUES ('alex_123', 'Alex Johnson', 'alex@example.com', '2023-11-13 15:45:00+0000');
INSERT INTO user (username, name, email, registTime) VALUES ('emily_wilson', 'Emily Wilson', 'emily@example.com', '2023-11-12 11:20:00+0000');
INSERT INTO user (username, name, email, registTime) VALUES ('michael_jones', 'Michael Jones', 'michael@example.com', '2023-11-11 09:15:00+0000');
INSERT INTO user (username, name, email, registTime) VALUES ('sara_brown', 'Sara Brown', 'sara@example.com', '2023-11-10 14:00:00+0000');
INSERT INTO user (username, name, email, registTime) VALUES ('david_roberts', 'David Roberts', 'david@example.com', '2023-11-09 17:45:00+0000');
INSERT INTO user (username, name, email, registTime) VALUES ('olivia_smith', 'Olivia Smith', 'olivia@example.com', '2023-11-08 20:30:00+0000');
INSERT INTO user (username, name, email, registTime) VALUES ('sam_wilson', 'Sam Wilson', 'sam@example.com', '2023-11-07 12:10:00+0000');
INSERT INTO user (username, name, email, registTime) VALUES ('lucas_baker', 'Lucas Baker', 'lucas@example.com', '2023-11-06 16:20:00+0000');

-- Video Inserts

INSERT INTO video (id, author, name, description, tags, uploadTime) VALUES (1, 'john_doe', 'First Video by John', 'My first video', {'vlog', 'fun'}, '2023-11-15 08:00:00+0000');
INSERT INTO video (id, author, name, description, tags, uploadTime) VALUES (2, 'john_doe', 'Cooking Tutorial', 'Learn how to cook a delicious meal', {'cooking', 'tutorial'}, '2023-11-14 10:30:00+0000');
INSERT INTO video (id, author, name, description, tags, uploadTime) VALUES (3, 'jane_smith', 'Fitness Tips', 'Get fit with these workout tips', {'fitness', 'health'}, '2023-11-13 15:45:00+0000');
INSERT INTO video (id, author, name, description, tags, uploadTime) VALUES (4, 'jane_smith', 'Travel Vlog', 'Exploring new places around the world', {'travel', 'vlog'}, '2023-11-12 11:20:00+0000');
INSERT INTO video (id, author, name, description, tags, uploadTime) VALUES (5, 'alex_123', 'Gaming Highlights', 'Exciting gaming moments', {'gaming', 'highlights'}, '2023-11-11 09:15:00+0000');
INSERT INTO video (id, author, name, description, tags, uploadTime) VALUES (6, 'alex_123', 'Programming Tutorial', 'Learn programming concepts', {'programming', 'tutorial'}, '2023-11-10 14:00:00+0000');
INSERT INTO video (id, author, name, description, tags, uploadTime) VALUES (7, 'emily_wilson', 'Fashion Tips', 'Latest fashion trends and tips', {'fashion', 'style'}, '2023-11-09 17:45:00+0000');
INSERT INTO video (id, author, name, description, tags, uploadTime) VALUES (8, 'emily_wilson', 'Art Showcase', 'Showcasing amazing artworks', {'art', 'showcase'}, '2023-11-08 20:30:00+0000');
INSERT INTO video (id, author, name, description, tags, uploadTime) VALUES (9, 'michael_jones', 'Science Experiments', 'Fun and interesting science experiments', {'science', 'experiments'}, '2023-11-07 12:10:00+0000');
INSERT INTO video (id, author, name, description, tags, uploadTime) VALUES (10, 'michael_jones', 'Pet Care Tips', 'Taking care of your pets', {'pets', 'care'}, '2023-11-06 16:20:00+0000');
INSERT INTO video (id, author, name, description, tags, uploadTime) VALUES (11, 'sara_brown', 'Aveiro Vlog', 'Exploring Aveiro', {'Aveiro', 'vlog'}, '2023-11-05 18:30:00+0000');
INSERT INTO video (id, author, name, description, tags, uploadTime) VALUES (12, 'sara_brown', 'Ovos Moles', 'Tasting traditional Aveiro food', {'food', 'Aveiro'}, '2023-11-04 20:40:00+0000');

-- Comment Inserts

INSERT INTO commentUser (id, author, videoId, content, postTime) VALUES (1, 'jane_smith', 1, 'Great video!', '2023-11-15 08:05:00+0000');
INSERT INTO commentUser (id, author, videoId, content, postTime) VALUES (2, 'alex_123', 1, 'Nice video!', '2023-11-15 08:10:00+0000');
INSERT INTO commentUser (id, author, videoId, content, postTime) VALUES (3, 'emily_wilson', 2, 'Awesome video!', '2023-11-15 08:15:00+0000');
INSERT INTO commentUser (id, author, videoId, content, postTime) VALUES (4, 'michael_jones', 3, 'Amazing video!', '2023-11-15 08:20:00+0000');
INSERT INTO commentUser (id, author, videoId, content, postTime) VALUES (5, 'sara_brown', 4, 'Cool video!', '2023-11-15 08:25:00+0000');
INSERT INTO commentUser (id, author, videoId, content, postTime) VALUES (6, 'david_roberts', 6, 'Meh', '2023-11-15 08:30:00+0000');
INSERT INTO commentUser (id, author, videoId, content, postTime) VALUES (7, 'jane_smith', 7, 'Nice video!', '2023-11-15 08:35:00+0000');
INSERT INTO commentUser (id, author, videoId, content, postTime) VALUES (8, 'john_doe', 8, 'Awesome video!', '2023-11-15 08:40:00+0000');
INSERT INTO commentUser (id, author, videoId, content, postTime) VALUES (9, 'alex_123', 10, 'Coooool', '2023-11-15 08:45:00+0000');
INSERT INTO commentUser (id, author, videoId, content, postTime) VALUES (10, 'emily_wilson', 4, 'Great!', '2023-11-15 08:50:00+0000');
INSERT INTO commentUser (id, author, videoId, content, postTime) VALUES (11, 'john_doe', 4, 'Mid', '2023-11-13 08:55:00+0000');

INSERT INTO commentVideo (id, author, videoId, content, postTime) VALUES (1, 'jane_smith', 1, 'Great video!', '2023-11-15 08:05:00+0000');
INSERT INTO commentVideo (id, author, videoId, content, postTime) VALUES (2, 'alex_123', 1, 'Nice video!', '2023-11-15 08:10:00+0000');
INSERT INTO commentVideo (id, author, videoId, content, postTime) VALUES (3, 'emily_wilson', 2, 'Awesome video!', '2023-11-15 08:15:00+0000');
INSERT INTO commentVideo (id, author, videoId, content, postTime) VALUES (4, 'michael_jones', 3, 'Amazing video!', '2023-11-15 08:20:00+0000');
INSERT INTO commentVideo (id, author, videoId, content, postTime) VALUES (5, 'sara_brown', 4, 'Cool video!', '2023-11-15 08:25:00+0000');
INSERT INTO commentVideo (id, author, videoId, content, postTime) VALUES (6, 'david_roberts', 6, 'Meh', '2023-11-15 08:30:00+0000');
INSERT INTO commentVideo (id, author, videoId, content, postTime) VALUES (7, 'jane_smith', 7, 'Nice video!', '2023-11-15 08:35:00+0000');
INSERT INTO commentVideo (id, author, videoId, content, postTime) VALUES (8, 'john_doe', 8, 'Awesome video!', '2023-11-15 08:40:00+0000');
INSERT INTO commentVideo (id, author, videoId, content, postTime) VALUES (9, 'alex_123', 10, 'Coooool', '2023-11-15 08:45:00+0000');
INSERT INTO commentVideo (id, author, videoId, content, postTime) VALUES (10, 'emily_wilson', 4, 'Great!', '2023-11-15 08:50:00+0000');
INSERT INTO commentVideo (id, author, videoId, content, postTime) VALUES (11, 'john_doe', 4, 'Mid', '2023-11-13 08:55:00+0000');


-- Follower Inserts

INSERT INTO follower (videoId, username) VALUES (1, 'jane_smith');
INSERT INTO follower (videoId, username) VALUES (1, 'alex_123');
INSERT INTO follower (videoId, username) VALUES (1, 'emily_wilson');
INSERT INTO follower (videoId, username) VALUES (2, 'emily_wilson');
INSERT INTO follower (videoId, username) VALUES (2, 'michael_jones');
INSERT INTO follower (videoId, username) VALUES (2, 'sara_brown');
INSERT INTO follower (videoId, username) VALUES (3, 'john_doe');
INSERT INTO follower (videoId, username) VALUES (3, 'alex_123');
INSERT INTO follower (videoId, username) VALUES (3, 'emily_wilson');
INSERT INTO follower (videoId, username) VALUES (4, 'john_doe');
INSERT INTO follower (videoId, username) VALUES (4, 'lucas_baker');
INSERT INTO follower (videoId, username) VALUES (5, 'jane_smith');
INSERT INTO follower (videoId, username) VALUES (5, 'olivia_smith');
INSERT INTO follower (videoId, username) VALUES (6, 'lucas_baker');
INSERT INTO follower (videoId, username) VALUES (7, 'sara_brown');
INSERT INTO follower (videoId, username) VALUES (7, 'david_roberts');
INSERT INTO follower (videoId, username) VALUES (8, 'david_roberts');
INSERT INTO follower (videoId, username) VALUES (9, 'olivia_smith');
INSERT INTO follower (videoId, username) VALUES (10, 'sam_wilson');
INSERT INTO follower (videoId, username) VALUES (10, 'lucas_baker');
INSERT INTO follower (videoId, username) VALUES (10, 'sara_brown');

-- Event Inserts

INSERT INTO event (videoId, username, eventType, eventTime, moment) VALUES (1, 'jane_smith', 'play', '2023-11-15 08:05:00+0000', 0);
INSERT INTO event (videoId, username, eventType, eventTime, moment) VALUES (1, 'jane_smith', 'pause', '2023-11-15 08:06:00+0000', 30);
INSERT INTO event (videoId, username, eventType, eventTime, moment) VALUES (8, 'alex_123', 'play', '2023-11-15 08:10:00+0000', 0);
INSERT INTO event (videoId, username, eventType, eventTime, moment) VALUES (8, 'alex_123', 'stop', '2023-11-15 08:20:00+0000', 300);
INSERT INTO event (videoId, username, eventType, eventTime, moment) VALUES (3, 'emily_wilson', 'play', '2023-11-15 08:15:00+0000', 30);
INSERT INTO event (videoId, username, eventType, eventTime, moment) VALUES (3, 'emily_wilson', 'pause', '2023-11-15 08:20:00+0000', 60);
INSERT INTO event (videoId, username, eventType, eventTime, moment) VALUES (3, 'emily_wilson', 'stop', '2023-11-15 08:25:00+0000', 60);
INSERT INTO event (videoId, username, eventType, eventTime, moment) VALUES (3, 'emily_wilson', 'play', '2023-11-15 08:27:00+0000', 60);
INSERT INTO event (videoId, username, eventType, eventTime, moment) VALUES (3, 'emily_wilson', 'stop', '2023-11-15 08:30:00+0000', 90);
INSERT INTO event (videoId, username, eventType, eventTime, moment) VALUES (5, 'sara_brown', 'play', '2023-11-15 08:25:00+0000', 60);
INSERT INTO event (videoId, username, eventType, eventTime, moment) VALUES (5, 'sara_brown', 'pause', '2023-11-15 08:30:00+0000', 75);
INSERT INTO event (videoId, username, eventType, eventTime, moment) VALUES (5, 'sara_brown', 'play', '2023-11-15 08:32:00+0000', 75);
INSERT INTO event (videoId, username, eventType, eventTime, moment) VALUES (5, 'sara_brown', 'stop', '2023-11-15 08:36:00+0000', 100);
INSERT INTO event (videoId, username, eventType, eventTime, moment) VALUES (6, 'david_roberts', 'play', '2023-11-15 08:40:00+0000', 120);
INSERT INTO event (videoId, username, eventType, eventTime, moment) VALUES (6, 'david_roberts', 'pause', '2023-11-15 08:45:00+0000', 135);

-- Rating Inserts

INSERT INTO rating (id, videoId, rating) VALUES (1, 1, 5);
INSERT INTO rating (id, videoId, rating) VALUES (2, 1, 4);
INSERT INTO rating (id, videoId, rating) VALUES (3, 2, 4);
INSERT INTO rating (id, videoId, rating) VALUES (4, 2, 3);
INSERT INTO rating (id, videoId, rating) VALUES (5, 3, 4);
INSERT INTO rating (id, videoId, rating) VALUES (6, 3, 5);
INSERT INTO rating (id, videoId, rating) VALUES (7, 9, 3);
INSERT INTO rating (id, videoId, rating) VALUES (8, 9, 2);
INSERT INTO rating (id, videoId, rating) VALUES (9, 6, 5);
INSERT INTO rating (id, videoId, rating) VALUES (10, 5, 4);