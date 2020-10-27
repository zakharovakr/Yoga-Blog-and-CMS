DROP DATABASE IF EXISTS blogCMSDBTest;
CREATE DATABASE blogCMSDBTest;

USE blogCMSDBTest;

CREATE TABLE `role` (
	roleId int PRIMARY KEY auto_increment,
    `role` varchar(30)
);

CREATE TABLE `user` (
	userId int PRIMARY KEY auto_increment,
    username varchar(30) not null,
    `password` varchar(100) not null,
    firstName varchar(30) not null,
    lastName varchar(30) not null,
    email varchar(30) not null,
    `enable` boolean not null
);

CREATE TABLE `user_role` (
	userId int,
    roleId int,
    PRIMARY KEY (userId, roleId),
    FOREIGN KEY (userId) references `user`(userId),
    FOREIGN KEY (roleId) references `role`(roleId)
);

CREATE TABLE blogpost (
	blogpostId int PRIMARY KEY auto_increment,
    timePosted datetime,
    title varchar(100) not null,
    `type` varchar(10) not null,
    `status` varchar(10) not null,
    photoFileName varchar(255),
    content text not null,
    userId int,
    FOREIGN KEY (userId) references `user`(userId)
);

CREATE TABLE comment (
	commentId int PRIMARY KEY auto_increment,
    timePosted datetime,
    content varchar(280) not null,
    userId int,
    FOREIGN KEY (userId) references user(userId),
    blogpostId int,
    FOREIGN KEY (blogpostId) references blogpost (blogpostId)
);

CREATE TABLE hashtag (
	hashtagId int PRIMARY KEY auto_increment,
    name varchar(30) not null
);

CREATE TABLE blogpost_hashtag (
	blogpostId int,
    hashtagId int,
    PRIMARY KEY (blogpostId, hashtagId),
    FOREIGN KEY (blogpostId) references blogpost (blogpostId),
    FOREIGN KEY (hashtagId) references hashtag (hashtagId)
);
