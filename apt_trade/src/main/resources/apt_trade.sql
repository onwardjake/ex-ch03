-- DB 생성 (문자셋/콜레이션은 한글 안전하게 utf8mb4 권장)
CREATE DATABASE IF NOT EXISTS realestate
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

USE realestate;

-- NULLIF 시 오류가 발생한다.
    -- utf8mb4_general_ci,COERCIBLE) and (utf8mb4_0900_ai_ci,COERCIBLE) for operation 'nullif'
    -- 아래와 같이 COLLATE utf8mb4_general_ci을 개별적으로 명시하는 방법을 사용할 수 있다.
    -- NULLIF(cleanup_text(@road_name) COLLATE utf8mb4_general_ci,
    --                    '' COLLATE utf8mb4_general_ci)

-- DB 및 테이블 설정을 utf8mb4_general_ci로 설정해준다.
-- MariaDB 기준 (MySQL8이면 0900_ai_ci로 바꾸세요)
ALTER DATABASE realestate
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

-- 각 테이블 적용
ALTER TABLE apt_trade_raw
    CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

ALTER TABLE apt_trade
    CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

ALTER TABLE lawd_code_map
    CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;


-- 재사용 가능한 정리 함수 만들어 두기
-- cleanup_text 특수문자/제로폭 제거 함수
DROP FUNCTION IF EXISTS cleanup_text;
DELIMITER $$
CREATE FUNCTION cleanup_text(s TEXT)
    RETURNS VARCHAR(1024)
        CHARACTER SET utf8mb4
        COLLATE utf8mb4_general_ci
BEGIN
RETURN TRIM(
        REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(
            s,
            CHAR(13), ''),       -- \r
            CHAR(10), ''),       -- \n
            CHAR(9),  ' '),      -- \t
            CHAR(160),' '),      -- NBSP
            UNHEX('E2808B'), ''),-- U+200B
            UNHEX('EFBBBF'), '') -- U+FEFF
       );
END$$
DELIMITER ;



-- 지역코드 매핑 테이블 만들기
DROP TABLE lawd_code_map;
CREATE TABLE IF NOT EXISTS lawd_code_map (
                                             lawd_cd CHAR(10) PRIMARY KEY,
    sigungu VARCHAR(50) NOT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- CSV 파일에서 읽어서 DB에 넣기
-- 첫 행이 헤더(한글 컬럼명)라고 가정
LOAD DATA LOCAL INFILE '~/Downloads/lawd_cd_20250805.csv'
INTO TABLE lawd_code_map
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(
  lawd_cd, sigungu
);



-- 실제 분석/검색용 정규 테이블
CREATE TABLE IF NOT EXISTS apt_trade (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    lawd_cd CHAR(10) NOT NULL,                   -- 지역코드 (LAWD_CD, 5자리)
    sigungu VARCHAR(50) NULL,                    -- 법정동
    jibun VARCHAR(30) NULL,                     -- 지번
    jibun_bonbun VARCHAR(10) NULL,              -- 지번 본번
    jibun_bubun VARCHAR(10) NULL,               -- 지번 부번
    apt_name VARCHAR(120) NOT NULL,             -- 아파트
    exclusive_area_m2 DECIMAL(10,2) NOT NULL,   -- 전용면적(㎡)
    exclusive_area_pyeong DECIMAL(10,2) NOT NULL,   -- 전용면적(평)
    contract_yearmonth INT UNSIGNED NOT NULL,   -- 계약 년월
    contract_day SMALLINT UNSIGNED NOT NULL,   -- 계약일
    contract_date DATE NOT NULL,                -- 계약 년월일
    deal_amount_10k INT NOT NULL,               -- 거래금액(만원)
    deal_amount_krw BIGINT NOT NULL,            -- 거래금액(원) - 정수원화
    dong  VARCHAR(10) NULL,                     -- 동
    floor TINYINT UNSIGNED NULL,                        -- 층
    buyer_type      VARCHAR(10) NULL,           -- 매수자
    seller_type      VARCHAR(10) NULL,           -- 매도자
    build_year CHAR(4) NULL,                   -- 건축년도
    road_name VARCHAR(120) NULL,                -- 도로명
    cancel_yn CHAR(1) NULL,                     -- 해제여부 (Y/N)
    cancel_date DATE NULL,                      -- 해제사유발생일
    trade_type VARCHAR(30) NULL,                -- 거래유형
    broker_area VARCHAR(60) NULL,               -- 중개사소재지
    registration_date DATE NULL,                -- 등기일
    apt_type     VARCHAR(10),                   -- 주택유형

    -- 기본키
    PRIMARY KEY (id),

    -- 중복 방지용 유니크 키(현업에서 가장 충돌 적은 조합 예시)
--    UNIQUE KEY uq_apt_trade_dedup
--        (lawd_cd, apt_name, exclusive_area_m2, floor, contract_date, deal_amount_krw),

    -- 조회 자주 쓰는 인덱스
    KEY ix_trade_date (contract_date),
    KEY ix_lawd_month (lawd_cd, contract_yearmonth),
    KEY ix_aptname_area (apt_name, exclusive_area_m2, exclusive_area_pyeong)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4_general_ci;


SELECT * FROM lawd_code_map LIMIT 10;

-- MySQL/MariaDB의 LOAD DATA INFILE은 다른 테이블과 JOIN을 직접 지원하지 않는다.
-- 1단계 : CSV → 임시 스테이징 테이블로 단순 적재
-- 2단계 : 스테이징 테이블 + lawd_code_map JOIN → apt_trade INSERT
-- 1-1) 스테이징 테이블 생성
CREATE TABLE IF NOT EXISTS apt_trade_raw (
    sigungu VARCHAR(100) NULL,  -- 예: "의왕시"
    jibun VARCHAR(30) NULL,
    jibun_bonbun VARCHAR(10) NULL,
    jibun_bubun VARCHAR(10) NULL,
    apt_name VARCHAR(120) NULL,
    exclusive_area_m2 DECIMAL(10,2) NULL,
    contract_yearmonth INT NULL,
    contract_day SMALLINT NULL,
    deal_amount_10k INT NULL,
    dong VARCHAR(10) NULL,
    floor TINYINT NULL,
    buyer_type      VARCHAR(10) NULL,           -- 매수자
    seller_type      VARCHAR(10) NULL,           -- 매도자
    build_year CHAR(4) NULL,
    road_name VARCHAR(120) NULL,
    cancel_date DATE NULL,
    trade_type VARCHAR(30) NULL,
    broker_area VARCHAR(60) NULL,
    registration_date DATE NULL,
    apt_type     VARCHAR(10)                   -- 주택유형
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4_general_ci;

-- 1-2) 스테이징 테이블에 CSV 파일 넣기
LOAD DATA LOCAL INFILE '~/Downloads/uiwang-si_apt_trade.csv'
INTO TABLE apt_trade_raw
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'   -- 파일이 LF라면 '\n'로 변경
IGNORE 1 LINES
(@sigungu, @jibun, @jibun_bonbun, @jibun_bubun,
 @apt_name, @exclusive_area_m2,
 @contract_yearmonth, @contract_day,
 @deal_amount_10k,            -- "98,000" → 문자열로 받음
 @dong, @floor, @buyer_type, @seller_type,
 @build_year, @road_name,
 @cancel_date, @trade_type, @broker_area,
 @registration_date, @apt_type)
SET
 sigungu             = cleanup_text(@sigungu),
 jibun               = cleanup_text(@jibun),
 jibun_bonbun        = cleanup_text(@jibun_bonbun),
 jibun_bubun         = cleanup_text(@jibun_bubun),
 apt_name            = cleanup_text(@apt_name),
 exclusive_area_m2   = CAST(REPLACE(@exclusive_area_m2, ',', '') AS DECIMAL(10,2)),
 contract_yearmonth  = CAST(@contract_yearmonth AS UNSIGNED),
 contract_day        = CAST(@contract_day AS UNSIGNED),
 deal_amount_10k     = CAST(REPLACE(@deal_amount_10k, ',', '') AS UNSIGNED),  -- ★ 핵심
 dong                = cleanup_text(@dong),
 floor               = NULLIF(CAST(REPLACE(@floor, ',', '') AS UNSIGNED), 0),
 buyer_type          = cleanup_text(@buyer_type),
 seller_type         = cleanup_text(@seller_type),
 build_year          = cleanup_text(@build_year),
 road_name           = NULLIF(cleanup_text(@road_name) COLLATE utf8mb4_general_ci, '' COLLATE utf8mb4_general_ci),
 cancel_date         = CASE
                        WHEN @cancel_date IN ('', '0', '0000-00-00') THEN NULL
                        ELSE STR_TO_DATE(@cancel_date, '%Y%m%d')
END,
 trade_type          = NULLIF(cleanup_text(@trade_type) COLLATE utf8mb4_general_ci, '' COLLATE utf8mb4_general_ci),
 broker_area         = NULLIF(cleanup_text(@broker_area) COLLATE utf8mb4_general_ci, '' COLLATE utf8mb4_general_ci),
 registration_date   = CASE
                            WHEN @registration_date IN ('', '0', '0000-00-00') THEN NULL
                            ELSE STR_TO_DATE(@registration_date, '%y.%m.%d')   -- ← 핵심
END,
 apt_type                = cleanup_text(@apt_type)
;
-- 데이터 확인
SELECT * FROM apt_trade_raw LIMIT 5;
SELECT * FROM apt_trade_raw WHERE cancel_date IS NOT NULL LIMIT 20;
SELECT * FROM apt_trade_raw WHERE contract_yearmonth=202509 AND contract_day=13;


-- 2단계 : 스테이징 테이블 + lawd_code_map JOIN → apt_trade INSERT
-- 쿼리 실행하는데 10분 정도 걸림.
  -- 느린 가장 큰 원인은 “JOIN 절에서 함수 사용 + 콜레이션 변환” 때문에 인덱스를 하나도 못 타고 두 테이블을 매번 풀스캔하기 때문이에요.
  -- cleanup_text(r.sigungu) = cleanup_text(m.sigungu)처럼 함수가 끼면
  -- MariaDB가 인덱스를 쓰지 못해 1977 × 49,861 ≈ 9,8천만 비교 + 함수 호출을 반복합니다.
  -- 여기에 자동 커밋/인덱스 갱신/콜레이션 변환까지 겹치면 분 단위로 늘어납니다.
-- 2-1) 인덱스를 생성하여 쿼리 실행 시간을 단축시킨다

-- apt_trade_raw: 정리된 시군구 컬럼 추가 + 값 채우기 + 인덱스
ALTER TABLE apt_trade_raw ADD COLUMN sigungu_clean VARCHAR(100);
UPDATE apt_trade_raw SET sigungu_clean = cleanup_text(sigungu);
CREATE INDEX ix_r_sigungu_clean ON apt_trade_raw(sigungu_clean);

-- lawd_code_map: 정리된 시군구/풀네임 추가 + 값 채우기 + 인덱스
ALTER TABLE lawd_code_map
    ADD COLUMN sigungu_clean VARCHAR(100);
UPDATE lawd_code_map
SET sigungu_clean = cleanup_text(sigungu);
CREATE INDEX ix_m_sigungu_clean   ON lawd_code_map(sigungu_clean);

-- 실행 계획 확인
-- type이 ref/eq_ref이고 key가 ix_*_sigungu_clean로 잡히면 성공.
-- type=ALL이면 풀스캔 → 인덱스/콜레이션/데이터 정규화 다시 점검.
EXPLAIN
SELECT 1
FROM apt_trade_raw r
         JOIN lawd_code_map m
              ON r.sigungu_clean = m.sigungu_clean;


-- 2-2) 스테이징 테이블 + lawd_code_map JOIN → apt_trade INSERT
SET autocommit = 0;
SET UNIQUE_CHECKS = 0;
SET FOREIGN_KEY_CHECKS = 0;

START TRANSACTION;

INSERT INTO apt_trade (
    lawd_cd, sigungu, jibun, jibun_bonbun, jibun_bubun,
    apt_name, exclusive_area_m2, exclusive_area_pyeong,
    contract_yearmonth, contract_day, contract_date,
    deal_amount_10k, deal_amount_krw,
    dong, floor, build_year, road_name,
    cancel_yn, cancel_date, trade_type, broker_area, apt_type
)
SELECT
    m.lawd_cd,
    r.sigungu,
    r.jibun,
    r.jibun_bonbun,
    r.jibun_bubun,
    r.apt_name,
    r.exclusive_area_m2,
    ROUND(r.exclusive_area_m2 / 3.305785, 2) AS exclusive_area_pyeong,
    r.contract_yearmonth,
    r.contract_day,
    STR_TO_DATE(
            CONCAT(r.contract_yearmonth, LPAD(r.contract_day, 2, '0')),
            '%Y%m%d'
    ) AS contract_date,
    r.deal_amount_10k,
    r.deal_amount_10k * 10000 AS deal_amount_krw,
    r.dong,
    r.floor,
    r.build_year,
    r.road_name,
    CASE WHEN r.cancel_date IS NULL THEN 'N' ELSE 'Y' END AS cancel_yn,
    r.cancel_date,
    r.trade_type,
    r.broker_area,
    r.apt_type
FROM apt_trade_raw r
         JOIN lawd_code_map m
              ON r.sigungu_clean = m.sigungu_clean;

COMMIT;

SET autocommit = 1;
SET UNIQUE_CHECKS = 1;
SET FOREIGN_KEY_CHECKS = 1;






