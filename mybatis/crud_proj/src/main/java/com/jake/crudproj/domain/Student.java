package com.jake.crudproj.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

/*
 * @Data : Getter/Setter, equals, toString, hashCode
 * @Builder : 빌더 패턴 ( Student.builder()
 * @NoArgsConstructor : 기본 생성자
 * @AllArgsConstructor : 모든 필드를 받는 생성자
 */

public class Student {
	private Long id;
	private String name;
	private String email;
	private Integer age;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
