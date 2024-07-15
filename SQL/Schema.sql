-- CREATE Schema 'sns'
-- author: flash
-- date: 24-07-03

USE sns;

DROP TABLE IF EXISTS `files`;
DROP TABLE IF EXISTS `category_permission`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `group_manager`;
DROP TABLE IF EXISTS `group_member`;
DROP TABLE IF EXISTS `group_notice`;
DROP TABLE IF EXISTS `post_like`;
DROP TABLE IF EXISTS `view`;
DROP TABLE IF EXISTS `comment`;
DROP TABLE IF EXISTS `post`;
DROP TABLE IF EXISTS `qna`;
DROP TABLE IF EXISTS `notice`;
DROP TABLE IF EXISTS `group`;
DROP TABLE IF EXISTS `user_device`;
DROP TABLE IF EXISTS `user_profiles`;
DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` ( --  사용자 정보
	`id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`phone` VARCHAR(13),
	`naver_id` VARCHAR(255),
	`kakao_id` VARCHAR(255),
	`apple_id` VARCHAR(255),
	`password` VARCHAR(255),
	`role` CHAR(1) DEFAULT '1',	-- 1 사용자, 9 관리자
	`status` CHAR(1) DEFAULT '1',  -- 0 승인 대기, 1 승인
	`banned` CHAR(1) DEFAULT '0',
	`removed` CHAR(1) DEFAULT '0', -- 회원 탈퇴 여부 0 탈퇴 안함, 1 탈퇴함
	`last_login` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`removed_time` TIMESTAMP NULL DEFAULT NULL, -- 사용자가 탈퇴한 시간
	`created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	
	INDEX idx_users_id (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `user_profiles` (	-- 사용자 추가 정보
	`id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`user_id` INT(10) UNSIGNED NOT NULL,
	`gender` CHAR(1) DEFAULT '0',  -- 0 남자, 1 여자
	`full_name` VARCHAR(10),
	`nick_name` VARCHAR(10),
	`address` VARCHAR(255),
	`address_detail` VARCHAR(255),
	`picture` VARCHAR(200),
	`birthd` DATE,	-- 생년월일
	`agree_privacy_3party` CHAR(1) DEFAULT '1',	-- 개인정보 3자 제공 동의 여부
	`agree_marketing` CHAR(1) DEFAULT '1',	-- 광고 수신 동의 여부
	`fcm_notice` CHAR(1) DEFAULT '1',	-- 공지 알림 수신 여부
	`fcm_group_notice` CHAR(1) DEFAULT '1',	-- 그룹 공지 알림 수신 여부
	
	INDEX idx_user_profiles_user_id (`user_id`),
	CONSTRAINT `fk_user_profiles_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `user_device` (	-- 사용자 단말정보
	`id` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`user_id` INT(10) UNSIGNED NOT NULL,
	`device_type` CHAR(1) DEFAULT 'a',	-- i ios, a android
	`device_key` VARCHAR(255),
	`created` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
	`updated` TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

	INDEX idx_user_device_user_id (`user_id`),
	CONSTRAINT `fk_user_device_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `group` (	-- 그룹
    `id` INT(10) UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `name` VARCHAR(128) NOT NULL,	-- 이름
	`address` VARCHAR(255),	-- 지번, 도로명 주소
	`address_detail` VARCHAR(255),	-- 상세주소
	`introduce` TEXT,	-- 소개
	`etc` TEXT,	-- 기타
	`lot` FLOAT,	-- 경도 x
	`lat` FLOAT,	-- 위도 y
	`phone` VARCHAR(20),	-- 전화번호
	`represent_image` VARCHAR(255),	-- 대표사진
	`background_image` VARCHAR(255)	-- 배경사진

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `category` (	-- 그룹 카테고리
    `id` INT(10) UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
	`group_id` INT(10) UNSIGNED NOT NULL,
    `name` VARCHAR(128) NOT NULL,	-- 이름
	`created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

	INDEX idx_category_group_id (`group_id`),
	CONSTRAINT `fk_category_group_id` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `category_permission` (	-- 카테고리 권한
    `id` INT(10) UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id` INT(10) UNSIGNED NOT NULL,	-- 사용자 아이디
	`category_id` INT(10) UNSIGNED NOT NULL,	--  카테괴 아이디
	`created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	
	INDEX `index_category_permission_user_id` (`user_id`),
	INDEX `index_category_permission_category_id` (`category_id`),
    CONSTRAINT `fk_category_permission_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk_category_permission_category_id` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `group_member` (	-- 그룹 멤버
    `id` INT(10) UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id` INT(10) UNSIGNED NOT NULL,	-- 사용자 아이디
	`group_id` INT(10) UNSIGNED NOT NULL,	-- 그룹 아이디
	`created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	
	INDEX `index_group_member_user_id` (`user_id`),
	INDEX `index_group_member_group_id` (`group_id`),
    CONSTRAINT `fk_group_member_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk_group_member_group_id` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `group_manager` (	-- 그룹 관리자
    `id` INT(10) UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
	-- 타입 분리 0 그룹장, 1 운영진
	`group_id` INT(10) UNSIGNED NOT NULL,	-- 그룹 아이디
	`group_member_id` INT(10) UNSIGNED NOT NULL,	-- 그룹 멤버 아이디
	`created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	
	INDEX `index_group_manager_group_member_id` (`group_member_id`),
	INDEX `index_group_manager_group_id` (`group_id`),
    CONSTRAINT `fk_group_manager_group_member_id` FOREIGN KEY (`group_member_id`) REFERENCES `group_member` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk_group_manager_group_id` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `group_notice` (	-- 그룹 공지 사항
    `id` INT(10) UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id` INT(10) UNSIGNED NOT NULL,	-- 작성자
	`group_id` INT(10) UNSIGNED NOT NULL,	-- 그룹 아이디
	`title` VARCHAR(128) NOT NULL,	-- 제목
	`contents` TEXT,	-- 내용
	`view` INT(20) UNSIGNED,	-- 조회수
	`created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,	-- 생성 일시
	`modifyd` TIMESTAMP,	-- 수정 일시. 조회수를 사용하기에, 수정 시 date를 입력해야 함
	
	INDEX `index_group_notice_user_id` (`user_id`),
	INDEX `index_group_notice_group_id` (`group_id`),
    CONSTRAINT `fk_group_notice_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk_group_notice_group_id` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `notice` (	-- 공지 사항(필요시 faq와 같이 사용). 댓글은 사용 안함
    `id` INT(10) UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id` INT(10) UNSIGNED NOT NULL,	-- 작성자
	`title` VARCHAR(128) NOT NULL,	-- 제목
	`contents` TEXT,	-- 내용
	`view` INT(20) UNSIGNED,	-- 조회수
	`created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,	-- 생성 일시
	`modifyd` TIMESTAMP,	-- 수정 일시. 조회수를 사용하기에, 수정 시 date를 입력해야 함
	
	INDEX `index_notice_user_id` (`user_id`),
	CONSTRAINT `fk_notice_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `qna` (	-- QnA
    `id` INT(10) UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id` INT(10) UNSIGNED NOT NULL,	-- 작성자
	`answer_id` INT(10) UNSIGNED NOT NULL,	-- 답변자
	`contents` TEXT,	-- 질문
	`answer` TEXT,	-- 답변
	`created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,	-- 생성 일시
	`answerd` TIMESTAMP,	-- 답변 일시
	
	INDEX `index_qna_user_id` (`user_id`),
	INDEX `index_qna_answer_id` (`answer_id`),
	CONSTRAINT `fk_qna_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk_qna_answer_id` FOREIGN KEY (`answer_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `post` (	-- 게시물
    `id` INT(10) UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
	`user_id` INT(10) UNSIGNED NOT NULL,
	`group_id` INT(10) UNSIGNED NOT NULL,
	`category_id` INT(10) UNSIGNED,
    `title` VARCHAR(128) NOT NULL,	-- 제목
	`contents` TEXT,	-- 내용
	`type` VARCHAR(1), -- 0 전체 게시물, 1 그룹 게시물
	`view` INT(20) UNSIGNED,	-- 조회수
	`created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,	-- 생성 일시
	`modifyd` TIMESTAMP,	-- 수정 일시. 조회수를 사용하기에, 수정 시 date를 입력해야 함

    INDEX `index_post_user_id` (`user_id`),
	INDEX `index_post_group_id` (`group_id`),
	INDEX `index_post_type` (`type`),
	INDEX `index_post_category_id` (`category_id`),
    CONSTRAINT `fk_post_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON UPDATE CASCADE,
	CONSTRAINT `fk_post_category_id` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON UPDATE CASCADE,
	CONSTRAINT `fk_post_group_id` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `comment` (	-- 댓글
    `id` INT(10) UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
	`parent_id` INT(10) UNSIGNED,	-- 대댓그까지만 허용
	`user_id` INT(10) UNSIGNED NOT NULL,
	`post_id` INT(10) UNSIGNED NOT NULL,
	`contents` TEXT,	-- 내용
	`created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	`modifyd` TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	
	INDEX `index_comment_user_id` (`user_id`),
	INDEX `index_comment_post_id` (`post_id`),
    CONSTRAINT `fk_comment_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk_comment_post_id` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `view` (	-- 조회수
    `id` INT(10) UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
	`user_id` INT(10) UNSIGNED NOT NULL,
	`post_id` INT(10) UNSIGNED NOT NULL,
	`created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	
	INDEX `index_view_like_user_id` (`user_id`),
	INDEX `index_view_like_post_id` (`post_id`),
    CONSTRAINT `fk_view_like_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk_view_like_post_id` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `post_like` (	-- 게시물 좋아요
    `id` INT(10) UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
	`user_id` INT(10) UNSIGNED NOT NULL,
	`post_id` INT(10) UNSIGNED NOT NULL,
	`created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	
	INDEX `index_post_like_user_id` (`user_id`),
	INDEX `index_post_like_post_id` (`post_id`),
    CONSTRAINT `fk_post_like_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk_post_like_post_id` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `files` (	-- 파일
    `id` INT(10) UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `type` VARCHAR(1) DEFAULT 0,	-- 0 게시물, 1 그룹, 2 그룹 공지, 3 공지사항(faq)
	`post_id` INT(10) UNSIGNED,	-- 게시물
	`group_id` INT(10) UNSIGNED,	-- 그룹
	`group_notice_id` INT(10) UNSIGNED,	-- 그룹 공지
	`notice_id` INT(10) UNSIGNED,	-- 공지사항(faq)
	`name` VARCHAR(128), -- 파일명
	`created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	
	INDEX `index_files_group_id` (`group_id`),
	INDEX `index_files_post_id` (`post_id`),
	INDEX `index_files_group_notice_id` (`group_notice_id`),
	INDEX `index_files_notice_id` (`notice_id`),
    CONSTRAINT `fk_files_group_id` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk_files_post_id` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_files_group_notice_id` FOREIGN KEY (`group_notice_id`) REFERENCES `group_notice` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk_files_notice_id` FOREIGN KEY (`notice_id`) REFERENCES `notice` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;