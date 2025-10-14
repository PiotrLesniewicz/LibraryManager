INSERT INTO address (city, street, number, post_code) VALUES
('City1', 'Street1', '1A', '12345'),
('City2', 'Street2', '2B', '23456'),
('City3', 'Street3', '3C', '34567'),
('City4', 'Street4', '4D', '45678'),
('City5', 'Street5', '5E', '56789'),
('City6', 'Street6', '6F', '67890'),
('City7', 'Street7', '7G', '78901'),
('City8', 'Street8', '8H', '89012');

INSERT INTO librarian (role, hire_date) VALUES
('ADMIN', '2022-01-01'), --user5
('TECHNIC', '2023-05-15'), --user8
('ASSISTANT', '2021-09-20'); --user12

INSERT INTO users (user_name, name, surname, email, password, phone_number, membership_date, role, address_id, librarian_id) VALUES
('user1', 'John', 'Doe', 'john.doe1@example.com', '$2a$10$K4OZD/pA5dL0Hcd01keldOU6vmI4nkI3AaYLBEa2qHf42NMx39trW', '1234567890', '2025-03-18', 'USER', 1, null),
('user2', 'Jane', 'Smith', 'jane.smith2@example.com', '$2a$10$6IGNfzgi.kMMlEelTPsxeuKykQDYoV8T/9iqUmnfwCOrAMYXvId7e', '1234567891', '2025-03-18', 'USER', 2, null),
('user3', 'Alice', 'Johnson', 'alice.johnson3@example.com', '$2a$10$Oob1L6LkoV7MyFx1CjApC.I6fRF9lfO7yCbVC2RpebA0SK1WgA0Xi', '1234567892', '2025-03-18', 'USER', 2, null),
('user4', 'Bob', 'Williams', 'bob.williams4@example.com', '$2a$10$aS95/Y39aTjL.QXFhzcneOt2GLLe.4KR6kJTEP8BJg.sWfSpAb.0W', '1234567893', '2025-03-18', 'USER', 3, null),
('user5', 'Charlie', 'Brown', 'charlie.brown5@example.com', '$2a$10$rSJ8MJceWV84YUc67btBpeb/FeT/0xTxxwNZDJ67Rh8HQ6GrGwOx2', '1234567894', '2025-03-18', 'LIBRARIAN', 4, 1),
('user6', 'David', 'Davis', 'david.davis6@example.com', '$2a$10$rua86rbh7b.ZjgZLm2dzlO7KZivyovw2XI98zVIenekwjk8307gtq', '1234567895', '2025-03-18', 'USER', 2, null),
('user7', 'Emily', 'Miller', 'emily.miller7@example.com', '$2a$10$kzXZg1dxHHF1fDE0jdIea.3c.DU.aqHO3IuKq12GDCEJ1HlG3JpKO', '1234567896', '2025-03-18', 'USER', 7, null),
('user8', 'Frank', 'Wilson', 'frank.wilson8@example.com', '$2a$10$nVGz3/iTrPJNBnKn8Syw1.3YwiQXM2QETsojnlUcyLH1pe3/eNJaC', '1234567897', '2025-03-18', 'LIBRARIAN', 8, 2),
('user9', 'Grace', 'Moore', 'grace.moore9@example.com', '$2a$10$zUIhIjjI3ALQ0KwkGI/9bOPSVx4dG9T4AOW8x2qQBg5yq6j9BZ/NC', '1234567898', '2025-03-18', 'USER', 5, null),
('user10', 'Henry', 'Taylor', 'henry.taylor10@example.com', '$2a$10$bRG9DrqdPSYG04Aj29Dy.Oezsxs1zUwaSiFT19BAVXemAggfiT.N6', '1234567899', '2025-03-18', 'USER', 6, null),
('user11', 'Isabel', 'Anderson', 'isabel.anderson11@example.com', '$2a$10$Y.1hMfg9LTIu0xeMIkjtCe9JLUI7CZUGu9vyUMbKgB17yNk/ntqly', '1234567800', '2025-03-18', 'USER', 8, null),
('user12', 'Jack', 'Thomas', 'jack.thomas12@example.com', '$2a$10$rja.NFEgYRLdP7zatSCxx.KQ1CpW10OHIudAn71xTosEagTIowkKm', '1234567801', '2025-03-18', 'LIBRARIAN', 8, 3),
('user13', 'Kimberly', 'Jackson', 'kimberly.jackson13@example.com', '$2a$10$m3pyfknhYBRds0HUSP7tNOiIXGb8fpR/.PJmBxpsB/jRwZsZVKfuG', '1234567802', '2025-03-18', 'USER', 2, null),
('user14', 'Liam', 'White', 'liam.white14@example.com', '$2a$10$KzWfn8QzPx52TjqlQP8J4eTcDO7UmwJY.0VBsB12I0LdENHLF68EO', '1234567803', '2025-03-18', 'USER', 3, null),
('user15', 'Mia', 'Harris', 'mia.harris15@example.com', '$2a$10$0ntS0DBxPqaJbqJm0POEke/9H0u1hfYG7SJ.ZE2bu7TvsCvpXhOYy', '1234567804', '2025-03-18', 'USER', 1, null),
('user16', 'Noah', 'Clark', 'noah.clark16@example.com', '$2a$10$/x8p6SWj.jrZDzt80hsbU.qmAdnqZVj8VIbulvxyQtIftn8nhG93.', '1234567805', '2025-03-18', 'USER', 4, null),
('user17', 'Olivia', 'Lewis', 'olivia.lewis17@example.com', '$2a$10$ioA/nZYGgdkln0j4CshJium.jwhqKXn.XgBaxJXxXMkUD4h3qOSJq', '1234567806', '2025-03-18', 'USER', 2, null),
('user18', 'Peter', 'Walker', 'peter.walker18@example.com', '$2a$10$nOb0SQ.O2GgxMXiFBeo7QOrcvUoWBwbudMVI5VNazWFMURCYIvSWa', '1234567807', '2025-03-18', 'USER', 7, null),
('user19', 'Quinn', 'Hall', 'quinn.hall19@example.com', '$2a$10$xbUhU98xheN46m8CZLOd.OSg7fAm2oTQub5eG83iS9h17u663f30q', '1234567808', '2025-03-18', 'USER', 8, null),
('user20', 'Rachel', 'Allen', 'rachel.allen20@example.com', '$2a$10$CqeEDqCar7KTWNFJwnVOweHkUgkQL9fH4GxRHWW/Q3zp4kr/vennC', '1234567809', '2025-03-18', 'USER', 2, null);

INSERT INTO books (title, isbn) VALUES
('Book Title 1', '978-1234567890'),
('Book Title 2', '978-1234567891'),
('Book Title 3', '978-1234567892'),
('Book Title 4', '978-1234567893'),
('Book Title 5', '978-1234567894'),
('Book Title 6', '978-1234567895'),
('Book Title 7', '978-1234567896'),
('Book Title 8', '978-1234567897'),
('Book Title 9', '978-1234567898'),
('Book Title 10', '978-1234567899'),
('Book Title 11', '978-1234567900'),
('Book Title 12', '978-1234567901'),
('Book Title 13', '978-1234567902'),
('Book Title 14', '978-1234567903'),
('Book Title 15', '978-1234567904'),
('Book Title 16', '978-1234567905'),
('Book Title 17', '978-1234567906'),
('Book Title 18', '978-1234567907'),
('Book Title 19', '978-1234567908'),
('Book Title 20', '978-1234567909');

INSERT INTO categories (name) VALUES
('Fiction'),
('Non-fiction'),
('Science'),
('History'),
('Biography'),
('Fantasy'),
('Mystery'),
('Romance'),
('Horror'),
('Children');

INSERT INTO books_categories (book_id, category_id) VALUES
(1, 1), -- Book 1, Fiction
(2, 2), -- Book 2, Non-fiction
(3, 3), -- Book 3, Science
(4, 4), -- Book 4, History
(5, 5), -- Book 5, Biography
(6, 6), -- Book 6, Fantasy
(7, 7), -- Book 7, Mystery
(8, 8), -- Book 8, Romance
(9, 9), -- Book 9, Horror
(10, 10), -- Book 10, Children
(11, 1), -- Book 11, Fiction
(12, 2), -- Book 12, Non-fiction
(13, 3), -- Book 13, Science
(14, 4), -- Book 14, History
(15, 5), -- Book 15, Biography
(16, 6), -- Book 16, Fantasy
(17, 7), -- Book 17, Mystery
(18, 8), -- Book 18, Romance
(19, 9), -- Book 19, Horror
(20, 10); -- Book 20, Children

INSERT INTO authors (name, surname, biography) VALUES
('John', 'Doe', 'John Doe is an acclaimed author known for his work in science fiction and fantasy genres.'),
('Jane', 'Smith', 'Jane Smith writes historical fiction and is passionate about bringing past events to life through her novels.'),
('Alice', 'Johnson', 'Alice Johnson is a mystery novel author with a deep interest in psychological thrillers.'),
('Bob', 'Williams', 'Bob Williams is a non-fiction writer who focuses on technology and business.'),
('Charlie', 'Brown', 'Charlie Brown is a renowned author of children books, with a creative and imaginative writing style.'),
('David', 'Davis', 'David Davis is an author of biographies, known for his detailed and empathetic approach to real-life stories.'),
('Emily', 'Miller', 'Emily Miller writes romance novels, weaving tales of love and relationships in contemporary settings.'),
('Frank', 'Wilson', 'Frank Wilson is a well-known author in the horror genre, famous for his chilling and suspenseful storytelling.'),
('Grace', 'Moore', 'Grace Moore is a prolific author of fantasy novels, known for building intricate and magical worlds.'),
('Henry', 'Taylor', 'Henry Taylor writes science fiction, exploring the future of humanity and technological advancements.');

INSERT INTO books_authors (book_id, author_id) VALUES
(1, 1), -- Book 1, Author 1 (John Doe)
(2, 2), -- Book 2, Author 2 (Jane Smith)
(3, 3), -- Book 3, Author 3 (Alice Johnson)
(4, 4), -- Book 4, Author 4 (Bob Williams)
(5, 5), -- Book 5, Author 5 (Charlie Brown)
(6, 6), -- Book 6, Author 6 (David Davis)
(7, 7), -- Book 7, Author 7 (Emily Miller)
(8, 8), -- Book 8, Author 8 (Frank Wilson)
(9, 9), -- Book 9, Author 9 (Grace Moore)
(10, 10), -- Book 10, Author 10 (Henry Taylor)
(11, 1), -- Book 11, Author 1 (John Doe)
(12, 2), -- Book 12, Author 2 (Jane Smith)
(13, 3), -- Book 13, Author 3 (Alice Johnson)
(14, 4), -- Book 14, Author 4 (Bob Williams)
(15, 5), -- Book 15, Author 5 (Charlie Brown)
(16, 6), -- Book 16, Author 6 (David Davis)
(17, 7), -- Book 17, Author 7 (Emily Miller)
(18, 8), -- Book 18, Author 8 (Frank Wilson)
(19, 9), -- Book 19, Author 9 (Grace Moore)
(20, 10); -- Book 20, Author 10 (Henry Taylor)

INSERT INTO publishers (name, website) VALUES
('Penguin Random House', 'https://www.penguinrandomhouse.com'),
('HarperCollins', 'https://www.harpercollins.com'),
('Simon & Schuster', 'https://www.simonandschuster.com'),
('Hachette Book Group', null),
('Macmillan', 'https://us.macmillan.com');

INSERT INTO books_publishers (book_id, publisher_id) VALUES
(1, 1), -- Book 1, Publisher 1 (Penguin Random House)
(2, 2), -- Book 2, Publisher 2 (HarperCollins)
(3, 3), -- Book 3, Publisher 3 (Simon & Schuster)
(4, 4), -- Book 4, Publisher 4 (Hachette Book Group)
(5, 5), -- Book 5, Publisher 5 (Macmillan)
(6, 1), -- Book 6, Publisher 1 (Penguin Random House)
(7, 2), -- Book 7, Publisher 2 (HarperCollins)
(8, 3), -- Book 8, Publisher 3 (Simon & Schuster)
(9, 4), -- Book 9, Publisher 4 (Hachette Book Group)
(10, 5), -- Book 10, Publisher 5 (Macmillan)
(11, 1), -- Book 11, Publisher 1 (Penguin Random House)
(12, 2), -- Book 12, Publisher 2 (HarperCollins)
(13, 3), -- Book 13, Publisher 3 (Simon & Schuster)
(14, 4), -- Book 14, Publisher 4 (Hachette Book Group)
(15, 5), -- Book 15, Publisher 5 (Macmillan)
(16, 1), -- Book 16, Publisher 1 (Penguin Random House)
(17, 2), -- Book 17, Publisher 2 (HarperCollins)
(18, 3), -- Book 18, Publisher 3 (Simon & Schuster)
(19, 4), -- Book 19, Publisher 4 (Hachette Book Group)
(20, 5); -- Book 20, Publisher 5 (Macmillan)

INSERT INTO opinions (book_id, user_id, rating, comment, create_date) VALUES
(1, 1, 5, 'Excellent book, highly recommended!', '2025-03-18'),
(2, 2, 4, 'Great read, but a bit slow at times.', '2025-03-17'),
(3, 3, 3, 'It was okay, not my favorite genre.', '2025-03-16'),
(4, 4, 5, 'Loved it, couldnt put it down!', '2025-03-15'),
(5, 5, 2, 'Not my cup of tea, the plot was weak.', '2025-03-14'),
(6, 6, 4, 'Very engaging, with an unexpected twist.', '2025-03-13'),
(7, 7, 5, 'A masterpiece, I will definitely read it again.', '2025-03-12'),
(8, 8, 3, 'It had potential, but it felt rushed.', '2025-03-11'),
(9, 9, 1, 'Didnt enjoy it at all, didnt finish the book.', '2025-03-10'),
(10, 10, 4, 'A solid book, some parts could be improved.', '2025-03-09'),
(11, 1, 3, 'An average read, but worth a try.', '2025-03-08'),
(12, 2, 5, 'Truly amazing, couldnt put it down!', '2025-03-07'),
(13, 3, 2, 'Not engaging, I couldnt connect with the characters.', '2025-03-06'),
(14, 4, 4, 'Enjoyed the story, though it was a bit predictable.', '2025-03-05'),
(15, 5, 1, 'Very disappointing, didnt live up to the hype.', '2025-03-04'),
(16, 6, 5, 'Fantastic, highly recommend to anyone who loves thrillers.', '2025-03-03'),
(17, 7, 4, 'Pretty good, but I expected a bit more depth.', '2025-03-02'),
(18, 8, 2, 'The writing style didnt appeal to me.', '2025-03-01'),
(19, 9, 3, 'It was fine, but I was hoping for a more exciting plot.', '2025-02-28'),
(20, 10, 4, 'A very enjoyable book, some moments were unforgettable.', '2025-02-27'),
(1, 2, 5, 'Absolutely loved this book, one of my favorites.', '2025-02-26'),
(2, 3, 3, 'Its an okay book, not my favorite, but worth a read.', '2025-02-25'),
(3, 4, 2, 'Not what I was expecting, quite disappointing.', '2025-02-24'),
(4, 5, 5, 'An excellent read, I highly recommend it!', '2025-02-23'),
(5, 6, 4, 'Good book, a bit slow-paced in some parts, but overall enjoyable.', '2025-02-22'),
(6, 7, 1, 'Didnt like it at all, the plot was weak and predictable.', '2025-02-21'),
(7, 8, 5, 'Incredible! A must-read for fans of the genre.', '2025-02-20'),
(8, 9, 3, 'It was okay, but it didnt really capture my attention.', '2025-02-19'),
(9, 10, 4, 'I enjoyed the book, it was a fun and light read.', '2025-02-18'),
(10, 1, 2, 'I didnt like it, the pacing was too slow for me.', '2025-02-17'),
(11, 2, 4, 'Really liked it, had some great moments, but a bit slow in parts.', '2025-02-16'),
(12, 3, 5, 'One of the best books Ive read this year, highly recommend it!', '2025-02-15'),
(13, 4, 1, 'Couldnt finish it, wasnt engaging at all.', '2025-02-14'),
(14, 5, 5, 'Such an exciting read, I couldnt stop turning the pages!', '2025-02-13'),
(15, 6, 3, 'It was fine, but not as good as I had hoped.', '2025-02-12'),
(16, 7, 2, 'Didnt really connect with the story, but others might like it.', '2025-02-11'),
(17, 8, 4, 'Pretty good, it could have been better, but enjoyable overall.', '2025-02-10'),
(18, 9, 1, 'Worst book Ive read in a long time, very disappointing.', '2025-02-09'),
(19, 10, 3, 'Decent, but not my favorite.', '2025-02-08'),
(20, 1, 5, 'Amazing book! Couldnt put it down.', '2025-02-07'),
(1, 2, 4, 'Really good, but I felt like it could have been even better.', '2025-02-06'),
(2, 3, 2, 'I didnt enjoy it, found it a bit too predictable.', '2025-02-05'),
(3, 4, 3, 'An average book, some parts were good, but others not so much.', '2025-02-04'),
(4, 5, 5, 'Fantastic! Loved every moment of it.', '2025-02-03'),
(5, 6, 1, 'Not for me, I couldnt get into it.', '2025-02-02'),
(6, 7, 4, 'Great book, but a little too long at times.', '2025-02-01'),
(7, 8, 5, 'Such an exciting plot, couldnt stop reading.', '2025-01-31'),
(8, 9, 2, 'Didnt really engage me, the plot was too slow.', '2025-01-30'),
(9, 10, 3, 'Okay, but I was expecting more depth in the story.', '2025-01-29'),
(10, 1, 4, 'Good, but not as good as the first one.', '2025-01-28');

INSERT INTO book_item (book_id, barcode, year_of_publication) VALUES
(1, '978-3-16-148410-0', 2021),
(1, '978-3-16-148410-1', 2021),
(1, '978-3-16-148410-2', 2020),
(2, '978-0-306-40615-7', 2019),
(2, '978-0-306-40615-8', 2020),
(3, '978-1-4028-9462-6', 2021),
(3, '978-1-4028-9462-7', 2019),
(4, '978-1-56619-909-4', 2022),
(5, '978-0-06-093546-7', 2018),
(5, '978-0-06-093546-8', 2019),
(6, '978-1-63149-551-9', 2020),
(6, '978-1-63149-551-0', 2022),
(7, '978-0-141-03435-5', 2021),
(7, '978-0-141-03435-6', 2020),
(8, '978-0-141-03575-8', 2019),
(8, '978-0-141-03575-9', 2022),
(9, '978-0-307-35278-5', 2020),
(9, '978-0-307-35278-6', 2021),
(10, '978-0-399-53050-6', 2022),
(10, '978-0-399-53050-7', 2021),
(11, '978-1-4165-8050-4', 2019),
(11, '978-1-4165-8050-5', 2021),
(12, '978-0-8129-9390-3', 2020),
(12, '978-0-8129-9390-4', 2022),
(13, '978-0-307-38678-0', 2021),
(13, '978-0-307-38678-1', 2020),
(14, '978-1-4028-9462-8', 2020),
(14, '978-1-4028-9462-9', 2021),
(15, '978-0-06-241752-4', 2019),
(15, '978-0-06-241752-5', 2021),
(16, '978-0-451-46525-0', 2019),
(16, '978-0-451-46525-1', 2020),
(17, '978-0-06-248948-4', 2021),
(17, '978-0-06-248948-5', 2022),
(18, '978-0-374-17115-0', 2021),
(18, '978-0-374-17115-1', 2020),
(19, '978-0-06-297347-8', 2022),
(19, '978-0-06-297347-9', 2021),
(20, '978-0-525-57688-0', 2020),
(20, '978-0-525-57688-1', 2021);