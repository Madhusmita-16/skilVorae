package com.skilvorae.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Long id;
    private String title;
    private String slug;
    private String description;
    private String instructorName;
    private Long categoryId;
    private String categoryName;
    private String difficulty;
    private Double durationHours;
    private String thumbnailUrl;
    private Double rating;
    private Integer enrollmentCount;
    private Double price;
    private Double originalPrice;
    private Integer discountPercentage;
    private String formattedPrice;
    private Integer totalModules;
    private Integer totalLessons;
    private Boolean isEnrolled;
    private Integer progressPercentage;
    private List<ModuleDto> modules;
    private List<CourseReviewDto> reviews;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public Double getDurationHours() { return durationHours; }
    public void setDurationHours(Double durationHours) { this.durationHours = durationHours; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public Integer getEnrollmentCount() { return enrollmentCount; }
    public void setEnrollmentCount(Integer enrollmentCount) { this.enrollmentCount = enrollmentCount; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Double getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(Double originalPrice) { this.originalPrice = originalPrice; }
    public Integer getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(Integer discountPercentage) { this.discountPercentage = discountPercentage; }
    public String getFormattedPrice() { return formattedPrice; }
    public void setFormattedPrice(String formattedPrice) { this.formattedPrice = formattedPrice; }
    public Integer getTotalModules() { return totalModules; }
    public void setTotalModules(Integer totalModules) { this.totalModules = totalModules; }
    public Integer getTotalLessons() { return totalLessons; }
    public void setTotalLessons(Integer totalLessons) { this.totalLessons = totalLessons; }
    public Boolean getIsEnrolled() { return isEnrolled; }
    public void setIsEnrolled(Boolean isEnrolled) { this.isEnrolled = isEnrolled; }
    public Integer getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Integer progressPercentage) { this.progressPercentage = progressPercentage; }
    public List<ModuleDto> getModules() { return modules; }
    public void setModules(List<ModuleDto> modules) { this.modules = modules; }
    public List<CourseReviewDto> getReviews() { return reviews; }
    public void setReviews(List<CourseReviewDto> reviews) { this.reviews = reviews; }

    public static CourseDtoBuilder builder() { return new CourseDtoBuilder(); }

    public static class CourseDtoBuilder {
        private Long id;
        private String title;
        private String slug;
        private String description;
        private String instructorName;
        private Long categoryId;
        private String categoryName;
        private String difficulty;
        private Double durationHours;
        private String thumbnailUrl;
        private Double rating;
        private Integer enrollmentCount;
        private Double price;
        private Double originalPrice;
        private Integer discountPercentage;
        private String formattedPrice;
        private Integer totalModules;
        private Integer totalLessons;
        private Boolean isEnrolled;
        private Integer progressPercentage;
        private List<ModuleDto> modules;
        private List<CourseReviewDto> reviews;

        public CourseDtoBuilder id(Long id) { this.id = id; return this; }
        public CourseDtoBuilder title(String title) { this.title = title; return this; }
        public CourseDtoBuilder slug(String slug) { this.slug = slug; return this; }
        public CourseDtoBuilder description(String description) { this.description = description; return this; }
        public CourseDtoBuilder instructorName(String instructorName) { this.instructorName = instructorName; return this; }
        public CourseDtoBuilder categoryId(Long categoryId) { this.categoryId = categoryId; return this; }
        public CourseDtoBuilder categoryName(String categoryName) { this.categoryName = categoryName; return this; }
        public CourseDtoBuilder difficulty(String difficulty) { this.difficulty = difficulty; return this; }
        public CourseDtoBuilder durationHours(Double durationHours) { this.durationHours = durationHours; return this; }
        public CourseDtoBuilder thumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; return this; }
        public CourseDtoBuilder rating(Double rating) { this.rating = rating; return this; }
        public CourseDtoBuilder enrollmentCount(Integer enrollmentCount) { this.enrollmentCount = enrollmentCount; return this; }
        public CourseDtoBuilder price(Double price) { this.price = price; return this; }
        public CourseDtoBuilder originalPrice(Double originalPrice) { this.originalPrice = originalPrice; return this; }
        public CourseDtoBuilder discountPercentage(Integer discountPercentage) { this.discountPercentage = discountPercentage; return this; }
        public CourseDtoBuilder formattedPrice(String formattedPrice) { this.formattedPrice = formattedPrice; return this; }
        public CourseDtoBuilder totalModules(Integer totalModules) { this.totalModules = totalModules; return this; }
        public CourseDtoBuilder totalLessons(Integer totalLessons) { this.totalLessons = totalLessons; return this; }
        public CourseDtoBuilder isEnrolled(Boolean isEnrolled) { this.isEnrolled = isEnrolled; return this; }
        public CourseDtoBuilder progressPercentage(Integer progressPercentage) { this.progressPercentage = progressPercentage; return this; }
        public CourseDtoBuilder modules(List<ModuleDto> modules) { this.modules = modules; return this; }
        public CourseDtoBuilder reviews(List<CourseReviewDto> reviews) { this.reviews = reviews; return this; }

        public CourseDto build() {
            return new CourseDto(id, title, slug, description, instructorName, categoryId, categoryName, difficulty, durationHours, thumbnailUrl, rating, enrollmentCount, price, originalPrice, discountPercentage, formattedPrice, totalModules, totalLessons, isEnrolled, progressPercentage, modules, reviews);
        }
    }
}
