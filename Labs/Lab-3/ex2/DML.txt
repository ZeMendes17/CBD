-- User Inserts

INSERT INTO videos.user (username, name, email, registryTime) VALUES ('john_doe', 'John Doe', 'john@example.com', toTimestamp(now()));
INSERT INTO videos.user (username, name, email, registryTime) VALUES ('jane_smith', 'Jane Smith', 'jane@example.com', toTimestamp(now()));
INSERT INTO videos.user (username, name, email, registryTime) VALUES ('alex_123', 'Alex Johnson', 'alex@example.com', toTimestamp(now()));
INSERT INTO videos.user (username, name, email, registryTime) VALUES ('emily_wilson', 'Emily Wilson', 'emily@example.com', toTimestamp(now()));
INSERT INTO videos.user (username, name, email, registryTime) VALUES ('michael_jones', 'Michael Jones', 'michael@example.com', toTimestamp(now()));
INSERT INTO videos.user (username, name, email, registryTime) VALUES ('sara_brown', 'Sara Brown', 'sara@example.com', toTimestamp(now()));
INSERT INTO videos.user (username, name, email, registryTime) VALUES ('david_roberts', 'David Roberts', 'david@example.com', toTimestamp(now()));
INSERT INTO videos.user (username, name, email, registryTime) VALUES ('olivia_smith', 'Olivia Smith', 'olivia@example.com', toTimestamp(now()));
INSERT INTO videos.user (username, name, email, registryTime) VALUES ('sam_wilson', 'Sam Wilson', 'sam@example.com', toTimestamp(now()));
INSERT INTO videos.user (username, name, email, registryTime) VALUES ('lucas_baker', 'Lucas Baker', 'lucas@example.com', toTimestamp(now()));

-- Video Inserts

INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (1, 'john_doe', 'First Video by John', 'My first video', {'vlog', 'fun'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (2, 'john_doe', 'Cooking Tutorial', 'Learn how to cook a delicious meal', {'cooking', 'tutorial'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (3, 'jane_smith', 'Fitness Tips', 'Get fit with these workout tips', {'fitness', 'health'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (4, 'jane_smith', 'Travel Vlog', 'Exploring new places around the world', {'travel', 'vlog'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (5, 'alex_123', 'Gaming Highlights', 'Exciting gaming moments', {'gaming', 'highlights'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (6, 'alex_123', 'Programming Tutorial', 'Learn programming concepts', {'programming', 'tutorial'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (7, 'emily_wilson', 'Fashion Tips', 'Latest fashion trends and tips', {'fashion', 'style'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (8, 'emily_wilson', 'Art Showcase', 'Showcasing amazing artworks', {'art', 'showcase'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (9, 'michael_jones', 'Science Experiments', 'Fun and interesting science experiments', {'science', 'experiments'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (10, 'michael_jones', 'Pet Care Tips', 'Taking care of your pets', {'pets', 'care'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (11, 'sara_brown', 'Aveiro Vlog', 'Exploring Aveiro', {'Aveiro', 'vlog'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (12, 'sara_brown', 'Ovos Moles', 'Tasting traditional Aveiro food', {'food', 'Aveiro'}, toTimestamp(now()));

-- Comment Inserts

INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (1, 'jane_smith', 1, 'Great video!', toTimestamp(now()));
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (2, 'alex_123', 1, 'Lowkey Bad.', toTimestamp(now()));
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (3, 'emily_wilson', 2, 'Awesome video!', toTimestamp(now()));
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (4, 'michael_jones', 3, 'Amazing video!', toTimestamp(now()));
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (5, 'sara_brown', 4, 'Cool video!', toTimestamp(now()));
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (6, 'david_roberts', 6, 'Meh', toTimestamp(now()));
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (7, 'jane_smith', 7, 'Nice video!', toTimestamp(now()));
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (8, 'john_doe', 8, 'My Art is Better!', toTimestamp(now()));
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (9, 'alex_123', 10, 'Coooool', toTimestamp(now()));
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (10, 'emily_wilson', 4, 'Great!', toTimestamp(now()));
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (11, 'john_doe', 4, 'Mid', toTimestamp(now()));

INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (1, 'jane_smith', 1, 'Great video!', toTimestamp(now()));
INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (2, 'alex_123', 1, 'Lowkey Bad.', toTimestamp(now()));
INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (3, 'emily_wilson', 2, 'Awesome video!', toTimestamp(now()));
INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (4, 'michael_jones', 3, 'Amazing video!', toTimestamp(now()));
INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (5, 'sara_brown', 4, 'Cool video!', toTimestamp(now()));
INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (6, 'david_roberts', 6, 'Meh', toTimestamp(now()));
INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (7, 'jane_smith', 7, 'Nice video!', toTimestamp(now()));
INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (8, 'john_doe', 8, 'My Art is Better!', toTimestamp(now()));
INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (9, 'alex_123', 10, 'Coooool', toTimestamp(now()));
INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (10, 'emily_wilson', 4, 'Great!', toTimestamp(now()));
INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (11, 'john_doe', 4, 'Mid', toTimestamp(now()));

-- Follower Inserts

INSERT INTO videos.follower (videoId, users) VALUES (1, {'jane_smith', 'alex_123', 'emily_wilson'});
INSERT INTO videos.follower (videoId, users) VALUES (2, {'emily_wilson'});
INSERT INTO videos.follower (videoId, users) VALUES (3, {'john_doe', 'olivia_smith', 'sam_wilson'});
INSERT INTO videos.follower (videoId, users) VALUES (4, {'alex_123', 'david_roberts', 'sara_brown'});
INSERT INTO videos.follower (videoId, users) VALUES (5, {'john_doe', 'emily_wilson', 'sam_wilson'});
INSERT INTO videos.follower (videoId, users) VALUES (6, {'lucas_baker', 'olivia_smith', 'david_roberts'});
INSERT INTO videos.follower (videoId, users) VALUES (7, {'john_doe', 'jane_smith'});
INSERT INTO videos.follower (videoId, users) VALUES (8, {'michael_jones', 'sara_brown'});
INSERT INTO videos.follower (videoId, users) VALUES (9, {'david_roberts'});
INSERT INTO videos.follower (videoId, users) VALUES (10, {'john_doe', 'jane_smith', 'alex_123', 'sara_brown'});

-- Event Inserts

INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (1, 'jane_smith', 'play', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (1, 'jane_smith', 'pause', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (8, 'alex_123', 'play', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (8, 'alex_123', 'stop', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (3, 'emily_wilson', 'play', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (3, 'emily_wilson', 'pause', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (3, 'emily_wilson', 'stop', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (3, 'emily_wilson', 'play', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (3, 'emily_wilson', 'stop', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (5, 'sara_brown', 'play', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (5, 'sara_brown', 'pause', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (5, 'sara_brown', 'play', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (5, 'sara_brown', 'stop', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (6, 'david_roberts', 'play', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (6, 'david_roberts', 'pause', toTimestamp(now()), toTimestamp(now()));

-- Rating Inserts

INSERT INTO videos.rating (id, videoId, rate) VALUES (1, 1, 5);
INSERT INTO videos.rating (id, videoId, rate) VALUES (2, 1, 4);
INSERT INTO videos.rating (id, videoId, rate) VALUES (3, 2, 4);
INSERT INTO videos.rating (id, videoId, rate) VALUES (4, 2, 3);
INSERT INTO videos.rating (id, videoId, rate) VALUES (5, 3, 4);
INSERT INTO videos.rating (id, videoId, rate) VALUES (6, 3, 5);
INSERT INTO videos.rating (id, videoId, rate) VALUES (7, 9, 3);
INSERT INTO videos.rating (id, videoId, rate) VALUES (8, 9, 2);
INSERT INTO videos.rating (id, videoId, rate) VALUES (9, 6, 5);
INSERT INTO videos.rating (id, videoId, rate) VALUES (10, 5, 4);
INSERT INTO videos.rating (id, videoId, rate) VALUES (11, 1, 3);
INSERT INTO videos.rating (id, videoId, rate) VALUES (12, 10, 5);
INSERT INTO videos.rating (id, videoId, rate) VALUES (13, 10, 4);
INSERT INTO videos.rating (id, videoId, rate) VALUES (14, 7, 3);
INSERT INTO videos.rating (id, videoId, rate) VALUES (15, 6, 4);