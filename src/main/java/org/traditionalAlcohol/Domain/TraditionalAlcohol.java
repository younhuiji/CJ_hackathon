package org.traditionalAlcohol.Domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity(name = "ALCOHOL") // 테이블명
@SequenceGenerator(name = "ALCOHOL_SEQ_GEN", sequenceName = "ALCOHOL_SEQ", allocationSize = 1)

public class TraditionalAlcohol {

    @Id
        private Integer no;
        private String category;
        private String name;
        private String url;
        private String image_path;
        private String comment;
        private Double score;
        private Long reviewCount;
        private String kind;
        private String alcohol_degree;
        private String capacity;
        private String taste_point;
        private String foods_point;
        private String special_point;
        private String foods;
        private Integer sweet_flavor;
        private Integer body_flavor;
        private Integer sour_flavor;
        private Integer carbonic_flavor;
        private Integer tannin_flavor;
        private String en_foods;

        // 생성자, getter 및 setter 메서드 등 필요한 코드 추가


}