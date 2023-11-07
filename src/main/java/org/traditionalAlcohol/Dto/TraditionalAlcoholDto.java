package org.traditionalAlcohol.Dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TraditionalAlcoholDto {

        private Integer no;
        private String category;
        private String name;
        private String url;
        private String image_path;
        private String comment;
        private double score;
        private int reviewCount;
        private String kind;
        private String alcohol_degree;
        private String capacity;
        private String taste_point;
        private String foods_point;
        private String special_point;
        private String foods;
        private int sweet_flavor;
        private int body_flavor;
        private int sour_flavor;
        private int carbonic_flavor;
        private int tannin_flavor;
        private String en_foods;

}
